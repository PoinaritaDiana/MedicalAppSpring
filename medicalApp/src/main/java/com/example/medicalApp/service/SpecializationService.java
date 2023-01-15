package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.Specialization;
import com.example.medicalApp.repository.SpecializationRepository;
import com.example.medicalApp.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializationService {
    private final SpecializationRepository specializationRepository;

    @Autowired
    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public Specialization addNewSpecialization(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    public Specialization getSpecialization(int specializationId) {
        return specializationRepository.findById(specializationId)
                .orElseThrow(() -> new NoRecordWithIdFoundException(String.format(Constants.SPECIALIZATION_NOT_FOUND, specializationId)));
    }

    public List<Specialization> getAllSpecializations() {
        return specializationRepository.findAll();
    }

    public Specialization updateSpecializationDescription(int specializationId, String description) {
        Specialization specialization = getSpecialization(specializationId);
        specialization.setDescription(description);
        return specializationRepository.save(specialization);
    }
}
