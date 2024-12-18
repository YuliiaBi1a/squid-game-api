package com.example.squid_game_api.exceptions;

public class DuplicateIdException extends AppException {

    public DuplicateIdException(Long playerId, Long gameId) {
        super("Player with ID " + playerId + " is already participating in Game with ID " + gameId + ".");
    }
}
