package com.example.medicalApp.controller;

import com.example.medicalApp.service.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AppointmentController.class)
public class AppointmentControllerTests {
    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;
}
