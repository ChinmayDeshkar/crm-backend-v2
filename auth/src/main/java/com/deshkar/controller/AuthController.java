package com.deshkar.controller;

import com.deshkar.dto.LoginRequest;
import com.deshkar.dto.ResetPassword;
import com.deshkar.dto.SignUpRequest;
import com.deshkar.model.Users;
import com.deshkar.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public  ResponseEntity<?> login(@RequestBody LoginRequest req) throws LoginException {
        return authService.login(req);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest user){
        return authService.signup(user);
    }

    @PostMapping("/first-login")
    public ResponseEntity<?> firstLogin(@RequestBody ResetPassword req){
        return authService.firstLogin(req);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> map){
        String token = map.get("token");

        boolean isValid = authService.validateToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("isValid", isValid));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> req) {

        return authService.verifyOtp(req);
    }
}