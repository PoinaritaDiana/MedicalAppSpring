package com.example.medicalApp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medical_unit")
public class MedicalUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int medicalUnitId;

    @NotNull(message = "The city where the office is located cannot be null")
    @NotEmpty(message = "The city where the office is located cannot be empty")
    private String city;

    @NotNull(message = "The name of the medical unit cannot be null")
    @NotEmpty(message = "The name of the medical unit cannot be empty")
    private String name;

    @NotNull(message = "The phone number of the medical unit cannot be null")
    @NotEmpty(message = "The phone number of the medical unit cannot be empty")
    @Pattern(regexp = "^\\d{10}$")
    private String phone;

    @JsonIgnore
    @OneToMany(mappedBy = "medicalUnit")
    private List<Doctor> doctorList = new ArrayList<>();

    public MedicalUnit() {
    }

    public MedicalUnit(String city, String name, String phone) {
        this.city = city;
        this.name = name;
        this.phone = phone;
    }

    public int getMedicalUnitId() {
        return medicalUnitId;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setMedicalUnitId(int medicalUnitId) {
        this.medicalUnitId = medicalUnitId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }
}
