package com.example.medicalApp.controller;


import com.example.medicalApp.model.Doctor;
import com.example.medicalApp.service.DoctorService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DoctorController.class)
public class DoctorControllerTests {
    @MockBean
    private DoctorService doctorService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Add new doctor endpoint - exception")
    public void addDoctorEndpointException() throws Exception {
        Doctor doctor = new Doctor("", "");
        when(doctorService.addNewDoctor(anyInt(), anyInt(), any())).thenReturn(doctor);
        String doctorResponseBody = mapper.writeValueAsString(doctor);
        Matcher<Iterable<? extends String>> matcher = Matchers.containsInAnyOrder("LastName cannot be empty",
                "FirstName cannot be empty");

        mockMvc.perform(post("/doctor/new")
                .param("medicalUnitId", "1")
                .param("specializationId", "1")
                .content(doctorResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Reason.length()").value(2))
                .andExpect(jsonPath("$.Reason", matcher))
                .andReturn();
    }

    @Test
    @DisplayName("Add new doctor endpoint - happy flow")
    public void addDoctorEndpointHappyFlow() throws Exception {
        Doctor doctor = new Doctor("Popescu", "Ioan");
        doctor.setDoctorId(1);
        when(doctorService.addNewDoctor(anyInt(), anyInt(), any())).thenReturn(doctor);
        String doctorResponseBody = mapper.writeValueAsString(doctor);

        MvcResult result = mockMvc.perform(post("/doctor/new")
                .param("medicalUnitId", "1")
                .param("specializationId", "1")
                .content(doctorResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/doctor/1"))
                .andExpect(jsonPath("$.doctorId").value("1"))
                .andExpect(jsonPath("$.lastName").value("Popescu"))
                .andExpect(jsonPath("$.firstName").value("Ioan"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), doctorResponseBody);
    }

    @Test
    @DisplayName("Get doctor by id endpoint - happy flow")
    public void getDoctorHappyFlow() throws Exception {
        Doctor doctor = new Doctor("Popescu", "Ioan");
        doctor.setDoctorId(1);
        when(doctorService.getDoctor(anyInt())).thenReturn(doctor);

        MvcResult result = mockMvc.perform(get("/doctor/{doctorId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value("1"))
                .andExpect(jsonPath("$.lastName").value("Popescu"))
                .andExpect(jsonPath("$.firstName").value("Ioan"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(doctor));
    }

    @Test
    @DisplayName("Get doctors from specialization endpoint - happy flow")
    public void getDoctorsFromSpecializationHappyFlow() throws Exception {
        List<Doctor> doctorList = new ArrayList<>();
        doctorList.add(new Doctor("Popescu", "Ioan"));
        when(doctorService.getDoctorsFromSpecialization(anyInt())).thenReturn(doctorList);

        MvcResult result = mockMvc.perform(get("/doctor/specialization", 1)
                .param("specializationId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].lastName").value("Popescu"))
                .andExpect(jsonPath("$.[0].firstName").value("Ioan"))
                .andReturn();
    }

    @Test
    @DisplayName("Get doctors from medical unit endpoint - happy flow")
    public void getDoctorsFromMedicalUnitHappyFlow() throws Exception {
        List<Doctor> doctorList = new ArrayList<>();
        doctorList.add(new Doctor("Popescu", "Ioan"));
        when(doctorService.getDoctorsFromMedicalUnit(anyInt())).thenReturn(doctorList);

        MvcResult result = mockMvc.perform(get("/doctor/unit", 1)
                .param("medicalUnitId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].lastName").value("Popescu"))
                .andExpect(jsonPath("$.[0].firstName").value("Ioan"))
                .andReturn();
    }

    @Test
    @DisplayName("Update specialization endpoint - happy flow")
    public void updateDoctorSpecializationsHappyFlow() throws Exception {
        Doctor doctor = new Doctor("Popescu", "Ioan");
        when(doctorService.addNewSpecializationInfo(anyInt(), anyInt())).thenReturn(doctor);

        MvcResult result = mockMvc.perform(put("/doctor/{doctorId}/add/specialization/{specializationId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Popescu"))
                .andExpect(jsonPath("$.firstName").value("Ioan"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(doctor));
    }
}
