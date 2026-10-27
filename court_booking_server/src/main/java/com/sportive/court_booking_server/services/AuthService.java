package com.sportive.court_booking_server.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sportive.court_booking_server.common.enums.UserRole;
import com.sportive.court_booking_server.dto.auth.LoginDTO;
import com.sportive.court_booking_server.dto.auth.RegisterDTO;
import com.sportive.court_booking_server.models.Profile;
import com.sportive.court_booking_server.models.User;
import com.sportive.court_booking_server.repositories.UserRepo;

@Service 
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterDTO request) throws Exception {
        Boolean isEmailExist = userRepo.findByEmailIgnoreCase(request.getEmail()).isPresent();

        if(isEmailExist) {
            throw new Exception("Email đã tồn tại");
        }

        String hashPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User();

        newUser.setEmail(request.getEmail());
        newUser.setPassword(hashPassword);
        newUser.addRole(UserRole.USER);

        Profile newProfile = new Profile();
        newProfile.setUsername(request.getUsername());
        newProfile.setPhone(request.getPhone());
        newProfile.setUser(newUser);

        newUser.setProfile(newProfile);

        return userRepo.save(newUser);
    }

    public String login(LoginDTO request) throws Exception{
        User existUser = userRepo.findByEmailIgnoreCase(request.getEmail()).orElse(null);

        if(existUser == null) {
            throw new Exception("Email không tồn tại");
        }

        Boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), existUser.getPassword());

        if(!isPasswordMatch) {
            throw new Exception("Mật khẩu không trùng khớp");
        }

        return "Đăng nhập thành công";
    }
}
