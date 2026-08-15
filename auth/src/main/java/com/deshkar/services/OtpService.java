package com.deshkar.services;

public interface OtpService {

    String sendOtp(String phoneNumber);
    Boolean verifyOtp(String phoneNumber, String otp);
}
