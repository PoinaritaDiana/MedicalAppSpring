package com.example.medicalApp.service;

import com.example.medicalApp.model.MedicalUnit;
import com.example.medicalApp.repository.MedicalUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class MedicalUnitService {
    private final MedicalUnitRepository medicalUnitRepository;

    @Autowired
    public MedicalUnitService(MedicalUnitRepository medicalUnitRepository) {
        this.medicalUnitRepository = medicalUnitRepository;
    }

    public MedicalUnit addNewMedicalUnit(MedicalUnit medicalUnit) {
        return medicalUnitRepository.save(medicalUnit);
    }

    public List<MedicalUnit> getAllMedicalUnitsFromCity(String cityName) {
        return medicalUnitRepository.findAllByCity(cityName.toLowerCase());
    }
}
