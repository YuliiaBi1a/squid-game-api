package com.example.squid_game_api.repositories;

import com.example.squid_game_api.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByName(String name);

    //@Query(value = "SELECT p FROM Player p WHERE LOWER(p.name) LIKE LOWER(CONCAT(:name, '%'))")
    @Query(value = "SELECT p FROM Player p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Player> findLikeName(String name);

    @Query("SELECT p FROM Player p WHERE p.isPlaying = false")
    List<Player> findByIsPlayingFalse();
}


