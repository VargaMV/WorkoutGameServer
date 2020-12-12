package com.msh.WorkoutGameServer.service;

import com.msh.WorkoutGameServer.database.GameDataAccess;
import com.msh.WorkoutGameServer.model.*;
import com.msh.WorkoutGameServer.model.message.ConnectionResponseEnum;
import com.msh.WorkoutGameServer.model.message.MessageType;
import com.msh.WorkoutGameServer.model.message.in.*;
import com.msh.WorkoutGameServer.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public
class GameServiceImpl implements GameService {

    @Autowired
    private GameDataAccess gameDataAccess;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public boolean createGame(Game game) {
        if (gameDataAccess.findByTitle(game.getTitle()) == null) {
            this.gameDataAccess.save(game);
            return true;
        }
        return false;
    }

    @Override
    public void deleteGame(String id) {
        gameDataAccess.deleteById(id);
    }

    @Override
    public void start(String id) {
        Game game = getGame(id);
        game.setRunning(true);
        game.setSubscriptionOn(false);
        game.randomizePlayerPositions();
        game.setVisionIncPriceForPlayers();
        this.gameDataAccess.save(game);
    }

    @Override
    public void stop(String id) {
        Game game = getGame(id);
        game.setRunning(false);
        gameDataAccess.save(game);
        List<Player> players = game.getResults();
        int i = 1;
        for (var player : players) {
            userService.setCurrentGameId(player.getName(), "");
            System.out.printf("%d. place: %s -> fields: %d -> total: %d\n", i, player.getName(), player.getFieldsOwned(), player.getTotalScore());
            i++;
        }
    }

    @Override
    public ConnectionResponseEnum joinGame(GameMessage msg) {
        String playerName = msg.getFrom();
        if (!msg.getText().equals("")) {
            userService.setCurrentGameId(playerName, msg.getText());
        }
        User user = userService.findByName(playerName);
        if (user.getCurrentGameId().equals("")) {
            return ConnectionResponseEnum.NULL;
        }
        Game game = getGame(user.getCurrentGameId());
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
            return ConnectionResponseEnum.GAME;
        } else if (game.isPlayerExist(playerName)) {
            return ConnectionResponseEnum.USED;
        } else if (game.isSubscriptionOn() && game.getFreeColors().size() > 0) {
            game.addPlayer(playerName);
            gameDataAccess.save(game);
            return ConnectionResponseEnum.SUB;
        } else {
            return ConnectionResponseEnum.OFF;
        }
    }

    @Override
    public Game getGame(String id) {
        return gameDataAccess.findById(id).orElse(null);
    }

    @Override
    public Game getLastGame() {
        return this.gameDataAccess.findFirstByOrderByIdDesc();
    }

    @Override
    public Player getPlayer(String playerName) {
        User user = userService.findByName(playerName);
        Game game = getGame(user.getCurrentGameId());
        return game.getPlayerByName(playerName);
    }

    @Override
    public Field[][] getMap(String playerName) {
        User user = userService.findByName(playerName);
        Game game = getGame(user.getCurrentGameId());
        return game.getMap();
    }

    @Override
    public Map<String, Integer> getAllStocks(String playerName) {
        User user = userService.findByName(playerName);
        Game game = getGame(user.getCurrentGameId());
        return game.getTotalStockNumbers();
    }

    @Override
    public Map<String, Integer> getStocks(String playerName) {
        User user = userService.findByName(playerName);
        Game game = getGame(user.getCurrentGameId());
        return game.getPlayerByName(playerName).getStockNumbers();
    }

    @Override
    public void showPlayers(String gameId) {
        Game game = getGame(gameId);
        game.getPlayers().forEach(System.out::println);
    }

    @Override
    public List<Player> getPlayersRanked(String gameId) {
        return getGame(gameId).getResults();
    }

    @Override
    public List<SimpleGame> getActiveSimpleGames() {
        return gameDataAccess.findAll()
                .stream()
                .filter(g -> (g.isRunning() || g.isSubscriptionOn()))
                .map(SimpleGame::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminGame> getActiveGames() {
        return gameDataAccess.findAll()
                .stream()
                .filter(g -> (g.isRunning() || g.isSubscriptionOn()))
                .map(AdminGame::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminGame> getArchiveGames() {
        return gameDataAccess.findAll()
                .stream()
                .filter(g -> (!g.isRunning() && !g.isSubscriptionOn()))
                .map(AdminGame::new)
                .collect(Collectors.toList());
    }

    @Override
    public Player modifyMap(GameMessage msg) {
        String playerName = msg.getFrom();
        User user = userService.findByName(playerName);
        Game game = getGame(user.getCurrentGameId());
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
        User user = userService.findByName(playerName);
        Game game = getGame(user.getCurrentGameId());
        String exercise = msg.getText();
        double exp = game.getPriceIncExponent();
        game.stockBought(playerName, exercise, exp);
        this.gameDataAccess.save(game);
    }

    @Override
    public void saveExerciseReps(GameMessage msg) {
        String playerName = msg.getFrom();
        User user = userService.findByName(playerName);
        Game game = getGame(user.getCurrentGameId());
        String exercise = ((PlayerExerciseMessage) msg).getExercise();
        double amount = ((PlayerExerciseMessage) msg).getAmount();
        game.exerciseDone(playerName, exercise, amount);
        this.gameDataAccess.save(game);
    }

    @Override
    public void saveVisionInc(GameMessage msg) {
        String playerName = msg.getFrom();
        User user = userService.findByName(playerName);
        Game game = getGame(user.getCurrentGameId());
        game.incVision(playerName);
        this.gameDataAccess.save(game);
    }

    @Override
    public void executeConversion(GameMessage msg) {
        String playerName = msg.getFrom();
        User user = userService.findByName(playerName);
        Game game = getGame(user.getCurrentGameId());
        game.convertScoreToMoney(playerName, ((PlayerConversionMessage) msg).getAmount());
        this.gameDataAccess.save(game);
    }

    @Override
    public void saveTime(GameMessage msg) {
        String playerName = msg.getFrom();
        User user = userService.findByName(playerName);
        Game game = getGame(user.getCurrentGameId());
        Player player = game.getPlayerByName(playerName);
        player.setLastDisconnect(LocalDateTime.now());
        int remainingTime = ((PlayerTimeMessage) msg).getSeconds();
        player.setSecondsUntilMove(remainingTime);
        this.gameDataAccess.save(game);
    }

    @Override
    public String getGameId(GameMessage msg) {
        String playerName = msg.getFrom();
        User user = userService.findByName(playerName);
        Game game = getGame(user.getCurrentGameId());
        return game.getId();
    }

    @Override
    public GameDTO getGameDTO(String playerName) {
        User user = userService.findByName(playerName);
        Game game = getGame(user.getCurrentGameId());
        return GameDTO.builder()
                .id(game.getId())
                .map(game.getMap())
                .player(game.getPlayerByName(playerName))
                .exerciseValues(game.getExerciseValues())
                .totalStockNumbers(game.getTotalStockNumbers())
                .waitingTime(game.getWaitingTime())
                .priceIncExponent(game.getPriceIncExponent())
                .build();
    }

}
