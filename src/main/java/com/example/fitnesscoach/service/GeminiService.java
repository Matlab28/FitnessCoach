package com.example.fitnesscoach.service;


import com.example.fitnesscoach.dto.gemini.Root;
import com.example.fitnesscoach.dto.request.FitnessRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface GeminiService {
    Root processUserRequest(FitnessRequestDto dto);

    Root getLatestResponse();
}
