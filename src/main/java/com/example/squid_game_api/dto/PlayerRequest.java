package com.example.squid_game_api.dto;

import com.example.squid_game_api.entities.Player;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlayerRequest(
        @NotBlank(message = "Name is required.")
        @Size(max = 100, message = "Name must not exceed 100 characters.")
        String name,

        @Min(value = 18, message = "Player must be at least 18 years old.")
        @Max(value = 100, message = "Player age must not exceed 100 years.")
        int age
        ) {

    public Player toEntity() {
        return new Player(this.name, this.age);
    }
}