package com.shivPrakash.healthCareSystem.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String specialty; // Note the change from 'specialization' to 'specialty'

    @Column(nullable = false)
    private int numPatients;

    public Doctor() {}

    public Doctor(String name, String specialty, int numPatients) {
        this.name = name;
        this.specialty = specialty; // Note the change from 'specialization' to 'specialty'
        this.numPatients = numPatients;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() { // Ensure this matches the Thymeleaf template
        return specialty;
    }

    public void setSpecialty(String specialty) { // Ensure this matches the Thymeleaf template
        this.specialty = specialty;
    }

	public int getNumPatients() {
		return numPatients;
	}

	public void setNumPatients(int numPatients) {
		this.numPatients = numPatients;
	}
    
}
