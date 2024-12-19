package com.example.squid_game_api;

import com.example.squid_game_api.dto.ParticipationRequest;
import com.example.squid_game_api.entities.Game;
import com.example.squid_game_api.entities.Participation;
import com.example.squid_game_api.entities.Player;
import com.example.squid_game_api.repositories.GameRepository;
import com.example.squid_game_api.repositories.ParticipationRepository;
import com.example.squid_game_api.repositories.PlayerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class ParticipationAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ParticipationRepository participationRepository;

    private Player testPlayer;
    private Game testGame;

    @BeforeEach
    void setup() {
        testPlayer = new Player();
        testPlayer.setName("Player 1");
        testPlayer.setPlaying(true);
        playerRepository.save(testPlayer);

        testGame = new Game();
        testGame.setGameName("Game 1");
        testGame.setGameDate(LocalDate.now());
        testGame.setGameTime(LocalTime.now());
        testGame.setEndTime(LocalTime.now().minusHours(1));

        gameRepository.save(testGame);
    }

    @Test
    void testCreateParticipation() throws Exception {
        ParticipationRequest request = new ParticipationRequest(
                testPlayer.getId(),
                testGame.getId(),
                null,
                0
        );

        mockMvc.perform(post("/participations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.player.id", is(testPlayer.getId().intValue())))
                .andExpect(jsonPath("$.game.id", is(testGame.getId().intValue())))
                .andExpect(jsonPath("$.isPassed", is(nullValue())))
                .andExpect(jsonPath("$.score", is(0)));
    }

    @Test
    void testGetAllParticipations() throws Exception {
        Participation participation = new Participation(testPlayer, testGame, true, 1000);
        participationRepository.save(participation);

        mockMvc.perform(get("/participations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].player.id", is(testPlayer.getId().intValue())))
                .andExpect(jsonPath("$[0].game.id", is(testGame.getId().intValue())));
    }

    @Test
    void testGetParticipationById() throws Exception {
        Participation participation = new Participation(testPlayer, testGame, true, 1000);
        participationRepository.save(participation);

        mockMvc.perform(get("/participations/" + participation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(participation.getId().intValue())))
                .andExpect(jsonPath("$.player.id", is(testPlayer.getId().intValue())))
                .andExpect(jsonPath("$.game.id", is(testGame.getId().intValue())));
    }

    @Test
    void testUpdateParticipation() throws Exception {
        Participation participation = new Participation(testPlayer, testGame, true, 1000);
        participationRepository.save(participation);

        ParticipationRequest updateRequest = new ParticipationRequest(
                testPlayer.getId(),
                testGame.getId(),
                false,
                2000
        );

        mockMvc.perform(put("/participations/" + participation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(participation.getId().intValue())))
                .andExpect(jsonPath("$.isPassed", is(false)))
                .andExpect(jsonPath("$.score", is(2000)));
    }

    @Test
    void testDeleteParticipation() throws Exception {
        Participation participation = new Participation(testPlayer, testGame, true, 1000);
        participationRepository.save(participation);

        mockMvc.perform(delete("/participations/" + participation.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Participation has been deleted."));
    }

    @Test
    void testFinalizeGame() throws Exception {
        Participation participation = new Participation(testPlayer, testGame, null, 0);
        participationRepository.save(participation);

        mockMvc.perform(put("/participations/finalize/" + testGame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Game finalized and participants statuses updated."));

        List<Participation> updatedParticipations = participationRepository.findByGameId(testGame.getId());
        updatedParticipations.forEach(part -> {
            assertNotNull(part.getIsPassed(), "Participation status should be finalized.");
        });
    }
}
