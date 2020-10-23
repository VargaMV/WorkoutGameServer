package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.model.Game;
import com.msh.WorkoutGameServer.model.JoinResponse;
import com.msh.WorkoutGameServer.model.message.in.GameMessage;

public interface GameService {
    void createGame(Game game);

    void switchSub();

    void switchSubTo(boolean to);

    void start();

    void stop();

    JoinResponse joinGame(GameMessage msg);

    void modifyMap(GameMessage msg);

    Game getGame();
}
