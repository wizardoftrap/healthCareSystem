package com.shivPrakash.healthCareSystem.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shivPrakash.healthCareSystem.Models.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByName(String name);
}

