package com.msh.WorkoutGameServer.model.message.out;

import com.msh.WorkoutGameServer.model.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlayersResponse extends SimpleResponse {
    private List<Player> players;

    public PlayersResponse() {

    }

    public PlayersResponse(String from, String text, String response, List<Player> players) {
        super(from, text, response);
        this.players = players;
    }
}
