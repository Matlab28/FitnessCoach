package com.example.fitnesscoach.service;

import com.example.fitnesscoach.client.GeminiApiClient;
import com.example.fitnesscoach.dto.gemini.Candidate;
import com.example.fitnesscoach.dto.gemini.Root;
import com.example.fitnesscoach.dto.request.FitnessRequestDto;
import com.example.fitnesscoach.dto.response.FitnessResponseDto;
import com.example.fitnesscoach.entity.FitnessEntity;
import com.example.fitnesscoach.repository.FitnessRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FitnessCoachService implements GeminiService {

    private final ModelMapper modelMapper;
    private final FitnessRepository repository;
    private final String key = "AIzaSyB3GRRnXnOsnRJOhPpLQtWvKf4Bs1Hx-nU";
    private final GeminiApiClient client;
    private Root latestResponse;

    public FitnessCoachService(ModelMapper modelMapper,
                               FitnessRepository repository,
                               GeminiApiClient client) {
        this.modelMapper = modelMapper;
        this.repository = repository;
        this.client = client;
    }

    @Override
    public Root processUserRequest(FitnessRequestDto dto) {
        FitnessEntity entity = modelMapper.map(dto, FitnessEntity.class);

        if (dto.getAge() <= 0 || dto.getHeight() <= 0 || dto.getWeight() <= 0) {
            log.error("Invalid fitness details.");
            throw new RuntimeException("Age, height, and weight must be positive numbers...");
        }

        String instruction = constructInstruction(dto);
        Root updates = getUpdates(instruction);
        String extractedText = extractTextFromGeminiResponse(updates);
        dto.setGeminiResponse(extractedText);

        repository.save(entity);
        log.info("Fitness info saved.");

        latestResponse = updates;
        return latestResponse;
    }

    @Override
    public Root getLatestResponse() {
        return latestResponse;
    }

    private String constructInstruction(FitnessRequestDto dto) {
        StringBuilder instruction = new StringBuilder();
        instruction.append("User's age: ").append(dto.getAge()).append("\n");
        instruction.append("User's height: ").append(dto.getHeight()).append(" cm\n");
        instruction.append("User's weight: ").append(dto.getWeight()).append(" kg\n");
        instruction.append("Please provide a personalized fitness plan based on the given information.");
        return instruction.toString();
    }

    private Root getUpdates(String instruction) {
        try {
            JsonObject json = new JsonObject();
            JsonArray contentsArray = new JsonArray();
            JsonObject contentsObject = new JsonObject();
            JsonArray partsArray = new JsonArray();
            JsonObject partsObject = new JsonObject();

            partsObject.add("text", new JsonPrimitive(instruction));
            partsArray.add(partsObject);
            contentsObject.add("parts", partsArray);
            contentsArray.add(contentsObject);
            json.add("contents", contentsArray);

            String content = json.toString();
            return client.getData(key, content);
        } catch (Exception e) {
            log.error("Error while getting updates from Gemini API: ", e);
            throw e;
        }
    }

    private String extractTextFromGeminiResponse(Root updates) {
        StringBuilder textBuilder = new StringBuilder();

        if (updates.getCandidates() != null) {
            for (Candidate candidate : updates.getCandidates()) {
                String text = candidate.getContent().getParts().get(0).getText();
                text = text.replace("*", "");
                textBuilder.append(text).append("\n\n");
            }
        }

        String response = textBuilder.toString().trim();

        return response
                .replaceFirst("^##", "")
                .replace("##", "\n##")
                .replace("Fitness Plan:", "\nFitness Plan:\n")
                .replace("Weekly Goals:", "\nWeekly Goals:")
                .replace("Daily Routine:", "\nDaily Routine:")
                .replace("Important Considerations:", "\nImportant Considerations:\n");
    }

    public List<FitnessResponseDto> readAll() {
        log.info("All fitness info responded");
        return repository
                .findAll()
                .stream()
                .map(m -> modelMapper.map(m, FitnessResponseDto.class))
                .collect(Collectors.toList());
    }
}
