package com.example.medicalApp.controller;

import com.example.medicalApp.model.Doctor;
import com.example.medicalApp.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/doctor")
@Validated
public class DoctorController {
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/new")
    public ResponseEntity<Doctor> addDoctor(@RequestBody @Valid Doctor doctor,
                                            @RequestParam int medicalUnitId,
                                            @RequestParam int specializationId) {
        Doctor newDoctor = doctorService.addNewDoctor(medicalUnitId, specializationId, doctor);
        return ResponseEntity.created(URI.create("/" + newDoctor.getDoctorId()))
                .body(newDoctor);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<Doctor> getDoctor(@PathVariable @Min(0) int doctorId) {
        return ResponseEntity.ok().body(doctorService.getDoctor(doctorId));
    }

    @GetMapping("/specialization")
    public ResponseEntity<List<Doctor>> getDoctorsFromSpecialization(@RequestParam int specializationId) {
        return ResponseEntity.ok().body(doctorService.getDoctorsFromSpecialization(specializationId));
    }

    @GetMapping("/unit")
    public ResponseEntity<List<Doctor>> getDoctorsFromMedicalUnit(@RequestParam int medicalUnitId) {
        return ResponseEntity.ok().body(doctorService.getDoctorsFromMedicalUnit(medicalUnitId));
    }

    @PutMapping("/{doctorId}/add/specialization/{specializationId}")
    public ResponseEntity<Doctor> updateDoctorSpecializations(@PathVariable int doctorId,
                                                              @PathVariable int specializationId) {
        return ResponseEntity.ok().body(doctorService.addNewSpecializationInfo(doctorId, specializationId));
    }

}
