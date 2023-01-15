package com.example.medicalApp.exceptions;

public class InvalidPriceForInvestigationException extends RuntimeException{
    public InvalidPriceForInvestigationException(String message) {
        super(message);
    }
}
