package com.shivPrakash.healthCareSystem.Controller;

import com.shivPrakash.healthCareSystem.Models.Patient;
import com.shivPrakash.healthCareSystem.Services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // List all patients
    @GetMapping
    public String listPatients(Model model) {
        List<Patient> patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return "patient-list";
    }

    // Show the form for creating a new patient
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("patient", new Patient()); // Add an empty Patient object
        model.addAttribute("problems", List.of(
            "Cardiology - Heart Diseases",
            "Neurology - Brain Disorders",
            "Oncology - Cancer Treatment",
            "Pediatrics - Child Health",
            "Orthopedics - Bone and Joint",
            "Gastroenterology - Digestive System",
            "Dermatology - Skin Conditions",
            "Pulmonology - Lung Diseases",
            "Endocrinology - Hormonal Disorders",
            "Rheumatology - Autoimmune Diseases"
        ));
        model.addAttribute("emergencyLevels", List.of(1, 2, 3, 4, 5));
        return "patient-form";
    }

    // Add a new patient using form submission
    @PostMapping
    public String addPatient(
            @RequestParam String name,
            @RequestParam int age,
            @RequestParam int emergencyLevel,
            @RequestParam String problem) {

        Patient patient = new Patient();
        patient.setName(name);
        patient.setAge(age);
        patient.setEmergencyLevel(emergencyLevel);
        patient.setProblem(problem);

        patientService.addPatient(patient);

        return "redirect:/patients";
    }

    // Show the form for editing an existing patient
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Patient patient = patientService.getPatientById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + id));

        model.addAttribute("patient", patient); // Ensure patient object is added here
        model.addAttribute("problems", List.of(
            "Cardiology - Heart Diseases",
            "Neurology - Brain Disorders",
            "Oncology - Cancer Treatment",
            "Pediatrics - Child Health",
            "Orthopedics - Bone and Joint",
            "Gastroenterology - Digestive System",
            "Dermatology - Skin Conditions",
            "Pulmonology - Lung Diseases",
            "Endocrinology - Hormonal Disorders",
            "Rheumatology - Autoimmune Diseases"
        ));
        model.addAttribute("emergencyLevels", List.of(1, 2, 3, 4, 5));
        return "patient-form";
    }

    // Update an existing patient using form submission
    @PostMapping("/update/{id}")
    public String updatePatient(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam int age,
            @RequestParam int emergencyLevel,
            @RequestParam String problem) {

        Patient patient = patientService.getPatientById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient Id:" + id));

        patient.setName(name);
        patient.setAge(age);
        patient.setEmergencyLevel(emergencyLevel);
        patient.setProblem(problem);

        patientService.updatePatient(patient);

        return "redirect:/patients";
    }

    // Check out a patient
    @GetMapping("/checkout/{id}")
    public String checkedOutPatient(@PathVariable Long id) {
        patientService.checkedOutPatient(id);
        return "redirect:/patients";
    }
    @GetMapping("/refer/{id}")
    public String referPatient(@PathVariable Long id) {
        patientService.referPatient(patientService.getPatientById(id).get());
        return "redirect:/patients";
    }
}
