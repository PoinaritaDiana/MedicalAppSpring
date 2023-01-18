package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.Appointment;
import com.example.medicalApp.model.MedicalReport;
import com.example.medicalApp.repository.AppointmentRepository;
import com.example.medicalApp.repository.MedicalReportRepository;
import com.example.medicalApp.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicalReportService {
    private final MedicalReportRepository medicalReportRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public MedicalReportService(MedicalReportRepository medicalReportRepository,
                                AppointmentRepository appointmentRepository) {
        this.medicalReportRepository = medicalReportRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public MedicalReport getMedicalReport(int reportId) {
        return medicalReportRepository.findById(reportId)
                .orElseThrow(() -> new NoRecordWithIdFoundException(String.format(Constants.MEDICAL_REPORT_NOT_FOUND, reportId)));
    }

    public MedicalReport addNewMedicalReportForAppointment(int appointmentId, MedicalReport report) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NoRecordWithIdFoundException(String.format(Constants.APPOINTMENT_NOT_FOUND, appointmentId)));

        appointment.setMedicalReport(report);
        MedicalReport savedReport = medicalReportRepository.save(report);
        appointmentRepository.save(appointment);
        return savedReport;
    }

    public MedicalReport updateMedicalReport(int reportId, MedicalReport report) {
        MedicalReport medicalReport = getMedicalReport(reportId);
        medicalReport.setDiagnostic(report.getDiagnostic());
        medicalReport.setAdditionalInvestigationRequired(report.getAdditionalInvestigationRequired());
        medicalReport.setInterpretationResults(report.getInterpretationResults());
        return medicalReportRepository.save(medicalReport);
    }
}
