package com.example.squid_game_api.services;

import com.example.squid_game_api.dto.GameRequest;
import com.example.squid_game_api.dto.GameResponse;
import com.example.squid_game_api.entities.Game;
import com.example.squid_game_api.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // Create a new game
    public GameResponse createGame(GameRequest gameRequest) {
        Optional<Game> game = gameRepository.findByGameName(gameRequest.gameName());
                if(game.isPresent()){
                    throw new RuntimeException("Game with name " + gameRequest.gameName() + " already exist.");
                }
        Game newGame = gameRequest.toEntity();
        Game savedGame = gameRepository.save(newGame);
        return GameResponse.fromEntity(savedGame);
    }

    // Get all games
    public List<GameResponse> findAllGames() {
        List<Game> games = gameRepository.findAll();
        return games.stream().map(GameResponse::fromEntity).toList();
    }

    // Get game by ID
    public GameResponse findGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game with ID " + id + " not found."));
        return GameResponse.fromEntity(game);
    }

    // Update a game
    public GameResponse updateGame(Long id, GameRequest gameRequest) {
        Game existingGame = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game with ID " + id + " not found."));

        Optional<Game> game = gameRepository.findByGameName(gameRequest.gameName());
        if(game.isPresent()){
            throw new RuntimeException("Game with name " + gameRequest.gameName() + " already exist.");
        }

        existingGame.setGameName(gameRequest.gameName());
        existingGame.setDescription(gameRequest.description());
        existingGame.setRoundNumber(gameRequest.roundNumber());
        existingGame.setGameDate(gameRequest.gameDate());
        existingGame.setGameTime(gameRequest.gameTime());
        existingGame.setEndTime(gameRequest.endTime());

        Game updatedGame = gameRepository.save(existingGame);
        return GameResponse.fromEntity(updatedGame);
    }

    // Delete a game
    public void deleteGameById(Long id) {
        if (!gameRepository.existsById(id)) {
            throw new RuntimeException("Game with ID " + id + " not found.");
        }
        gameRepository.deleteById(id);
    }
    //Check if game is already has been finish
    public boolean isGameFinished(Game game) {
        LocalTime now = LocalTime.now();
        return now.isAfter(game.getEndTime());
    }
}
