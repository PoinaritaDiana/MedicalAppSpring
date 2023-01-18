package com.example.medicalApp.controller;

import com.example.medicalApp.model.Appointment;
import com.example.medicalApp.service.AppointmentService;
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

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AppointmentController.class)
public class AppointmentControllerTests {
    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Get appointment by id endpoint - happy flow")
    public void getAppointmentByIdEndpointHappyFlow() throws Exception {
        Appointment appointment = new Appointment(LocalDateTime.of(2023, Month.JANUARY, 18, 19, 39));
        when(appointmentService.getAppointment(anyInt())).thenReturn(appointment);

        MvcResult result = mockMvc.perform(get("/appointment/{appointmentId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentDate").value("2023-01-18T19:39:00"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(appointment));
    }

    @Test
    @DisplayName("Get old appointments endpoint - happy flow")
    public void getOldAppointmentsHappyFlow() throws Exception {
        List<Appointment> appointmentList = new ArrayList<>();
        Appointment appointment = new Appointment(LocalDateTime.of(2023, Month.JANUARY, 18, 19, 39));
        appointmentList.add(appointment);
        when(appointmentService.getOldAppointments(anyInt())).thenReturn(appointmentList);

        mockMvc.perform(get("/appointment/old")
                .param("patientId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].appointmentDate").value("2023-01-18T19:39:00"))
                .andReturn();
    }

    @Test
    @DisplayName("Get future appointments endpoint - happy flow")
    public void getFutureAppointmentsHappyFlow() throws Exception {
        List<Appointment> appointmentList = new ArrayList<>();
        Appointment appointment = new Appointment(LocalDateTime.of(9999, Month.JANUARY, 18, 19, 39));
        appointmentList.add(appointment);
        when(appointmentService.getFutureAppointments(anyInt())).thenReturn(appointmentList);

        mockMvc.perform(get("/appointment/future")
                .param("patientId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].appointmentDate").value("9999-01-18T19:39:00"))
                .andReturn();
    }


    @Test
    @DisplayName("Delete appointment endpoint - happy flow")
    public void deleteAppointmentsHappyFlow() throws Exception {
        String message = "Appointment was successfully deleted";
        when(appointmentService.deleteFutureAppointment(anyInt())).thenReturn(message);

        mockMvc.perform(delete("/appointment/{appointmentId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(message))
                .andReturn();
    }

    @Test
    @DisplayName("Add appointment endpoint - happy flow")
    public void addAppointmentHappyFlow() throws Exception {
        Appointment appointment = new Appointment(LocalDateTime.of(2023, Month.JANUARY, 18, 19, 39));
        appointment.setAppointmentId(1);
        when(appointmentService.addNewAppointment(anyInt(), anyInt(), any())).thenReturn(appointment);
        String appointmentResponseBody = mapper.writeValueAsString(appointment);

        MvcResult result = mockMvc.perform(post("/appointment/new")
                .param("patientId", "1")
                .param("investigationId", "1")
                .content(appointmentResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/appointment/1"))
                .andExpect(jsonPath("$.appointmentDate").value("2023-01-18T19:39:00"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(appointment));
    }

    @Test
    @DisplayName("Add appointment endpoint - exception")
    public void addAppointmentException() throws Exception {
        Appointment appointment = new Appointment();
        when(appointmentService.addNewAppointment(anyInt(), anyInt(), any())).thenReturn(appointment);
        String appointmentResponseBody = mapper.writeValueAsString(appointment);
        Matcher<Iterable<? extends String>> matcher = Matchers.containsInAnyOrder("Appointment date cannot be null");

        mockMvc.perform(post("/appointment/new")
                .param("patientId", "1")
                .param("investigationId", "1")
                .content(appointmentResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Reason.length()").value(1))
                .andExpect(jsonPath("$.Reason", matcher))
                .andReturn();
    }
}
