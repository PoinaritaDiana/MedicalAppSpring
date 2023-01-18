package com.example.medicalApp.controller;

import com.example.medicalApp.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PatientController.class)
public class PatientControllerTests {
    @MockBean
    private PatientService patientService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
}
