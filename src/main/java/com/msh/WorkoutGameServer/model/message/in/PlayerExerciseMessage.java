package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.message.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerExerciseMessage extends GameMessage {

    private String exercise;
    private double amount;

    public PlayerExerciseMessage() {
    }

    public PlayerExerciseMessage(String from, String text, String exercise, double amount) {
        super(MessageType.EXERCISE, from, text);
        this.exercise = exercise;
        this.amount = amount;
    }
}
