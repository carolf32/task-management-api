package com.example.tasks_management.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDTO {
    @NotBlank(message="Name is required")
    private String name;

    @NotBlank(message="E-mail is required")
    @Email(message="Enter a valid e-mail")
    private String email;

    @NotBlank(message="Password is required")
    @Size(min=4, message="Enter a password with at least 4 carachters")
    private String password;

}
