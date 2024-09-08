package com.shivPrakash.healthCareSystem.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "accommodations")
public class Accommodation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccommodationType type;

    @Column(nullable = false)
    private Integer totalCapacity;  // Total number of beds/rooms

    @Column(nullable = false)
    private Integer availableCapacity;  // Number of available beds/rooms

    // Default constructor
    public Accommodation() {}

    public Accommodation(AccommodationType type, Integer totalCapacity, Integer availableCapacity) {
        this.type = type;
        this.totalCapacity = totalCapacity;
        this.availableCapacity = availableCapacity;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AccommodationType getType() {
        return type;
    }

    public void setType(AccommodationType type) {
        this.type = type;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Integer getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(Integer availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    // Enum for accommodation types
    public enum AccommodationType {
        ICU,
        PRIVATE_WARD,
        NORMAL_BED,
        NO_ACCOMMODATION
    }
}

