package com.msh.WorkoutGameServer.model;

import java.awt.*;

public class Player {
    String name;
    Color color;
    Coordinate position;
    int money;
    int currentScore;
    int totalScore;
    int rangeOfVision;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        //this.position = new Coordinate(0,0);
        this.money = Constants.STARTING_MONEY;
        this.currentScore = 0;
        this.totalScore = 0;
        this.rangeOfVision = 1;
    }

}
