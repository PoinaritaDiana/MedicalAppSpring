package com.example.medicalApp.repository;

import com.example.medicalApp.model.MedicalUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicalUnitRepository extends JpaRepository<MedicalUnit, Integer> {
    @Query(value = "select * from medical_unit where lower(city) = :cityName", nativeQuery = true)
    List<MedicalUnit> findAllByCity(String cityName);
}
