package com.msh.WorkoutGameServer.controller;

import com.msh.WorkoutGameServer.model.JoinResponse;
import com.msh.WorkoutGameServer.model.message.in.GameMessage;
import com.msh.WorkoutGameServer.model.message.out.SimpleResponse;
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
    GameServiceImpl gameService;

    @Autowired
    SimpMessageSendingOperations simpleMessagingTemplate;

    private final Logger logger = LogManager.getLogger(MessageController.class);

    @MessageMapping("/action")
    public void handlePlayerAction(@Payload GameMessage msg) {
        logger.info("Somebody connected: " + msg.getFrom());
        switch (msg.getType()) {
            case JOIN:
                logger.info(msg.getFrom() + " wants to join.");
                JoinResponse response = gameService.joinGame(msg);
                switch (response) {
                    case SUB:
                        logger.info(msg.getFrom() + " successfully subscribed to the game.");
                        this.simpleMessagingTemplate.convertAndSend("/public", new SimpleResponse<>("Server", msg.getFrom() + " subscribed to the game.", response.toString()));
                        break;
                    case USED:
                        logger.info(msg.getFrom() + " was unable to subscribe, because the name was already in use");
                        this.simpleMessagingTemplate.convertAndSend("/private/" + msg.getFrom(), new SimpleResponse<>("Server", "This name is already in use.", response.toString()));
                        break;
                    case GAME:
                        logger.info(msg.getFrom() + " joined to the game.");
                        this.simpleMessagingTemplate.convertAndSend("/public", new SimpleResponse<>("Server", msg.getFrom() + " joined the game.", response.toString()));
                        break;
                    case OFF:
                        logger.info(msg.getFrom() + "  was unable to subscribe, because the subscription time expired.");
                        this.simpleMessagingTemplate.convertAndSend("/public", new SimpleResponse<>("Server", msg.getFrom() + " You are late to subscribe to this game.", response.toString()));
                        break;
                }

            /*case MOVE:
                gameService.modifyMap(msg);
                this.simpleMessagingTemplate.convertAndSend("/public", gameService.getGame().getMap());

             */
        }
    }

}
