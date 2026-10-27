package com.sportive.court_booking_server.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sportive.court_booking_server.models.User;
import com.sportive.court_booking_server.repositories.UserRepo;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    public DatabaseUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String[] authorities = user.getRoles().stream()
            .map(role -> "ROLE_" + role.name())
            .toArray(String[]::new);

        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
            .password(user.getPassword())
            .authorities(authorities)
            .build();
    }
}
