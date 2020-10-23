package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.message.MsgType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameMsg {
    MsgType type;
    String from;
    String text;

    public GameMsg(MsgType type, String sender, String text) {
        this.type = type;
        this.from = sender;
        this.text = text;
    }
}
