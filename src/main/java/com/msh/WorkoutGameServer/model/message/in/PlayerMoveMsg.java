package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.Coordinate;
import com.msh.WorkoutGameServer.model.message.MsgType;
import lombok.Getter;

@Getter
public class PlayerMoveMsg extends GameMsg {

    Coordinate from;
    Coordinate to;

    PlayerMoveMsg(String sender, String text, Coordinate from, Coordinate to) {
        super(MsgType.MOVE, sender, text);
        this.from = from;
        this.to = to;
    }
}
