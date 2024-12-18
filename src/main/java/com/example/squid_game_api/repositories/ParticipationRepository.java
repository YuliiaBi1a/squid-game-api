package com.example.squid_game_api.repositories;

import com.example.squid_game_api.entities.Participation;
import com.example.squid_game_api.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findByGameId(Long gameId);

    boolean existsByPlayerIdAndGameId(Long aLong, Long aLong1);

    boolean existsByPlayerIdAndGameIdAndIdNot(Long aLong, Long aLong1, Long id);

    @Query("SELECT p.player FROM Participation p WHERE p.game.id = :gameId")
    List<Player> findPlayersByGameId(@Param("gameId") Long gameId);

    List<Participation> findByPlayerId(Long id);

    boolean existsByGameId(Long id);

    @Modifying
    @Query("DELETE FROM Participation p WHERE p.player.id = :playerId")
    void deleteByPlayerId(@Param("playerId") Long playerId);
}

