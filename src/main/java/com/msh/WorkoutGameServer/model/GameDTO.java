package com.msh.WorkoutGameServer.model;

import lombok.Builder;

import java.util.Map;

@Builder
public class GameDTO {

    public String id;
    public Field[][] map;
    public Player player;
    public Map<String, Integer> totalStockNumbers;
    public Map<String, Exercise> exerciseValues;
    public int waitingTime;
    public double priceIncExponent;

    public GameDTO() {
    }

    public GameDTO(String id, Field[][] map, Player player, Map<String, Integer> totalStockNumbers, Map<String, Exercise> exerciseValues, int waitingTime, double priceIncExponent) {
        this.id = id;
        this.map = map;
        this.player = player;
        this.totalStockNumbers = totalStockNumbers;
        this.exerciseValues = exerciseValues;
        this.waitingTime = waitingTime;
        this.priceIncExponent = priceIncExponent;
    }
}
