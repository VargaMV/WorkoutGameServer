package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.model.Field;
import com.msh.WorkoutGameServer.model.Game;
import com.msh.WorkoutGameServer.model.Player;
import com.msh.WorkoutGameServer.model.message.SimpleMessage;
import com.msh.WorkoutGameServer.model.message.in.GameMessage;
import com.msh.WorkoutGameServer.model.message.out.JoinResponse;

import java.util.Map;

public interface GameService {
    void createGame(Game game);

    void switchSub();

    void switchSubTo(boolean to);

    void start();

    void stop();

    JoinResponse joinGame(GameMessage msg);

    void modifyMap(GameMessage msg);

    void modifyStocks(SimpleMessage msg);

    Game getGame();

    Player getPlayer(String name);

    Field[][] getMap();

    Map<String, Integer> getStocks();
}
