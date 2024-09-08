package com.shivPrakash.healthCareSystem.Controller;

import com.shivPrakash.healthCareSystem.Models.Accommodation;
import com.shivPrakash.healthCareSystem.Models.Accommodation.AccommodationType;
import com.shivPrakash.healthCareSystem.Repositories.AccommodationRepository;
import com.shivPrakash.healthCareSystem.Services.AccommodationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/accommodations")
public class AccommodationController {

    @Autowired
    private AccommodationService accommodationService;

    // Show list of accommodations
    @GetMapping
    public String listAccommodations(Model model) {
        List<Accommodation> accommodations = accommodationService.getAllAccommodations();
        model.addAttribute("accommodations", accommodations);
        return "accommodation-list";
    }

    // Show form to add a new accommodation
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("accommodation", new Accommodation());
        model.addAttribute("types", AccommodationType.values());
        return "accommodation-form";
    }

    // Save accommodation
    @PostMapping("/save")
    public String saveAccommodation(@ModelAttribute("accommodation") Accommodation accommodation) {
    	List<Accommodation> available = accommodationService.getAllAccommodations();
    	boolean flag = true;
    	for (int i = 0; i < available.size(); i++) {
    		if(accommodation.getType().equals(available.get(i).getType())) {
    			flag=false;
    			break;
    		}
		}
    	if(flag) {
			 accommodationService.saveAccommodation(accommodation);
		}
       
        return "redirect:/accommodations";
    }
    // Show form to edit accommodation
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Accommodation accommodation = accommodationService.getAccommodationById(id);
        if (accommodation != null) {
            model.addAttribute("accommodation", accommodation);
            model.addAttribute("types", AccommodationType.values());
            return "accommodation-form";
        }
        return "redirect:/accommodations";
    }

    // Delete accommodation
    @GetMapping("/delete/{id}")
    public String deleteAccommodation(@PathVariable Long id) {
        accommodationService.deleteAccommodation(id);
        return "redirect:/accommodations";
    }
}
