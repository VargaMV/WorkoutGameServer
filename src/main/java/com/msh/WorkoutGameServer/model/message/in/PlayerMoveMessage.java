package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.Coordinate;
import com.msh.WorkoutGameServer.model.message.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerMoveMessage extends GameMessage {

    Coordinate prevLocation;
    Coordinate nextLocation;

    PlayerMoveMessage(String sender, String text, Coordinate prevLocation, Coordinate nextLocation) {
        super(MessageType.MOVE, sender, text);
        this.prevLocation = prevLocation;
        this.nextLocation = nextLocation;
    }
}
