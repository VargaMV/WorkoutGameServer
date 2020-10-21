package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.database.GameDataAccess;
import com.msh.WorkoutGameServer.model.Coordinate;
import com.msh.WorkoutGameServer.model.Game;
import com.msh.WorkoutGameServer.model.message.in.GameMsg;
import com.msh.WorkoutGameServer.model.message.in.PlayerMoveMsg;
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
    }

    @Override
    public void stop() {
        System.out.println("Stopping game...");
    }

    @Override
    public boolean joinGame(GameMsg msg) {
        String playerName = msg.getSender();
        Game game = getGame();
        if (game.isNameInUse(playerName) || !game.isSubscriptionOn()) {
            return false;
        }
        game.addPlayer(playerName);
        gameDataAccess.save(game);
        return true;
    }

    @Override
    public Game getGame() {
        return this.gameDataAccess.findFirstByOrderByIdDesc();
    }

    @Override
    public void modifyMap(GameMsg msg) {
        String playerName = msg.getSender();
        Coordinate from = ((PlayerMoveMsg) msg).getFrom();
        Coordinate to = ((PlayerMoveMsg) msg).getTo();
        Game game = getGame();
        game.setPlayerPosition(playerName, to);
        game.setPlayerPositionOnMap(playerName, from, to);
        this.gameDataAccess.save(game);
    }
}
