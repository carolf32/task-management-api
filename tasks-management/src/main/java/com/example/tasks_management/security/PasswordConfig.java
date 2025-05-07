package com.example.tasks_management.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordConfig {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encryptPassword(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encryptedPassword){
        return passwordEncoder.matches(rawPassword, encryptedPassword);
    }
}
