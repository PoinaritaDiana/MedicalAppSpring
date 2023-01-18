package com.example.medicalApp.controller;

import com.example.medicalApp.model.Appointment;
import com.example.medicalApp.model.Doctor;
import com.example.medicalApp.model.MedicalUnit;
import com.example.medicalApp.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/appointment")
@Validated
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable int appointmentId) {
        return ResponseEntity.ok().body(appointmentService.getAppointment(appointmentId));
    }

    @GetMapping("/old")
    public ResponseEntity<List<Appointment>> getOldAppointments(@RequestParam int patientId) {
        return ResponseEntity.ok().body(appointmentService.getOldAppointments(patientId));
    }

    @GetMapping("/future")
    public ResponseEntity<List<Appointment>> getFutureAppointments(@RequestParam int patientId) {
        return ResponseEntity.ok().body(appointmentService.getFutureAppointments(patientId));
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<String> deleteAppointment(@PathVariable int appointmentId) {
        return ResponseEntity.ok().body(appointmentService.deleteFutureAppointment(appointmentId));
    }

    @PostMapping("/new")
    public ResponseEntity<Appointment> addNewAppointmentForInvestigation(@RequestParam int patientId,
                                                                         @RequestParam int investigationId,
                                                                         @RequestBody @Valid Appointment appointment) {
        Appointment newAppointment = appointmentService.addNewAppointment(patientId, investigationId, appointment);
        return ResponseEntity.created(URI.create("/appointment/" + newAppointment.getAppointmentId()))
                .body(newAppointment);
    }
}
