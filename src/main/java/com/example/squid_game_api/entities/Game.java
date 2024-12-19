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
    private LocalTime endTime;

    public Game(String gameName, String description,  int roundNumber, LocalDate gameDate, LocalTime startTime, LocalTime endTime) {
        this.gameName = gameName;
        this.description = description;
        this.roundNumber = roundNumber;
        this.gameDate = gameDate;
        this.gameTime = startTime;
        this.endTime = endTime;
    }
//for test
    public Game(long l, String gameName, String description, int i, LocalDate now, LocalTime of, LocalTime of1) {
        this.id = l;
        this.gameName = gameName;
        this.description = description;
        this.roundNumber = i;
        this.gameDate = now;
        this.gameTime = of;
        this.endTime = of1;
    }
}
