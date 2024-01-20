package com.example.controller;

import com.example.exceptions.ApplianceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(ApplianceNotFoundException.class)
    public ProblemDetail applianceNotFound(ApplianceNotFoundException e){
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,"Appliance not found");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail applianceNotValid(MethodArgumentNotValidException e){
        var errorMessage = e.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(" "));

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,errorMessage);
    }

}
