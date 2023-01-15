package com.example.medicalApp.service;

import com.example.medicalApp.exceptions.NoRecordWithIdFoundException;
import com.example.medicalApp.model.Doctor;
import com.example.medicalApp.model.MedicalUnit;
import com.example.medicalApp.model.Specialization;
import com.example.medicalApp.repository.DoctorRepository;
import com.example.medicalApp.repository.MedicalUnitRepository;
import com.example.medicalApp.repository.SpecializationRepository;
import com.example.medicalApp.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;
    private final MedicalUnitRepository medicalUnitRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository,
                         SpecializationRepository specializationRepository,
                         MedicalUnitRepository medicalUnitRepository) {
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
        this.medicalUnitRepository = medicalUnitRepository;
    }

    public Doctor addNewDoctor(int medicalUnitId, int specializationId, Doctor doctor) {
        MedicalUnit medicalUnit = getMedicalUnit(medicalUnitId);
        Specialization specialization = getSpecialization(specializationId);
        doctor.setMedicalUnit(medicalUnit);
        doctor.getSpecializationList().add(specialization);
        return doctorRepository.save(doctor);
    }

    public Doctor getDoctor(int doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NoRecordWithIdFoundException(String.format(Constants.DOCTOR_NOT_FOUND, doctorId)));
    }

    public List<Doctor> getDoctorsFromSpecialization(int specializationId) {
        Specialization specialization = getSpecialization(specializationId);
        return doctorRepository.findAllBySpecializationId(specialization.getSpecializationId());
    }

    public List<Doctor> getDoctorsFromMedicalUnit(int medicalUnitId) {
        MedicalUnit medicalUnit = getMedicalUnit(medicalUnitId);
        return doctorRepository.findAllByMedicalUnitId(medicalUnit.getMedicalUnitId());
    }

    public Doctor addNewSpecializationInfo(int doctorId, int specializationId) {
        Doctor doctor = getDoctor(doctorId);
        Specialization newSpecialization = getSpecialization(specializationId);
        doctor.getSpecializationList().add(newSpecialization);
        return doctorRepository.save(doctor);
    }

    private Specialization getSpecialization(int specializationId) {
        return specializationRepository.findById(specializationId)
                .orElseThrow(() -> new NoRecordWithIdFoundException(String.format(Constants.SPECIALIZATION_NOT_FOUND, specializationId)));
    }

    private MedicalUnit getMedicalUnit(int medicalUnitId) {
        return medicalUnitRepository.findById(medicalUnitId)
                .orElseThrow(() -> new NoRecordWithIdFoundException(String.format(Constants.MEDICAL_UNIT_NOT_FOUND, medicalUnitId)));
    }
}
