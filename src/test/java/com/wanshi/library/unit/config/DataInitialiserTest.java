package com.wanshi.library.unit.config;

import com.wanshi.library.config.DataInitialiser;
import com.wanshi.library.entity.Role;
import com.wanshi.library.repository.RoleRepository;
import com.wanshi.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

class DataInitialiserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private DataInitialiser dataInitialiser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataInitialiser = new DataInitialiser(
                userRepository,
                roleRepository,
                passwordEncoder
        );
    }

    @Test
    void shouldCreateRolesAndUsersIfNotExists() {

        when(roleRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        when(roleRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(anyString()))
                .thenReturn("encoded");

        dataInitialiser.run();

        verify(roleRepository, times(3)).save(any(Role.class));
        verify(userRepository, times(3)).save(any());
    }
}