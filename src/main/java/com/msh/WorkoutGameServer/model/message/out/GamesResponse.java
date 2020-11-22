package com.msh.WorkoutGameServer.model.message.out;

import com.msh.WorkoutGameServer.model.AdminGame;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GamesResponse extends SimpleResponse {

    List<AdminGame> games;

    public GamesResponse() {

    }

    public GamesResponse(String from, String text, String response, List<AdminGame> games) {
        super(from, text, response);
        this.games = games;
    }
}
