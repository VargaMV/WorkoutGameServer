package com.msh.WorkoutGameServer.model;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Field {
    private Color color;
    private int value;
    private List<String> playersOnField;

    public Field(){
        color = Color.white;
        value = 0;
        playersOnField = new ArrayList<>();
    }
}
