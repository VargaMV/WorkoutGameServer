package com.msh.WorkoutGameServer.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "Games")
public class Game {
    private int mapSize;
    private Field[][] map;
    private List<Player> players = new ArrayList<>();
    private Map<String, Integer> stockNumbers = new HashMap<>();
    private Map<String, Integer> exerciseValues = new HashMap<>();

    private boolean subscriptionOn;
    private boolean started;

    public Game(int mapSize) {
        this.mapSize = mapSize;
        //TODO: read exerciseValues from csv file
        //TODO: create map
        //TODO: randomize player positions, after starting the game
    }
}
