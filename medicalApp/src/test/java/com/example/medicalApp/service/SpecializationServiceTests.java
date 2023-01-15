package com.example.medicalApp.service;

import com.example.medicalApp.model.Specialization;
import com.example.medicalApp.repository.SpecializationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpecializationServiceTests {
    @InjectMocks
    private SpecializationService specializationService;

    @Mock
    private SpecializationRepository specializationRepository;

    @Test
    @DisplayName("Adding new specialization - happy flow")
    void addNewSpecializationHappyFlow() {
        //arrange
        Specialization specialization = new Specialization("", "");
        when(specializationRepository.save(specialization)).thenReturn(specialization);

        //act
        Specialization result = specializationService.addNewSpecialization(specialization);

        //assert
        assertEquals(specialization.getName(), result.getName());
        assertEquals(specialization.getDescription(), result.getDescription());
    }
}
