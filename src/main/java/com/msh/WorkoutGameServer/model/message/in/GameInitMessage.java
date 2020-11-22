package com.msh.WorkoutGameServer.model.message.in;

import com.msh.WorkoutGameServer.model.GameInit;
import com.msh.WorkoutGameServer.model.message.SimpleMessage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameInitMessage extends SimpleMessage {

    private GameInit gameInit;

    public GameInitMessage() {
    }

    public GameInitMessage(String from, String text, GameInit gameInit) {
        super(from, text);
        this.gameInit = gameInit;
    }
}
