package com.example.medicalApp.service;

import com.example.medicalApp.repository.MedicalUnitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MedicalUnitServiceTests {
    @InjectMocks
    private MedicalUnitService medicalUnitService;

    @Mock
    private MedicalUnitRepository medicalUnitRepository;

    @Test
    @DisplayName("Adding new medical unit - happy flow")
    void addNewMedicalUnitHappyFlow() {
    }
}
