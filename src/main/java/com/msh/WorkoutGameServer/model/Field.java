package com.msh.WorkoutGameServer.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Field {
    private Color color;
    private int value;
    private List<Player> playersOnField;

    public Field(){
        color = Color.WHITE;
        value = 0;
        playersOnField = new ArrayList<>();
    }

    public void addPlayerToField(Player player) {
        playersOnField.add(player);
    }

    public Player removePlayerFromField(Player player) {
        int ind = playersOnField.indexOf(player);
        return playersOnField.remove(ind);
    }

    @Override
    public String toString() {
        boolean isPlayer = playersOnField.size() > 0;
        return "[" + (isPlayer ? "x" : " ") + "]";
    }
}
