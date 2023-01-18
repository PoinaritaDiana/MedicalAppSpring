package com.example.medicalApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "investigation")
public class Investigation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int investigationId;

    @NotNull(message = "The name of the investigation cannot be null")
    @NotEmpty(message = "The name of the investigation cannot be empty")
    private String name;

    @NotNull(message = "The description of the investigation cannot be null")
    @NotEmpty(message = "The description of the investigation cannot be empty")
    private String description;

    @Min(value = 10, message = "The price of the investigation must be higher than 10 lei")
    @Max(value = 1000, message = "The price of the investigation must be less than 1000 lei")
    private double price;

    @Min(value = 10, message = "The duration of the investigation cannot be less than 10 minutes")
    @Max(value = 60, message = "The duration of the investigation cannot be less than 60 minutes")
    private long time;

    @Column(name = "cnas_deductable")
    private boolean cnasDeductable;

    @Column(name = "private_insurance_deductable")
    private boolean privateInsuranceDeductable;

    @ManyToOne(optional = false)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @JsonIgnore
    @OneToMany(mappedBy = "investigation")
    private List<Appointment> appointmentList = new ArrayList<>();

    public Investigation() {

    }

    public Investigation(String name, String description, double price, long time) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.time = time;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public int getInvestigationId() {
        return investigationId;
    }

    public void setInvestigationId(int investigationId) {
        this.investigationId = investigationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isCnasDeductable() {
        return cnasDeductable;
    }

    public void setCnasDeductable(boolean cnasDeductable) {
        this.cnasDeductable = cnasDeductable;
    }

    public boolean isPrivateInsuranceDeductable() {
        return privateInsuranceDeductable;
    }

    public void setPrivateInsuranceDeductable(boolean privateInsuranceDeductable) {
        this.privateInsuranceDeductable = privateInsuranceDeductable;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
