package com.example.tasks_management.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tasks_management.exceptions.customExceptions.NotFoundException;
import com.example.tasks_management.security.PasswordConfig;
import com.example.tasks_management.user.UserEntity;
import com.example.tasks_management.user.UserRepository;
import com.example.tasks_management.user.UserService;
import com.example.tasks_management.user.dtos.UserCreateDTO;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordConfig passwordConfig;

    @InjectMocks
    private UserService userService;

    @Test 
    public void testCreateUserFromDTO () {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setName("John Doe");
        dto.setEmail("john@mail.com");
        dto.setPassword("abc123");

        UserEntity userEntity = UserEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password("encryptedPassword")
                .build();

        when(passwordConfig.encryptPassword(dto.getPassword())).thenReturn("encryptedPassword");

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity created = userService.createUser(dto);

        assertNotNull(created);
        assertEquals("John Doe", created.getName());
        assertEquals("john@mail.com", created.getEmail());
        verify(passwordConfig, times(1)).encryptPassword(dto.getPassword());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testCreateUserFromDTOWithExistingEmail() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setEmail("existing@mail.com");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.createUser(dto), "You already have an account");
    }

    @Test
    public void testGetUserByEmail_Success(){
        UserEntity user = UserEntity.builder()
                .email("john@mail.com")
                .build();

        when(userRepository.findByEmail("john@mail.com")).thenReturn(Optional.of(user));

        UserEntity result = userService.getUserByEmail("john@mail.com");
        assertEquals("john@mail.com", result.getEmail());
    }

    @Test
    public void testGetUserByEmail_NotFound(){
        when(userRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserByEmail("unknown@mail.com"), "There's no user with this email");
    }

    @Test
    public void testDeleteUser_Success(){
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteUser(1L);
    }

    @Test
    public void testDeleteUser_NotFound(){
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> userService.deleteUser(1L), "User not found");
    }

    @Test
    public void testUpdateUser_Success(){

        UserEntity existingUser = UserEntity.builder()
                .id(1L)
                .name("Original Name")
                .email("original@mail.com")
                .password("oldEncryptedPassword")
                .build();

        UserCreateDTO dto = new UserCreateDTO();
                dto.setName("Updated Name");
                dto.setEmail("new@mail.com");
                dto.setPassword("newEncyptedPassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordConfig.encryptPassword(dto.getPassword())).thenReturn("newEncryptedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity updatedUser = userService.updateUser(1L, dto);

        assertEquals(1L, updatedUser.getId());
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("new@mail.com", updatedUser.getEmail());
        assertEquals("newEncryptedPassword", updatedUser.getPassword());

        verify(userRepository).existsByEmail("new@mail.com");
        verify(passwordConfig).encryptPassword("newEncyptedPassword");
        verify(userRepository).save(updatedUser);
    }

    @Test
    public void testUpdateUser_EmailAlreadyInUse(){
        UserEntity existingUser = UserEntity.builder()
                .id(1L)
                .email("original@mail.com")
                .build();

        UserCreateDTO dto = new UserCreateDTO();
                dto.setEmail("taken@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("taken@mail.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, dto), "This email is already in use");
    }
}