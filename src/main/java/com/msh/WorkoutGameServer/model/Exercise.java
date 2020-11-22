package com.msh.WorkoutGameServer.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Exercise {
    private String name;
    private String type;
    private int value;

    public Exercise() {
    }

    public Exercise(String name, String type, int value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
}
