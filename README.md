# Hospital Management System

A comprehensive Hospital Management System built with **Java Spring Boot**, **Thymeleaf**, and **MySQL**. This application allows hospital staff to manage doctors, nurses, patients, and accommodations efficiently.

## Features

- **Patient Management**: 
  - Add, edit, and delete patients.
  - Assign doctors, nurses, and accommodations to patients.
  - Track patient status and emergency levels (1-5).
  - Manage patient check-out and referrals.

- **Doctor Management**: 
  - Add, edit, and delete doctors.
  - Assign doctors to patients.
  - View doctor details, including the number of assigned patients.

- **Nurse Management**: 
  - Add, edit, and delete nurses.
  - Assign nurses to doctors.
  - Manage nurse-doctor relationships.

- **Accommodation Management**: 
  - Add, edit, and track accommodations.
  - Manage total and available accommodation capacity for patients.

- **Emergency Levels**: 
  - Track emergency levels assigned to patients (1-5), indicating the urgency of their condition.
- **Auto Re-allocate**
   - When emergency level of patient is changed the respective patient get new accommodation according to their new emergency level if needed.
   - When a patient is checked out or get referred vacant accomodation is allocated to needed or compromised patients.
- **Responsive and Modern UI**:
  - The application uses pastel colors with color gradients and animations.
  - Designed to be user-friendly and easy to navigate.

## Tools & Technologies

- **Java**: Primary language for backend logic and data processing.
- **Spring Boot**: Framework for creating stand-alone Java applications with RESTful APIs.
- **Thymeleaf**: Server-side Java template engine used for dynamic rendering of HTML pages.
- **MySQL**: Relational database for storing and managing patient, doctor, nurse, and accommodation data.
- **Maven**: Build automation tool for managing dependencies and building the application.
- **CSS**: Custom styling with gradients and animations to enhance user experience.
## Screenshots
### Home Page
![image](https://github.com/user-attachments/assets/b1148f45-774b-41bb-9e17-1d6472273a81)

### Doctors List
![image](https://github.com/user-attachments/assets/0b41277a-0e90-4cb2-83ef-f2dea9d6d511)


### Patients List
![image](https://github.com/user-attachments/assets/2f72ce39-c7ca-4530-bdc0-3c37115ae434)


### Nurse Management
![image](https://github.com/user-attachments/assets/21db9e6f-f6ff-4ef8-9e41-385db5ca2b92)


### Accommodation Form
![image](https://github.com/user-attachments/assets/ba2b34ee-86ef-4a5b-80ac-318cffb4a1fd)
