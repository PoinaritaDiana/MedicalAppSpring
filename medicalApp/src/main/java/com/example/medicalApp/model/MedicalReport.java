package com.example.medicalApp.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "medical_report")
public class MedicalReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int medicalReportId;

    @NotNull(message = "The diagnosis given to the patient cannot be null")
    @NotEmpty(message = "The diagnosis given to the patient cannot be empty")
    private String diagnostic;

    @Column(name = "interpretation_results")
    private String interpretationResults;

    @Column(name = "additional_investigation_required")
    private String additionalInvestigationRequired;

    public MedicalReport() {
    }

    public MedicalReport(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public int getMedicalReportId() {
        return medicalReportId;
    }

    public void setMedicalReportId(int medicalReportId) {
        this.medicalReportId = medicalReportId;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getInterpretationResults() {
        return interpretationResults;
    }

    public void setInterpretationResults(String interpretationResults) {
        this.interpretationResults = interpretationResults;
    }

    public String getAdditionalInvestigationRequired() {
        return additionalInvestigationRequired;
    }

    public void setAdditionalInvestigationRequired(String additionalInvestigationRequired) {
        this.additionalInvestigationRequired = additionalInvestigationRequired;
    }
}
