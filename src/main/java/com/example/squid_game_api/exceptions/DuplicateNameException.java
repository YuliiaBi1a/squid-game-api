package com.example.squid_game_api.exceptions;

public class DuplicateNameException extends AppException {

    public DuplicateNameException(String name) {
        super("Name you have introduced [" + name + "] already exists");
    }
}
