package com.example.squid_game_api.controllers;

import com.example.squid_game_api.dto.ParticipationRequest;
import com.example.squid_game_api.dto.ParticipationResponse;
import com.example.squid_game_api.services.ParticipationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participations")
public class ParticipationController {

    private final ParticipationService participationService;

    public ParticipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @PostMapping
    public ResponseEntity<ParticipationResponse> createParticipation(@Valid @RequestBody ParticipationRequest request) {
        ParticipationResponse newParticipation = participationService.createParticipation(request);
        return new ResponseEntity<>(newParticipation, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ParticipationResponse>> getAllParticipations() {
        List<ParticipationResponse> participations = participationService.findAllParticipations();
        return new ResponseEntity<>(participations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipationResponse> getParticipationById(@PathVariable Long id) {
        ParticipationResponse participation = participationService.findParticipationById(id);
        return new ResponseEntity<>(participation, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParticipationResponse> updateParticipation(@PathVariable Long id, @Valid @RequestBody ParticipationRequest request) {
        ParticipationResponse updatedParticipation = participationService.updateParticipation(id, request);
        return new ResponseEntity<>(updatedParticipation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteParticipation(@PathVariable Long id) {
        participationService.deleteParticipationById(id);
        return new ResponseEntity<>("Participation has been deleted.", HttpStatus.OK);
    }

    @PutMapping("/finalize/{gameId}")
    public ResponseEntity<String> finalizeGame(@PathVariable Long gameId) {
        participationService.finalizeGame(gameId);
        return new ResponseEntity<>("Game finalized and participants' statuses updated.", HttpStatus.OK);
    }

}
