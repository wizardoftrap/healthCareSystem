package com.shivPrakash.healthCareSystem.Controller;

import com.shivPrakash.healthCareSystem.Models.Doctor;
import com.shivPrakash.healthCareSystem.Models.Nurse;
import com.shivPrakash.healthCareSystem.Services.DoctorService;
import com.shivPrakash.healthCareSystem.Services.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/nurses")
public class NurseController {

    @Autowired
    private NurseService nurseService;

    @Autowired
    private DoctorService doctorService;

    // Display list of all nurses
    @GetMapping
    public String listNurses(Model model) {
        List<Nurse> nurses = nurseService.getAllNurses();
        model.addAttribute("nurses", nurses);
        return "nurses-list";
    }

    // Show the form to add a new nurse
    @GetMapping("/new")
    public String showAddNurseForm(Model model) {
        model.addAttribute("nurse", new Nurse());
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "nurse-form";
    }

    // Show the form to edit an existing nurse
    @GetMapping("/edit/{id}")
    public String showEditNurseForm(@PathVariable("id") Long id, Model model) {
        Optional<Nurse> nurse = nurseService.getNurseById(id);
        if (nurse.isPresent()) {
            model.addAttribute("nurse", nurse.get());
            List<Doctor> doctors = doctorService.getAllDoctors();
            model.addAttribute("doctors", doctors);
            return "nurse-form";
        } else {
            return "redirect:/nurses";
        }
    }

    // Save or update a nurse
    @PostMapping("/save")
    public String saveNurse(@ModelAttribute("nurse") Nurse nurse) {
        // Save nurse without department
        nurseService.createNurse(nurse, nurse.getDoctor().getId());
        return "redirect:/nurses";
    }

    // Delete a nurse
    @GetMapping("/delete/{id}")
    public String deleteNurse(@PathVariable("id") Long id) {
        nurseService.deleteNurse(id);
        return "redirect:/nurses";
    }
}
