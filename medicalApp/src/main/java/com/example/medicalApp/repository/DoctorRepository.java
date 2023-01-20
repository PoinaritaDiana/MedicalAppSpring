package com.example.medicalApp.repository;

import com.example.medicalApp.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    @Query(value = "select * from doctor JOIN doctor_specialization ON doctor.doctor_id=doctor_specialization.doctor_id where specialization_id = :specializationId", nativeQuery = true)
    List<Doctor> findAllBySpecializationId(int specializationId);

    @Query(value = "select * from doctor where medical_unit_id = :medicalUnitId", nativeQuery = true)
    List<Doctor> findAllByMedicalUnitId(int medicalUnitId);
}
