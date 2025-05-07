package com.example.tasks_management.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;

import com.example.tasks_management.user.UserEntity;

@Component
public class AuthenticatedUser {

    @Autowired
    private SecurityContext securityContext;
    
    public UserEntity getAuthenticatedUser(){
        return (UserEntity) securityContext.getAuthentication().getPrincipal();
    }
}
