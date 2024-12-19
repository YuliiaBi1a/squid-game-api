package com.example.squid_game_api.services;

import com.example.squid_game_api.dto.PlayerRequest;
import com.example.squid_game_api.dto.PlayerResponse;
import com.example.squid_game_api.entities.Participation;
import com.example.squid_game_api.entities.Player;
import com.example.squid_game_api.exceptions.*;
import com.example.squid_game_api.repositories.ParticipationRepository;
import com.example.squid_game_api.repositories.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ParticipationRepository participationRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void should_createNewPlayer() {
        // GIVEN
        PlayerRequest request = new PlayerRequest("John", 25);
        Player playerToSave = request.toEntity();
        Player savedPlayer = new Player(1L, "John", 25, true);
        PlayerResponse expectedResponse = PlayerResponse.fromEntity(savedPlayer);

        when(playerRepository.findByName(request.name())).thenReturn(Optional.empty());
        when(playerRepository.save(Mockito.any(Player.class))).thenReturn(savedPlayer);

        // WHEN
        PlayerResponse response = playerService.createPlayer(request);

        // THEN
        verify(playerRepository).save(Mockito.any(Player.class));
        assertEquals(expectedResponse, response);
    }

    @Test
    void should_throwExceptionWhenCreatingDuplicatePlayer() {
        // GIVEN
        PlayerRequest request = new PlayerRequest("John", 25);
        Player existingPlayer = new Player(1L, "John", 25, false);

        when(playerRepository.findByName(request.name())).thenReturn(Optional.of(existingPlayer));

        // WHEN & THEN
        assertThrows(DuplicateNameException.class, () -> playerService.createPlayer(request));
    }

    @Test
    void should_findAllPlayers() {
        // GIVEN
        Player player1 = new Player(1L, "John", 25, false);
        Player player2 = new Player(2L, "Jane", 30, false);
        List<Player> players = List.of(player1, player2);
        List<PlayerResponse> expectedResponse = players.stream()
                .map(PlayerResponse::fromEntity)
                .toList();

        when(playerRepository.findAll()).thenReturn(players);

        // WHEN
        List<PlayerResponse> response = playerService.findAllPlayers();

        // THEN
        assertEquals(expectedResponse, response);
    }

    @Test
    void should_findPlayerById() {
        // GIVEN
        Player player = new Player(1L, "John", 25, false);
        PlayerResponse expectedResponse = PlayerResponse.fromEntity(player);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        // WHEN
        PlayerResponse response = playerService.findPlayerById(1L);

        // THEN
        assertEquals(expectedResponse, response);
    }

    @Test
    void should_throwExceptionWhenPlayerByIdNotFound() {
        // GIVEN
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(NoIdFoundException.class, () -> playerService.findPlayerById(1L));
    }

    @Test
    void should_updatePlayer() {
        // GIVEN
        PlayerRequest request = new PlayerRequest("Updated Name", 30);
        Player existingPlayer = new Player(1L, "John", 25, false);
        Player updatedPlayer = new Player(1L, "Updated Name", 30, false);
        PlayerResponse expectedResponse = PlayerResponse.fromEntity(updatedPlayer);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.findByName(request.name())).thenReturn(Optional.empty());
        when(playerRepository.save(existingPlayer)).thenReturn(updatedPlayer);

        // WHEN
        PlayerResponse response = playerService.updatePlayer(1L, request);

        // THEN
        assertEquals(expectedResponse, response);
    }

    @Test
    void should_throwExceptionWhenUpdatingWithDuplicateName() {
        // GIVEN
        PlayerRequest request = new PlayerRequest("Existing Name", 30);
        Player existingPlayer = new Player(1L, "John", 25, false);
        Player duplicatePlayer = new Player(2L, "Existing Name", 30, false);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.findByName(request.name())).thenReturn(Optional.of(duplicatePlayer));

        // WHEN & THEN
        assertThrows(DuplicateNameException.class, () -> playerService.updatePlayer(1L, request));
    }

    @Test
    void should_deletePlayerById() {
        // GIVEN
        Player player = new Player(1L, "John", 25, false);
        List<Participation> participations = List.of(new Participation());

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(participationRepository.findByPlayerId(1L)).thenReturn(participations);

        // WHEN
        playerService.deletePlayerById(1L);

        // THEN
        verify(participationRepository).deleteAll(participations);
        verify(playerRepository).delete(player);
    }

    @Test
    void should_throwExceptionWhenDeletingActivePlayer() {
        // GIVEN
        Player player = new Player(1L, "John", 25, true);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        // WHEN & THEN
        assertThrows(DependencyException.class, () -> playerService.deletePlayerById(1L));
    }

    @Test
    void should_deleteAllInactivePlayers() {
        // GIVEN
        Player player1 = new Player(1L, "John", 25, false);
        Player player2 = new Player(2L, "Jane", 30, false);
        List<Player> inactivePlayers = List.of(player1, player2);

        when(playerRepository.findByIsPlayingFalse()).thenReturn(inactivePlayers);

        // WHEN
        playerService.deleteAllInactivePlayers();

        // THEN
        verify(participationRepository).deleteByPlayerId(player1.getId());
        verify(participationRepository).deleteByPlayerId(player2.getId());
        verify(playerRepository).delete(player1);
        verify(playerRepository).delete(player2);
    }

    @Test
    void should_throwExceptionWhenNoInactivePlayers() {
        // GIVEN
        when(playerRepository.findByIsPlayingFalse()).thenReturn(List.of());

        // WHEN & THEN
        assertThrows(NoRegistersFoundException.class, () -> playerService.deleteAllInactivePlayers());
    }
}
