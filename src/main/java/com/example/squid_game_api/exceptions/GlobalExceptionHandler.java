package com.example.squid_game_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateNameException.class)
    public ResponseEntity<String> handleDuplicateName(DuplicateNameException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(DuplicateIdException.class)
    public ResponseEntity<String> handleDuplicateId(DuplicateIdException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoRegistersFoundException.class)
    public ResponseEntity<String> handleNotFoundRegisters(NoRegistersFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoIdFoundException.class)
    public ResponseEntity<String> handleNotFoundId(NoIdFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFinishGameException.class)
    public ResponseEntity<String> handleNoFinishGame(NotFinishGameException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DependencyException.class)
    public ResponseEntity<String> handleDependencyExist(DependencyException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(NoPlayerNameFoundException.class)
    public ResponseEntity<String> handleNotFoundName(NoPlayerNameFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoPlayingException.class)
    public ResponseEntity<String> handleNotFoundPlayingPlayers(NoPlayingException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>>handleNotValidException(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        for(FieldError error: exception.getBindingResult().getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }


}
