package com.msh.WorkoutGameServer.logic;

import com.msh.WorkoutGameServer.model.Game;
import com.msh.WorkoutGameServer.service.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class GameRunner implements CommandLineRunner {

    @Autowired
    private GameServiceImpl gameService;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("New game? (y/n)");
        String response = scanner.nextLine();
        while (!response.equals("y") && !response.equals("n")) {
            response = scanner.nextLine();
        }
        if (response.equals("y")) {
            System.out.println("Title:");
            String title = scanner.nextLine();
            System.out.println("Map size (3-12):");
            int size = scanner.nextInt();
            scanner.nextLine();

            Game game = new Game(title, size);
            gameService.createGame(game);
        }
        String command = "";
        while (!"terminate".equals(command)) {
            command = scanner.nextLine();
            switch (command) {
                case "switchSub":
                    gameService.switchSub();
                    break;
                case "start":
                    gameService.start();
                    break;
                case "stop":
                    gameService.stop();
                    break;
                default:
                    System.out.println("Illegal command.");
                    break;
            }
        }
    }
}
