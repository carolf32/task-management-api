package com.example.tasks_management.task.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskCreateDTO {
    @NotBlank(message="This field is required")
    private String task;

    private int daysUntilDeadline;
    
    private boolean completed;
}
