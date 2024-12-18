package com.example.squid_game_api.exceptions;

public class NoRegistersFoundException extends AppException {

    public NoRegistersFoundException() {

        super("No registers found for current Game");
    }
}
