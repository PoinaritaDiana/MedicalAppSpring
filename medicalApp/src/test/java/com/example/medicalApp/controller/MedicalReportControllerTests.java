package com.example.medicalApp.controller;

import com.example.medicalApp.service.MedicalReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MedicalUnitController.class)
public class MedicalReportControllerTests {
    @MockBean
    private MedicalReportService medicalReportService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
}
