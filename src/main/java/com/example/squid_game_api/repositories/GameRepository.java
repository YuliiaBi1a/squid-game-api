package com.example.squid_game_api.repositories;

import com.example.squid_game_api.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
