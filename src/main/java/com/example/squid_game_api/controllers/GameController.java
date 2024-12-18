package com.example.squid_game_api.controllers;

import com.example.squid_game_api.dto.GameRequest;
import com.example.squid_game_api.dto.GameResponse;
import com.example.squid_game_api.services.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameResponse> createGame(@Valid @RequestBody GameRequest gameRequest) {
        GameResponse newGame = gameService.createGame(gameRequest);
        return new ResponseEntity<>(newGame, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GameResponse>> getAllGames() {
        List<GameResponse> games = gameService.findAllGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> getGameById(@PathVariable Long id) {
        GameResponse game = gameService.findGameById(id);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponse> updateGame(@PathVariable Long id, @Valid @RequestBody GameRequest gameRequest) {
        GameResponse updatedGame = gameService.updateGame(id, gameRequest);
        return new ResponseEntity<>(updatedGame, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGame(@PathVariable Long id) {
        gameService.deleteGameById(id);
        return new ResponseEntity<>("Game has been deleted.", HttpStatus.OK);
    }
}
