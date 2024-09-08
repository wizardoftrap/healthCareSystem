package com.shivPrakash.healthCareSystem.Controller;

import com.shivPrakash.healthCareSystem.Models.Doctor;
import com.shivPrakash.healthCareSystem.Models.Nurse;
import com.shivPrakash.healthCareSystem.Services.DoctorService;
import com.shivPrakash.healthCareSystem.Services.NurseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private NurseService nurseService;
    private List<String> specialtyOptions = Arrays.asList(
            "Cardiology - Heart Diseases",
            "Neurology - Brain Disorders",
            "Oncology - Cancer Treatment",
            "Pediatrics - Child Health",
            "Orthopedics - Bone and Joint",
            "Gastroenterology - Digestive System",
            "Dermatology - Skin Conditions",
            "Pulmonology - Lung Diseases",
            "Endocrinology - Hormonal Disorders",
            "Rheumatology - Autoimmune Diseases",
            "Vacant"
        );

    // Display list of all doctors
    @GetMapping
    public String listDoctors(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "doctor-list";
    }

    // Show the form to add a new doctor
    @GetMapping("/new")
    public String showAddDoctorForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("specialtyOptions", specialtyOptions);
        return "doctor-form";
    }

    // Show the form to edit an existing doctor
    @GetMapping("/edit/{id}")
    public String showEditDoctorForm(@PathVariable("id") Long id, Model model) {
    	  Optional<Doctor> doctor = doctorService.getDoctorById(id);
          if (doctor.isPresent()) {
              model.addAttribute("doctor", doctor.get());
              model.addAttribute("specialtyOptions", specialtyOptions);
          } else {
              return "redirect:/doctors";
          }
          return "doctor-form";
    }

    // Save or update a doctor
    @PostMapping("/save")
    public String saveDoctor(@ModelAttribute("doctor") Doctor doctor) {
        doctorService.createDoctor(doctor);
        return "redirect:/doctors";
    }
    /*
     
    */
    @GetMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable("id") Long id) {
    	Optional<Doctor> doctor = doctorService.getDoctorById(id);
    	List<Nurse> nurses = nurseService.getAllNurses();
    	for (Iterator iterator = nurses.iterator(); iterator.hasNext();) {
			Nurse nurse = (Nurse) iterator.next();
			if(nurse.getDoctor().getId()==id) {
				Optional<Doctor> vacant =doctorService.getDoctorByName("Vacant");
				if(vacant.isPresent()) {
					nurse.setDoctor(vacant.get());
				}
                				
			}
		}
        doctorService.deleteDoctor(id);; 
        return "redirect:/doctors";
    }
}
