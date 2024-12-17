package com.example.squid_game_api.dto;

import com.example.squid_game_api.entities.Game;
import com.example.squid_game_api.entities.Participation;
import com.example.squid_game_api.entities.Player;

public record ParticipationResponse(
        Long id,
        Player player,
        Game game,
        boolean isPassed,
        int score) {

    public ParticipationResponse fromEntity(Participation participation) {
        return new ParticipationResponse(participation.getId(),
                participation.getPlayer(),
                participation.getGame(),
                participation.isPassed(),
                participation.getScore());
    }
}
