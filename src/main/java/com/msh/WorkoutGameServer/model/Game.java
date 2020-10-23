package com.msh.WorkoutGameServer.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        //TODO: read exerciseValues from csv file
        //TODO: create map
        //TODO: randomize player positions, after starting the game
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

    public void setPlayerPosition(String playerName, Coordinate position) {
        players.stream()
                .filter(p -> p.getName().equals(playerName))
                .forEach(p -> p.setPosition(position));
    }

    public void setPlayerPositionOnMap(String playerName, Coordinate from, Coordinate to) {
        List<String> fromFieldPlayers = map[from.getX()][from.getY()].getPlayersOnField();
        List<String> newPlayerList = fromFieldPlayers.stream().filter(p -> !p.equals(playerName)).collect(Collectors.toList());
        map[from.getX()][from.getY()].setPlayersOnField(newPlayerList);

        map[to.getX()][to.getY()].getPlayersOnField().add(playerName);
    }

    public void addPlayer(String playerName) {
        Random rand = new Random();
        Color color = freeColors.remove(rand.nextInt(10));
        Player newPlayer = new Player(playerName, color);
        players.add(newPlayer);
    }

    public boolean isPlayerExist(String name) {
        return players.stream().anyMatch(p -> p.getName().equals(name));
    }
}
