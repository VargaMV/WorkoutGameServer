package com.msh.WorkoutGameServer.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class Player implements Comparable<Player>, Serializable {
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
        this.position = new Coordinate(0, 0);
        this.money = Constants.STARTING_MONEY;
        this.currentScore = 0;
        this.totalScore = 0;
        this.rangeOfVision = 1;
    }

    @Override
    public int compareTo(Player o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + " -> " + color + " -> pos:" + position;
    }
}
