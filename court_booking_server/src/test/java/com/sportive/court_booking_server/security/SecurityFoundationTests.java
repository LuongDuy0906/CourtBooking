package com.sportive.court_booking_server.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sportive.court_booking_server.common.enums.UserRole;
import com.sportive.court_booking_server.models.User;
import com.sportive.court_booking_server.repositories.UserRepo;

class SecurityFoundationTests {
    @Test
    void loadsDatabaseRolesAndAuthenticatesEncodedPassword() {
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder = config.passwordEncoder();
        UserRepo userRepo = mock(UserRepo.class);
        User user = new User();
        user.setEmail("player@example.com");
        user.setPassword(encoder.encode("correct-password"));
        user.addRole(UserRole.USER);
        user.addRole(UserRole.MANAGER);
        when(userRepo.findByEmailIgnoreCase("PLAYER@example.com")).thenReturn(Optional.of(user));

        DatabaseUserDetailsService userDetailsService = new DatabaseUserDetailsService(userRepo);
        UserDetails details = userDetailsService.loadUserByUsername("PLAYER@example.com");

        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER")));
        assertEquals("player@example.com", details.getUsername());

        var authenticationManager = config.authenticationManager(userDetailsService, encoder);
        var validCredentials = UsernamePasswordAuthenticationToken.unauthenticated(
            "PLAYER@example.com", "correct-password"
        );
        assertTrue(authenticationManager.authenticate(validCredentials).isAuthenticated());

        var invalidCredentials = UsernamePasswordAuthenticationToken.unauthenticated(
            "PLAYER@example.com", "wrong-password"
        );
        assertThrows(BadCredentialsException.class, () -> authenticationManager.authenticate(invalidCredentials));
    }
}
