package com.example.squid_game_api.dto;

import com.example.squid_game_api.entities.Game;

import java.time.LocalDate;
import java.time.LocalTime;

public record GameResponse(Long id,
                           String gameName,
                           String description,
                           int roundNumber,
                           LocalDate gameDate,
                           LocalTime gameTime,
                           LocalTime endTime) {

    public static GameResponse fromEntity(Game game) {
        return new GameResponse(
                game.getId(),
                game.getGameName(),
                game.getDescription(),
                game.getRoundNumber(),
                game.getGameDate(),
                game.getGameTime(),
                game.getEndTime()
        );
    }
}
