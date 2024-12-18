package com.example.squid_game_api.services;

import com.example.squid_game_api.dto.ParticipationRequest;
import com.example.squid_game_api.dto.ParticipationResponse;
import com.example.squid_game_api.entities.Game;
import com.example.squid_game_api.entities.Participation;
import com.example.squid_game_api.entities.Player;
import com.example.squid_game_api.repositories.GameRepository;
import com.example.squid_game_api.repositories.ParticipationRepository;
import com.example.squid_game_api.repositories.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final GameService gameService;

    public ParticipationService(ParticipationRepository participationRepository,
                                PlayerRepository playerRepository,
                                GameRepository gameRepository, GameService gameService) {
        this.participationRepository = participationRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.gameService = gameService;
    }

    //Create participation
    // Create a new participation
    public ParticipationResponse createParticipation(ParticipationRequest request) {
        Player player = playerRepository.findById(request.playerId())
                .orElseThrow(() -> new RuntimeException("Player with ID " + request.playerId() + " not found."));

        // Перевірка, чи гравець активний
        if (!player.isPlaying()) {
            throw new RuntimeException("Player with ID " + request.playerId() + " cannot play anymore.");
        }

        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new RuntimeException("Game with ID " + request.gameId() + " not found."));

        // Перевірка, чи вже існує участь цього гравця в цій грі
        boolean exists = participationRepository.existsByPlayerIdAndGameId(request.playerId(), request.gameId());
        if (exists) {
            throw new RuntimeException("Player with ID " + request.playerId() + " is already participating in Game with ID " + request.gameId() + ".");
        }

        Participation participation = request.toEntity(player, game);
        participation.setIsPassed(null); // Початковий статус `isPassed` встановлюється як `null`

        Participation savedParticipation = participationRepository.save(participation);
        return ParticipationResponse.fromEntity(savedParticipation);
    }


    // Get all participations
    public List<ParticipationResponse> findAllParticipations() {
        List<Participation> participations = participationRepository.findAll();
        if (participations.isEmpty()) {
            throw new RuntimeException("No participation's found.");
        }
        return participations.stream()
                .map(ParticipationResponse::fromEntity).toList();
    }

    // Get participation by ID
    public ParticipationResponse findParticipationById(Long id) {
        Participation participation = participationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participation with ID " + id + " not found."));
        return ParticipationResponse.fromEntity(participation);
    }

    // Update a participation
    public ParticipationResponse updateParticipation(Long id, ParticipationRequest request) {
        Participation existingParticipation = participationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participation with ID " + id + " not found."));

        Player player = playerRepository.findById(request.playerId())
                .orElseThrow(() -> new RuntimeException("Player with ID " + request.playerId() + " not found."));

        if (!player.isPlaying()) {
            throw new RuntimeException("Player with ID " + request.playerId() + " is not currently playing.");
        }

        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new RuntimeException("Game with ID " + request.gameId() + " not found."));

        boolean exists = participationRepository.existsByPlayerIdAndGameIdAndIdNot(request.playerId(), request.gameId(), id);
        if (exists) {
            throw new RuntimeException("Player with ID " + request.playerId() + " is already participating in Game with ID " + request.gameId() + ".");
        }

        existingParticipation.setPlayer(player);
        existingParticipation.setGame(game);
        existingParticipation.setPassed(request.isPassed());
        existingParticipation.setScore(request.score());

        Participation updatedParticipation = participationRepository.save(existingParticipation);
        return ParticipationResponse.fromEntity(updatedParticipation);
    }


    // Delete a participation
    public void deleteParticipationById(Long id) {
        if (!participationRepository.existsById(id)) {
            throw new RuntimeException("Participation with ID " + id + " not found.");
        }
        participationRepository.deleteById(id);
    }

    // Method for finish game and change status of the player
    @Transactional
    public void finalizeGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found with ID: " + gameId));

        if (!gameService.isGameFinished(game)) {
            throw new RuntimeException("Game has not finished yet. End time: " + game.getEndTime());
        }

        List<Participation> participations = participationRepository.findByGameId(gameId);

        Random random = new Random();
        for (Participation participation : participations) {
            boolean isPassed = random.nextBoolean();
            participation.setPassed(isPassed);

            if (isPassed) {
                participation.setScore(1000);
            } else {
                Player player = participation.getPlayer();
                player.setPlaying(false);
                playerRepository.save(player);
            }

            participationRepository.save(participation);
        }
    }
}