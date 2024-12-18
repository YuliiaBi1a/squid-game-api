package com.example.squid_game_api.dto;

import com.example.squid_game_api.entities.Game;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;

public record GameRequest(
        @NotBlank(message = "Game name is required.")
        @Size(max = 100, message = "Game name must not exceed 100 characters.")
        String gameName,

        @NotBlank(message = "Description is required.")
        @Size(max = 500, message = "Description must not exceed 500 characters.")
        String description,

        @Min(value = 1, message = "Round number must be at least 1.")
        int roundNumber,

        @NotNull(message = "Game date is required.")
        @FutureOrPresent(message = "Game date must not be in the past.")
        LocalDate gameDate,

        @NotNull(message = "Game start time is required.")
        LocalTime gameTime,

        @NotNull(message = "Game end time is required.")
        LocalTime endTime) {

    public Game toEntity() {

        return new Game(
                this.gameName,
                this.description,
                this.roundNumber,
                this.gameDate,
                this.gameTime,
                this.endTime
        );
    }
}
