package com.example.fitnesscoach.controller;

import com.example.fitnesscoach.dto.gemini.Root;
import com.example.fitnesscoach.dto.request.FitnessRequestDto;
import com.example.fitnesscoach.dto.response.FitnessResponseDto;
import com.example.fitnesscoach.service.FitnessCoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness")
@RequiredArgsConstructor
public class FitnessController {

    private final FitnessCoachService fitnessCoachService;

    @PostMapping("/generateProgram")
    public ResponseEntity<Root> generateProgram(@RequestBody FitnessRequestDto requestDto) {
        try {
            Root root = fitnessCoachService.processUserRequest(requestDto);
            return ResponseEntity.ok(root);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/latestResponse")
    public ResponseEntity<Root> getLatestResponse() {
        try {
            Root latestResponse = fitnessCoachService.getLatestResponse();
            return ResponseEntity.ok(latestResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/read-all")
    public ResponseEntity<List<FitnessResponseDto>> getAllFitnessInfo() {
        try {
            List<FitnessResponseDto> allInfo = fitnessCoachService.readAll();
            return ResponseEntity.ok(allInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
