package com.shivPrakash.healthCareSystem.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shivPrakash.healthCareSystem.Models.Nurse;

public interface NurseRepository extends JpaRepository<Nurse, Long> {

	Optional<Nurse> findByName(String string);
}
