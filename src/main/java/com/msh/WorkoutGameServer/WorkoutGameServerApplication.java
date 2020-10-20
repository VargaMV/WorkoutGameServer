package com.msh.WorkoutGameServer;

import com.msh.WorkoutGameServer.logic.GameLogic;
import com.msh.WorkoutGameServer.model.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkoutGameServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkoutGameServerApplication.class, args);
		GameLogic gameLogic = new GameLogic();
		gameLogic.init();
	}

}
