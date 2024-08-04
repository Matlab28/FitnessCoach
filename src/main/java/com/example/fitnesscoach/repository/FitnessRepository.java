package com.example.fitnesscoach.repository;

import com.example.fitnesscoach.entity.FitnessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FitnessRepository extends JpaRepository<FitnessEntity, Long> {
}
