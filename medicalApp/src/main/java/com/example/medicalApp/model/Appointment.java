package com.example.medicalApp.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentId;

    @Column(name = "appointment_date")
    @NotNull(message = "Appointment date cannot be null")
    private LocalDateTime appointmentDate;

    @OneToOne(optional = true)
    @JoinColumn(name = "medical_report_id")
    @JsonIgnore
    private MedicalReport medicalReport;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(optional = false)
    @JoinColumn(name = "investigation_id")
    private Investigation investigation;

    public Appointment() {
    }

    public Appointment(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setMedicalReport(MedicalReport medicalReport) {
        this.medicalReport = medicalReport;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setInvestigation(Investigation investigation) {
        this.investigation = investigation;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public MedicalReport getMedicalReport() {
        return medicalReport;
    }

    public Patient getPatient() {
        return patient;
    }

    public Investigation getInvestigation() {
        return investigation;
    }
}
