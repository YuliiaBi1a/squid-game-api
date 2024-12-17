package com.example.squid_game_api.repositories;

import com.example.squid_game_api.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
