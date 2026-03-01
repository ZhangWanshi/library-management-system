package com.wanshi.library.service;

import com.wanshi.library.dto.CreateUserRequestDTO;
import com.wanshi.library.dto.UserDTO;
import com.wanshi.library.entity.Role;
import com.wanshi.library.entity.User;
import com.wanshi.library.exception.ResourceAlreadyExistsException;
import com.wanshi.library.repository.RoleRepository;
import com.wanshi.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // -----------------------
    // CREATE USER
    // -----------------------
    public UserDTO createUser(CreateUserRequestDTO request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        return mapToDTO(savedUser);
    }

    // -----------------------
    // GET ALL USERS
    // -----------------------
    public List<UserDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
                //.collect(Collectors.toList());
    }

    // -----------------------
    // MAPPING METHOD
    // -----------------------
    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }
}