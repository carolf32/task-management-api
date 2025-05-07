package com.example.tasks_management.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tasks_management.user.dtos.UserCreateDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping
    public UserEntity createUser(@Valid @RequestBody UserCreateDTO dto){
        return userService.createUser(dto);
    }

    @GetMapping
    public List<UserEntity> getAllUsers(){
        return userService.listAllUsers();
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/email/{email}")
    public UserEntity getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public UserEntity updateUser(@PathVariable Long id, @Valid @RequestBody UserCreateDTO dto){
        return userService.updateUser(id, dto);
    }
}
