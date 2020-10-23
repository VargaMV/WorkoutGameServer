package com.msh.WorkoutGameServer.model.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
abstract public class SimpleMessage {
    String from;
    String text;

    public SimpleMessage() {

    }

    public SimpleMessage(String from, String text) {
        this.from = from;
        this.text = text;
    }
}
