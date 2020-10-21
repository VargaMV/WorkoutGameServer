package com.msh.WorkoutGameServer.controller;

import com.msh.WorkoutGameServer.model.message.in.GameMsg;
import com.msh.WorkoutGameServer.service.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

public class MessageController {

    @Autowired
    GameServiceImpl gameService;

    @Autowired
    SimpMessageSendingOperations simpleMessagingTemplate;

    @MessageMapping("/action")
    public void handlePlayerAction(@Payload GameMsg msg) {
        switch (msg.getType()) {
            case JOIN:
                boolean success = gameService.joinGame(msg);
                if (success) {
                    this.simpleMessagingTemplate.convertAndSend("/public", msg.getSender() + " joined");
                } else {
                    this.simpleMessagingTemplate.convertAndSend("player/" + msg.getSender(), "Used name or game is closed.");
                }
            case MOVE:
                gameService.modifyMap(msg);
                this.simpleMessagingTemplate.convertAndSend("/state", gameService.getGame().getMap());
        }
    }

}
