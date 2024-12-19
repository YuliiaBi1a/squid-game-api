package com.example.squid_game_api.services;

import com.example.squid_game_api.dto.ParticipationRequest;
import com.example.squid_game_api.dto.ParticipationResponse;
import com.example.squid_game_api.entities.Game;
import com.example.squid_game_api.entities.Participation;
import com.example.squid_game_api.entities.Player;
import com.example.squid_game_api.exceptions.*;
import com.example.squid_game_api.repositories.GameRepository;
import com.example.squid_game_api.repositories.ParticipationRepository;
import com.example.squid_game_api.repositories.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ParticipationServiceTest {

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameService gameService;

    @InjectMocks
    private ParticipationService participationService;

    public ParticipationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_createParticipationSuccessfully() {
        // GIVEN
        ParticipationRequest request = new ParticipationRequest(1L, 1L, null, 0);

        Player player = new Player(1L, "John", 25, true);
        Game game = new Game();
        game.setId(1L);

        Participation participation = new Participation(player, game, null, 0);
        participation.setId(1L); // Set the generated ID

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(participationRepository.existsByPlayerIdAndGameId(1L, 1L)).thenReturn(false);
        when(participationRepository.save(any(Participation.class))).thenReturn(participation);

        // WHEN
        ParticipationResponse response = participationService.createParticipation(request);

        // THEN
        assertEquals(1L, response.id());
        assertEquals(1L, response.player().getId());
        assertEquals(1L, response.game().getId());
        verify(participationRepository).save(any(Participation.class));
    }

    @Test
    void should_returnAllParticipations() {
        // GIVEN
        Participation participation1 = new Participation();
        Participation participation2 = new Participation();
        when(participationRepository.findAll()).thenReturn(List.of(participation1, participation2));

        // WHEN
        List<ParticipationResponse> responses = participationService.findAllParticipations();

        // THEN
        assertEquals(2, responses.size());
        verify(participationRepository).findAll();
    }

    @Test
    void should_throwExceptionWhenNoParticipationsFound() {
        // GIVEN
        when(participationRepository.findAll()).thenReturn(List.of());

        // WHEN & THEN
        assertThrows(NoRegistersFoundException.class, () -> participationService.findAllParticipations());
    }

    @Test
    void should_returnParticipationById() {
        // GIVEN
        Participation participation = new Participation();
        participation.setId(1L);
        when(participationRepository.findById(1L)).thenReturn(Optional.of(participation));

        // WHEN
        ParticipationResponse response = participationService.findParticipationById(1L);

        // THEN
        assertEquals(1L, response.id());
        verify(participationRepository).findById(1L);
    }

    @Test
    void should_throwExceptionWhenParticipationNotFound() {
        // GIVEN
        when(participationRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(NoIdFoundException.class, () -> participationService.findParticipationById(1L));
    }

    @Test
    void should_updateParticipationSuccessfully() {
        // GIVEN
        Participation existingParticipation = new Participation();
        existingParticipation.setId(1L);

        Player player = new Player(1L, "John", 25, true);
        Game game = new Game();
        game.setId(1L);

        ParticipationRequest request = new ParticipationRequest(1L, 1L, true, 200);

        when(participationRepository.findById(1L)).thenReturn(Optional.of(existingParticipation));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(participationRepository.existsByPlayerIdAndGameIdAndIdNot(1L, 1L, 1L)).thenReturn(false);
        when(participationRepository.save(any(Participation.class))).thenReturn(existingParticipation);

        // WHEN
        ParticipationResponse response = participationService.updateParticipation(1L, request);

        // THEN
        assertEquals(1L, response.id());
        verify(participationRepository).save(any(Participation.class));
    }

    @Test
    void should_deleteParticipationByIdSuccessfully() {
        // GIVEN
        when(participationRepository.existsById(1L)).thenReturn(true);

        // WHEN
        participationService.deleteParticipationById(1L);

        // THEN
        verify(participationRepository).deleteById(1L);
    }

    @Test
    void should_throwExceptionWhenDeletingNonexistentParticipation() {
        // GIVEN
        when(participationRepository.existsById(1L)).thenReturn(false);

        // WHEN & THEN
        assertThrows(NoIdFoundException.class, () -> participationService.deleteParticipationById(1L));
    }

    @Test
    void should_finalizeGameSuccessfully() {
        // GIVEN
        Game game = new Game();
        game.setId(1L);

        Player player1 = new Player(1L, "Player1", 25, true);
        Participation participation1 = new Participation(player1, game, null, 0);
        participation1.setId(1L);

        Player player2 = new Player(2L, "Player2", 30, true);
        Participation participation2 = new Participation(player2, game, null, 0);
        participation2.setId(2L);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameService.isGameFinished(game)).thenReturn(true);
        when(participationRepository.findByGameId(1L)).thenReturn(List.of(participation1, participation2));

        // WHEN
        participationService.finalizeGame(1L);

        // THEN
        verify(participationRepository, times(2)).save(any(Participation.class));
        verify(playerRepository, times(1)).save(any(Player.class));
    }
}
