package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.InvalidDeletionOfInvestigation;
import com.example.medicalApp.exceptions.InvalidPriceForInvestigationException;
import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.Doctor;
import com.example.medicalApp.model.Investigation;
import com.example.medicalApp.repository.DoctorRepository;
import com.example.medicalApp.repository.InvestigationRepository;
import com.example.medicalApp.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvestigationService {
    private final InvestigationRepository investigationRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public InvestigationService(InvestigationRepository investigationRepository,
                                DoctorRepository doctorRepository) {
        this.investigationRepository = investigationRepository;
        this.doctorRepository = doctorRepository;
    }

    public Investigation updatePriceOfInvestigation(int investigationId, double newPrice) {
        Investigation investigation = getInvestigation(investigationId);

        double currentPrice = investigation.getPrice();
        double lowLimitPrice = currentPrice - currentPrice / 10.0;
        double highLimitPrice = currentPrice + currentPrice / 10.0;

        if (newPrice < lowLimitPrice || newPrice > highLimitPrice) {
            throw new InvalidPriceForInvestigationException("The new price for the investigation cannot be less than " + lowLimitPrice
                    + " or bigger than " + highLimitPrice);
        }
        investigation.setPrice(newPrice);
        return investigationRepository.save(investigation);
    }

    public List<Investigation> getInvestigationsWithMaxPrice(double price) {
        return investigationRepository.findAll()
                .stream()
                .filter(i -> i.getPrice() <= price)
                .sorted(Comparator.comparingDouble(Investigation::getPrice))
                .collect(Collectors.toList());
    }

    public Investigation getInvestigation(int investigationId) {
        return investigationRepository.findById(investigationId)
                .orElseThrow(() -> new NoRecordWithIdFoundException(String.format(Constants.INVESTIGATION_NOT_FOUND, investigationId)));
    }

    public Investigation addNewInvestigation(int doctorId, Investigation investigation) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NoRecordWithIdFoundException(String.format(Constants.DOCTOR_NOT_FOUND, doctorId)));

        investigation.setDoctor(doctor);
        return investigationRepository.save(investigation);
    }

    public List<Investigation> getInvestigationsForDoctor(int doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new NoRecordWithIdFoundException(String.format(Constants.DOCTOR_NOT_FOUND, doctorId));
        }
        return investigationRepository.findAllByDoctorId(doctorId);
    }

    public String deleteInvestigation(int investigationId) {
        Investigation investigation = getInvestigation(investigationId);

        // The investigation can be deleted only if no future appointments exist for it

        int futureAppointmentsNumber = investigation.getAppointmentList()
                .stream()
                .filter(a -> a.getAppointmentDate().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList())
                .size();

        if (futureAppointmentsNumber > 0) {
            throw new InvalidDeletionOfInvestigation("The investigation cannot be deleted because there are still future appointments for it");
        }
        investigationRepository.deleteById(investigationId);
        return String.format("Investigation %s was successfully deleted", investigationId);
    }
}
