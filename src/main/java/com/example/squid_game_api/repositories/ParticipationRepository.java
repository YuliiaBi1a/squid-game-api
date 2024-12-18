package com.example.squid_game_api.repositories;

import com.example.squid_game_api.entities.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findByGameId(Long gameId);
}
