package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.Appointment;
import com.example.medicalApp.model.MedicalReport;
import com.example.medicalApp.repository.AppointmentRepository;
import com.example.medicalApp.repository.MedicalReportRepository;
import com.example.medicalApp.utils.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalReportServiceTests {

    private static final String DEFAULT_INTERPRETATION = "test interpretation";
    private static final String DEFAULT_ADDITIONAL_INVESTIGATION = "test additional investigation";

    @InjectMocks
    private MedicalReportService medicalReportService;

    @Mock
    private MedicalReportRepository medicalReportRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Test
    @DisplayName("Adding new medical report - happy flow")
    void addNewMedicalReportHappyFlow() {
        //arrange
        int reportId = 1;
        MedicalReport report = buildMedicalReportMock(reportId, DEFAULT_INTERPRETATION, DEFAULT_ADDITIONAL_INVESTIGATION);
        when(medicalReportRepository.save(report)).thenReturn(report);

        int appointmentId = 1;
        Appointment appointment = buildAppointmentMock(appointmentId);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        //act
        MedicalReport result = medicalReportService.addNewMedicalReportForAppointment(appointmentId, report);

        //assert
    }

    @Test
    @DisplayName("Adding new medical report - Exception: no appointment with given id")
    void addNewMedicalReportException() {
        //arrange
        MedicalReport report = buildMedicalReportMock(1, DEFAULT_INTERPRETATION, DEFAULT_ADDITIONAL_INVESTIGATION);
        int appointmentId = 1;
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        //act
        RuntimeException noAppointmentWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> medicalReportService.addNewMedicalReportForAppointment(appointmentId, report));

        //assert
        assertEquals(String.format(Constants.APPOINTMENT_NOT_FOUND, appointmentId),
                noAppointmentWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Update medical report - happy flow")
    void updateMedicalReportHappyFlow() {
        //arrange
        int reportId = 1;
        MedicalReport initialReport = buildMedicalReportMock(reportId, DEFAULT_INTERPRETATION, DEFAULT_ADDITIONAL_INVESTIGATION);
        when(medicalReportRepository.findById(reportId)).thenReturn(Optional.of(initialReport));

        MedicalReport reportInfoToUpdate = buildMedicalReportMock(reportId, "updated interpretation", "updated additional investigation");
        when(medicalReportRepository.save(initialReport)).thenReturn(initialReport);

        //act
        MedicalReport result = medicalReportService.updateMedicalReport(reportId, reportInfoToUpdate);

        //assert
        assertNotNull(result);
        assertEquals(reportInfoToUpdate.getMedicalReportId(), result.getMedicalReportId());
        assertEquals(initialReport.getMedicalReportId(), result.getMedicalReportId());
        assertEquals(reportInfoToUpdate.getDiagnostic(), result.getDiagnostic());
        assertEquals(reportInfoToUpdate.getInterpretationResults(), result.getInterpretationResults());
    }

    @Test
    @DisplayName("Update medical report - Exception: no report with given in")
    void updateMedicalReportException() {
        //arrange
        int reportId = 1;
        when(medicalReportRepository.findById(reportId)).thenReturn(Optional.empty());
        MedicalReport report = buildMedicalReportMock(reportId, DEFAULT_INTERPRETATION, DEFAULT_ADDITIONAL_INVESTIGATION);

        //act
        RuntimeException noReportWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> medicalReportService.updateMedicalReport(reportId, report));

        //assert
        assertEquals(String.format(Constants.MEDICAL_REPORT_NOT_FOUND, reportId),
                noReportWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Get medical report by id - happy flow")
    void getMedicalReportHappyFlow() {
        //arrange
        int reportId = 1;
        MedicalReport report = buildMedicalReportMock(reportId, DEFAULT_INTERPRETATION, DEFAULT_ADDITIONAL_INVESTIGATION);
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
        when(medicalReportRepository.findById(reportId)).thenReturn(Optional.empty());

        //act
        RuntimeException noReportWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> medicalReportService.getMedicalReport(reportId));

        //assert
        assertEquals(String.format(Constants.MEDICAL_REPORT_NOT_FOUND, reportId),
                noReportWithIdFoundException.getMessage());
    }

    private MedicalReport buildMedicalReportMock(int reportId,
                                                 String interpretationResults,
                                                 String additionalInvestigation) {
        MedicalReport report = new MedicalReport("test diagnostic");
        report.setMedicalReportId(reportId);
        report.setInterpretationResults(interpretationResults);
        report.setAdditionalInvestigationRequired(additionalInvestigation);
        return report;
    }

    private Appointment buildAppointmentMock(int appointmentId) {
        Appointment appointment = new Appointment(LocalDateTime.now());
        appointment.setAppointmentId(appointmentId);
        return appointment;
    }
}
