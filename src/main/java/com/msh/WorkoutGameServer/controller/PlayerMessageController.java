package com.msh.WorkoutGameServer.controller;

import com.msh.WorkoutGameServer.model.Player;
import com.msh.WorkoutGameServer.model.message.ConnectionResponseEnum;
import com.msh.WorkoutGameServer.model.message.SimpleMessage;
import com.msh.WorkoutGameServer.model.message.in.*;
import com.msh.WorkoutGameServer.model.message.out.*;
import com.msh.WorkoutGameServer.model.user.LoginUser;
import com.msh.WorkoutGameServer.model.user.User;
import com.msh.WorkoutGameServer.service.GameServiceImpl;
import com.msh.WorkoutGameServer.service.UserServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
public class PlayerMessageController {

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private SimpMessageSendingOperations simpleMessagingTemplate;

    @MessageMapping("/action/games")
    public void getGames(@Payload SimpleMessage msg) {
        log.info("get games: " + msg.getFrom());
        this.simpleMessagingTemplate.convertAndSend("/private/games/" + msg.getFrom(), new SimpleGamesResponse("Server", "Active games.", "GAMES", gameService.getActiveSimpleGames()));
    }

    @MessageMapping("/action/auth")
    public void handlePlayerAuthentication(@Payload AuthMessage msg) {
        switch (msg.getType()) {
            case REGISTER:
                User newUser = userService.save(msg.getUser());
                if (newUser == null) {
                    log.info("Failed registration: " + msg.getFrom());
                    this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/connection", new SimpleResponse("Server", "This name is already in use.", "USED"));
                } else {
                    log.info("Successful registration: " + msg.getFrom());
                    this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/connection", new SimpleResponse("Server", "Successful registration!", "SUCCESS"));
                }
                break;
            case JOIN:
                LoginUser loginUser = msg.getUser();
                if (!userService.isUsernameTaken(loginUser)) {
                    log.info(msg.getFrom() + " couldn't join, since (s)he is not registered.");
                    this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/connection", new SimpleResponse("Server", "You have to register first!", "INVALID"));
                } else if (!userService.isPasswordValid(loginUser)) {
                    log.info(msg.getFrom() + " couldn't join, since the password didn't match.");
                    this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/connection", new SimpleResponse("Server", "Invalid password.", "INVALID"));
                } else {
                    ConnectionResponseEnum response = gameService.joinGame(msg);
                    String gameId = "";
                    if (response != ConnectionResponseEnum.NULL) {
                        gameId = gameService.getGameId(msg);
                    }
                    switch (response) {
                        case NULL:
                            log.info(msg.getFrom() + " isn't participating in any game.");
                            this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/connection", new SimpleResponse("Server", "You are not participating in a game.\n Note that from this interface you cannot register an account or subscribe to a game.", response.toString()));
                            break;
                        case SUB:
                            log.info(msg.getFrom() + " successfully subscribed to the game.");
                            this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/connection", new SimpleResponse("Server", "Successful subscription.", response.toString()));
                            this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/connection/" + gameId, new SimpleResponse("Server", "Successful subscription.", response.toString()));
                            this.simpleMessagingTemplate.convertAndSend("/admin/games", new GamesResponse("Server", "Active games.", "GAMES", gameService.getActiveGames()));
                            this.simpleMessagingTemplate.convertAndSend("/public/games", new SimpleGamesResponse("Server", "Active games.", "GAMES", gameService.getActiveSimpleGames()));
                            break;
                        case USED:
                            log.info(msg.getFrom() + " was unable to subscribe, because the name was already in use.");
                            this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/connection", new SimpleResponse("Server", "You have already subscribed to the game.", response.toString()));
                            this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/connection/" + gameId, new SimpleResponse("Server", "You have already subscribed to the game.", response.toString()));
                            break;
                        case GAME:
                            log.info(msg.getFrom() + " joined to the game.");
                            this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/game", new GameResponse(msg.getFrom(), "Getting game state.", response.toString(), gameService.getGameDTO(msg.getFrom())));
                            this.simpleMessagingTemplate.convertAndSend("/public", new SimpleResponse("Server", msg.getFrom() + " joined the game.", "JOIN"));
                            this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/game/" + gameId, new GameResponse(msg.getFrom(), "Getting game state.", response.toString(), gameService.getGameDTO(msg.getFrom())));
                            break;
                        case OFF:
                            log.info(msg.getFrom() + "  was unable to subscribe, because the subscription time expired.");
                            this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/connection", new SimpleResponse("Server", "You are late to subscribe to this game.", response.toString()));
                            this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom() + "/connection/" + gameId, new SimpleResponse("Server", msg.getFrom() + ", you are late to subscribe to this game.", response.toString()));
                            break;
                    }
                }
                break;
            default:
                break;
        }

    }

    @MessageMapping("/action/move")
    public void handlePlayerMove(@Payload PlayerMoveMessage msg) {
        log.info(msg.getFrom() + " moved to " + msg.getNewPos());
        gameService.modifyMap(msg);
        this.simpleMessagingTemplate.convertAndSend("/public/map/" + gameService.getGameId(msg), new MapStateResponse(msg.getFrom(), gameService.getGameId(msg), "MAP", gameService.getMap(msg.getFrom())));
    }

    @MessageMapping("/action/occupy")
    public void handlePlayerOccupation(@Payload PlayerOccupationMessage msg) {
        log.info(msg.getFrom() + " occupied " + msg.getOccupiedField());
        Player prevPlayer = gameService.modifyMap(msg);
        this.simpleMessagingTemplate.convertAndSend("/public/map/" + gameService.getGameId(msg), new MapStateResponse(msg.getFrom(), gameService.getGameId(msg), "MAP", gameService.getMap(msg.getFrom())));
        if (prevPlayer != null) {
            this.simpleMessagingTemplate.convertAndSend("/private/" + prevPlayer.getName() + "/player/" + gameService.getGameId(msg), new PlayerStateResponse("Server", "Player update!", "PLAYER", prevPlayer));
        }
        String gameId = gameService.getGameId(msg);
        this.simpleMessagingTemplate.convertAndSend("/public/players", new PlayersResponse("Server", "Most recent player stat!", "PLAYERS", gameService.getPlayersRanked(gameId)));
    }

    @MessageMapping("/action/stock")
    public void handlePlayerBuying(@Payload GameMessage msg) {
        log.info(msg.getFrom() + " bought a(n) " + msg.getText() + " stock.");
        gameService.modifyStocks(msg);
        this.simpleMessagingTemplate.convertAndSend("/public/stock/" + gameService.getGameId(msg), new StockResponse(msg.getFrom(), gameService.getGameId(msg), "STOCK", gameService.getAllStocks(msg.getFrom()), gameService.getStocks(msg.getFrom()), gameService.getPlayer(msg.getFrom()).getMoney()));
        String gameId = gameService.getGameId(msg);
        this.simpleMessagingTemplate.convertAndSend("/public/players", new PlayersResponse("Server", "Most recent player stat!", "PLAYERS", gameService.getPlayersRanked(gameId)));
    }

    @MessageMapping("/action/exercise")
    public void handlePlayerWorkingOut(@Payload PlayerExerciseMessage msg) {
        log.info(String.format("%s doin' some workout. (%s, %f) ", msg.getFrom(), msg.getExercise(), msg.getAmount()));
        gameService.saveExerciseReps(msg);
        String gameId = gameService.getGameId(msg);
        this.simpleMessagingTemplate.convertAndSend("/public/players", new PlayersResponse("Server", "Most recent player stat!", "PLAYERS", gameService.getPlayersRanked(gameId)));
    }

    @MessageMapping("/action/vision")
    public void handlePlayerVisionInc(@Payload GameMessage msg) {
        log.info(msg.getFrom() + " has better vision now. ");
        gameService.saveVisionInc(msg);
    }

    @MessageMapping("/action/convert")
    public void handlePlayerConversion(@Payload PlayerConversionMessage msg) {
        log.info(msg.getFrom() + " converted " + msg.getAmount() + " score to money. ");
        gameService.executeConversion(msg);
    }

    @MessageMapping("/action/time")
    public void handlePlayerTimeUntilMove(@Payload PlayerTimeMessage msg) {
        log.info(msg.getFrom() + " logged out and " + msg.getSeconds() + " seconds remained on the clock. ");
        gameService.saveTime(msg);
    }

    @MessageMapping("/action/players")
    public void handleResults(@Payload GameMessage msg) {
        log.info(msg.getFrom() + " is requesting player info.");
        String gameId = gameService.getGameId(msg);
        this.simpleMessagingTemplate.convertAndSend("/public/players", new PlayersResponse("Server", "Most recent player stat!", "PLAYERS", gameService.getPlayersRanked(gameId)));
    }
}
