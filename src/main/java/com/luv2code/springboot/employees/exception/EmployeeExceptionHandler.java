package com.luv2code.springboot.employees.exception;


import com.luv2code.springboot.employees.Dto.EmployeeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class EmployeeExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<EmployeeErrorResponse> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        EmployeeErrorResponse response = new EmployeeErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EmployeeErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors=new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach((error)->errors.add(error.getDefaultMessage()));

        EmployeeErrorResponse response = new EmployeeErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errors.toString(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }


}
