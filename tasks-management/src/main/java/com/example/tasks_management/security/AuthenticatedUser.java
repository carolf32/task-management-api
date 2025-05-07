package com.example.tasks_management.security;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.tasks_management.user.UserEntity;

public class AuthenticatedUser {
    
    public static UserEntity getAuthenticatedUser(){
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
