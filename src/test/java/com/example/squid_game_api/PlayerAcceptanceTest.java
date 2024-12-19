package com.example.squid_game_api;

import com.example.squid_game_api.dto.PlayerRequest;
import com.example.squid_game_api.entities.Player;
import com.example.squid_game_api.repositories.PlayerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class PlayerAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void testCreatePlayer() throws Exception {
        PlayerRequest request = new PlayerRequest("New Player", 30);

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(request.name())))
                .andExpect(jsonPath("$.age", is(request.age())));
    }

    @Test
    void testGetPlayerList() throws Exception {

        playerRepository.save(new Player("Player One", 25));
        playerRepository.save(new Player("Player Two", 30));

        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Player One")))
                .andExpect(jsonPath("$[1].name", is("Player Two")));
    }

    @Test
    void testSearchPlayerByName() throws Exception {

        Player testPlayer = playerRepository.save(new Player("Searchable Player", 28));

        mockMvc.perform(get("/players?name=Searchable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].name", is(testPlayer.getName())));
    }

    @Test
    void testGetPlayerById() throws Exception {

        Player testPlayer = playerRepository.save(new Player("Test Player", 25));

        mockMvc.perform(get("/players/" + testPlayer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testPlayer.getId().intValue())))
                .andExpect(jsonPath("$.name", is(testPlayer.getName())))
                .andExpect(jsonPath("$.age", is(testPlayer.getAge())));
    }

    @Test
    void testUpdatePlayer() throws Exception {

        Player testPlayer = playerRepository.save(new Player("Old Name", 22));

        PlayerRequest updateRequest = new PlayerRequest("Updated Name", 30);

        mockMvc.perform(put("/players/" + testPlayer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testPlayer.getId().intValue())))
                .andExpect(jsonPath("$.name", is(updateRequest.name())))
                .andExpect(jsonPath("$.age", is(updateRequest.age())));
    }

    @Test
    void testDeletePlayer() throws Exception {

        Player testPlayer = new Player("Deletable Player", 25);
        testPlayer.setPlaying(false);
        playerRepository.save(testPlayer);

        mockMvc.perform(delete("/players/" + testPlayer.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string("Player has been deleted."));
    }

    @Test
    void testDeleteInactivePlayers() throws Exception {

        Player testPlayer1 = new Player("Active Player", 30);
        playerRepository.save(testPlayer1);

        Player testPlayer2 = new Player("Inactive Player", 40);
        testPlayer2.setPlaying(false);
        playerRepository.save(testPlayer2);

        mockMvc.perform(delete("/players/inactive"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].name", is("Active Player")));
    }

}
