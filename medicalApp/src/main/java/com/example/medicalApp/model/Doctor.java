package com.example.medicalApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doctorId;

    @NotNull(message = "LastName cannot be null")
    @NotEmpty(message = "LastName cannot be empty")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "FirstName cannot be null")
    @NotEmpty(message = "FirstName cannot be empty")
    @Column(name = "first_name")
    private String firstName;

    private String phone;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medical_unit_id")
    private MedicalUnit medicalUnit;

    @ManyToMany
    @JoinTable(name = "doctor_specialization", joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id"))
    private List<Specialization> specializationList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "doctor")
    private List<Investigation> investigationList = new ArrayList<>();

    public Doctor() {
    }

    public Doctor(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
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

    public void setMedicalUnit(MedicalUnit medicalUnit) {
        this.medicalUnit = medicalUnit;
    }

    public void setSpecializationList(List<Specialization> specializationList) {
        this.specializationList = specializationList;
    }

    public void setInvestigationList(List<Investigation> investigationList) {
        this.investigationList = investigationList;
    }

    public int getDoctorId() {
        return doctorId;
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

    public MedicalUnit getMedicalUnit() {
        return medicalUnit;
    }

    public List<Specialization> getSpecializationList() {
        return specializationList;
    }

    public List<Investigation> getInvestigationList() {
        return investigationList;
    }
}
