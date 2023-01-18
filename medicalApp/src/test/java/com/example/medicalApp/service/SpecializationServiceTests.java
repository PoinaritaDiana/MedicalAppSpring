package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.Specialization;
import com.example.medicalApp.repository.SpecializationRepository;
import com.example.medicalApp.utils.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpecializationServiceTests {
    @InjectMocks
    private SpecializationService specializationService;

    @Mock
    private SpecializationRepository specializationRepository;

    @Test
    @DisplayName("Add new specialization - happy flow")
    void addNewSpecializationHappyFlow() {
        //arrange
        Specialization specialization = buildSpecializationMock(1);
        when(specializationRepository.save(specialization)).thenReturn(specialization);

        //act
        Specialization result = specializationService.addNewSpecialization(specialization);

        //assert
        assertNotNull(result);
        assertEquals(specialization.getSpecializationId(), result.getSpecializationId());
        assertEquals(specialization.getName(), result.getName());
        assertEquals(specialization.getDescription(), result.getDescription());
    }

    @Test
    @DisplayName("Get specialization by id - happy flow")
    void getSpecializationByIdHappyFlow() {
        //arrange
        int specializationId = 1;
        Specialization specialization = buildSpecializationMock(specializationId);
        when(specializationRepository.findById(specializationId)).thenReturn(Optional.of(specialization));

        //act
        Specialization result = specializationService.getSpecialization(specializationId);

        //assert
        assertNotNull(result);
        assertEquals(specialization.getSpecializationId(), result.getSpecializationId());
        assertEquals(specialization.getName(), result.getName());
        assertEquals(specialization.getDescription(), result.getDescription());
    }

    @Test
    @DisplayName("Get specialization by id - Exception: no specialization with given id")
    void getSpecializationException() {
        //arrange
        int specializationId = 1;
        when(specializationRepository.findById(anyInt())).thenReturn(Optional.empty());

        //act
        RuntimeException noSpecializationWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> specializationService.getSpecialization(specializationId));

        //assert
        assertEquals(String.format(Constants.SPECIALIZATION_NOT_FOUND, specializationId),
                noSpecializationWithIdFoundException.getMessage());
    }

    @Test
    @DisplayName("Get all specialization - happy flow")
    void getAllSpecializationsHappyFlow() {
        //arrange
        List<Specialization> specializationList = new ArrayList<>();
        specializationList.add(buildSpecializationMock(1));
        specializationList.add(buildSpecializationMock(2));
        when(specializationRepository.findAll()).thenReturn(specializationList);

        //act
        List<Specialization> result = specializationService.getAllSpecializations();

        //assert
        assertEquals(2, result.size());

        assertEquals(specializationList.get(0).getSpecializationId(), result.get(0).getSpecializationId());
        assertEquals(specializationList.get(0).getName(), result.get(0).getName());
        assertEquals(specializationList.get(0).getDescription(), result.get(0).getDescription());

        assertEquals(specializationList.get(1).getSpecializationId(), result.get(1).getSpecializationId());
        assertEquals(specializationList.get(1).getName(), result.get(1).getName());
        assertEquals(specializationList.get(1).getDescription(), result.get(1).getDescription());
    }

    @Test
    @DisplayName("Update description for specialization - happy flow")
    void updateSpecializationDescriptionHappyFlow() {
        //arrange
        String newDescription = "new description for specialization";
        int specializationId = 1;
        Specialization specialization = buildSpecializationMock(specializationId);
        when(specializationRepository.findById(specializationId)).thenReturn(Optional.of(specialization));
        when(specializationRepository.save(specialization)).thenReturn(specialization);

        //act
        Specialization result = specializationService.updateSpecializationDescription(specializationId, newDescription);

        //assert
        assertNotNull(result);
        assertEquals(specialization.getSpecializationId(), result.getSpecializationId());
        assertEquals(specialization.getName(), result.getName());
        assertEquals(newDescription, result.getDescription());
    }

    @Test
    @DisplayName("Update description for specialization - Exception: no specialization with given id")
    void updateSpecializationDescriptionException() {
        //arrange
        String newDescription = "new description for specialization";
        int specializationId = 1;
        when(specializationRepository.findById(specializationId)).thenReturn(Optional.empty());

        //act
        RuntimeException noSpecializationWithIdFoundException =
                assertThrows(NoRecordWithIdFoundException.class,
                        () -> specializationService.updateSpecializationDescription(specializationId, newDescription));

        //assert
        assertEquals(String.format(Constants.SPECIALIZATION_NOT_FOUND, specializationId),
                noSpecializationWithIdFoundException.getMessage());
    }

    private Specialization buildSpecializationMock(int specializationId) {
        Specialization specialization = new Specialization("test name", "test description");
        specialization.setSpecializationId(specializationId);
        return specialization;
    }
}
