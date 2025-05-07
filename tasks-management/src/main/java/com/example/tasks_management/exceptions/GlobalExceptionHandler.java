package com.example.tasks_management.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.tasks_management.exceptions.customExceptions.NotFoundException;
import com.example.tasks_management.exceptions.dtos.ErrorMessageDTO;

public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ErrorMessageDTO> handleNotFoundException(NotFoundException ex){
            ErrorMessageDTO error = new ErrorMessageDTO(ex.getMessage(), LocalDateTime.now());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    

    @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ErrorMessageDTO> handleRuntimeException(RuntimeException ex){
            ErrorMessageDTO error = new ErrorMessageDTO(ex.getMessage(), LocalDateTime.now());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    

    @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorMessageDTO> handleGenericException(Exception ex){
            ErrorMessageDTO error = new ErrorMessageDTO("Internal server error", LocalDateTime.now());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    
}
