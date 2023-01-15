package com.example.medicalApp.exceptions;

public class InvalidDeletionOfOldAppointmentException extends RuntimeException {
    public InvalidDeletionOfOldAppointmentException(String message) {
        super(message);
    }
}

