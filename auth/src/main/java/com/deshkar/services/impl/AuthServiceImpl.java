package com.deshkar.services.impl;

import com.deshkar.code.dto.Code;
import com.deshkar.code.entity.CodeType;
import com.deshkar.code.service.CodeService;
import com.deshkar.dto.LoginRequest;
import com.deshkar.dto.ResetPassword;
import com.deshkar.dto.SignUpRequest;
import com.deshkar.exceptions.DuplicateRecordException;
import com.deshkar.exceptions.LoginException;
import com.deshkar.exceptions.ResourceNotFoundException;
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
                .orElseThrow(() -> new LoginException("User not found with username: " + req.username()));

        if(!user.getIsActive())
            throw new LoginException("User is inactive, contact admin");

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
            throw new DuplicateRecordException("Username is already exists");

        // Check if phone number present
        if(userRepo.existsByPhone(user.getPhone()))
            throw new DuplicateRecordException("Phone number is already exists");

        if (userRepo.existsByEmail(user.getEmail()))
            throw new DuplicateRecordException("Email id is already exists");

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
        if(u.isEmpty()) throw new LoginException("User not found: " + req.getUsername());
        Users user = u.get();
        if(!encoder.matches(req.getOldPassword(), user.getPassword()))
            throw new LoginException("Username/password is incorrect, please reset password if forgot");
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
            throw new LoginException("Invalid OTP");
        }

        Users user = userRepo.findByUsername(username).orElse(null);
        if (user == null) throw new LoginException("User not found: " + username);

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
