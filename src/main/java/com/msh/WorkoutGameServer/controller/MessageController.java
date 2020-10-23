package com.msh.WorkoutGameServer.controller;

import com.msh.WorkoutGameServer.model.message.in.SimpleMessage;
import com.msh.WorkoutGameServer.service.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @Autowired
    GameServiceImpl gameService;

    @Autowired
    SimpMessageSendingOperations simpleMessagingTemplate;

    @MessageMapping("/action")
    @SendTo("/public")
    public SimpleMessage handlePlayerAction(@Payload SimpleMessage msg) {
        System.out.println("Somebody connected: " + msg.getFrom());
        return new SimpleMessage("Server", "Hi you!");
        /*switch (msg.getType()) {
            case JOIN:
                System.out.println("Somebody wants to join.");
                /*boolean success = gameService.joinGame(msg);
                if (success) {
                    this.simpleMessagingTemplate.convertAndSend("/public", msg.getSender() + " joined");
                } else {
                    this.simpleMessagingTemplate.convertAndSend("player/" + msg.getSender(), "Used name or game is closed.");
                }
            case MOVE:
                gameService.modifyMap(msg);
                this.simpleMessagingTemplate.convertAndSend("/public", gameService.getGame().getMap());
        }*/
    }

}
