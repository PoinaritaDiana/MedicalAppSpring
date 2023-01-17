package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.MedicalReport;
import com.example.medicalApp.repository.MedicalReportRepository;
import com.example.medicalApp.utils.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalReportServiceTests {

    @InjectMocks
    private MedicalReportService medicalReportService;

    @Mock
    private MedicalReportRepository medicalReportRepository;

    @Test
    @DisplayName("Adding new medical report - happy flow")
    void addNewMedicalReportHappyFlow() {
    }

    @Test
    @DisplayName("Adding new medical report - Exception: no appointment with given id")
    void addNewMedicalReportException() {
    }

    @Test
    @DisplayName("Updatemedical report - happy flow")
    void updateMedicalReportHappyFlow() {
    }

    @Test
    @DisplayName("Updatemedical report - Exception: no report with given in")
    void updateMedicalReportException() {
    }

    @Test
    @DisplayName("Get medical report by id - happy flow")
    void getMedicalReportHappyFlow() {
        //arrange
        int reportId = 1;
        MedicalReport report = buildMedicalReportMock(reportId);
        when(medicalReportRepository.findById(reportId)).thenReturn(Optional.of(report));

        //act
        MedicalReport result = medicalReportService.getMedicalReport(reportId);

        //assert
        assertNotNull(result);
        assertEquals(report.getMedicalReportId(), result.getMedicalReportId());
        assertEquals(report.getDiagnostic(), result.getDiagnostic());
        assertEquals(report.getAdditionalInvestigationRequired(), result.getAdditionalInvestigationRequired());
        assertEquals(report.getInterpretationResults(), result.getInterpretationResults());
    }

    @Test
    @DisplayName("Get medical report by id - Exception: no report with given id")
    void getMedicalReportException() {
        //arrange
        int reportId = 1;
        when(medicalReportRepository.findById(anyInt())).thenReturn(Optional.empty());

        //act
        NoRecordWithIdFoundException noReportWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> medicalReportService.getMedicalReport(reportId));

        //assert
        assertEquals(String.format(Constants.MEDICAL_REPORT_NOT_FOUND, reportId),
                noReportWithIdFoundException.getMessage());
    }

    private MedicalReport buildMedicalReportMock(int reportId) {
        MedicalReport report = new MedicalReport("test diagnostic");
        report.setMedicalReportId(reportId);
        report.setInterpretationResults("test interpretation");
        report.setAdditionalInvestigationRequired("test additional investigation");
        return report;
    }
}
