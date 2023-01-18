package com.example.medicalApp.controller;

import com.example.medicalApp.model.Investigation;
import com.example.medicalApp.service.InvestigationService;
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

@WebMvcTest(controllers = InvestigationController.class)
public class InvestigationControllerTests {
    @MockBean
    private InvestigationService investigationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("Get investigation by id endpoint - happy flow")
    public void getInvestigationByIdEndpointHappyFlow() throws Exception {
        Investigation investigation = new Investigation("consultatie", "consultatie de rutina", 100, 30);
        when(investigationService.getInvestigation(anyInt())).thenReturn(investigation);

        MvcResult result = mockMvc.perform(get("/investigation/{investigationId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("consultatie de rutina"))
                .andExpect(jsonPath("$.name").value("consultatie"))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.time").value(30))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), mapper.writeValueAsString(investigation));
    }

    @Test
    @DisplayName("Get investigations for doctor endpoint - happy flow")
    public void getInvestigationsForDoctorEndpointHappyFlow() throws Exception {
        List<Investigation> investigationList = new ArrayList<>();
        Investigation investigation = new Investigation("consultatie", "consultatie de rutina", 100, 30);
        investigationList.add(investigation);
        when(investigationService.getInvestigationsForDoctor(anyInt())).thenReturn(investigationList);

        mockMvc.perform(get("/investigation/doctor")
                .param("doctorId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].description").value("consultatie de rutina"))
                .andExpect(jsonPath("$.[0].name").value("consultatie"))
                .andExpect(jsonPath("$.[0].price").value(100))
                .andExpect(jsonPath("$.[0].time").value(30))
                .andReturn();
    }

    @Test
    @DisplayName("Delete investigation by id endpoint - happy flow")
    public void deleteInvestigationEndpointHappyFlow() throws Exception {
        String message = "Investigation was successfully deleted";
        when(investigationService.deleteInvestigation(anyInt())).thenReturn(message);

        mockMvc.perform(delete("/investigation/{investigationId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(message))
                .andReturn();
    }

    @Test
    @DisplayName("Get investigations with max price endpoint - happy flow")
    public void getInvestigationsWithMaxPriceHappyFlow() throws Exception {
        List<Investigation> investigationList = new ArrayList<>();
        Investigation investigation = new Investigation("consultatie", "consultatie de rutina", 100, 30);
        investigationList.add(investigation);
        when(investigationService.getInvestigationsWithMaxPrice(anyDouble())).thenReturn(investigationList);

        mockMvc.perform(get("/investigation/filter")
                .param("price", "1000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].description").value("consultatie de rutina"))
                .andExpect(jsonPath("$.[0].name").value("consultatie"))
                .andExpect(jsonPath("$.[0].price").value(100))
                .andExpect(jsonPath("$.[0].time").value(30))
                .andReturn();
    }

    @Test
    @DisplayName("Get investigations with max price endpoint - exception")
    public void getInvestigationsWithMaxPriceException() throws Exception {
        Matcher<Iterable<? extends String>> matcher = Matchers.containsInAnyOrder("must be greater than or equal to 0");

        mockMvc.perform(get("/investigation/filter")
                .param("price", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Reason.length()").value(1))
                .andExpect(jsonPath("$.Reason", matcher))
                .andReturn();
    }

    @Test
    @DisplayName("Update investigations price endpoint - exception")
    public void updateInvestigationPriceException() throws Exception {
        Matcher<Iterable<? extends String>> matcher = Matchers.containsInAnyOrder("must be less than or equal to 1000");

        mockMvc.perform(put("/investigation/{investigationId}", 1)
                .param("price", "10000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Reason.length()").value(1))
                .andExpect(jsonPath("$.Reason", matcher))
                .andReturn();
    }

    @Test
    @DisplayName("Add new investigation endpoint - happy flow")
    public void addInvestigationEndpointHappyFlow() throws Exception {
        Investigation investigation = new Investigation("consultatie", "consultatie de rutina", 100, 30);
        investigation.setInvestigationId(1);
        when(investigationService.addNewInvestigation(anyInt(), any())).thenReturn(investigation);

        String investigationResponseBody = mapper.writeValueAsString(investigation);

        MvcResult result = mockMvc.perform(post("/investigation/new")
                .param("doctorId", "1")
                .content(investigationResponseBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/investigation/1"))
                .andExpect(jsonPath("$.description").value("consultatie de rutina"))
                .andExpect(jsonPath("$.name").value("consultatie"))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.time").value(30))
                .andReturn();

        assertEquals(result.getResponse().getContentAsString(), investigationResponseBody);
    }
}
