package com.example.squid_game_api.services;

import com.example.squid_game_api.dto.PlayerRequest;
import com.example.squid_game_api.dto.PlayerResponse;
import com.example.squid_game_api.entities.Participation;
import com.example.squid_game_api.entities.Player;
import com.example.squid_game_api.exceptions.*;
import com.example.squid_game_api.repositories.ParticipationRepository;
import com.example.squid_game_api.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ParticipationRepository participationRepository;

    public PlayerService(PlayerRepository playerRepository, ParticipationRepository participationRepository) {
        this.playerRepository = playerRepository;
        this.participationRepository = participationRepository;
    }

    // Create a new player
    public PlayerResponse createPlayer(PlayerRequest playerRequest) {
        Optional<Player> player = playerRepository.findByName(playerRequest.name());
        if(player.isPresent()){
            throw new DuplicateNameException(playerRequest.name());
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
                .orElseThrow(() -> new NoIdFoundException(id));
        return PlayerResponse.fromEntity(player);
    }

    // GET: search player like name
    public List<PlayerResponse> searchByName(String name) {
        List<Player> playerList = playerRepository.findLikeName(name);
        if (playerList.isEmpty()) {
            throw new NoPlayerNameFoundException(name);
        }
        return playerList.stream()
                .map(PlayerResponse::fromEntity).toList();
    }

    // GET: search player by game ID
    public List<PlayerResponse> findPlayersByGameId(Long gameId) {
        List<Player> players = participationRepository.findPlayersByGameId(gameId);
        if (players.isEmpty()) {
            throw new NoRegistersFoundException();
        }
        return players.stream()
                .map(PlayerResponse::fromEntity).toList();
    }

    // Update a player
    public PlayerResponse updatePlayer(Long id, PlayerRequest playerRequest) {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new NoIdFoundException(id));

        Optional<Player> player = playerRepository.findByName(playerRequest.name());
        if(player.isPresent()){
            throw new DuplicateNameException(playerRequest.name());
        }

        existingPlayer.setName(playerRequest.name());
        existingPlayer.setAge(playerRequest.age());

        Player updatedPlayer = playerRepository.save(existingPlayer);
        return PlayerResponse.fromEntity(updatedPlayer);
    }

    // Delete a player
    @Transactional
    public void deletePlayerById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new NoIdFoundException(id));

        if (player.isPlaying()) {
            throw new DependencyException(id);
        }

        List<Participation> participations = participationRepository.findByPlayerId(id);
        participationRepository.deleteAll(participations);

        playerRepository.delete(player);
    }

    // Delete all players with isPlaying = false
    @Transactional
    public void deleteAllInactivePlayers() {
        List<Player> inactivePlayers = playerRepository.findByIsPlayingFalse();

        if (inactivePlayers.isEmpty()) {
            throw new NoRegistersFoundException();
        }

        for (Player player : inactivePlayers) {
            participationRepository.deleteByPlayerId(player.getId());
            playerRepository.delete(player);
        }
    }
}
