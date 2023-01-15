package com.example.medicalApp.exceptions;

public class NoRecordWithIdFoundException extends RuntimeException{
    public NoRecordWithIdFoundException(String message) {
        super(message);
    }
}
