package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.model.Field;
import com.msh.WorkoutGameServer.model.Game;
import com.msh.WorkoutGameServer.model.Player;
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

    Player modifyMap(GameMessage msg);
    void modifyStocks(GameMessage msg);
    void saveExerciseReps(GameMessage msg);
    void saveVisionInc(GameMessage msg);

    void executeConversion(GameMessage msg);

    void saveTime(GameMessage msg);

    Game getGame();
    Player getPlayer(String name);
    Field[][] getMap();
    Map<String, Integer> getStocks();

    Map<String, Integer> getExerciseValues();
}
