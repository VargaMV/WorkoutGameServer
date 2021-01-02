package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.message.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerBuyMessage extends GameMessage {

    private String exercise;
    private int amount;

    public PlayerBuyMessage() {
    }

    public PlayerBuyMessage(String from, String text, String exercise, int amount) {
        super(MessageType.STOCK, from, text);
        this.exercise = exercise;
        this.amount = amount;
    }
}
