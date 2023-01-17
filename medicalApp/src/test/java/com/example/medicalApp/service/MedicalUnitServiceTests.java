package com.example.medicalApp.service;

import com.example.medicalApp.model.MedicalUnit;
import com.example.medicalApp.repository.MedicalUnitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalUnitServiceTests {
    @InjectMocks
    private MedicalUnitService medicalUnitService;

    @Mock
    private MedicalUnitRepository medicalUnitRepository;

    @Test
    @DisplayName("Adding new medical unit - happy flow")
    void addNewMedicalUnitHappyFlow() {
        //arrange
        MedicalUnit medicalUnit = buildMedicalUnitMock();
        when(medicalUnitRepository.save(medicalUnit)).thenReturn(medicalUnit);

        //act
        MedicalUnit result = medicalUnitService.addNewMedicalUnit(medicalUnit);

        //assert
        assertNotNull(result);
        assertEquals(medicalUnit.getName(), result.getName());
        assertEquals(medicalUnit.getCity(), result.getCity());
        assertEquals(medicalUnit.getPhone(), result.getPhone());
    }


    @Test
    @DisplayName("Get all medical units from city - happy flow")
    void getAllMedicalUnitsFromCityHappyFlow() {
        //arrange
        String city = "Ploiesti";
        List<MedicalUnit> medicalUnitList = new ArrayList<>();
        medicalUnitList.add(buildMedicalUnitMock());
        when(medicalUnitRepository.findAllByCity(anyString())).thenReturn(medicalUnitList);

        //act
        List<MedicalUnit> result = medicalUnitService.getAllMedicalUnitsFromCity(city);

        //assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(medicalUnitList.get(0).getName(), result.get(0).getName());
        assertEquals(medicalUnitList.get(0).getCity(), result.get(0).getCity());
        assertEquals(medicalUnitList.get(0).getPhone(), result.get(0).getPhone());
    }

    private MedicalUnit buildMedicalUnitMock() {
        return new MedicalUnit("Ploiesti", "Medlife", "1111111111");
    }
}
