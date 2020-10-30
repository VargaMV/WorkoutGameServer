package com.msh.WorkoutGameServer.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Field {
    private SimplePlayer owner;
    private int value;
    private List<SimplePlayer> playersOnField;

    public Field(){
        owner = new SimplePlayer("-", Color.WHITE);
        value = 0;
        playersOnField = new ArrayList<>();
    }

    public Color getColor() {
        return owner.getColor();
    }

    public void addPlayerToField(Player player) {
        playersOnField.add(new SimplePlayer(player));
    }

    @Override
    public String toString() {
        boolean isPlayer = playersOnField.size() > 0;
        return "[" + (isPlayer ? "x" : " ") + "]";
    }
}
