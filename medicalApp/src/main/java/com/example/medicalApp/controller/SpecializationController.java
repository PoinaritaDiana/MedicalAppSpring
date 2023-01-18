package com.example.medicalApp.controller;

import com.example.medicalApp.model.Specialization;
import com.example.medicalApp.service.SpecializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/specialization")
@Validated
public class SpecializationController {
    private final SpecializationService specializationService;

    @Autowired
    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @PostMapping("/new")
    public ResponseEntity<Specialization> addSpecialization(@RequestBody @Valid Specialization specialization) {
        Specialization newSpecialization = specializationService.addNewSpecialization(specialization);
        return ResponseEntity.created(URI.create("/specialization/" + newSpecialization.getSpecializationId()))
                .body(newSpecialization);
    }

    @PutMapping("/{specializationId}")
    public ResponseEntity<Specialization> updateSpecializationDescription(@PathVariable @Min(0) int specializationId,
                                                                          @RequestBody @NotEmpty @NotNull String description) {
        Specialization updatedSpecialization = specializationService.updateSpecializationDescription(specializationId, description);
        return ResponseEntity.ok().body(updatedSpecialization);
    }

    @GetMapping("/{specializationId}")
    public ResponseEntity<Specialization> getSpecialization(@PathVariable @Min(0) int specializationId) {
        return ResponseEntity.ok().body(specializationService.getSpecialization(specializationId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Specialization>> getAllSpecializations() {
        return ResponseEntity.ok().body(specializationService.getAllSpecializations());
    }
}
