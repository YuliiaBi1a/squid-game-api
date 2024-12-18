package com.example.squid_game_api.exceptions;

import java.time.LocalTime;

public class NotFinishGameException extends AppException {


    public NotFinishGameException(LocalTime endTime) {
        super("Game has not finished yet. End time: " + endTime);
    }
}
