package com.msh.WorkoutGameServer.controller.REST;

import com.msh.WorkoutGameServer.model.Game;
import com.msh.WorkoutGameServer.model.GameInit;
import com.msh.WorkoutGameServer.model.message.ApiResponse;
import com.msh.WorkoutGameServer.model.message.out.GamesResponse;
import com.msh.WorkoutGameServer.model.message.out.SimpleGamesResponse;
import com.msh.WorkoutGameServer.model.message.out.SimpleResponse;
import com.msh.WorkoutGameServer.service.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameServiceImpl gameService;

    @Autowired
    private SimpMessageSendingOperations simpleMessagingTemplate;

    @PostMapping("/init")
    ApiResponse initGame(@RequestBody GameInit gameInit) {
        Game game = new Game(gameInit.getTitle(), gameInit.getMapSize(), gameInit.getWaitingTime());
        boolean success = gameService.createGame(game);
        if (success) {
            //TODO: make this and other clear and better
            this.simpleMessagingTemplate.convertAndSend("/admin", new SimpleResponse("Server", "Game initialized!", "INIT"));
            this.simpleMessagingTemplate.convertAndSend("/admin/games", new GamesResponse("Server", "Active games.", "GAMES", gameService.getActiveGames()));
            this.simpleMessagingTemplate.convertAndSend("/public/games", new SimpleGamesResponse("Server", "Active games.", "GAMES", gameService.getActiveSimpleGames()));
            return new ApiResponse(true, "Game created.", null);
        } else {
            //TODO: messages
            return new ApiResponse(false, "Game creation failed.", null);
        }
    }

    @PostMapping("/start")
    public ApiResponse startGame(@RequestBody String gameId) {
        gameService.start(gameId);
        this.simpleMessagingTemplate.convertAndSend("/admin", new SimpleResponse("Server", "Game started!", "START"));
        this.simpleMessagingTemplate.convertAndSend("/admin/games", new GamesResponse("Server", "Active games.", "GAMES", gameService.getActiveGames()));
        this.simpleMessagingTemplate.convertAndSend("/public/games", new SimpleGamesResponse("Server", "Active games.", "GAMES", gameService.getActiveSimpleGames()));

        return new ApiResponse(true, "Game started.", null);
    }

    @PostMapping("/stop")
    public ApiResponse stopGame(@RequestBody String gameId) {
        gameService.stop(gameId);
        this.simpleMessagingTemplate.convertAndSend("/admin", new SimpleResponse("Server", "Game stopped!", "STOP"));
        this.simpleMessagingTemplate.convertAndSend("/admin/games", new GamesResponse("Server", "Active games.", "GAMES", gameService.getActiveGames()));
        this.simpleMessagingTemplate.convertAndSend("/public/stop", new SimpleResponse("Server", "Game stopped.", "STOP"));

        return new ApiResponse(true, "Game stopped.", null);
    }

    @GetMapping("/active")
    public ApiResponse getActiveGames() {
        return new ApiResponse(true, "Getting active games.", gameService.getActiveGames());
    }

    @GetMapping("/archive")
    public ApiResponse getArchiveGames() {
        return new ApiResponse(true, "Getting archived games.", gameService.getArchiveGames());
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteGameById(@PathVariable String id) {
        gameService.deleteGame(id);
        this.simpleMessagingTemplate.convertAndSend("/admin/games", new GamesResponse("Server", "Active games.", "GAMES", gameService.getActiveGames()));

        return new ApiResponse(true, "Game deleted.", null);
    }
}
