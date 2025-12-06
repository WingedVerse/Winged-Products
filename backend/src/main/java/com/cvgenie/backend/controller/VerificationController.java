package com.cvgenie.backend.controller;

import com.cvgenie.backend.entity.OtpEntity;
import com.cvgenie.backend.jwt.JwtHelper;
import com.cvgenie.backend.repository.OtpRepository;
import com.cvgenie.backend.serviceImpl.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@CrossOrigin
public class VerificationController {
    @Autowired
    private OtpService otpService;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {

        String mobile = request.get("mobile");
        String otp = otpService.generateOtp();

        OtpEntity entity = new OtpEntity();
        entity.setMobile(mobile);
        entity.setOtp(otp);
        entity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(entity);
        otpService.sendSms(mobile, otp);

        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {

        String mobile = request.get("mobile");
        String otp = request.get("otp");

        OtpEntity entity = otpRepository.findById(mobile).orElse(null);

        if (entity == null)
            return ResponseEntity.status(400).body("OTP not found");

        if (!entity.getOtp().equals(otp))
            return ResponseEntity.status(400).body("Invalid OTP");

        if (entity.getExpiryTime().isBefore(LocalDateTime.now()))
            return ResponseEntity.status(400).body("OTP expired");

        // create JWT token
        String token = jwtHelper.generateToken(mobile);

        return ResponseEntity.ok(Map.of("token", token));
    }


}
