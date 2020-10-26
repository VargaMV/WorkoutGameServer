package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.Coordinate;
import com.msh.WorkoutGameServer.model.message.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerMoveMessage extends GameMessage {

    Coordinate prevPos;
    Coordinate newPos;

    public PlayerMoveMessage() {
    }

    public PlayerMoveMessage(String from, String text, Coordinate prevPos, Coordinate newPos) {
        super(MessageType.MOVE, from, text);
        this.prevPos = prevPos;
        this.newPos = newPos;
    }
}
