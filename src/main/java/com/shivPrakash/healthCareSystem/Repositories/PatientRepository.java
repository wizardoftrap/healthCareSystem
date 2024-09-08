package com.shivPrakash.healthCareSystem.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shivPrakash.healthCareSystem.Models.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}

