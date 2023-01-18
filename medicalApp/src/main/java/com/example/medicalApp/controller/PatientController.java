package com.example.medicalApp.controller;

import com.example.medicalApp.model.Patient;
import com.example.medicalApp.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/patient")
@Validated
public class PatientController {
    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/new")
    public ResponseEntity<Patient> addPatient(@RequestBody @Valid Patient patient) {
        Patient newPatient = patientService.addNewPatient(patient);
        return ResponseEntity.created(URI.create("/patient/" + newPatient.getPatientId()))
                .body(newPatient);
    }

    @PutMapping("/{patientId}/medicalhistory")
    public ResponseEntity<String> addInfoToPatientMedicalHistory(@PathVariable int patientId,
                                                                 @RequestBody @NotEmpty @NotNull String newMedicalHistoryInfo) {
        return ResponseEntity.ok().body(patientService.updateMedicalHistoryWithNewInfo(patientId, newMedicalHistoryInfo));
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<Patient> getPatient(@PathVariable int patientId) {
        return ResponseEntity.ok().body(patientService.getPatient(patientId));
    }
}
