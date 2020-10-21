package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.Coordinate;
import com.msh.WorkoutGameServer.model.message.MsgType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlayerMoveMsg extends GameMsg {

    Coordinate from;
    Coordinate to;

    PlayerMoveMsg(String sender) {
        super(MsgType.MOVE, sender);
    }
}
