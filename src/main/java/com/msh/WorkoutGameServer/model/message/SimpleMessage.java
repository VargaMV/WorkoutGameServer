package com.msh.WorkoutGameServer.model.message;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
abstract public class SimpleMessage implements Serializable {
    String from;
    String text;

    public SimpleMessage() {

    }

    public SimpleMessage(String from, String text) {
        this.from = from;
        this.text = text;
    }
}
