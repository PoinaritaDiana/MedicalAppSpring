package com.example.medicalApp.service;

import com.example.medicalApp.repository.MedicalReportRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MedicalReportServiceTests {

    @InjectMocks
    private MedicalReportService medicalReportService;

    @Mock
    private MedicalReportRepository medicalReportRepository;

    @Test
    @DisplayName("Adding new medical report - happy flow")
    void addNewMedicalReportHappyFlow() {
    }
}
