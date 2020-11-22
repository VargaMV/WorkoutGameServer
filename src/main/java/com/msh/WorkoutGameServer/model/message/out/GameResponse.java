package com.msh.WorkoutGameServer.model.message.out;

import com.msh.WorkoutGameServer.model.GameDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResponse extends SimpleResponse {
    private GameDTO game;

    public GameResponse() {

    }

    public GameResponse(String from, String text, String response, GameDTO game) {
        super(from, text, response);
        this.game = game;
    }
}
