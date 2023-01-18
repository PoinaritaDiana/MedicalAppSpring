package com.example.medicalApp.controller;

import com.example.medicalApp.model.Patient;
import com.example.medicalApp.service.PatientService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PatientController.class)
public class PatientControllerTests {
    @MockBean
    private PatientService patientService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;


    @Test
    @DisplayName("Get patient - happy flow")
    public void getPatientEndpointHappyFlow() throws Exception {
        Patient patient = new Patient("last name", "first name", "1111111111", "14-08-2000");
        when(patientService.getPatient(anyInt())).thenReturn(patient);

        MvcResult result = mockMvc.perform(get("/patient/{patientId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("last name"))
                .andExpect(jsonPath("$.firstName").value("first name"))
                .andExpect(jsonPath("$.phone").value("1111111111"))
                .andExpect(jsonPath("$.dateOfBirth").value("14-08-2000"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(patient));
    }

    @Test
    @DisplayName("Add patient - exception")
    public void addPatientEndpointException() throws Exception {
        Patient patient = new Patient("", "", "11111", "");
        String patientResponseBody = mapper.writeValueAsString(patient);
        Matcher<Iterable<? extends String>> matcher = Matchers.containsInAnyOrder("LastName cannot be empty",
                "FirstName cannot be empty",
                "The patient's date of birth cannot be empty",
                "must match \"^\\d{10}$\"");

        mockMvc.perform(post("/patient/new")
                .content(patientResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Reason.length()").value(4))
                .andExpect(jsonPath("$.Reason", matcher))
                .andReturn();
    }

    @Test
    @DisplayName("Add patient - happy flow")
    public void addPatientEndpointHappyFlow() throws Exception {
        Patient patient = new Patient("Poinarita", "Diana", "1111111111", "14-08-2000");
        when(patientService.addNewPatient(any())).thenReturn(patient);
        String patientResponseBody = mapper.writeValueAsString(patient);

        MvcResult result = mockMvc.perform(post("/patient/new")
                .content(patientResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lastName").value("Poinarita"))
                .andExpect(jsonPath("$.firstName").value("Diana"))
                .andExpect(jsonPath("$.phone").value("1111111111"))
                .andExpect(jsonPath("$.dateOfBirth").value("14-08-2000"))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), patientResponseBody);
    }

    @Test
    @DisplayName("Update medical history - happy flow")
    public void addInfoToPatientMedicalHistoryHappyFlow() throws Exception {
        String newMedicalHistoryInfo = "new history info";
        when(patientService.updateMedicalHistoryWithNewInfo(anyInt(), anyString())).thenReturn(newMedicalHistoryInfo);

        MvcResult result = mockMvc.perform(put("/patient/{patientId}/medicalhistory", 1)
                .content(newMedicalHistoryInfo)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(newMedicalHistoryInfo))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), newMedicalHistoryInfo);
    }
}
