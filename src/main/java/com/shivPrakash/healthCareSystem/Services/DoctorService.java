package com.shivPrakash.healthCareSystem.Services;

import com.shivPrakash.healthCareSystem.Models.Doctor;
import com.shivPrakash.healthCareSystem.Repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }
    public Optional<Doctor> getDoctorByName(String name) {
        return doctorRepository.findByName(name);
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(id);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            doctor.setName(updatedDoctor.getName());
            doctor.setSpecialty(updatedDoctor.getSpecialty());
            doctor.setNumPatients(updatedDoctor.getNumPatients());
            return doctorRepository.save(doctor);
        }
        return null;
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
