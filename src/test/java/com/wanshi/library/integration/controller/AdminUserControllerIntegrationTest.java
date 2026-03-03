package com.wanshi.library.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanshi.library.dto.CreateUserRequestDTO;
import com.wanshi.library.entity.Role;
import com.wanshi.library.repository.RoleRepository;
import com.wanshi.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminUserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        roleRepository.save(Role.builder().name("ADMIN").build());
        roleRepository.save(Role.builder().name("MEMBER").build());
        roleRepository.save(Role.builder().name("LIBRARIAN").build());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_shouldReturn201_whenValidRequest() throws Exception {

        CreateUserRequestDTO request = CreateUserRequestDTO.builder()
                .username("john")
                .email("john@test.com")
                .password("1234")
                .role("MEMBER")
                .build();

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.email").value("john@test.com"))
                .andExpect(jsonPath("$.role").value("MEMBER"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_shouldReturn400_whenInvalidRequest() throws Exception {

        CreateUserRequestDTO request = CreateUserRequestDTO.builder()
                .username("")   // invalid
                .email("invalid-email")
                .password("12")
                .role("")
                .build();

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_shouldReturn409_whenUsernameExists() throws Exception {

        CreateUserRequestDTO request = CreateUserRequestDTO.builder()
                .username("john")
                .email("john@test.com")
                .password("1234")
                .role("MEMBER")
                .build();

        mockMvc.perform(post("/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_shouldReturn200() throws Exception {

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    void createUser_shouldReturn403_whenNotAdmin() throws Exception {

        CreateUserRequestDTO request = CreateUserRequestDTO.builder()
                .username("john")
                .email("john@test.com")
                .password("1234")
                .role("MEMBER")
                .build();

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}