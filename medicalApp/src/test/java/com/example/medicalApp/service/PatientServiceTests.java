package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.Patient;
import com.example.medicalApp.repository.PatientRepository;
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
public class PatientServiceTests {
    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Test
    @DisplayName("Adding new patient - happy flow")
    void addNewPatientHappyFlow() {
        //arrange
        Patient patient = buildPatientMock(1);
        when(patientRepository.save(patient)).thenReturn(patient);

        //act
        Patient result = patientService.addNewPatient(patient);

        //assert
        assertNotNull(result);
        assertEquals(patient.getPatientId(), result.getPatientId());
        assertEquals(patient.getLastName(), result.getLastName());
        assertEquals(patient.getFirstName(), result.getFirstName());
        assertEquals(patient.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(patient.getPhone(), result.getPhone());
    }

    @Test
    @DisplayName("Update medical history or patient - happy flow")
    void updateMedicalHistoryHappyFlow() {
        //arrange
        String newMedicalHistory = "New medical history for patient.";
        int patientId = 1;
        Patient patient = buildPatientMock(patientId);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);

        //act
        String result = patientService.updateMedicalHistoryWithNewInfo(patientId, newMedicalHistory);

        //assert
        assertNotNull(result);
        assertEquals(newMedicalHistory, result);
    }

    @Test
    @DisplayName("Update medical history or patient - Exception: no specialization with given id")
    void updateMedicalHistoryException() {
        //arrange
        String newMedicalHistory = "New medical history for patient.";
        int patientId = 1;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        //act
        RuntimeException noPatientWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> patientService.updateMedicalHistoryWithNewInfo(patientId, newMedicalHistory));

        //assert
        assertEquals(String.format(Constants.PATIENT_NOT_FOUND, patientId),
                noPatientWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Get patient by id - happy flow")
    void getPatientByIdHappyFlow() {
        //arrange
        int patientId = 1;
        Patient patient = buildPatientMock(patientId);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        //act
        Patient result = patientService.getPatient(patientId);

        //assert
        assertNotNull(result);
        assertEquals(patient.getPatientId(), result.getPatientId());
        assertEquals(patient.getLastName(), result.getLastName());
        assertEquals(patient.getFirstName(), result.getFirstName());
        assertEquals(patient.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(patient.getPhone(), result.getPhone());
    }

    @Test
    @DisplayName("Get patient by id - Exception: no specialization with given id")
    void getPatientByIdException() {
        //arrange
        int patientId = 1;
        when(patientRepository.findById(anyInt())).thenReturn(Optional.empty());

        //act
        RuntimeException noPatientWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> patientService.getPatient(patientId));

        //assert
        assertEquals(String.format(Constants.PATIENT_NOT_FOUND, patientId),
                noPatientWithIdFoundException.getMessage());
    }

    private Patient buildPatientMock(int patientId) {
        Patient patientMock = new Patient("last name test", "first name test",
                "1111111111", "14-08-2000");
        patientMock.setPatientId(patientId);
        return patientMock;
    }
}
