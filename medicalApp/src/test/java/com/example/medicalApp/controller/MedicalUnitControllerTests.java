package com.example.medicalApp.controller;

import com.example.medicalApp.model.MedicalUnit;
import com.example.medicalApp.service.MedicalUnitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MedicalUnitController.class)
public class MedicalUnitControllerTests {
    @MockBean
    private MedicalUnitService medicalUnitService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Get medical units from city - happy flow")
    public void getMedicalUnitsFromCityEndpointHappyFlow() throws Exception {
        String cityName = "Ploiesti";
        List<MedicalUnit> medicalUnitList = new ArrayList<>();
        medicalUnitList.add(new MedicalUnit("Ploiesti", "Medlife", "1111111111"));
        when(medicalUnitService.getAllMedicalUnitsFromCity(cityName)).thenReturn(medicalUnitList);

        mockMvc.perform(get("/unit")
                .param("cityName", cityName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].city").value("Ploiesti"))
                .andExpect(jsonPath("$.[0].name").value("Medlife"))
                .andExpect(jsonPath("$.[0].phone").value("1111111111"))
                .andReturn();
    }

    @Test
    @DisplayName("Get medical units from city - exception")
    public void getMedicalUnitsFromCityEndpointException() throws Exception {
        String cityName = "";
        List<MedicalUnit> medicalUnitList = new ArrayList<>();
        medicalUnitList.add(new MedicalUnit("Ploiesti", "Medlife", "1111111111"));
        when(medicalUnitService.getAllMedicalUnitsFromCity(cityName)).thenReturn(medicalUnitList);

        mockMvc.perform(get("/unit")
                .param("cityName", cityName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Reason").value("must not be empty"))
                .andReturn();
    }

    @Test
    @DisplayName("Add medical unit endpoint - happy flow")
    public void addMedicalUnitEndpointHappyFlow() throws Exception {
        MedicalUnit medicalUnit = new MedicalUnit("Ploiesti", "Medlife", "1111111111");
        when(medicalUnitService.addNewMedicalUnit(any())).thenReturn(medicalUnit);
        String medicalUnitResponseBody = mapper.writeValueAsString(medicalUnit);

        MvcResult result = mockMvc.perform(post("/unit/new")
                .content(medicalUnitResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city").value("Ploiesti"))
                .andExpect(jsonPath("$.name").value("Medlife"))
                .andExpect(jsonPath("$.phone").value("1111111111"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), medicalUnitResponseBody);
    }

    @Test
    @DisplayName("Add medical unit endpoint - exception")
    public void addMedicalUnitEndpointException() throws Exception {
        MedicalUnit medicalUnit = new MedicalUnit("", "", "11111111");
        when(medicalUnitService.addNewMedicalUnit(any())).thenReturn(medicalUnit);
        String medicalUnitResponseBody = mapper.writeValueAsString(medicalUnit);
        Matcher<Iterable<? extends String>> matcher = Matchers.containsInAnyOrder("The name of the medical unit cannot be empty",
                "The city where the office is located cannot be empty",
                "must match \"^\\d{10}$\"");

        mockMvc.perform(post("/unit/new")
                .content(medicalUnitResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Reason.length()").value(3))
                .andExpect(jsonPath("$.Reason", matcher))
                .andReturn();
    }
}
