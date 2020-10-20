package com.msh.WorkoutGameServer.logic;

import com.msh.WorkoutGameServer.model.Game;
import com.msh.WorkoutGameServer.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;

public class GameLogic {

    @Autowired
    private GameService gameService;

    public void init() {
        Game game = new Game(8);
        gameService.createGame(game);
    }
}
