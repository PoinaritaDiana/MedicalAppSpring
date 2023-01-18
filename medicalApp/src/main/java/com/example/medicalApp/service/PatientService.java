package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.Patient;
import com.example.medicalApp.repository.PatientRepository;
import com.example.medicalApp.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient addNewPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public String updateMedicalHistoryWithNewInfo(int patientId, String newMedicalHistoryInfo) {
        Patient patient = getPatient(patientId);
        String currentMedicalHistory = patient.getMedicalHistory() == null ? "" : patient.getMedicalHistory();
        patient.setMedicalHistory(currentMedicalHistory + newMedicalHistoryInfo);
        return patientRepository.save(patient).getMedicalHistory();
    }

    public Patient getPatient(int patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new NoRecordWithIdFoundException(String.format(Constants.PATIENT_NOT_FOUND, patientId)));
    }
}
