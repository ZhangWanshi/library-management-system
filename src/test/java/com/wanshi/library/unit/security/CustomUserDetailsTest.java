package com.wanshi.library.unit.security;

import com.wanshi.library.entity.Role;
import com.wanshi.library.entity.User;
import com.wanshi.library.security.CustomUserDetails;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void shouldReturnCorrectAuthorities() {

        Role role = Role.builder().name("LIBRARIAN").build();

        User user = User.builder()
                .username("lib")
                .password("pass")
                .role(role)
                .build();

        CustomUserDetails details = new CustomUserDetails(user);

        assertEquals("lib", details.getUsername());
        assertEquals("pass", details.getPassword());
        assertEquals("ROLE_LIBRARIAN",
                details.getAuthorities().iterator().next().getAuthority());
        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
    }
}
