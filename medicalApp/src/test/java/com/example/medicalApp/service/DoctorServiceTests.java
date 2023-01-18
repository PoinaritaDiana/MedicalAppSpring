package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.Doctor;
import com.example.medicalApp.model.MedicalUnit;
import com.example.medicalApp.model.Specialization;
import com.example.medicalApp.repository.DoctorRepository;
import com.example.medicalApp.repository.MedicalUnitRepository;
import com.example.medicalApp.repository.SpecializationRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTests {
    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private SpecializationRepository specializationRepository;

    @Mock
    private MedicalUnitRepository medicalUnitRepository;

    @Test
    @DisplayName("Adding new doctor - happy flow")
    void addNewDoctorHappyFlow() {
        //arrange
        Doctor doctor = buildDoctorMock();
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        int medicalUnitId = 1;
        MedicalUnit medicalUnit = new MedicalUnit("Ploiesti", "Medlife", "1111111111");
        when(medicalUnitRepository.findById(medicalUnitId)).thenReturn(Optional.of(medicalUnit));

        int specializationId = 1;
        Specialization specialization = new Specialization("Cardiologie", "test description");
        when(specializationRepository.findById(specializationId)).thenReturn(Optional.of(specialization));

        //act
        Doctor result = doctorService.addNewDoctor(medicalUnitId, specializationId, doctor);

        //assert
        assertNotNull(result);
        assertEquals(doctor.getLastName(), result.getLastName());
        assertEquals(doctor.getFirstName(), result.getFirstName());
        assertEquals(specialization.getName(), result.getSpecializationList().get(0).getName());
        assertEquals(medicalUnit.getName(), result.getMedicalUnit().getName());
    }

    @Test
    @DisplayName("Adding new doctor - Exception: no medical unit with given id")
    void addNewDoctorExceptionMedicalUnit() {
        //arrange
        Doctor doctor = buildDoctorMock();
        int specializationId = 1;
        int medicalUnitId = 1;
        when(medicalUnitRepository.findById(medicalUnitId)).thenReturn(Optional.empty());

        //act
        RuntimeException noMedicalUnitWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> doctorService.addNewDoctor(medicalUnitId, specializationId, doctor));

        //assert
        assertEquals(String.format(Constants.MEDICAL_UNIT_NOT_FOUND, medicalUnitId),
                noMedicalUnitWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Adding new doctor - Exception: no specialization with given id")
    void addNewDoctorExceptionSpecialization() {
        //arrange
        Doctor doctor = buildDoctorMock();

        int medicalUnitId = 1;
        MedicalUnit medicalUnit = new MedicalUnit("Ploiesti", "Medlife", "1111111111");
        when(medicalUnitRepository.findById(medicalUnitId)).thenReturn(Optional.of(medicalUnit));

        int specializationId = 1;
        when(specializationRepository.findById(specializationId)).thenReturn(Optional.empty());

        //act
        RuntimeException noSpecializationWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> doctorService.addNewDoctor(medicalUnitId, specializationId, doctor));

        //assert
        assertEquals(String.format(Constants.SPECIALIZATION_NOT_FOUND, specializationId),
                noSpecializationWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Get doctor by id - happy flow")
    void getDoctorByIdHappyFlow() {
        //arrange
        int doctorId = 1;
        Doctor doctor = buildDoctorMock();
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        //act
        Doctor result = doctorService.getDoctor(doctorId);

        //assert
        assertNotNull(result);
        assertEquals(doctor.getLastName(), result.getLastName());
        assertEquals(doctor.getFirstName(), result.getFirstName());
    }

    @Test
    @DisplayName("Get doctor by id - Exception: no doctor with given id")
    void getDoctorByIdException() {
        //arrange
        int doctorId = 1;
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        //act
        RuntimeException noDoctorWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> doctorService.getDoctor(doctorId));

        //assert
        assertEquals(String.format(Constants.DOCTOR_NOT_FOUND, doctorId),
                noDoctorWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Get doctors from specialization - happy flow")
    void getDoctorsFromSpecializationHappyFlow() {
        //arrange
        int specializationId = 1;
        Specialization specialization = new Specialization("Cardiologie", "test description");
        specialization.setSpecializationId(1);
        when(specializationRepository.findById(specializationId)).thenReturn(Optional.of(specialization));

        List<Doctor> doctorList = new ArrayList<>();
        Doctor doctor = buildDoctorMock();
        doctorList.add(doctor);
        when(doctorRepository.findAllBySpecializationId(specializationId)).thenReturn(doctorList);

        //act
        List<Doctor> result = doctorService.getDoctorsFromSpecialization(specializationId);

        //assert
        assertEquals(1, result.size());
        assertEquals(doctor.getLastName(), result.get(0).getLastName());
        assertEquals(doctor.getFirstName(), result.get(0).getFirstName());
    }

    @Test
    @DisplayName("Get doctors from specialization - Exception: no specialization with given id")
    void getDoctorsFromSpecializationException() {
        //arrange
        int specializationId = 1;
        when(specializationRepository.findById(specializationId)).thenReturn(Optional.empty());

        //act
        RuntimeException noSpecializationWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> doctorService.getDoctorsFromSpecialization(specializationId));

        //assert
        assertEquals(String.format(Constants.SPECIALIZATION_NOT_FOUND, specializationId),
                noSpecializationWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Get doctors from medical unit - happy flow")
    void getDoctorsFromMedicalUnitHappyFlow() {
        //arrange
        int medicalUnitId = 1;
        MedicalUnit medicalUnit = new MedicalUnit("Ploiesti", "Medlife", "1111111111");
        medicalUnit.setMedicalUnitId(medicalUnitId);
        when(medicalUnitRepository.findById(medicalUnitId)).thenReturn(Optional.of(medicalUnit));

        List<Doctor> doctorList = new ArrayList<>();
        Doctor doctor = buildDoctorMock();
        doctorList.add(doctor);
        when(doctorRepository.findAllByMedicalUnitId(medicalUnitId)).thenReturn(doctorList);

        //act
        List<Doctor> result = doctorService.getDoctorsFromMedicalUnit(medicalUnitId);

        //assert
        assertEquals(1, result.size());
        assertEquals(doctor.getLastName(), result.get(0).getLastName());
        assertEquals(doctor.getFirstName(), result.get(0).getFirstName());
    }

    @Test
    @DisplayName("Get doctors from medical unit - Exception: no medical unit with given id")
    void getDoctorsFromMedicalUnitException() {
        //arrange
        int medicalUnitId = 1;
        when(medicalUnitRepository.findById(medicalUnitId)).thenReturn(Optional.empty());

        //act
        RuntimeException noMedicalUnitWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> doctorService.getDoctorsFromMedicalUnit(medicalUnitId));

        //assert
        assertEquals(String.format(Constants.MEDICAL_UNIT_NOT_FOUND, medicalUnitId),
                noMedicalUnitWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Add new specialization info for doctor - happy flow")
    void addNewSpecializationInfoHappyFlow() {
        //arrange
        int doctorId = 1;
        Doctor doctor = buildDoctorMock();
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        int specializationId = 1;
        Specialization specialization = new Specialization("Cardiologie", "test description");
        when(specializationRepository.findById(specializationId)).thenReturn(Optional.of(specialization));

        //act
        Doctor result = doctorService.addNewSpecializationInfo(doctorId, specializationId);

        //assert
        assertEquals(doctor.getLastName(), result.getLastName());
        assertEquals(doctor.getFirstName(), result.getFirstName());
        assertEquals(1, result.getSpecializationList().size());
        assertEquals(specialization.getName(), result.getSpecializationList().get(0).getName());
        assertEquals(specialization.getDescription(), result.getSpecializationList().get(0).getDescription());
    }

    @Test
    @DisplayName("Add new specialization info for doctor - Exception: no doctor with given id")
    void addNewSpecializationInfoExceptionDoctor() {
        //arrange
        int doctorId = 1;
        int specializationId = 1;
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        //act
        RuntimeException noDoctorWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> doctorService.addNewSpecializationInfo(doctorId, specializationId));

        //assert
        assertEquals(String.format(Constants.DOCTOR_NOT_FOUND, doctorId),
                noDoctorWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Add new specialization info for doctor - Exception: no specialization with given id")
    void addNewSpecializationInfoExceptionSpecialization() {
        //arrange
        int doctorId = 1;
        Doctor doctor = buildDoctorMock();
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        int specializationId = 1;
        when(specializationRepository.findById(specializationId)).thenReturn(Optional.empty());

        //act
        RuntimeException noSpecializationWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> doctorService.addNewSpecializationInfo(doctorId, specializationId));

        //assert
        assertEquals(String.format(Constants.SPECIALIZATION_NOT_FOUND, specializationId),
                noSpecializationWithIdFoundException.getMessage());
    }

    private Doctor buildDoctorMock() {
        Doctor doctor = new Doctor("last name test", "first name test");
        return doctor;
    }
}
