package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.database.GameDataAccess;
import com.msh.WorkoutGameServer.model.Coordinate;
import com.msh.WorkoutGameServer.model.Game;
import com.msh.WorkoutGameServer.model.JoinResponse;
import com.msh.WorkoutGameServer.model.message.in.GameMessage;
import com.msh.WorkoutGameServer.model.message.in.PlayerMoveMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private GameDataAccess gameDataAccess;

    @Override
    public void createGame(Game game) {
        this.gameDataAccess.save(game);
    }

    @Override
    public void switchSub() {
        Game latestGame = this.gameDataAccess.findFirstByOrderByIdDesc();
        boolean sub = latestGame.isSubscriptionOn();
        latestGame.setSubscriptionOn(!sub);
        this.gameDataAccess.save(latestGame);
        System.out.println(latestGame);
    }

    @Override
    public void switchSubTo(boolean to) {
        Game game = getGame();
        game.setSubscriptionOn(to);
        this.gameDataAccess.save(game);
        System.out.println(game);
    }


    @Override
    public void start() {
        Game game = getGame();
        game.setStarted(true);
        game.setSubscriptionOn(false);
        this.gameDataAccess.save(game);
        System.out.println(game);
    }

    @Override
    public void stop() {
        System.out.println("Stopping game...");
    }

    @Override
    public JoinResponse joinGame(GameMessage msg) {
        String playerName = msg.getFrom();
        Game game = getGame();
        if (game.isStarted() && game.isPlayerExist(playerName)) {
            return JoinResponse.GAME;
        } else if (game.isPlayerExist(playerName)) {
            return JoinResponse.USED;
        } else if (game.isSubscriptionOn()) {
            game.addPlayer(playerName);
            gameDataAccess.save(game);
            return JoinResponse.SUB;
        } else {
            return JoinResponse.OFF;
        }
    }

    @Override
    public Game getGame() {
        return this.gameDataAccess.findFirstByOrderByIdDesc();
    }

    @Override
    public void modifyMap(GameMessage msg) {
        String playerName = msg.getFrom();
        Coordinate from = ((PlayerMoveMessage) msg).getPrevLocation();
        Coordinate to = ((PlayerMoveMessage) msg).getNextLocation();
        Game game = getGame();
        game.setPlayerPosition(playerName, to);
        game.setPlayerPositionOnMap(playerName, from, to);
        this.gameDataAccess.save(game);
    }
}
