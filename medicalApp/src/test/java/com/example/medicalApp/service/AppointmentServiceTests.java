package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.InvalidAppointmentTimeException;
import com.example.medicalApp.exceptions.InvalidDeletionOfOldAppointmentException;
import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.Appointment;
import com.example.medicalApp.model.Doctor;
import com.example.medicalApp.model.Investigation;
import com.example.medicalApp.model.Patient;
import com.example.medicalApp.repository.AppointmentRepository;
import com.example.medicalApp.repository.InvestigationRepository;
import com.example.medicalApp.repository.PatientRepository;
import com.example.medicalApp.utils.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTests {
    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private InvestigationRepository investigationRepository;

    @Test
    @DisplayName("Adding new appointment - happy flow")
    void addNewAppointmentHappyFlow() {
        //arrange
        Appointment appointment = new Appointment(LocalDateTime.of(2023, Month.JANUARY, 18, 19, 39));

        int patientId = 1;
        Patient patient = new Patient("last name", "first name", "11111111", "14-08-2000");
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        int investigationId = 1;
        Investigation investigation = new Investigation("consultatie", "consultatie de rutina", 100, 30);
        investigation.setDoctor(new Doctor());
        when(investigationRepository.findById(investigationId)).thenReturn(Optional.of(investigation));

        when(appointmentRepository.findAll()).thenReturn(new ArrayList<>());
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        //act
        Appointment result = appointmentService.addNewAppointment(patientId, investigationId, appointment);

        //assert
        assertNotNull(result);
        assertEquals(appointment.getAppointmentDate(), result.getAppointmentDate());
        assertEquals(appointment.getInvestigation().getName(), result.getInvestigation().getName());
        assertEquals(appointment.getPatient().getLastName(), result.getPatient().getLastName());
    }

    @Test
    @DisplayName("Adding new appointment - Exception: other appointments during period")
    void addNewAppointmentException() {
        //arrange
        Appointment appointment = new Appointment(LocalDateTime.of(2023, Month.JANUARY, 18, 19, 39));

        int patientId = 1;
        Patient patient = new Patient("last name", "first name", "11111111", "14-08-2000");
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        List<Investigation> investigationList = new ArrayList<>();
        int investigationId = 1;
        Investigation investigation = new Investigation("consultatie", "consultatie de rutina", 100, 30);
        investigationList.add(investigation);
        Doctor doctor = new Doctor();
        doctor.setInvestigationList(investigationList);
        investigation.setDoctor(doctor);
        when(investigationRepository.findById(investigationId)).thenReturn(Optional.of(investigation));

        List<Appointment> appointmentList = new ArrayList<>();
        Appointment overlapAppointment = new Appointment(LocalDateTime.of(2023, Month.JANUARY, 18, 19, 45));
        overlapAppointment.setInvestigation(investigation);
        appointmentList.add(overlapAppointment);
        when(appointmentRepository.findAll()).thenReturn(appointmentList);

        //act
        RuntimeException invalidAppointmentTimeException =
                assertThrows(InvalidAppointmentTimeException.class,
                        () -> appointmentService.addNewAppointment(patientId, investigationId, appointment));

        //assert
        assertEquals("The doctor doing the investigation has other appointments during " + appointment.getAppointmentDate()
                        + "and " + appointment.getAppointmentDate().plusMinutes(investigation.getTime()),
                invalidAppointmentTimeException.getMessage());
    }

    @Test
    @DisplayName("Adding new appointment - Exception: no patient with given id")
    void addNewAppointmentExceptionPatient() {
        //arrange
        Appointment appointment = new Appointment(LocalDateTime.of(2023, Month.JANUARY, 18, 19, 39));
        int investigationId = 1;
        int patientId = 1;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        //act
        RuntimeException noPatientWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> appointmentService.addNewAppointment(patientId, investigationId, appointment));

        //assert
        assertEquals(String.format(Constants.PATIENT_NOT_FOUND, patientId),
                noPatientWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Adding new appointment - Exception: no investigation with given id")
    void addNewAppointmentExceptionInvestigation() {
        //arrange
        Appointment appointment = new Appointment(LocalDateTime.of(2023, Month.JANUARY, 18, 19, 39));

        int investigationId = 1;
        when(investigationRepository.findById(investigationId)).thenReturn(Optional.empty());

        int patientId = 1;
        Patient patient = new Patient("last name", "first name", "11111111", "14-08-2000");
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        //act
        RuntimeException noInvestigationWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> appointmentService.addNewAppointment(patientId, investigationId, appointment));

        //assert
        assertEquals(String.format(Constants.INVESTIGATION_NOT_FOUND, investigationId),
                noInvestigationWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Get appointment - happy flow")
    void getAppointmentHappyFlow() {
        //arrange
        int appointmentId = 1;
        Appointment appointment = new Appointment(LocalDateTime.of(2023, Month.JANUARY, 18, 19, 39));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        //act
        Appointment result = appointmentService.getAppointment(appointmentId);

        //assert
        assertNotNull(result);
        assertEquals(appointment.getAppointmentDate(), result.getAppointmentDate());
    }

    @Test
    @DisplayName("Get appointment - Exception: no appointment with given id")
    void getAppointmentException() {
        //arrange
        int appointmentId = 1;
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        //act
        RuntimeException noAppointmentWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> appointmentService.getAppointment(appointmentId));

        //assert
        assertEquals(String.format(Constants.APPOINTMENT_NOT_FOUND, appointmentId),
                noAppointmentWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Get future appointments - happy flow")
    void getFutureAppointmentsHappyFlow() {
        //arrange
        int patientId = 1;

        Appointment oldAppointment = new Appointment(LocalDateTime.of(9, Month.JANUARY, 18, 19, 39));
        oldAppointment.setAppointmentId(1);

        Appointment futureAppointment = new Appointment(LocalDateTime.of(9999, Month.JANUARY, 18, 19, 39));
        futureAppointment.setAppointmentId(2);

        List<Appointment> appointmentList = new ArrayList<>();
        appointmentList.add(futureAppointment);
        appointmentList.add(oldAppointment);

        when(appointmentRepository.findAllByPatientId(patientId)).thenReturn(appointmentList);

        //act
        List<Appointment> result = appointmentService.getFutureAppointments(patientId);

        //assert
        assertEquals(1, result.size());
        assertNotEquals(oldAppointment.getAppointmentDate(), result.get(0).getAppointmentDate());
        assertNotEquals(oldAppointment.getAppointmentId(), result.get(0).getAppointmentId());
        assertEquals(futureAppointment.getAppointmentDate(), result.get(0).getAppointmentDate());
        assertEquals(futureAppointment.getAppointmentId(), result.get(0).getAppointmentId());
    }

    @Test
    @DisplayName("Get old appointments - happy flow")
    void getOldAppointmentsHappyFlow() {
        //arrange
        int patientId = 1;

        Appointment oldAppointment = new Appointment(LocalDateTime.of(9, Month.JANUARY, 18, 19, 39));
        oldAppointment.setAppointmentId(1);

        Appointment futureAppointment = new Appointment(LocalDateTime.of(9999, Month.JANUARY, 18, 19, 39));
        futureAppointment.setAppointmentId(2);

        List<Appointment> appointmentList = new ArrayList<>();
        appointmentList.add(futureAppointment);
        appointmentList.add(oldAppointment);

        when(appointmentRepository.findAllByPatientId(patientId)).thenReturn(appointmentList);

        //act
        List<Appointment> result = appointmentService.getOldAppointments(patientId);

        //assert
        assertEquals(1, result.size());
        assertEquals(oldAppointment.getAppointmentDate(), result.get(0).getAppointmentDate());
        assertEquals(oldAppointment.getAppointmentId(), result.get(0).getAppointmentId());
        assertNotEquals(futureAppointment.getAppointmentDate(), result.get(0).getAppointmentDate());
        assertNotEquals(futureAppointment.getAppointmentId(), result.get(0).getAppointmentId());
    }

    @Test
    @DisplayName("Delete appointment - happy flow")
    void deleteFutureAppointmentHappyFlow() {
        //arrange
        int appointmentId = 1;
        Appointment appointment = new Appointment(LocalDateTime.of(9999, Month.JANUARY, 18, 19, 39));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        //act
        String result = appointmentService.deleteFutureAppointment(appointmentId);

        //assert
        assertEquals("Appointment 1 was successfully deleted", result);
    }

    @Test
    @DisplayName("Delete appointment - exception")
    void deleteFutureAppointmentException() {
        //arrange
        int appointmentId = 1;
        Appointment appointment = new Appointment(LocalDateTime.of(9, Month.JANUARY, 18, 19, 39));
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        //act
        RuntimeException invalidDeletionOfOldAppointmentException =
                assertThrows(InvalidDeletionOfOldAppointmentException.class,
                        () -> appointmentService.deleteFutureAppointment(appointmentId));

        //assert
        assertEquals("You can't delete appointment with id = 1 because it's an old or ongoing appointment",
                invalidDeletionOfOldAppointmentException.getMessage());
    }
}
