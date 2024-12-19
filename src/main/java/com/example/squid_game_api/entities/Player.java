package com.example.squid_game_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String name;
    private int age;
    private boolean isPlaying;

    public Player(String name, int age) {
        this.name = name;
        this.age = age;
        this.isPlaying = true;
    }
//for test
    public Player(long l, String name, int i, boolean b) {
        this.id = l;
        this.name = name;
        this.age = i;
        this.isPlaying = b;
    }

}
