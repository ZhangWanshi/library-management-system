package com.wanshi.library.unit.security;

import com.wanshi.library.entity.Role;
import com.wanshi.library.entity.User;
import com.wanshi.library.repository.UserRepository;
import com.wanshi.library.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CustomUserDetailsService(userRepository);
    }

    @Test
    void shouldLoadUserByUsername() {

        Role role = Role.builder().name("ADMIN").build();

        User user = User.builder()
                .username("admin")
                .password("pass")
                .role(role)
                .build();

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        var userDetails = service.loadUserByUsername("admin");

        assertEquals("admin", userDetails.getUsername());
        assertEquals("pass", userDetails.getPassword());
        assertEquals("ROLE_ADMIN",
                userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void shouldThrowWhenUserNotFound() {

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("unknown"));
    }
}