package com.shivPrakash.healthCareSystem.Services;

import com.shivPrakash.healthCareSystem.Models.Accommodation;
import com.shivPrakash.healthCareSystem.Models.Doctor;
import com.shivPrakash.healthCareSystem.Repositories.AccommodationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccommodationService {

    @Autowired
    private AccommodationRepository accommodationRepository;

    public List<Accommodation> getAllAccommodations() {
        return accommodationRepository.findAll();
    }

    public Accommodation getAccommodationById(Long id) {
        return accommodationRepository.findById(id).orElse(null);
    }
   public Accommodation updateAccommodation(Long id, Accommodation updatedAccommodation) {
        Optional<Accommodation> accommodationOpt = accommodationRepository.findById(id);
        if (accommodationOpt.isPresent()) {
            Accommodation accommodation = accommodationOpt.get();
            accommodation.setType(updatedAccommodation.getType());
            accommodation.setTotalCapacity(updatedAccommodation.getTotalCapacity());
            accommodation.setAvailableCapacity(updatedAccommodation.getAvailableCapacity());
            return accommodationRepository.save(accommodation);
        }
        return null;
    }
    public Accommodation saveAccommodation(Accommodation accommodation) {
        return accommodationRepository.save(accommodation);
    }

    public void deleteAccommodation(Long id) {
        accommodationRepository.deleteById(id);
    }
}
