package com.example.fitnesscoach.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class FitnessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer age;
    private Integer height;
    private Double weight;
    private boolean gender;

    @Column(name = "type_of_sport")
    private String typeOfSport;
    private boolean place;

    @Column(name = "days_of_the_week")
    private Integer daysOfTheWeek;

    @Column(name = "time_period")
    private Integer timePeriod;
}
