package com.example.medicalApp.controller;

import com.example.medicalApp.model.Investigation;
import com.example.medicalApp.service.InvestigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/investigation")
@Validated
public class InvestigationController {
    private final InvestigationService investigationService;

    @Autowired
    public InvestigationController(InvestigationService investigationService) {
        this.investigationService = investigationService;
    }

    @GetMapping("/{investigationId}")
    public ResponseEntity<Investigation> getInvestigation(@PathVariable @Min(0) int investigationId) {
        return ResponseEntity.ok().body(investigationService.getInvestigation(investigationId));
    }

    @PostMapping("/new")
    public ResponseEntity<Investigation> addInvestigation(@RequestParam int doctorId, @RequestBody @Valid Investigation investigation) {
        Investigation newInvestigation = investigationService.addNewInvestigation(doctorId, investigation);
        return ResponseEntity.created(URI.create("/investigation/" + newInvestigation.getInvestigationId()))
                .body(newInvestigation);
    }

    @PutMapping("/{investigationId}")
    public ResponseEntity<Investigation> updateInvestigationPrice(@PathVariable @Min(0) int investigationId,
                                                                  @RequestParam @Min(10) @Max(1000) double price) {
        return ResponseEntity.ok().body(investigationService.updatePriceOfInvestigation(investigationId, price));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Investigation>> getInvestigationsWithMaxPrice(@RequestParam @Min(0) double price) {
        return ResponseEntity.ok().body(investigationService.getInvestigationsWithMaxPrice(price));
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<Investigation>> getInvestigationsForDoctor(@RequestParam @Min(0) int doctorId) {
        return ResponseEntity.ok().body(investigationService.getInvestigationsForDoctor(doctorId));
    }

    @DeleteMapping("/{investigationId}")
    public ResponseEntity<String> deleteInvestigation(@PathVariable int investigationId) {
        investigationService.deleteInvestigation(investigationId);
        return ResponseEntity.ok().body("Investigation " + investigationId + " was successfully deleted");
    }
}
