package com.msh.WorkoutGameServer.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Document(collection = "Games")
public class Game {
    @Id
    private String id;
    private String title;
    private int mapSize;
    private Field[][] map;
    private List<Player> players = new ArrayList<>();
    private Map<String, Integer> stockNumbers = new HashMap<>();
    private Map<String, Integer> exerciseValues = new HashMap<>();
    private List<Color> freeColors = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
            Color.BLACK, Color.BROWN, Color.PURPLE, Color.WHITE, Color.YELLOW, Color.CYAN);

    private boolean subscriptionOn;
    private boolean started;

    private LocalDateTime creationDate;

    public Game(String title, int mapSize) {
        this.title = title;
        this.mapSize = mapSize;
        this.creationDate = LocalDateTime.now();
        this.subscriptionOn = true;
        this.map = new Field[mapSize][mapSize];
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                map[i][j] = new Field();
            }
        }
        //TODO: read exerciseValues from csv file
    }

    @Override
    public String toString() {
        return "Game{" +
                "title=" + title +
                ", mapSize=" + mapSize +
                ", subscriptionOn=" + subscriptionOn +
                ", started=" + started +
                ", creationDate=" + creationDate +
                '}';
    }

    public void randomizePlayerPositions() {
        Random random = new Random();
        int x;
        int y;
        for (var player : players) {
            x = random.nextInt(mapSize);
            y = random.nextInt(mapSize);
            player.setPosition(new Coordinate(x, y));
            map[x][y].addPlayerToField(player);
        }
    }

    public void setPlayerPosition(String playerName, Coordinate position) {
        players.stream()
                .filter(p -> p.getName().equals(playerName))
                .forEach(p -> p.setPosition(position));
    }

    public void setPlayerPositionOnMap(String playerName, Coordinate from, Coordinate to) {
        List<Player> fromFieldPlayers = map[from.getX()][from.getY()].getPlayersOnField();
        Player player = fromFieldPlayers.stream().filter(p -> p.getName().equals(playerName)).findFirst().orElse(null);
        if (player != null) {
            fromFieldPlayers.remove(player);
            map[from.getX()][from.getY()].setPlayersOnField(fromFieldPlayers);
            map[to.getX()][to.getY()].getPlayersOnField().add(player);
        }
    }

    public void addPlayer(String playerName) {
        Random rand = new Random();
        Color color = freeColors.remove(rand.nextInt(freeColors.size()));
        Player newPlayer = new Player(playerName, color);
        players.add(newPlayer);
    }

    public boolean isPlayerExist(String name) {
        return players.stream().anyMatch(p -> p.getName().equals(name));
    }

    public Player getPlayerByName(String name) {
        return players.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }
}
