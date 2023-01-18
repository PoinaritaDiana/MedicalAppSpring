package com.example.medicalApp.controller;

import com.example.medicalApp.model.MedicalReport;
import com.example.medicalApp.service.MedicalReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = MedicalReportController.class)
public class MedicalReportControllerTests {
    @MockBean
    private MedicalReportService medicalReportService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Add new medical report endpoint - happy flow")
    public void addMedicalReportEndpointHappyFlow() throws Exception {
        MedicalReport report = new MedicalReport("test diagnostic");
        report.setMedicalReportId(1);
        when(medicalReportService.addNewMedicalReportForAppointment(anyInt(), any())).thenReturn(report);
        String reportResponseBody = mapper.writeValueAsString(report);

        MvcResult result = mockMvc.perform(post("/report/new")
                .param("appointmentId", "1")
                .content(reportResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/report/1"))
                .andExpect(jsonPath("$.diagnostic").value("test diagnostic"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), reportResponseBody);
    }

    @Test
    @DisplayName("Add new medical report endpoint - exception")
    public void addMedicalReportEndpointException() throws Exception {
        MedicalReport report = new MedicalReport("");
        String reportResponseBody = mapper.writeValueAsString(report);

        mockMvc.perform(post("/report/new")
                .param("appointmentId", "1")
                .content(reportResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Reason.length()").value(1))
                .andExpect(jsonPath("$.Reason.[0]").value("The diagnosis given to the patient cannot be empty"))
                .andReturn();
    }

    @Test
    @DisplayName("Get medical report by id endpoint - happy flow")
    public void getMedicalReportByIdEndpointHappyFlow() throws Exception {
        MedicalReport report = new MedicalReport("test diagnostic");
        when(medicalReportService.getMedicalReport(anyInt())).thenReturn(report);

        MvcResult result = mockMvc.perform(get("/report/{reportId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnostic").value("test diagnostic"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(report));
    }

    @Test
    @DisplayName("Update medical report info endpoint - happy flow")
    public void updateMedicalReportInfoHappyFlow() throws Exception {
        MedicalReport report = new MedicalReport("test diagnostic");
        String reportResponseBody = mapper.writeValueAsString(report);
        when(medicalReportService.updateMedicalReport(anyInt(), any())).thenReturn(report);

        MvcResult result = mockMvc.perform(put("/report/{reportId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reportResponseBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnostic").value("test diagnostic"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), reportResponseBody);
    }

}
