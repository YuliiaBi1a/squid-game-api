package com.example.squid_game_api.dto;

import com.example.squid_game_api.entities.Game;
import com.example.squid_game_api.entities.Participation;
import com.example.squid_game_api.entities.Player;

public record ParticipationRequest(
        Long playerId,
        Long gameId,
        Boolean isPassed,
        int score) {
    public Participation toEntity(Player player, Game game){
        return new Participation(
                player,
                game,
                this.isPassed,
                this.score
        );
    }
}
