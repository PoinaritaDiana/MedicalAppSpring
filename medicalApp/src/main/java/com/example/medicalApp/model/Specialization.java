package com.example.medicalApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "specialization")
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int specializationId;

    @NotNull(message = "Name of specialization cannot be null")
    @NotEmpty(message = "Name of specialization cannot be empty!")
    private String name;

    @NotNull(message = "Description of specialization cannot be null")
    @NotEmpty(message = "Description of specialization cannot be empty")
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "specializationList")
    private List<Doctor> doctorList = new ArrayList<>();

    public Specialization() {
    }

    public Specialization(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setSpecializationId(int specializationId) {
        this.specializationId = specializationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public int getSpecializationId() {
        return specializationId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }
}
