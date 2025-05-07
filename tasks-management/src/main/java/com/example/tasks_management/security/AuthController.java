package com.example.tasks_management.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tasks_management.exceptions.customExceptions.NotFoundException;
import com.example.tasks_management.user.UserEntity;
import com.example.tasks_management.user.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordConfig passwordConfig;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password){
        UserEntity user = userRepository.findByEmail(email)
        .orElseThrow(()->new NotFoundException("User not found"));

        if(!passwordConfig.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid password or email");
        }

        return jwtConfig.generateToken(user.getId());
    }
}
