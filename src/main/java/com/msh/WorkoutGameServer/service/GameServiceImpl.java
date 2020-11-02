package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.database.GameDataAccess;
import com.msh.WorkoutGameServer.model.Coordinate;
import com.msh.WorkoutGameServer.model.Field;
import com.msh.WorkoutGameServer.model.Game;
import com.msh.WorkoutGameServer.model.Player;
import com.msh.WorkoutGameServer.model.message.MessageType;
import com.msh.WorkoutGameServer.model.message.in.*;
import com.msh.WorkoutGameServer.model.message.out.JoinResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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
        game.setRunning(true);
        game.setSubscriptionOn(false);
        game.randomizePlayerPositions();
        game.setVisionIncPriceForPlayers();
        this.gameDataAccess.save(game);
        System.out.println(game);
    }

    @Override
    public void stop() {
        Game game = getGame();
        game.setRunning(false);
    }

    @Override
    public JoinResponse joinGame(GameMessage msg) {
        String playerName = msg.getFrom();
        Game game = getGame();
        Player player = game.getPlayerByName(playerName);
        if (player != null) {
            player.setLastConnect(LocalDateTime.now());
            if (player.getLastDisconnect() != null) {
                Duration duration = Duration.between(player.getLastDisconnect(), player.getLastConnect());
                int time = player.getSecondsUntilMove();
                player.setSecondsUntilMove((int) Math.max(time - duration.getSeconds(), 0));
                gameDataAccess.save(game);
            }
        }
        if (game.isRunning() && game.isPlayerExist(playerName)) {
            return JoinResponse.GAME;
        } else if (game.isPlayerExist(playerName)) {
            return JoinResponse.USED;
        } else if (game.isSubscriptionOn() && game.getFreeColors().size() > 0) {
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
    public Map<String, Integer> getExerciseValues() {
        Game game = getGame();
        return game.getExerciseValues();
    }

    @Override
    public Player modifyMap(GameMessage msg) {
        String playerName = msg.getFrom();
        Game game = getGame();
        if (msg.getType() == MessageType.MOVE) {
            Coordinate from = ((PlayerMoveMessage) msg).getPrevPos();
            Coordinate to = ((PlayerMoveMessage) msg).getNewPos();
            game.setPlayerPosition(playerName, to);
            game.setPlayerPositionOnMap(playerName, from, to);
            this.gameDataAccess.save(game);
        } else if (msg.getType() == MessageType.OCCUPY) {
            Coordinate target = ((PlayerOccupationMessage) msg).getOccupiedField();
            Player newOwner = game.getPlayerByName(playerName);
            Player prevPlayer = game.occupy(target, newOwner);
            this.gameDataAccess.save(game);
            return prevPlayer;
        }
        return null;
    }

    @Override
    public void modifyStocks(GameMessage msg) {
        String playerName = msg.getFrom();
        Game game = getGame();
        String exercise = msg.getText();
        game.stockBought(playerName, exercise);
        this.gameDataAccess.save(game);
    }

    @Override
    public void saveExerciseReps(GameMessage msg) {
        String playerName = msg.getFrom();
        Game game = getGame();
        String exercise = ((PlayerExerciseMessage) msg).getExercise();
        int amount = ((PlayerExerciseMessage) msg).getAmount();
        game.exerciseDone(playerName, exercise, amount);
        this.gameDataAccess.save(game);
    }

    @Override
    public void saveVisionInc(GameMessage msg) {
        String playerName = msg.getFrom();
        Game game = getGame();
        game.incVision(playerName);
        this.gameDataAccess.save(game);
    }

    @Override
    public void executeConversion(GameMessage msg) {
        String playerName = msg.getFrom();
        Game game = getGame();
        game.convertScoreToMoney(playerName, ((PlayerConversionMessage) msg).getAmount());
        this.gameDataAccess.save(game);
    }

    @Override
    public void saveTime(GameMessage msg) {
        String playerName = msg.getFrom();
        Game game = getGame();
        Player player = game.getPlayerByName(playerName);
        player.setLastDisconnect(LocalDateTime.now());
        int remainingTime = ((PlayerTimeMessage) msg).getSeconds();
        player.setSecondsUntilMove(remainingTime);
        this.gameDataAccess.save(game);
    }
}
