package com.shivPrakash.healthCareSystem.Repositories;

import com.shivPrakash.healthCareSystem.Models.Accommodation;
import com.shivPrakash.healthCareSystem.Models.Accommodation.AccommodationType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
	public Accommodation findAccommodationByType(AccommodationType type);
}
