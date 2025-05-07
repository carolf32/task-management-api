package com.example.tasks_management.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tasks_management.exceptions.customExceptions.NotFoundException;
import com.example.tasks_management.security.AuthenticatedUser;
import com.example.tasks_management.security.PasswordConfig;
import com.example.tasks_management.user.dtos.UserCreateDTO;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordConfig passwordConfig;


    @Transactional
    public UserEntity createUser(UserCreateDTO dto){
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("You already have an account");
        }

        UserEntity user = UserEntity.builder()
        .name(dto.getName())
        .email(dto.getEmail())
        .password(passwordConfig.encryptPassword(dto.getPassword()))
        .build();

        return userRepository.save(user);
    }

    public UserEntity getLoggedUser(){
        return AuthenticatedUser.getAuthenticatedUser();
    }

    public List<UserEntity> listAllUsers(){
        return userRepository.findAll();
    }

    public UserEntity getUserByEmail(String email){
        return userRepository.findByEmail(email)
        .orElseThrow(()->new NotFoundException("There's no user with this email"));
    }

    public UserEntity getUserById(Long id){
        return userRepository.findById(id)
        .orElseThrow(()->new NotFoundException("User not found"));
    }

    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new NotFoundException("User not found");
        }
        userRepository.deleteUser(id);
    }

    @Transactional
    public UserEntity updateUser(Long id, UserCreateDTO dto){
        UserEntity user = userRepository.findById(id)
        .orElseThrow(()->new NotFoundException("User not found"));

        if(!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("This email is already in use");
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordConfig.encryptPassword(dto.getPassword()));
        
        return user;
    }
}
