package com.wanshi.library.unit.service;

import com.wanshi.library.dto.CreateUserRequestDTO;
import com.wanshi.library.dto.UserDTO;
import com.wanshi.library.entity.Role;
import com.wanshi.library.entity.User;
import com.wanshi.library.exception.ResourceAlreadyExistsException;
import com.wanshi.library.repository.RoleRepository;
import com.wanshi.library.repository.UserRepository;
import com.wanshi.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private CreateUserRequestDTO request;
    private Role role;

    @BeforeEach
    void setUp() {
        request = CreateUserRequestDTO.builder()
                .username("john")
                .email("john@test.com")
                .password("1234")
                .role("MEMBER")
                .build();

        role = Role.builder()
                .id(1L)
                .name("MEMBER")
                .build();
    }

    @Test
    void createUser_shouldThrowException_whenUsernameExists() {

        when(userRepository.existsByUsername("john")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> userService.createUser(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_shouldThrowException_whenEmailExists() {

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@test.com")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> userService.createUser(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_shouldThrowException_whenRoleNotFound() {

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(roleRepository.findByName("MEMBER")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.createUser(request));
    }

    @Test
    void createUser_shouldReturnUserDTO_whenSuccess() {

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(roleRepository.findByName("MEMBER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("1234")).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .id(1L)
                .username("john")
                .email("john@test.com")
                .password("encodedPassword")
                .role(role)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("john@test.com", result.getEmail());
        assertEquals("MEMBER", result.getRole());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnUserDTOList() {

        User user = User.builder()
                .id(1L)
                .username("john")
                .email("john@test.com")
                .role(role)
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("john", result.get(0).getUsername());
        assertEquals("MEMBER", result.get(0).getRole());
    }

    @Test
    void getAllUsers_shouldReturnEmptyList_whenNoUsers() {

        when(userRepository.findAll()).thenReturn(List.of());

        List<UserDTO> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }
}