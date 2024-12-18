package com.example.squid_game_api.repositories;

import com.example.squid_game_api.dto.GameResponse;
import com.example.squid_game_api.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByGameName(String name);
}
