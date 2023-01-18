package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.InvalidAppointmentTimeException;
import com.example.medicalApp.exceptions.InvalidDeletionOfOldAppointmentException;
import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.Appointment;
import com.example.medicalApp.model.Investigation;
import com.example.medicalApp.model.Patient;
import com.example.medicalApp.repository.AppointmentRepository;
import com.example.medicalApp.repository.InvestigationRepository;
import com.example.medicalApp.repository.PatientRepository;
import com.example.medicalApp.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final InvestigationRepository investigationRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              InvestigationRepository investigationRepository,
                              PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.investigationRepository = investigationRepository;
        this.patientRepository = patientRepository;
    }

    public Appointment getAppointment(int appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NoRecordWithIdFoundException(String.format(Constants.APPOINTMENT_NOT_FOUND, appointmentId)));
    }

    public String deleteFutureAppointment(int appointmentId) {
        Appointment appointment = getAppointment(appointmentId);

        // check if it's an appointment that has not yet taken place -> you can delete it
        // if it's a past appointment or ongoing, you can't delete it
        if (appointment.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new InvalidDeletionOfOldAppointmentException("You can't delete appointment with id = " + appointmentId
                    + " because it's an old or ongoing appointment");
        }
        appointmentRepository.deleteById(appointmentId);
        return String.format("Appointment %s was successfully deleted", appointmentId);
    }

    public Appointment addNewAppointment(int patientId, int investigationId, Appointment appointment) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NoRecordWithIdFoundException(String.format(Constants.PATIENT_NOT_FOUND, patientId)));

        Investigation investigation = investigationRepository.findById(investigationId)
                .orElseThrow(() -> new NoRecordWithIdFoundException(String.format(Constants.INVESTIGATION_NOT_FOUND, investigationId)));

        appointment.setInvestigation(investigation);
        appointment.setPatient(patient);

        // Add an appointment for the investigation if the doctor doing the investigation has no other appointment during that period
        LocalDateTime appointmentStartTime = appointment.getAppointmentDate();
        LocalDateTime appointmentEndTime = appointmentStartTime.plusMinutes(investigation.getTime());

        List<Integer> doctorsInvestigationIds = investigation.getDoctor().getInvestigationList()
                .stream()
                .map(i -> i.getInvestigationId())
                .collect(Collectors.toList());
        int overlappingAppointmentsNumber = appointmentRepository.findAll()
                .stream()
                .filter(a -> {
                    if (!doctorsInvestigationIds.contains(a.getInvestigation().getInvestigationId())) {
                        return false;
                    }
                    long duration = a.getInvestigation().getTime();
                    LocalDateTime startTime = a.getAppointmentDate();
                    LocalDateTime endTime = startTime.plusMinutes(duration);
                    return (startTime.isAfter(appointmentEndTime) || endTime.isBefore(appointmentStartTime));
                })
                .collect(Collectors.toList())
                .size();

        if (overlappingAppointmentsNumber > 0) {
            throw new InvalidAppointmentTimeException("The doctor doing the investigation has other appointments during "
                    + appointmentStartTime + "and " + appointmentEndTime);
        }
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getFutureAppointments(int patientId) {
        return appointmentRepository.findAllByPatientId(patientId)
                .stream()
                .filter(a -> a.getAppointmentDate().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public List<Appointment> getOldAppointments(int patientId) {
        return appointmentRepository.findAllByPatientId(patientId)
                .stream()
                .filter(a -> a.getAppointmentDate().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }
}
