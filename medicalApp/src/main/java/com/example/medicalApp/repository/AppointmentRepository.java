package com.example.medicalApp.repository;

import com.example.medicalApp.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    @Query(value = "select * from appointment where patient_id = :patientId", nativeQuery = true)
    List<Appointment> findAllByPatientId(int patientId);
}
