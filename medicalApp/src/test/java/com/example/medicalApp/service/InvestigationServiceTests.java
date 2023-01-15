package com.example.medicalApp.service;

import com.example.medicalApp.repository.InvestigationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InvestigationServiceTests {
    @InjectMocks
    private InvestigationService investigationService;

    @Mock
    private InvestigationRepository investigationRepository;

    @Test
    @DisplayName("Add new investigation - happy flow")
    void addNewInvestigationHappyFlow() {
    }

    @Test
    @DisplayName("Update price for investigation - happy flow")
    void updatePriceForInvestigationHappyFlow() {
    }

    @Test
    @DisplayName("Update price too high for investigation - exception")
    void updatePriceTooHighForInvestigationException() {
    }

    @Test
    @DisplayName("Update price too low for investigation - exception")
    void updatePriceTooLowForInvestigationException() {
    }

    @Test
    @DisplayName("Get investigation - exception")
    void getInvestigationException() {
    }

    @Test
    @DisplayName("Get investigation - happy flow")
    void getInvestigationHappyFlow() {
    }

    @Test
    @DisplayName("Get investigations having given max price - happy flow")
    void getInvestigationsWithMaxPriceHappyFlow() {
    }

    @Test
    @DisplayName("Get investigations having given max price - exception")
    void getInvestigationsWithMaxPriceException() {
    }
}
