package com.example.squid_game_api.dto;

import com.example.squid_game_api.entities.Player;

public record PlayerResponse(Long id,
                             String name,
                             int age,
                             boolean isPlaying) {

    public static PlayerResponse fromEntity(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.getAge(),
                player.isPlaying()
        );
    }
}
