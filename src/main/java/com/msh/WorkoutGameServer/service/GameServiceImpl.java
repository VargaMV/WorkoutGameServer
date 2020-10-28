package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.database.GameDataAccess;
import com.msh.WorkoutGameServer.model.Coordinate;
import com.msh.WorkoutGameServer.model.Field;
import com.msh.WorkoutGameServer.model.Game;
import com.msh.WorkoutGameServer.model.Player;
import com.msh.WorkoutGameServer.model.message.MessageType;
import com.msh.WorkoutGameServer.model.message.SimpleMessage;
import com.msh.WorkoutGameServer.model.message.in.GameMessage;
import com.msh.WorkoutGameServer.model.message.in.PlayerMoveMessage;
import com.msh.WorkoutGameServer.model.message.in.PlayerOccupationMessage;
import com.msh.WorkoutGameServer.model.message.out.JoinResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
        game.randomizePlayerPositions();
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
    public Player getPlayer(String name) {
        Game game = getGame();
        return game.getPlayerByName(name);
    }

    @Override
    public Field[][] getMap() {
        Game game = getGame();
        return game.getMap();
    }

    @Override
    public Map<String, Integer> getStocks() {
        Game game = getGame();
        return game.getTotalStockNumbers();
    }

    @Override
    public void modifyMap(GameMessage msg) {
        String playerName = msg.getFrom();
        Game game = getGame();
        if (msg.getType() == MessageType.MOVE) {
            Coordinate from = ((PlayerMoveMessage) msg).getPrevPos();
            Coordinate to = ((PlayerMoveMessage) msg).getNewPos();

            game.setPlayerPosition(playerName, to);
            game.setPlayerPositionOnMap(playerName, from, to);
        } else if (msg.getType() == MessageType.OCCUPY) {
            Coordinate target = ((PlayerOccupationMessage) msg).getOccupiedField();
            Player newOwner = game.getPlayerByName(playerName);
            game.occupy(target, newOwner);
        }

        this.gameDataAccess.save(game);
    }

    @Override
    public void modifyStocks(SimpleMessage msg) {
        String playerName = msg.getFrom();
        Game game = getGame();
        String exercise = msg.getText();
        game.stockBought(playerName, exercise);
        this.gameDataAccess.save(game);
    }
}
