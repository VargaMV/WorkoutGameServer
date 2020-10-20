package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.database.GameDataAccess;
import com.msh.WorkoutGameServer.model.Game;
import org.springframework.beans.factory.annotation.Autowired;

public class GameServiceImpl implements GameService {
    @Autowired
    private GameDataAccess gameDataAccess;


    @Override
    public void createGame(Game game) {
        this.gameDataAccess.save(game);
    }
}
