package com.msh.WorkoutGameServer.model;

import com.msh.WorkoutGameServer.logic.PriceCalculator;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

@Getter
@Setter
public class Player implements Comparable<Player>, Serializable {
    private String name;
    private Color color;
    private Coordinate position;
    private int money;
    private int currentScore;
    private int totalScore;
    private int fieldsOwned;
    private boolean sqrRange;
    private int rangeOfVision;
    private int visionIncPrice;
    private int secondsUntilMove;
    private LocalDateTime lastConnect;
    private LocalDateTime lastDisconnect;
    private Map<String, Integer> exerciseNumbers = new LinkedHashMap<>();
    private Map<String, Integer> stockNumbers = new LinkedHashMap<>();

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
        this.position = new Coordinate(0, 0);
        this.money = Constants.STARTING_MONEY;
        this.currentScore = 0;
        this.totalScore = 0;
        this.sqrRange = false;
        this.rangeOfVision = 1;

        File data = new File("data.csv");
        try (Scanner scanner = new Scanner(data)) {
            while (scanner.hasNextLine()) {
                String dataLine = scanner.nextLine();
                String exercise = dataLine.split(",")[0].trim();
                exerciseNumbers.put(exercise, 0);
                stockNumbers.put(exercise, 0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void incMoney(int earning) {
        money += earning;
    }

    public void decMoney(int cost) {
        money -= cost;
    }

    public void incScore(int score) {
        currentScore += score;
        totalScore += score;
    }

    public void decScore(int score) {
        currentScore -= score;
    }

    public void incFieldsOwned() {
        fieldsOwned++;
    }

    public void decFieldsOwned() {
        fieldsOwned--;
    }

    public boolean isStockAffordable(String exercise) {
        return money >= PriceCalculator.calculateNext(stockNumbers.get(exercise));
    }

    public boolean isVisionIncAffordable() {
        return money >= visionIncPrice;
    }

    public void incRangeOfVision() {
        if (!isVisionMax() && isVisionIncAffordable()) {
            if (sqrRange) {
                rangeOfVision++;
                sqrRange = false;
            } else {
                sqrRange = true;
            }
            money -= visionIncPrice;
        }
    }

    public boolean isVisionMax() {
        return (sqrRange && rangeOfVision == 2);
    }


    public void incExerciseValue(String exercise, int newValue) {
        int prevValue = exerciseNumbers.get(exercise);
        exerciseNumbers.put(exercise, prevValue + newValue);
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
