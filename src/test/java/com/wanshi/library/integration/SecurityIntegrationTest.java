package com.wanshi.library.integration;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;

    @BeforeEach
    void setup() throws Exception {

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

        User loginRequest = new User();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin");

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, String> tokens =
                objectMapper.readValue(response, Map.class);

        adminToken = tokens.get("accessToken");
    }

    @Test
    void shouldAllowAdminAccess() throws Exception {

        mockMvc.perform(get("/admin")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectWithoutToken() throws Exception {

        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }
}