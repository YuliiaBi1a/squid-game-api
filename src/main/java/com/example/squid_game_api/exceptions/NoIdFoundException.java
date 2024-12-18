package com.example.squid_game_api.exceptions;

public class NoIdFoundException extends AppException {

    public NoIdFoundException(Long id) {
        super("Entity with ID " + id + " not found in data base");
    }
}
