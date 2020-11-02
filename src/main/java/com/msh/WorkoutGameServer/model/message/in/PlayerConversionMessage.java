package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.message.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerConversionMessage extends GameMessage {

    private int amount;

    public PlayerConversionMessage() {
    }

    public PlayerConversionMessage(String from, String text, String exercise, int amount) {
        super(MessageType.CONVERT, from, text);
        this.amount = amount;
    }
}
