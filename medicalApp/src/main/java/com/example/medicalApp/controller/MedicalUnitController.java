package com.example.medicalApp.controller;

import com.example.medicalApp.model.MedicalUnit;
import com.example.medicalApp.service.MedicalUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/unit")
@Validated
public class MedicalUnitController {
    private final MedicalUnitService medicalUnitService;

    @Autowired
    public MedicalUnitController(MedicalUnitService medicalUnitService) {
        this.medicalUnitService = medicalUnitService;
    }

    @PostMapping("/new")
    public ResponseEntity<MedicalUnit> addMedicalUnit(@RequestBody @Valid MedicalUnit medicalUnit) {
        MedicalUnit newMedicalUnit = medicalUnitService.addNewMedicalUnit(medicalUnit);
        return ResponseEntity.created(URI.create("/" + newMedicalUnit.getMedicalUnitId()))
                .body(newMedicalUnit);
    }

    @GetMapping
    public ResponseEntity<List<MedicalUnit>> getMedicalUnitsFromCity(@RequestParam @NotNull @NotEmpty String cityName) {
        return ResponseEntity.ok().body(medicalUnitService.getAllMedicalUnitsFromCity(cityName));
    }
}
