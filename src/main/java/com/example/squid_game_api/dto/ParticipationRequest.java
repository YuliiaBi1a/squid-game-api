package com.example.squid_game_api.dto;

import com.example.squid_game_api.entities.Game;
import com.example.squid_game_api.entities.Participation;
import com.example.squid_game_api.entities.Player;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ParticipationRequest(
        @NotNull(message = "Player ID is required.")
        Long playerId,

        @NotNull(message = "Game ID is required.")
        Long gameId,

        Boolean isPassed,

        @Min(value = 0, message = "Score must be at least 0.")
        int score
) {
    public Participation toEntity(Player player, Game game){
        return new Participation(
                player,
                game,
                this.isPassed,
                this.score
        );
    }
}
