package com.msh.WorkoutGameServer.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameInit {
    private String title;
    private int mapSize;
    private int waitingTime;

    public GameInit() {
    }

    public GameInit(String title, int mapSize, int waitingTime) {
        this.title = title;
        this.mapSize = mapSize;
        this.waitingTime = waitingTime;
    }
}
