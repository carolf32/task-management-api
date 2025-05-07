package com.example.tasks_management.exceptions.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessageDTO {
    private String message;
    private LocalDateTime timestamp;
}
