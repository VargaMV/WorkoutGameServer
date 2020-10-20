package com.msh.WorkoutGameServer.model;

import lombok.Getter;

import java.awt.*;

@Getter
public class Field {
    private Color color;
    private int value;

    public Field(){
        color = Color.white;
        value = 0;
    }
}
