package com.example.squid_game_api.services;

import com.example.squid_game_api.dto.GameRequest;
import com.example.squid_game_api.dto.GameResponse;
import com.example.squid_game_api.entities.Game;
import com.example.squid_game_api.exceptions.DependencyException;
import com.example.squid_game_api.exceptions.DuplicateNameException;
import com.example.squid_game_api.exceptions.NoIdFoundException;
import com.example.squid_game_api.repositories.GameRepository;
import com.example.squid_game_api.repositories.ParticipationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private ParticipationRepository participationRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void should_createNewGame() {
        // GIVEN
        GameRequest request = new GameRequest("GameName", "Description", 1, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0));
        Game savedGame = new Game(1L, "GameName", "Description", 1, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0));
        GameResponse expectedResponse = GameResponse.fromEntity(savedGame);

        when(gameRepository.findByGameName(request.gameName())).thenReturn(Optional.empty());
        when(gameRepository.save(Mockito.any(Game.class))).thenReturn(savedGame);

        // WHEN
        GameResponse response = gameService.createGame(request);

        // THEN
        verify(gameRepository).findByGameName(request.gameName());
        verify(gameRepository).save(Mockito.any(Game.class));
        assertEquals(expectedResponse, response);
    }

    @Test
    void should_throwDuplicateNameException_when_gameNameExists() {
        // GIVEN
        GameRequest request = new GameRequest("GameName", "Description", 1, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0));
        Game existingGame = request.toEntity();

        when(gameRepository.findByGameName(request.gameName())).thenReturn(Optional.of(existingGame));

        // WHEN & THEN
        assertThrows(DuplicateNameException.class, () -> gameService.createGame(request));
        verify(gameRepository).findByGameName(request.gameName());
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void should_findAllGames() {
        // GIVEN
        Game game1 = new Game(1L, "Game1", "Description1", 1, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0));
        Game game2 = new Game(2L, "Game2", "Description2", 2, LocalDate.now(), LocalTime.of(14, 0), LocalTime.of(16, 0));
        List<Game> games = List.of(game1, game2);
        List<GameResponse> expectedResponse = games.stream().map(GameResponse::fromEntity).toList();

        when(gameRepository.findAll()).thenReturn(games);

        // WHEN
        List<GameResponse> response = gameService.findAllGames();

        // THEN
        verify(gameRepository).findAll();
        assertEquals(expectedResponse, response);
    }

    @Test
    void should_findGameById() {
        // GIVEN
        Game game = new Game(1L, "GameName", "Description", 1, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(12, 0));
        GameResponse expectedResponse = GameResponse.fromEntity(game);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        // WHEN
        GameResponse response = gameService.findGameById(1L);

        // THEN
        verify(gameRepository).findById(1L);
        assertEquals(expectedResponse, response);
    }

    @Test
    void should_throwNoIdFoundException_when_gameIdNotFound() {
        // GIVEN
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(NoIdFoundException.class, () -> gameService.findGameById(1L));
        verify(gameRepository).findById(1L);
    }

    @Test
    void should_deleteGameById() {
        // GIVEN
        when(gameRepository.existsById(1L)).thenReturn(true);
        when(participationRepository.existsByGameId(1L)).thenReturn(false);

        // WHEN
        gameService.deleteGameById(1L);

        // THEN
        verify(gameRepository).existsById(1L);
        verify(participationRepository).existsByGameId(1L);
        verify(gameRepository).deleteById(1L);
    }

    @Test
    void should_throwDependencyException_when_gameHasParticipations() {
        // GIVEN
        when(gameRepository.existsById(1L)).thenReturn(true);
        when(participationRepository.existsByGameId(1L)).thenReturn(true);

        // WHEN & THEN
        assertThrows(DependencyException.class, () -> gameService.deleteGameById(1L));
        verify(gameRepository).existsById(1L);
        verify(participationRepository).existsByGameId(1L);
        verifyNoMoreInteractions(gameRepository);
    }

    @Test
    void should_throwNoIdFoundException_when_deletingNonExistentGame() {
        // GIVEN
        when(gameRepository.existsById(1L)).thenReturn(false);

        // WHEN & THEN
        assertThrows(NoIdFoundException.class, () -> gameService.deleteGameById(1L));
        verify(gameRepository).existsById(1L);
        verifyNoMoreInteractions(gameRepository, participationRepository);
    }

    @Test
    void should_checkIfGameIsFinished() {
        // GIVEN
        Game game = new Game();
        game.setEndTime(LocalTime.of(23, 59));

        GameService gameService = new GameService(gameRepository, participationRepository);

        // WHEN
        boolean isFinished = gameService.isGameFinished(game);

        // THEN
        assertFalse(isFinished);
    }
}
