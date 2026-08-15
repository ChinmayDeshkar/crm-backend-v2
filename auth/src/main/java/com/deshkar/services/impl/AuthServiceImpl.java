package com.deshkar.services.impl;

import com.deshkar.code.dto.Code;
import com.deshkar.code.entity.CodeType;
import com.deshkar.code.service.CodeService;
import com.deshkar.dto.LoginRequest;
import com.deshkar.dto.ResetPassword;
import com.deshkar.dto.SignUpRequest;
import com.deshkar.model.Role;
import com.deshkar.model.Users;
import com.deshkar.repo.UserRepo;
import com.deshkar.security.JwtUtil;
import com.deshkar.security.PasswordGenerator;
import com.deshkar.services.AuthService;
import com.deshkar.services.OtpService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor

public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final PasswordGenerator passwordGenerator;
    private final CodeService codeService;

    @Override
    public ResponseEntity<?> login(LoginRequest req) {
        Users user = userRepo.findByUsername(req.username())
                .orElse(null);

        if(user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", "UserNotFound",
                                                                                "Message", "User not found with username " + req.username()));

        if(!user.getIsActive())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", "User is inactive",
                    "Message", "User is inactive " + req.username()));

        if(encoder.matches(req.password(), user.getPassword())){

            String authToken = jwtUtil.generateToken(req.username(), codeService.getCode(user.getRole()).getCode());
            if (user.getFirstLogin()) return ResponseEntity.status(HttpStatus.OK).body(Map.of("Message", "First Login detected, Please reset Password"));

            log.info(user.getRole().toString());

            String otp = otpService.sendOtp(user.getUsername());

            // Tell frontend to go to OTP verify page
            return ResponseEntity.ok(Map.of(
                    "otpSent", true,
                    "username", user.getUsername(),
                    "otp", otp
                    ));
//            return ResponseEntity.status(HttpStatus.OK).body(Map.of("Message", "User logged in successfully",
//                                                                        "AuthToken", authToken,
//                                                                        "Role", user.getRole(),
//                                                                        "Username", user.getUsername()));
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", "Error while logging in"));
    }

    @Override
    public ResponseEntity<?> signup(SignUpRequest signUpRequest) {
        Users user = createUserEntity(signUpRequest);
        // Check if username presents
        if(userRepo.existsByUsername(user.getUsername()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Message: ", "User already present"));

        // Check if phone number present
        if(userRepo.existsByPhone(user.getPhone()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Message: ", "Phone number already present"));

        if (userRepo.existsByEmail(user.getEmail()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Message: ", "Email id already present"));

        String newPassword = passwordGenerator.generatePassword(8);
        Code code = codeService.getCode(Role.EMPLOYEE, CodeType.ROLE);
        if(code != null) log.info(code.toString());
        log.info("Code should be printed above");
        user.setRole(code.getId());
        user.setPassword(encoder.encode(newPassword));
        int userCount = userRepo.findAll().size();
        String userId = String.format("d%04d", userCount + 1);
        user.setUsername(userId);

        userRepo.save(user);
        log.info("One time password for user: " + user.getUsername() + " Password: " + newPassword);
        //emailService.createAndSendMailForNewUser("deshkarchinmay42@gmail.com", "New user created - " + newUser.getUsername(), newUser, newPassword);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Message: ", "User Created",
                                                                        "Username: ", user.getUsername()));


    }

    @Override
    public ResponseEntity<?> firstLogin(ResetPassword req){
        var u = userRepo.findByUsername(req.getUsername());
        if(u.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Message","User not found"));
        Users user = u.get();
        if(!encoder.matches(req.getOldPassword(), user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("Message","Incorrect old password"));
        user.setPassword(encoder.encode(req.getNewPassword()));
        user.setFirstLogin(false);
        userRepo.save(user);
        return ResponseEntity.ok(Map.of("Message","Password reset successful"));
    }

    @Override
    public Boolean validateToken(String token) {
        boolean isValid = false;
        if(jwtUtil.validateToken(token))
            isValid = true;
        return isValid;
    }

    @Override
    public ResponseEntity<?> verifyOtp(Map<String, String> req) {
        String username = req.get("username");
        String otp = req.get("otp");

        if (!otpService.verifyOtp(username, otp)) {
            return ResponseEntity.badRequest().body(Map.of("Message", "Invalid OTP"));
        }

        Users user = userRepo.findByUsername(username).orElse(null);
        if (user == null) return ResponseEntity.status(404).body(Map.of("Message", "User not found"));

        // Generate JWT (same as before)
        String authToken = jwtUtil.generateToken(username, codeService.getCode(user.getRole()).getCode());

        return ResponseEntity.ok(Map.of(
                "Message", "Login successful",
                "AuthToken", authToken,
                "Role", user.getRole(),
                "Username", username
        ));
    }

    Users createUserEntity(SignUpRequest req){
        Users user = new Users();
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setPassword(req.getPassword());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setSalary(req.getSalary() == null ? "0": req.getSalary());

        return user;
    }
}
