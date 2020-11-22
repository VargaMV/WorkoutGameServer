package com.msh.WorkoutGameServer.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AdminGame {

    private String id;
    private String title;
    private int mapSize;
    private List<Player> players = new ArrayList<>();
    private boolean subscriptionOn;
    private boolean running;
    private int waitingTime;
    private double priceIncExponent;

    public AdminGame() {

    }

    public AdminGame(String id, String title, int mapSize, List<Player> players, boolean subscriptionOn, boolean running, int waitingTime, double priceIncExponent) {
        this.id = id;
        this.title = title;
        this.mapSize = mapSize;
        this.players = players;
        this.subscriptionOn = subscriptionOn;
        this.running = running;
        this.waitingTime = waitingTime;
        this.priceIncExponent = priceIncExponent;
    }

    public AdminGame(Game game) {
        this.id = game.getId();
        this.title = game.getTitle();
        this.mapSize = game.getMapSize();
        this.players = game.getPlayers();
        this.running = game.isRunning();
        this.subscriptionOn = game.isSubscriptionOn();
        this.running = game.isRunning();
        this.waitingTime = game.getWaitingTime();
        this.priceIncExponent = game.getPriceIncExponent();
    }
}
