package com.shivPrakash.healthCareSystem.Services;

import com.shivPrakash.healthCareSystem.Models.Doctor;
import com.shivPrakash.healthCareSystem.Models.Nurse;
import com.shivPrakash.healthCareSystem.Repositories.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NurseService {

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private DoctorService doctorService;
    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }
    public Optional<Nurse> getNurseById(Long id) {
        return nurseRepository.findById(id);
    }
    public Nurse createNurse(Nurse nurse, Long doctorId) {
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        if (doctorOpt.isPresent()) {
            nurse.setDoctor(doctorOpt.get());
            return nurseRepository.save(nurse);
        }
        return null;
    }
    public Nurse updateNurse(Long id, Nurse updatedNurse) {
        Optional<Nurse> nurseOpt = nurseRepository.findById(id);
        if (nurseOpt.isPresent()) {
            Nurse nurse = nurseOpt.get();
            nurse.setName(updatedNurse.getName());
            nurse.setDoctor(updatedNurse.getDoctor());
            return nurseRepository.save(nurse);
        }
        return null;
    }
    public void deleteNurse(Long id) {
        nurseRepository.deleteById(id);
    }
}
