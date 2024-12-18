package com.example.squid_game_api.exceptions;

public class DependencyException extends AppException{
    public DependencyException(Long id) {
        super("Entity with ID " + id + " cannot be deleted because it has associated participations.");
    }
}
