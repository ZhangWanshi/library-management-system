package com.wanshi.library.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanshi.library.entity.Role;
import com.wanshi.library.entity.User;
import com.wanshi.library.repository.RoleRepository;
import com.wanshi.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {

        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role role = roleRepository.save(
                Role.builder().name("ADMIN").build()
        );

        User user = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .email("admin@test.com")
                .role(role)
                .build();

        userRepository.save(user);
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {

        User loginRequest = new User();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andExpect(jsonPath("$.refreshToken", notNullValue()))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void shouldFailLoginWithWrongPassword() throws Exception {

        User loginRequest = new User();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("wrong");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void shouldRefreshAccessToken() throws Exception {

        User loginRequest = new User();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin");

        String loginResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> tokens =
                objectMapper.readValue(loginResponse, Map.class);

        String refreshToken = tokens.get("refreshToken");

        Map<String, String> refreshBody = Map.of("refreshToken", refreshToken);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()));
    }
}