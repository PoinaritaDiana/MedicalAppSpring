package com.example.medicalApp.controller;

import com.example.medicalApp.model.Specialization;
import com.example.medicalApp.service.SpecializationService;
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


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SpecializationController.class)
public class SpecializationControllerTests {

    @MockBean
    private SpecializationService specializationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Add new specialization endpoint - happy flow")
    public void addSpecializationEndpointHappyFlow() throws Exception {
        Specialization specialization = buildSpecializationMock(1);
        when(specializationService.addNewSpecialization(any())).thenReturn(specialization);
        String specializationResponseBody = mapper.writeValueAsString(specialization);

        MvcResult result = mockMvc.perform(post("/specialization/new")
                .content(specializationResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/specialization/1"))
                .andExpect(jsonPath("$.specializationId").value("1"))
                .andExpect(jsonPath("$.name").value("testname"))
                .andExpect(jsonPath("$.description").value("testdescription"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), specializationResponseBody);
    }

    @Test
    @DisplayName("Add new specialization endpoint - exception")
    public void addSpecializationEndpointException() throws Exception {
        Specialization specialization = new Specialization("", "");
        when(specializationService.addNewSpecialization(any())).thenReturn(specialization);
        String specializationResponseBody = mapper.writeValueAsString(specialization);
        Matcher<Iterable<? extends String>> matcher = Matchers.containsInAnyOrder("Name of specialization cannot be empty",
                "Description of specialization cannot be empty");

        mockMvc.perform(post("/specialization/new")
                .content(specializationResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Reason.length()").value(2))
                .andExpect(jsonPath("$.Reason", matcher))
                .andReturn();
    }

    @Test
    @DisplayName("Get specialization by id endpoint - happy flow")
    public void getSpecializationByIdEndpointHappyFlow() throws Exception {
        Specialization specialization = buildSpecializationMock(1);
        when(specializationService.getSpecialization(anyInt())).thenReturn(specialization);

        MvcResult result = mockMvc.perform(get("/specialization/{specializationId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specializationId").value("1"))
                .andExpect(jsonPath("$.name").value("testname"))
                .andExpect(jsonPath("$.description").value("testdescription"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(specialization));
    }

    @Test
    @DisplayName("Get all specializations endpoint - happy flow")
    public void getAllSpecializationsHappyFlow() throws Exception {
        Specialization specialization = buildSpecializationMock(1);
        when(specializationService.getAllSpecializations()).thenReturn(Arrays.asList(specialization));

        MvcResult result = mockMvc.perform(get("/specialization/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].specializationId").value("1"))
                .andExpect(jsonPath("$.[0].name").value("testname"))
                .andExpect(jsonPath("$.[0].description").value("testdescription"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(Arrays.asList(specialization)));
    }

    @Test
    @DisplayName("Update description endpoint - happy flow")
    public void updateDescriptionEndpointHappyFlow() throws Exception {
        Specialization specialization = buildSpecializationMock(1);
        when(specializationService.updateSpecializationDescription(anyInt(), anyString())).thenReturn(specialization);

        MvcResult result = mockMvc.perform(put("/specialization/{specializationId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("testdescription"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specializationId").value("1"))
                .andExpect(jsonPath("$.name").value("testname"))
                .andExpect(jsonPath("$.description").value("testdescription"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(specialization));
    }

    private Specialization buildSpecializationMock(int id) {
        Specialization specialization = new Specialization("testname", "testdescription");
        specialization.setSpecializationId(id);
        return specialization;
    }
}
