package com.example.squid_game_api.dto;

import com.example.squid_game_api.entities.Player;

public record PlayerRequest(
        String name,
        int age
        ) {

    public Player toEntity() {
        return new Player(this.name, this.age);
    }
}