package com.msh.WorkoutGameServer.model.message.in;

import lombok.Getter;

@Getter
public class SimpleMessage {
    String from;
    String text;

    public SimpleMessage(String from, String text) {
        this.from = from;
        this.text = text;
    }
}
