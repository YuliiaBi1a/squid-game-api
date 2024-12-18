package com.example.squid_game_api.services;

import com.example.squid_game_api.dto.PlayerRequest;
import com.example.squid_game_api.dto.PlayerResponse;
import com.example.squid_game_api.entities.Game;
import com.example.squid_game_api.entities.Player;
import com.example.squid_game_api.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // Create a new player
    public PlayerResponse createPlayer(PlayerRequest playerRequest) {
        Optional<Player> player = playerRepository.findByName(playerRequest.name());
        if(player.isPresent()){
            throw new RuntimeException("Game with name " + playerRequest.name() + " already exist.");
        }
        Player newPlayer = playerRequest.toEntity();
        Player savedPlayer = playerRepository.save(newPlayer);
        return PlayerResponse.fromEntity(savedPlayer);
    }

    // Get all players
    public List<PlayerResponse> findAllPlayers() {
        List<Player> players = playerRepository.findAll();
        return players.stream().map(PlayerResponse::fromEntity).toList();
    }

    // Get player by ID
    public PlayerResponse findPlayerById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player with ID " + id + " not found."));
        return PlayerResponse.fromEntity(player);
    }

    // Update a player
    public PlayerResponse updatePlayer(Long id, PlayerRequest playerRequest) {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player with ID " + id + " not found."));

        Optional<Player> player = playerRepository.findByName(playerRequest.name());
        if(player.isPresent()){
            throw new RuntimeException("Game with name " + playerRequest.name() + " already exist.");
        }

        existingPlayer.setName(playerRequest.name());
        existingPlayer.setAge(playerRequest.age());

        Player updatedPlayer = playerRepository.save(existingPlayer);
        return PlayerResponse.fromEntity(updatedPlayer);
    }

    // Delete a player
    public void deletePlayerById(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new RuntimeException("Player with ID " + id + " not found.");
        }
        playerRepository.deleteById(id);
    }
}
