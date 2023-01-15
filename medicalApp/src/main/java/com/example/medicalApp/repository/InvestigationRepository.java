package com.example.medicalApp.repository;

import com.example.medicalApp.model.Investigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvestigationRepository extends JpaRepository<Investigation, Integer> {
    @Query(value = "select * from investigation where doctor_id = :doctorId", nativeQuery = true)
   List<Investigation> findAllByDoctorId(int doctorId);
}
