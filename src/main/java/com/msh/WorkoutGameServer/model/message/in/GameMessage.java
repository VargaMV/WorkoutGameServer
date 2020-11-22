package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.message.MessageType;
import com.msh.WorkoutGameServer.model.message.SimpleMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameMessage extends SimpleMessage {

    MessageType type;

    public GameMessage() {
    }

    public GameMessage(MessageType type, String from, String text) {
        super(from, text);
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString() +
                " type=" + type;
    }
}
