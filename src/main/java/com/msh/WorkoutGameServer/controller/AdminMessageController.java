package com.msh.WorkoutGameServer.controller;

import com.msh.WorkoutGameServer.model.Game;
import com.msh.WorkoutGameServer.model.GameInit;
import com.msh.WorkoutGameServer.model.message.SimpleMessage;
import com.msh.WorkoutGameServer.model.message.in.GameInitMessage;
import com.msh.WorkoutGameServer.model.message.out.GamesResponse;
import com.msh.WorkoutGameServer.model.message.out.SimpleGamesResponse;
import com.msh.WorkoutGameServer.model.message.out.SimpleResponse;
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
public class AdminMessageController {

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private SimpMessageSendingOperations simpleMessagingTemplate;

    @MessageMapping("/admin/games")
    public void getGames(@Payload SimpleMessage msg) {
        log.info("get games: " + msg.getFrom());
        this.simpleMessagingTemplate.convertAndSend("/admin/games", new GamesResponse("Server", "Active games.", "GAMES", gameService.getActiveGames()));
    }

    @MessageMapping("/admin/init")
    public void chooseGame(@Payload GameInitMessage msg) {
        log.info(msg.getFrom() + " : " + msg.getText());
        boolean success = false;
        if ("new".equals(msg.getText())) {
            GameInit init = msg.getGameInit();
            Game game = new Game(init.getTitle(), init.getMapSize(), init.getWaitingTime());
            success = gameService.createGame(game);
        }
        if (success) {
            this.simpleMessagingTemplate.convertAndSend("/admin", new SimpleResponse(msg.getFrom(), "Game initialized!", "INIT"));
            this.simpleMessagingTemplate.convertAndSend("/admin/games", new GamesResponse("Server", "Active games.", "GAMES", gameService.getActiveGames()));
            this.simpleMessagingTemplate.convertAndSend("/public/games", new SimpleGamesResponse("Server", "Active games.", "GAMES", gameService.getActiveSimpleGames()));
        } else {
            //TODO: creation failed
        }
    }

    @MessageMapping("/admin/start")
    public void startGame(@Payload SimpleMessage msg) {
        log.info(msg.getFrom() + " : " + msg.getText());
        gameService.start(msg.getText());
        this.simpleMessagingTemplate.convertAndSend("/admin", new SimpleResponse(msg.getFrom(), "Game started!", "START"));
        this.simpleMessagingTemplate.convertAndSend("/admin/games", new GamesResponse("Server", "Active games.", "GAMES", gameService.getActiveGames()));
        this.simpleMessagingTemplate.convertAndSend("/public/games", new SimpleGamesResponse("Server", "Active games.", "GAMES", gameService.getActiveSimpleGames()));
    }

    @MessageMapping("/admin/stop")
    public void stopGame(@Payload SimpleMessage msg) {
        log.info(msg.getFrom() + " : " + msg.getText());
        gameService.stop(msg.getText());
        this.simpleMessagingTemplate.convertAndSend("/admin", new SimpleResponse(msg.getFrom(), "Game stopped!", "STOP"));
        this.simpleMessagingTemplate.convertAndSend("/admin/games", new GamesResponse("Server", "Active games.", "GAMES", gameService.getActiveGames()));
        this.simpleMessagingTemplate.convertAndSend("/public/stop", new SimpleResponse("Server", "Game stopped.", "STOP"));
    }

    @MessageMapping("/admin/update")
    public void getPlayers(@Payload SimpleMessage msg) {
        log.info(msg.getFrom() + " : " + msg.getText());
        gameService.showPlayers(msg.getText());
        this.simpleMessagingTemplate.convertAndSend("/admin/games", new GamesResponse("Server", "Active games.", "GAMES", gameService.getActiveGames()));
    }

    @MessageMapping("/admin/delete")
    public void deleteGame(@Payload SimpleMessage msg) {
        log.info(msg.getFrom() + " : " + msg.getText());
        gameService.deleteGame(msg.getText());
        this.simpleMessagingTemplate.convertAndSend("/admin/games", new GamesResponse("Server", "Active games.", "GAMES", gameService.getActiveGames()));
    }
}
