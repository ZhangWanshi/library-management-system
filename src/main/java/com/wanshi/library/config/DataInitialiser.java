package com.wanshi.library.config;

import com.wanshi.library.entity.Role;
import com.wanshi.library.entity.User;
import com.wanshi.library.repository.RoleRepository;
import com.wanshi.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitialiser implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        Role adminRole = createRoleIfNotExists("ADMIN");
        Role librarianRole = createRoleIfNotExists("LIBRARIAN");
        Role memberRole = createRoleIfNotExists("MEMBER");

        createUserIfNotExists("admin", "admin", "admin@library.com", adminRole);
        createUserIfNotExists("librarian", "librarian", "librarian@library.com", librarianRole);
        createUserIfNotExists("member", "member", "member@library.com", memberRole);
    }

    private Role createRoleIfNotExists(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name(name).build()
                ));
    }

    private void createUserIfNotExists(String username,
                                       String password,
                                       String email,
                                       Role role) {

        if (userRepository.findByUsername(username).isEmpty()) {

            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .role(role)
                    .build();

            userRepository.save(user);
            log.info("Created user: {}", username);
        }
    }
}