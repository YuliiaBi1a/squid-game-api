package com.example.squid_game_api.exceptions;

public class NoPlayerNameFoundException extends AppException {
    public NoPlayerNameFoundException(String name) {
        super("Player with name " + name + " not found in data base");
    }
}
