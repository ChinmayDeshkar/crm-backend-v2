package com.deshkar.services.impl;

import com.deshkar.model.Otp;
import com.deshkar.repo.OtpRepo;
import com.deshkar.services.EmailService;
import com.deshkar.services.OtpService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpRepo otpRepo;
    private final EmailService emailService;

    @Override
    @Transactional
    public String sendOtp(String username) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        otpRepo.deleteByUsername(username); // clean old OTP
        otpRepo.save(new Otp(null, username, otp, expiry));
        System.out.println(otp);
//        emailService.sendOtpEmail(otp, username);
        return otp;
    }

    @Override
    @Transactional
    public Boolean verifyOtp(String username, String otp) {
        Optional<Otp> record = otpRepo.findByUsername(username);
        if (record.isPresent()) {
            Otp otpRecord = record.get();
            if (otpRecord.getOtp().equals(otp) &&
                    otpRecord.getExpiryTime().isAfter(LocalDateTime.now())) {
                otpRepo.deleteByUsername(username);
                return true;
            }
        }
        return false;
    }
}
