package com.deshkar.services;

import com.deshkar.dto.LoginRequest;
import com.deshkar.dto.ResetPassword;
import com.deshkar.dto.SignUpRequest;
import com.deshkar.model.Users;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthService {

    ResponseEntity<?> login(LoginRequest req);

    ResponseEntity<?> signup(SignUpRequest user);

    ResponseEntity<?> firstLogin(ResetPassword req);

    Boolean validateToken(String token);

    ResponseEntity<?> verifyOtp(Map<String, String> req);
}
