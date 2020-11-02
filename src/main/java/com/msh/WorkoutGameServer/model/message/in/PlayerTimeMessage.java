package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.message.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerTimeMessage extends GameMessage {

    private int seconds;

    public PlayerTimeMessage() {
    }

    public PlayerTimeMessage(String from, String text, int seconds) {
        super(MessageType.TIME, from, text);
        this.seconds = seconds;
    }
}
