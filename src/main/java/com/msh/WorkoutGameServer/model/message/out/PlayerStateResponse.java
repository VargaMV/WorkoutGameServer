package com.msh.WorkoutGameServer.model.message.out;

import com.msh.WorkoutGameServer.model.Player;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerStateResponse extends SimpleResponse {
    private Player player;

    public PlayerStateResponse() {

    }

    public PlayerStateResponse(String from, String text, String response, Player player) {
        super(from, text, response);
        this.player = player;
    }
}
