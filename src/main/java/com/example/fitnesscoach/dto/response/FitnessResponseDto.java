package com.example.fitnesscoach.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FitnessResponseDto {
    private Long id;
    private Integer age;
    private Integer height;
    private Double weight;
    private boolean gender;
    private String typeOfSport;
    private boolean place;
    private Integer daysOfTheWeek;
    private Integer timePeriod;
    private String geminiResponse;
}
