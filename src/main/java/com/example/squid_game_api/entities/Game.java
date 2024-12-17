package com.example.squid_game_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String gameName;
    private String description;
    private int roundNumber;
    private LocalDate gameDate;
    private LocalTime gameTime;

    public Game(String description, String gameName, int roundNumber, LocalDate gameDate, LocalTime gameTime) {
        this.description = description;
        this.gameName = gameName;
        this.roundNumber = roundNumber;
        this.gameDate = gameDate;
        this.gameTime = gameTime;
    }
}
