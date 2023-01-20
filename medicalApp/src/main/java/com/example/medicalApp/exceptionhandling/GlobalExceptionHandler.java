package com.example.medicalApp.exceptionhandling;

import com.example.medicalApp.exceptions.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleHibernateObjectValidation(MethodArgumentNotValidException exception) {
        Map<String, Object> responseParameters = new HashMap<>();

        List<String> errors = exception.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        responseParameters.put("Reason", errors);
        responseParameters.put("DateTime", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(responseParameters);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParameter(MissingServletRequestParameterException exception) {
        Map<String, Object> responseParameters = new HashMap<>();

        responseParameters.put("Reason", exception.getMessage());
        responseParameters.put("DateTime", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(responseParameters);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleHibernateParametersValidation(ConstraintViolationException exception) {
        Map<String, Object> responseParameters = new HashMap<>();

        List<String> errors = exception.getConstraintViolations()
                .stream()
                .map(e -> e.getMessage())
                .collect(Collectors.toList());

        responseParameters.put("Reason", errors);
        responseParameters.put("DateTime", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(responseParameters);
    }

    @ExceptionHandler({InvalidAppointmentTimeException.class, InvalidDeletionOfOldAppointmentException.class,
            InvalidDeletionOfInvestigation.class, InvalidPriceForInvestigationException.class})
    public ResponseEntity<?> handleCustomException(RuntimeException e) {
        Map<String, String> responseParameters = new HashMap<>();
        responseParameters.put("Reason", e.getMessage());
        responseParameters.put("DateTime", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(responseParameters);
    }

    @ExceptionHandler({NoRecordWithIdFoundException.class})
    public ResponseEntity<?> handleRecordNotFound(RuntimeException e) {
        Map<String, String> responseParameters = new HashMap<>();
        responseParameters.put("Reason", e.getMessage());
        responseParameters.put("DateTime", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(responseParameters);
    }

}
