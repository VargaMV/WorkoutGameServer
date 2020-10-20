package com.msh.WorkoutGameServer.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;

public class MessageController {
    @MessageMapping("/action/move")
    public void handlePlayerMove() {

    }

}
