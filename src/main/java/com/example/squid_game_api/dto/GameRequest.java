package com.example.squid_game_api.dto;

import com.example.squid_game_api.entities.Game;

import java.time.LocalDate;
import java.time.LocalTime;

public record GameRequest(
        String gameName,
        String description,
        int roundNumber,
        LocalDate gameDate,
        LocalTime gameTime,
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
