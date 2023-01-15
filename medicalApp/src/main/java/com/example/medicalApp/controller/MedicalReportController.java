package com.example.medicalApp.controller;

import com.example.medicalApp.model.MedicalReport;
import com.example.medicalApp.service.MedicalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;

@RestController
@RequestMapping("/report")
@Validated
public class MedicalReportController {
    private final MedicalReportService medicalReportService;

    @Autowired
    public MedicalReportController(MedicalReportService medicalReportService) {
        this.medicalReportService = medicalReportService;
    }

    @GetMapping("{reportId}")
    public ResponseEntity<MedicalReport> getMedicalReport(@PathVariable @Min(0) int reportId) {
        return ResponseEntity.ok().body(medicalReportService.getMedicalReport(reportId));
    }

    @PutMapping("{reportId}")
    public ResponseEntity<MedicalReport> updateMedicalReportInfo(@PathVariable int reportId,
                                                                 @RequestBody @Valid MedicalReport report) {
        return ResponseEntity.ok().body(medicalReportService.updateMedicalReport(reportId, report));
    }

    @PostMapping("/new")
    public ResponseEntity<MedicalReport> addNewMedicalReport(@RequestParam int appointmentId,
                                                             @RequestBody @Valid MedicalReport report) {
        MedicalReport newReport = medicalReportService.addNewMedicalReportForAppointment(appointmentId, report);
        return ResponseEntity.created(URI.create("/" + newReport.getMedicalReportId()))
                .body(newReport);
    }
}
