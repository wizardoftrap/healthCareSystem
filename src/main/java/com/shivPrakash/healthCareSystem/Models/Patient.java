package com.shivPrakash.healthCareSystem.Models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String problem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Integer emergencyLevel;  // 0 to 5 scale

    // Association with Accommodation
    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    // Association with Doctor
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor assignedDoctor;

    // Association with list of Nurses
    @ManyToMany
    @JoinTable(
        name = "patient_nurses",
        joinColumns = @JoinColumn(name = "patient_id"),
        inverseJoinColumns = @JoinColumn(name = "nurse_id")
    )
    private List<Nurse> assignedNurses;

    // Default constructor
    public Patient() {}

    // Parameterized constructor
    public Patient(String name, Integer age, String problem, Status status, Integer emergencyLevel, Accommodation accommodation, Doctor assignedDoctor, List<Nurse> assignedNurses) {
        this.name = name;
        this.age = age;
        this.problem = problem;
        this.status = status;
        this.emergencyLevel = emergencyLevel;
        this.accommodation = accommodation;
        this.assignedDoctor = assignedDoctor;
        this.assignedNurses = assignedNurses;
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getEmergencyLevel() {
		return emergencyLevel;
	}

	public void setEmergencyLevel(Integer emergencyLevel) {
		this.emergencyLevel = emergencyLevel;
	}

	public Accommodation getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(Accommodation accommodation) {
		this.accommodation = accommodation;
	}

	public Doctor getAssignedDoctor() {
		return assignedDoctor;
	}

	public void setAssignedDoctor(Doctor assignedDoctor) {
		this.assignedDoctor = assignedDoctor;
	}

	public List<Nurse> getAssignedNurses() {
		return assignedNurses;
	}

	public void setAssignedNurses(List<Nurse> assignedNurses) {
		this.assignedNurses = assignedNurses;
	}
	// Enums for status
    public enum Status {
        CHECKED_IN,
        CHECKED_OUT,
        REFERRED
    }
}
