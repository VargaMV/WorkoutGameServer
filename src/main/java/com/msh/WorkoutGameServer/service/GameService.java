package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.model.Game;
import com.msh.WorkoutGameServer.model.message.in.GameMsg;

public interface GameService {
    void createGame(Game game);

    void switchSub();

    void switchSubTo(boolean to);

    void start();

    void stop();

    boolean joinGame(GameMsg msg);

    void modifyMap(GameMsg msg);

    Game getGame();
}
