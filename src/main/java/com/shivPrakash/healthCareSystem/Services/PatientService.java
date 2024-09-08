package com.shivPrakash.healthCareSystem.Services;

import com.shivPrakash.healthCareSystem.Models.Accommodation;
import com.shivPrakash.healthCareSystem.Models.Accommodation.AccommodationType;
import com.shivPrakash.healthCareSystem.Models.Doctor;
import com.shivPrakash.healthCareSystem.Models.Nurse;
import com.shivPrakash.healthCareSystem.Models.Patient;
import com.shivPrakash.healthCareSystem.Repositories.AccommodationRepository;
import com.shivPrakash.healthCareSystem.Repositories.DoctorRepository;
import com.shivPrakash.healthCareSystem.Repositories.NurseRepository;
import com.shivPrakash.healthCareSystem.Repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository  doctorRepository;
    @Autowired
    private NurseRepository  nurseRepository;
    @Autowired
    private NurseService  nurseService;
    @Autowired
    private DoctorService  doctorService;
    @Autowired
    private AccommodationRepository  accommodationRepository;
    @Autowired
    private AccommodationService accommodationService;
    // Get all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Get patient by ID
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // Add a new patient
    public Patient addPatient(Patient patient) {
    	allocatePatient(patient);
        return patientRepository.save(patient);
    }

    // Update an existing patient
    public Patient updatePatient(Patient patient) {
        if (patient.getId() == null || !patientRepository.existsById(patient.getId())) {
            throw new IllegalArgumentException("Patient does not exist");
        }
        Accommodation currAccommodation = patient.getAccommodation();
        Doctor currDoctor =patient.getAssignedDoctor();
        allocatePatient(patient);
        if(!currDoctor.equals(doctorRepository.findByName("Vacant").get())) {
        	 currDoctor.setNumPatients(currDoctor.getNumPatients()-1);	
        	 doctorService.updateDoctor(currDoctor.getId(), currDoctor);  
        }
        if(!currAccommodation.equals(accommodationRepository.findAccommodationByType(AccommodationType.NO_ACCOMMODATION))) {
            currAccommodation.setAvailableCapacity(currAccommodation.getAvailableCapacity()+1);
        	accommodationService.updateAccommodation(currAccommodation.getId(), currAccommodation);
        }
            	
        if(!currAccommodation.equals(patient.getAccommodation())) {        	
        	if(currAccommodation.getType().equals(Accommodation.AccommodationType.PRIVATE_WARD)) {
        		List<Patient> patients=getAllPatients();
        		for (int i = 0; i < patients.size(); i++) {
        			if(patients.get(i).getEmergencyLevel()==4&&patients.get(i).getAccommodation().getType().equals(Accommodation.AccommodationType.NORMAL_BED)) {
        				patients.get(i).setAccommodation(accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.PRIVATE_WARD));
        				currAccommodation.setAvailableCapacity(currAccommodation.getAvailableCapacity()-1);
        				patientRepository.save(patients.get(i));
        				break;
        			}
    				
    			}
        	}
        	if(currAccommodation.getType().equals(Accommodation.AccommodationType.NORMAL_BED)) {
        		List<Patient> patients=getAllPatients();
        		for (int i = 0; i < patients.size(); i++) {
        			if(patients.get(i).getEmergencyLevel()==2&&patients.get(i).getAccommodation().getType().equals(Accommodation.AccommodationType.NO_ACCOMMODATION)) {
        				patients.get(i).setAccommodation(accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.NORMAL_BED));
        				currAccommodation.setAvailableCapacity(currAccommodation.getAvailableCapacity()-1);
        				patientRepository.save(patients.get(i));
        				break;
        			}
    				
    			}
        	}
        }
        return patientRepository.save(patient);
    }
    public void allocatePatient(Patient patient) {
        int emergencyLevel = patient.getEmergencyLevel();
        List<Accommodation> accommodations = accommodationService.getAllAccommodations();
        Accommodation ward = accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.PRIVATE_WARD);
        Accommodation icu = accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.ICU);
        Accommodation bed = accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.NORMAL_BED);
        List<Doctor> doctors = doctorService.getAllDoctors();
        Doctor assignedDoctor = null;
        List<Doctor> availableDoctors = new ArrayList<>();
        for (int i = 0; i < doctors.size(); i++) {
        	if((patient.getProblem().equals(doctors.get(i).getSpecialty())&&(doctors.get(i).getNumPatients()<5))) {
        		availableDoctors.add(doctors.get(i));
        	}
        }
        patient.setStatus(Patient.Status.CHECKED_IN);
        boolean flag=false;
        for (int i = 0; i <availableDoctors.size(); i++) {
        		patient.setAssignedDoctor(availableDoctors.get(i));
        		assignedDoctor=availableDoctors.get(i);
        		flag=true;
        		break;
	    }
        if(!flag) {
        	referPatient(patient);
        }
        else {
        	assignedDoctor.setNumPatients(assignedDoctor.getNumPatients()+1);
        	doctorService.updateDoctor(assignedDoctor.getId(), assignedDoctor);
            List<Nurse> nurses = nurseService.getAllNurses();
            List<Nurse> icuNurses = new ArrayList<>();
            List<Nurse> wardNurses = new ArrayList<>();
            for (int i = 0; i < nurses.size(); i++) {
				if(nurses.get(i).getDoctor().equals(assignedDoctor)){
					icuNurses.add(nurses.get(i));
				}
			}
            Random rand = new Random();
            int r = rand.nextInt(1);
            wardNurses.add(icuNurses.get(r));
        	Optional<Nurse> vacNurse =nurseRepository.findByName("Vacant");
        	List<Nurse> vacNurses=new ArrayList<Nurse>();
        	if(vacNurse.isPresent()) {
        		vacNurses.add(vacNurse.get());
        	}
            switch (emergencyLevel) {
                case 5:
                    if (icu.getAvailableCapacity()>0) {
                        patient.setAccommodation(icu);
                        icu.setAvailableCapacity(icu.getAvailableCapacity()-1);
                        accommodationService.saveAccommodation(icu);
                        patient.setAssignedNurses(icuNurses);
                        
                    } else {
                        referPatient(patient); // Refer if no ICU is available
                    }
                    break;

                case 4:
                	
                    if (ward.getAvailableCapacity()>0) {
                        patient.setAccommodation(ward);
                        ward.setAvailableCapacity(ward.getAvailableCapacity()-1);
                        accommodationService.saveAccommodation(ward);
                        patient.setAssignedNurses(wardNurses);
                        
                    } 
                    else if((ward.getAvailableCapacity()==0)&&(bed.getAvailableCapacity()>0)){
                    	patient.setAccommodation(bed);
                        bed.setAvailableCapacity(bed.getAvailableCapacity()-1);
                        accommodationService.saveAccommodation(bed);
                        patient.setAssignedNurses(wardNurses);
                    }
                    else {
                        referPatient(patient); // Refer if no ICU is available
                    }
                    break;

                case 3:
                    if (bed.getAvailableCapacity()>0) {
                    	patient.setAccommodation(bed);
                        bed.setAvailableCapacity(bed.getAvailableCapacity()-1);
                        accommodationService.saveAccommodation(bed);
                        patient.setAssignedNurses(vacNurses);
                    } else {
                        referPatient(patient); // Refer if no bed available
                    }
                    break;

                case 2: 
                	patient.setAssignedNurses(vacNurses);
                	if (bed.getAvailableCapacity()>0) {
                    	patient.setAccommodation(bed);
                        bed.setAvailableCapacity(bed.getAvailableCapacity()-1);
                        accommodationService.saveAccommodation(bed);
                        
                    } else {
                    	patient.setAccommodation(accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.NO_ACCOMMODATION)); 
                    }
                    break;

                case 1:
                	patient.setAssignedNurses(vacNurses);
                	patient.setAccommodation(accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.NO_ACCOMMODATION));
                    break;

                default:
                    throw new IllegalArgumentException("Unknown emergency level: " + emergencyLevel);
            }
        }
    }
    public void referPatient(Patient patient) {
    	if(patient.getStatus().equals(Patient.Status.CHECKED_OUT)||patient.getStatus().equals(Patient.Status.REFERRED)) {
    	}
    	else {
    		Accommodation currAccommodation = patient.getAccommodation();
            Doctor currDoctor =patient.getAssignedDoctor();
            //allocatePatient(patient);
     
        	patient.setAccommodation(accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.NO_ACCOMMODATION));
        	Optional<Doctor> vacDoctor =doctorRepository.findByName("Vacant");
        	if(vacDoctor.isPresent()) {
        		patient.setAssignedDoctor(vacDoctor.get());
        	}
        	Optional<Nurse> vacNurse =nurseRepository.findByName("Vacant");
        	List<Nurse> vacNurses=new ArrayList<Nurse>();
        	if(vacNurse.isPresent()) {
        		vacNurses.add(vacNurse.get());
        	}
        	patient.setAssignedNurses(vacNurses);
        	patient.setStatus(Patient.Status.REFERRED);
        	if(!currDoctor.equals(patient.getAssignedDoctor())) {
            	currDoctor.setNumPatients(currDoctor.getNumPatients()-1);
            	doctorService.updateDoctor(currDoctor.getId(), currDoctor);      	
            }
            if(!currAccommodation.equals(patient.getAccommodation())) {
            	if(!currAccommodation.equals(accommodationRepository.findAccommodationByType(AccommodationType.NO_ACCOMMODATION))) {
            		currAccommodation.setAvailableCapacity(currAccommodation.getAvailableCapacity()+1);
                	accommodationService.updateAccommodation(currAccommodation.getId(), currAccommodation);
            	}
            	if(currAccommodation.getType().equals(Accommodation.AccommodationType.PRIVATE_WARD)) {
            		List<Patient> patients=getAllPatients();
            		for (int i = 0; i < patients.size(); i++) {
            			if(patients.get(i).getEmergencyLevel()==4&&patients.get(i).getAccommodation().getType().equals(Accommodation.AccommodationType.NORMAL_BED)) {
            				patients.get(i).setAccommodation(accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.PRIVATE_WARD));
            				currAccommodation.setAvailableCapacity(currAccommodation.getAvailableCapacity()-1);
            				patientRepository.save(patients.get(i));
            				break;
            			}
        				
        			}
            	}
            	if(currAccommodation.getType().equals(Accommodation.AccommodationType.NORMAL_BED)) {
            		List<Patient> patients=getAllPatients();
            		for (int i = 0; i < patients.size(); i++) {
            			if(patients.get(i).getEmergencyLevel()==2&&patients.get(i).getAccommodation().getType().equals(Accommodation.AccommodationType.NO_ACCOMMODATION)) {
            				patients.get(i).setAccommodation(accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.NORMAL_BED));
            				currAccommodation.setAvailableCapacity(currAccommodation.getAvailableCapacity()-1);
            				patientRepository.save(patients.get(i));
            				break;
            			}
        				
        			}
            	}
            }
        	//reAllocate(patient);
    	}
  	
    }

    // Mark patient as checked out
    public void checkedOutPatient(Long id) {
        Optional<Patient> patient = getPatientById(id);
        if (patient.isPresent()) {
        	if((patient.get().getStatus().equals(Patient.Status.CHECKED_OUT)||patient.get().getStatus().equals(Patient.Status.REFERRED))) {
        		
        	}
        	else {
        		Accommodation currAccommodation = patient.get().getAccommodation();
                Doctor currDoctor =patient.get().getAssignedDoctor();
                patient.get().setStatus(Patient.Status.CHECKED_OUT);
            	patient.get().setAccommodation(accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.NO_ACCOMMODATION));
            	Optional<Doctor> vacDoctor =doctorRepository.findByName("Vacant");
            	if(vacDoctor.isPresent()) {
            		patient.get().setAssignedDoctor(vacDoctor.get());
            	}
            	Optional<Nurse> vacNurse =nurseRepository.findByName("Vacant");
            	List<Nurse> vacNurses=new ArrayList<Nurse>();
            	if(vacNurse.isPresent()) {
            		vacNurses.add(vacNurse.get());
            	}
            	patient.get().setAssignedNurses(vacNurses);
                if(!currDoctor.equals(patient.get().getAssignedDoctor())) {
                	currDoctor.setNumPatients(currDoctor.getNumPatients()-1);
                	doctorService.updateDoctor(currDoctor.getId(), currDoctor);      	
                }
                if(!currAccommodation.equals(patient.get().getAccommodation())) {
                	if(!currAccommodation.equals(accommodationRepository.findAccommodationByType(AccommodationType.NO_ACCOMMODATION))) {
                    	currAccommodation.setAvailableCapacity(currAccommodation.getAvailableCapacity()+1);
                    	accommodationService.updateAccommodation(currAccommodation.getId(), currAccommodation);
                	}

                	if(currAccommodation.getType().equals(Accommodation.AccommodationType.PRIVATE_WARD)) {
                		List<Patient> patients=getAllPatients();
                		for (int i = 0; i < patients.size(); i++) {
                			if(patients.get(i).getEmergencyLevel()==4&&patients.get(i).getAccommodation().getType().equals(Accommodation.AccommodationType.NORMAL_BED)) {
                				patients.get(i).setAccommodation(accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.PRIVATE_WARD));
                				currAccommodation.setAvailableCapacity(currAccommodation.getAvailableCapacity()-1);
                				patientRepository.save(patients.get(i));
                				break;
                			}
            				
            			}
                	}
                	if(currAccommodation.getType().equals(Accommodation.AccommodationType.NORMAL_BED)) {
                		List<Patient> patients=getAllPatients();
                		for (int i = 0; i < patients.size(); i++) {
                			if(patients.get(i).getEmergencyLevel()==2&&patients.get(i).getAccommodation().getType().equals(Accommodation.AccommodationType.NO_ACCOMMODATION)) {
                				patients.get(i).setAccommodation(accommodationRepository.findAccommodationByType(Accommodation.AccommodationType.NORMAL_BED));
                				currAccommodation.setAvailableCapacity(currAccommodation.getAvailableCapacity()-1);
                				patientRepository.save(patients.get(i));
                				break;
                			}
            				
            			}
                	}
                }
            	
                patientRepository.save(patient.get());
        	}

        }
    }
}
