package com.example.medicalApp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int patientId;

    @NotNull(message = "LastName cannot be null")
    @NotEmpty(message = "LastName cannot be empty")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "FirstName cannot be null")
    @NotEmpty(message = "FirstName cannot be empty")
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "Phone number cannot be null")
    @NotEmpty(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\d{10}$")
    private String phone;

    @Column(name = "date_of_birth")
    @NotNull(message = "The patient's date of birth cannot be null")
    @NotEmpty(message = "The patient's date of birth cannot be empty")
    private String dateOfBirth;

    private String gender;

    @Column(name = "medical_history")
    private String medicalHistory;

    @JsonIgnore
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointmentList = new ArrayList<>();

    public Patient() {
    }

    public Patient(String lastName, String firstName, String phone, String dateOfBirth) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }
}
