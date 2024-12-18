package com.example.squid_game_api.controllers;

import com.example.squid_game_api.dto.PlayerRequest;
import com.example.squid_game_api.dto.PlayerResponse;
import com.example.squid_game_api.services.PlayerService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<PlayerResponse> createPlayer(@Valid @RequestBody PlayerRequest playerRequest) {
        PlayerResponse newPlayer = playerService.createPlayer(playerRequest);
        return new ResponseEntity<>(newPlayer, HttpStatus.CREATED);
    }
//search by name
    @GetMapping
    public ResponseEntity<List<PlayerResponse>> getPlayerList(@PathParam("name") String name) {
        if (name == null) {
            List<PlayerResponse> players = playerService.findAllPlayers();
            return new ResponseEntity<>(players, HttpStatus.OK);
        }
        List<PlayerResponse> searchPlayers = playerService.searchByName(name);
        return new ResponseEntity<>(searchPlayers, HttpStatus.OK);
    }
//search by game id
    @GetMapping("game/{gameId}")
    public ResponseEntity<List<PlayerResponse>> getPlayersByGameId(@PathVariable Long gameId) {
        List<PlayerResponse> players = playerService.findPlayersByGameId(gameId);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> getPlayerById(@PathVariable Long id) {
        PlayerResponse player = playerService.findPlayerById(id);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponse> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerRequest playerRequest) {
        PlayerResponse updatedPlayer = playerService.updatePlayer(id, playerRequest);
        return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayerById(id);
        return new ResponseEntity<>("Player has been deleted.", HttpStatus.OK);
    }

    @DeleteMapping("/inactive")
    public ResponseEntity<Void> deleteInactivePlayers() {
        playerService.deleteAllInactivePlayers();
        return ResponseEntity.noContent().build();
    }
}
