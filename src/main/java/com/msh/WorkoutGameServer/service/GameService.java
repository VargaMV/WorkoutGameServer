package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.model.*;
import com.msh.WorkoutGameServer.model.message.ConnectionResponseEnum;
import com.msh.WorkoutGameServer.model.message.in.GameMessage;

import java.util.List;
import java.util.Map;

public interface GameService {
    void createGame(Game game);

    void deleteGame(String id);

    void start(String gameId);

    void stop(String gameId);

    ConnectionResponseEnum joinGame(GameMessage msg);

    Player modifyMap(GameMessage msg);

    void modifyStocks(GameMessage msg);

    void saveExerciseReps(GameMessage msg);

    void saveVisionInc(GameMessage msg);

    void executeConversion(GameMessage msg);

    void saveTime(GameMessage msg);

    String getGameId(GameMessage msg);

    GameDTO getGameDTO(String playerName);

    Game getLastGame();

    Game getGame(String id);

    Player getPlayer(String name);

    Field[][] getMap(String playerName);

    Map<String, Integer> getAllStocks(String playerName);

    Map<String, Integer> getStocks(String playerName);

    void showPlayers(String gameId);

    List<Player> getPlayersRanked(String gameId);

    List<SimpleGame> getActiveSimpleGames();

    List<AdminGame> getActiveGames();
}
