package com.msh.WorkoutGameServer.controller;

import com.msh.WorkoutGameServer.model.Player;
import com.msh.WorkoutGameServer.model.message.in.*;
import com.msh.WorkoutGameServer.model.message.out.*;
import com.msh.WorkoutGameServer.service.GameServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private SimpMessageSendingOperations simpleMessagingTemplate;

    private final Logger logger = LogManager.getLogger(MessageController.class);

    @MessageMapping("/action")
    public void handlePlayerAction(@Payload GameMessage msg) {
        switch (msg.getType()) {
            case JOIN:
                logger.info("New connection: " + msg.getFrom());
                JoinResponse response = gameService.joinGame(msg);
                switch (response) {
                    case SUB:
                        logger.info(msg.getFrom() + " successfully subscribed to the game.");
                        this.simpleMessagingTemplate.convertAndSend("/public", new SimpleResponse("Server", msg.getFrom() + " subscribed to the game.", response.toString()));
                        break;
                    case USED:
                        logger.info(msg.getFrom() + " was unable to subscribe, because the name was already in use");
                        this.simpleMessagingTemplate.convertAndSend("/private/player/" + msg.getFrom(), new SimpleResponse("Server", "This name is already in use.", response.toString()));
                        break;
                    case GAME:
                        logger.info(msg.getFrom() + " joined to the game.");
                        this.simpleMessagingTemplate.convertAndSend("/public", new SimpleResponse("Server", msg.getFrom() + " joined the game.", response.toString()));
                        this.simpleMessagingTemplate.convertAndSend("/private/player/" + msg.getFrom(), new PlayerStateResponse("Server", "Player update!", "PLAYER", gameService.getPlayer(msg.getFrom())));
                        this.simpleMessagingTemplate.convertAndSend("/private/map/" + msg.getFrom(), new MapStateResponse("Server", "Map update!", "MAP", gameService.getMap()));
                        this.simpleMessagingTemplate.convertAndSend("/public/stock", new ExerciseInfoResponse(msg.getFrom(), "Stocks update!", "STOCK", gameService.getStocks()));
                        this.simpleMessagingTemplate.convertAndSend("/private/exercise/" + msg.getFrom(), new ExerciseInfoResponse(msg.getFrom(), "Exercise update!", "EXERCISE", gameService.getExerciseValues()));

                        break;
                    case OFF:
                        logger.info(msg.getFrom() + "  was unable to subscribe, because the subscription time expired.");
                        this.simpleMessagingTemplate.convertAndSend("/public", new SimpleResponse("Server", msg.getFrom() + " You are late to subscribe to this game.", response.toString()));
                        break;
                }
                break;
        }

    }

    @MessageMapping("/action/move")
    public void handlePlayerMove(@Payload PlayerMoveMessage msg) {
        logger.info(msg.getFrom() + " moved to " + msg.getNewPos());
        gameService.modifyMap(msg);
        this.simpleMessagingTemplate.convertAndSend("/public/map", new MapStateResponse(msg.getFrom(), "Map move update!", "MAP", gameService.getMap()));
    }

    @MessageMapping("/action/occupy")
    public void handlePlayerOccupation(@Payload PlayerOccupationMessage msg) {
        logger.info(msg.getFrom() + " occupied " + msg.getOccupiedField());
        Player prevPlayer = gameService.modifyMap(msg);
        this.simpleMessagingTemplate.convertAndSend("/public/map", new MapStateResponse(msg.getFrom(), "Map occupy update!", "MAP", gameService.getMap()));
        if (prevPlayer != null) {
            this.simpleMessagingTemplate.convertAndSend("/private/player/" + prevPlayer.getName(), new PlayerStateResponse("Server", "Player update!", "PLAYER", prevPlayer));
        }
    }

    @MessageMapping("/action/stock")
    public void handlePlayerBuying(@Payload GameMessage msg) {
        logger.info(msg.getFrom() + " bought a(n) " + msg.getText() + " stock.");
        gameService.modifyStocks(msg);
        this.simpleMessagingTemplate.convertAndSend("/public/stock", new ExerciseInfoResponse(msg.getFrom(), "Stocks update!", "STOCK", gameService.getStocks()));
    }

    @MessageMapping("/action/exercise")
    public void handlePlayerWorkingOut(@Payload PlayerExerciseMessage msg) {
        logger.info(msg.getFrom() + " doin' some workout. ");
        gameService.saveExerciseReps(msg);
    }

    @MessageMapping("/action/vision")
    public void handlePlayerVisionInc(@Payload GameMessage msg) {
        logger.info(msg.getFrom() + " has better vision now. ");
        gameService.saveVisionInc(msg);
    }

    @MessageMapping("/action/convert")
    public void handlePlayerConversion(@Payload PlayerConversionMessage msg) {
        logger.info(msg.getFrom() + " converted " + msg.getAmount() + " score to money. ");
        gameService.executeConversion(msg);
    }

    @MessageMapping("/action/time")
    public void handlePlayerTimeUntilMove(@Payload PlayerTimeMessage msg) {
        logger.info(msg.getFrom() + " logged out and " + msg.getSeconds() + " seconds remained on the clock. ");
        gameService.saveTime(msg);
    }
}
