package com.example.squid_game_api;

import com.example.squid_game_api.dto.GameRequest;
import com.example.squid_game_api.entities.Game;
import com.example.squid_game_api.repositories.GameRepository;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class GameAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GameRepository gameRepository;

    private Game testGame;

    @BeforeEach
    void setup() {
        testGame = new Game();
        testGame.setGameName("Test Game");
        testGame.setDescription("Test description");
        testGame.setRoundNumber(1);
        testGame.setGameDate(LocalDate.now());
        testGame.setGameTime(LocalTime.of(12, 0, 0));
        testGame.setEndTime(LocalTime.of(14, 0, 0));
        gameRepository.save(testGame);
    }

    @Test
    void testCreateGame() throws Exception {
        GameRequest request = new GameRequest(
                "New Game",
                "New description",
                1,
                LocalDate.now(),
                LocalTime.of(12, 0, 0),
                LocalTime.of(14, 0, 0)
        );

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.gameName", is(request.gameName())))
                .andExpect(jsonPath("$.description", is(request.description())))
                .andExpect(jsonPath("$.roundNumber", is(request.roundNumber())))
                .andExpect(jsonPath("$.gameDate", is(request.gameDate().toString())))
                .andExpect(jsonPath("$.gameTime", is(request.gameTime().toString() + ":00")))
                .andExpect(jsonPath("$.endTime", is(request.endTime().toString() + ":00")));
    }

    @Test
    void testGetAllGames() throws Exception {
        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @Test
    void testGetGameById() throws Exception {
        mockMvc.perform(get("/games/" + testGame.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testGame.getId().intValue())))
                .andExpect(jsonPath("$.gameName", is(testGame.getGameName())))
                .andExpect(jsonPath("$.description", is(testGame.getDescription())))
                .andExpect(jsonPath("$.roundNumber", is(testGame.getRoundNumber())))
                .andExpect(jsonPath("$.gameDate", is(testGame.getGameDate().toString())))
                .andExpect(jsonPath("$.gameTime", is(testGame.getGameTime().toString()+ ":00")))
                .andExpect(jsonPath("$.endTime", is(testGame.getEndTime().toString()+ ":00")));
    }

    @Test
    void testUpdateGame() throws Exception {
        GameRequest updateRequest = new GameRequest(
                "Updated Game",
                "Updated description",
                2,
                LocalDate.now().plusDays(1),
                LocalTime.of(15, 0),
                LocalTime.of(17, 0)
        );

        mockMvc.perform(put("/games/" + testGame.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testGame.getId().intValue())))
                .andExpect(jsonPath("$.gameName", is(updateRequest.gameName())))
                .andExpect(jsonPath("$.description", is(updateRequest.description())))
                .andExpect(jsonPath("$.roundNumber", is(updateRequest.roundNumber())))
                .andExpect(jsonPath("$.gameDate", is(updateRequest.gameDate().toString())))
                .andExpect(jsonPath("$.gameTime", is(updateRequest.gameTime().toString()+ ":00")))
                .andExpect(jsonPath("$.endTime", is(updateRequest.endTime().toString()+ ":00")));
    }

    @Test
    void testDeleteGame() throws Exception {
        mockMvc.perform(delete("/games/" + testGame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Game has been deleted."));
    }

}
