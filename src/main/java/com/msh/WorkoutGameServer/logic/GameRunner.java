package com.msh.WorkoutGameServer.logic;

import com.msh.WorkoutGameServer.service.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GameRunner implements CommandLineRunner {

    @Autowired
    private GameServiceImpl gameService;

    @Override
    public void run(String... args) throws Exception {
    }
}
