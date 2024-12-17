package com.example.squid_game_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id_FK", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "game_id_FK", nullable = false)
    private Game game;

    private boolean isPassed;
    private int score;

    public Participation(Player player, Game game, boolean isPassed, int score) {
        this.player = player;
        this.game = game;
        this.isPassed = isPassed;
        this.score = score;
    }
}
