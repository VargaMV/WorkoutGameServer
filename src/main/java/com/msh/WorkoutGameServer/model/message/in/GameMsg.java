package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.message.MsgType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GameMsg {
    MsgType type;
    String sender;
}
