package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.InvalidPriceForInvestigationException;
import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.Doctor;
import com.example.medicalApp.model.Investigation;
import com.example.medicalApp.repository.DoctorRepository;
import com.example.medicalApp.repository.InvestigationRepository;
import com.example.medicalApp.utils.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvestigationServiceTests {
    @InjectMocks
    private InvestigationService investigationService;

    @Mock
    private InvestigationRepository investigationRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Test
    @DisplayName("Add new investigation - happy flow")
    void addNewInvestigationHappyFlow() {
        //arrange
        int doctorId = 1;
        Doctor doctor = buildDoctorMock(doctorId);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        int investigationId = 1;
        Investigation investigation = buildInvestigationMock(investigationId, 100, doctor);
        when(investigationRepository.save(investigation)).thenReturn(investigation);

        //act
        Investigation result = investigationService.addNewInvestigation(doctorId, investigation);

        //assert
        assertNotNull(result);
        assertEqualInvestigations(investigation, result);
        assertEquals(doctor.getDoctorId(), result.getDoctor().getDoctorId());
        assertEquals(doctor.getFirstName(), result.getDoctor().getFirstName());
        assertEquals(doctor.getLastName(), result.getDoctor().getLastName());
    }

    @Test
    @DisplayName("Add new investigation - Exception: no doctor with given id")
    void addNewInvestigationException() {
        //arrange
        int doctorId = 1;
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        Investigation investigation = buildInvestigationMock(1, 100, null);

        //act
        RuntimeException noDoctorWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> investigationService.addNewInvestigation(doctorId, investigation));

        //assert
        assertEquals(String.format(Constants.DOCTOR_NOT_FOUND, doctorId),
                noDoctorWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Update price for investigation - happy flow")
    void updatePriceForInvestigationHappyFlow() {
        //arrange
        Investigation investigation = buildInvestigationMock(1, 100, null);
        when(investigationRepository.findById(1)).thenReturn(Optional.of(investigation));

        Investigation investigationUpdated = buildInvestigationMock(1, 105, null);
        when(investigationRepository.save(investigation)).thenReturn(investigationUpdated);

        //act
        Investigation result = investigationService.updatePriceOfInvestigation(1, 105);

        //assert
        assertNotNull(result);
        assertEqualInvestigations(investigationUpdated, result);
    }

    @Test
    @DisplayName("Update price too high for investigation - exception")
    void updatePriceTooHighForInvestigationException() {
        //arrange
        Investigation investigation = buildInvestigationMock(1, 100, null);
        when(investigationRepository.findById(1)).thenReturn(Optional.of(investigation));

        //act
        RuntimeException invalidPriceForInvestigationException =
                assertThrows(InvalidPriceForInvestigationException.class,
                        () -> investigationService.updatePriceOfInvestigation(1, 1000));

        //assert
        assertEquals("The new price for the investigation cannot be less than 90.0 or bigger than 110.0",
                invalidPriceForInvestigationException.getMessage());
    }

    @Test
    @DisplayName("Update price too low for investigation - exception")
    void updatePriceTooLowForInvestigationException() {
        //arrange
        Investigation investigation = buildInvestigationMock(1, 100, null);
        when(investigationRepository.findById(1)).thenReturn(Optional.of(investigation));

        //act
        RuntimeException invalidPriceForInvestigationException =
                assertThrows(InvalidPriceForInvestigationException.class,
                        () -> investigationService.updatePriceOfInvestigation(1, 10));

        //assert
        assertEquals("The new price for the investigation cannot be less than 90.0 or bigger than 110.0",
                invalidPriceForInvestigationException.getMessage());
    }

    @Test
    @DisplayName("Get investigation - exception")
    void getInvestigationException() {
        //arrange
        int investigationId = 1;
        when(investigationRepository.findById(investigationId)).thenReturn(Optional.empty());

        //act
        RuntimeException noInvestigationWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> investigationService.getInvestigation(investigationId));

        //assert
        assertEquals(String.format(Constants.INVESTIGATION_NOT_FOUND, investigationId),
                noInvestigationWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Get investigation - happy flow")
    void getInvestigationHappyFlow() {
        //arrange
        Doctor doctor = buildDoctorMock(1);

        int investigationId = 1;
        Investigation investigation = buildInvestigationMock(investigationId, 100, doctor);
        when(investigationRepository.findById(investigationId)).thenReturn(Optional.of(investigation));

        //act
        Investigation result = investigationService.getInvestigation(investigationId);

        //assert
        assertNotNull(result);
        assertEqualInvestigations(investigation, result);
        assertEquals(doctor.getDoctorId(), result.getDoctor().getDoctorId());
        assertEquals(doctor.getFirstName(), result.getDoctor().getFirstName());
        assertEquals(doctor.getLastName(), result.getDoctor().getLastName());
    }

    @Test
    @DisplayName("Get investigations having given max price - happy flow")
    void getInvestigationsWithMaxPriceHappyFlow() {
        //arrange
        Investigation firstInvestigation = buildInvestigationMock(1, 100, null);
        Investigation secondInvestigation = buildInvestigationMock(2, 1500, null);
        List<Investigation> investigationList = new ArrayList<>();
        investigationList.add(firstInvestigation);
        investigationList.add(secondInvestigation);
        when(investigationRepository.findAll()).thenReturn(investigationList);

        //act
        List<Investigation> result = investigationService.getInvestigationsWithMaxPrice(1000);

        //assert
        assertEquals(1, result.size());
        assertEqualInvestigations(firstInvestigation, result.get(0));
        assertNotEquals(secondInvestigation.getInvestigationId(), result.get(0).getInvestigationId());
    }

    @Test
    @DisplayName("Get investigations for doctor - happy flow")
    void getInvestigationsForDoctorHappyFlow() {
        //arrange
        int doctorId = 1;
        Doctor doctor = buildDoctorMock(doctorId);
        when(doctorRepository.existsById(doctorId)).thenReturn(true);

        Investigation firstInvestigation = buildInvestigationMock(1, 100, doctor);
        Investigation secondInvestigation = buildInvestigationMock(2, 1500, doctor);
        List<Investigation> investigationList = new ArrayList<>();
        investigationList.add(firstInvestigation);
        investigationList.add(secondInvestigation);
        when(investigationRepository.findAllByDoctorId(doctorId)).thenReturn(investigationList);

        //act
        List<Investigation> result = investigationService.getInvestigationsForDoctor(doctorId);

        //assert
        assertEquals(2, result.size());
        assertEqualInvestigations(firstInvestigation, result.get(0));
        assertEqualInvestigations(secondInvestigation, result.get(1));
        assertEquals(doctor.getDoctorId(), result.get(0).getDoctor().getDoctorId());
        assertEquals(doctor.getFirstName(), result.get(0).getDoctor().getFirstName());
        assertEquals(doctor.getLastName(), result.get(0).getDoctor().getLastName());
    }

    @Test
    @DisplayName("Get investigation for doctor - exception")
    void getInvestigationsForDoctorException() {
        //arrange
        int doctorId = 1;
        when(doctorRepository.existsById(doctorId)).thenReturn(false);

        //act
        RuntimeException noDoctorWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> investigationService.getInvestigationsForDoctor(doctorId));

        //assert
        assertEquals(String.format(Constants.DOCTOR_NOT_FOUND, doctorId),
                noDoctorWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Delete investigation - Exception: no investigation with given id")
    void deleteInvestigationIdException() {
        //arrange
        int investigationId = 1;
        when(investigationRepository.findById(investigationId)).thenReturn(Optional.empty());

        //act
        RuntimeException noInvestigationWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> investigationService.deleteInvestigation(investigationId));

        //assert
        assertEquals(String.format(Constants.INVESTIGATION_NOT_FOUND, investigationId),
                noInvestigationWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Delete investigation - happy flow")
    void deleteInvestigationHappyFlow() {
        //arrange
        int investigationId = 1;
        Investigation investigation = buildInvestigationMock(investigationId, 100, null);
        when(investigationRepository.findById(investigationId)).thenReturn(Optional.of(investigation));
        doNothing().when(investigationRepository).deleteById(investigationId);

        //act
        String result = investigationService.deleteInvestigation(investigationId);

        //assert
        assertEquals("Investigation 1 was successfully deleted", result);
    }


    private Investigation buildInvestigationMock(int investigationId, double price, Doctor doctor) {
        Investigation investigation = new Investigation("consultatie", "consultatie de rutina", price, 30);
        investigation.setInvestigationId(investigationId);
        investigation.setDoctor(doctor);
        return investigation;
    }

    private Doctor buildDoctorMock(int doctorId) {
        Doctor doctor = new Doctor("Popescu", "Ioan");
        doctor.setDoctorId(doctorId);
        return doctor;
    }

    private void assertEqualInvestigations(Investigation expected, Investigation actual) {
        assertEquals(expected.getInvestigationId(), actual.getInvestigationId());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(expected.getPrice(), actual.getPrice());
    }
}
