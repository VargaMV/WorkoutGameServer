package com.msh.WorkoutGameServer.model.message.out;

import com.msh.WorkoutGameServer.model.SimpleGame;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SimpleGamesResponse extends SimpleResponse {

    List<SimpleGame> games;

    public SimpleGamesResponse() {

    }

    public SimpleGamesResponse(String from, String text, String response, List<SimpleGame> games) {
        super(from, text, response);
        this.games = games;
    }
}
