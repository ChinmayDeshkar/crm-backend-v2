package com.deshkar.services;

import com.deshkar.model.Users;

public interface EmailService {

    void sendEmail(String to, String subject, String htmlContent);

    void createAndSendMailForNewUser(String to, String subject, Users user, String password);

    void sendOtpEmail(String otp, String username);

}
