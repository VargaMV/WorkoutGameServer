package com.msh.WorkoutGameServer.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private boolean subscriptionOn;
    private boolean started;

    private LocalDateTime creationDate;

    public Game(String title, int mapSize) {
        this.title = title;
        this.mapSize = mapSize;
        this.creationDate = LocalDateTime.now();
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
        //TODO: color
        Player newPlayer = new Player(playerName, Color.GREEN);
        players.add(newPlayer);
    }

    public boolean isNameInUse(String name) {
        return players.stream().anyMatch(p -> p.getName().equals(name));
    }
}
