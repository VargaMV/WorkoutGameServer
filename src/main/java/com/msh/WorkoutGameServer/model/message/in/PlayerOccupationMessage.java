package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.Coordinate;
import com.msh.WorkoutGameServer.model.message.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerOccupationMessage extends GameMessage {

    Coordinate occupiedField;

    public PlayerOccupationMessage() {
    }

    public PlayerOccupationMessage(String from, String text, Coordinate occupiedField) {
        super(MessageType.OCCUPY, from, text);
        this.occupiedField = occupiedField;
    }
}
