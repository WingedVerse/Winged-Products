//package com.cvgenie.backend.serviceImpl;
//
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.Random;
//
//@Service
//public class OtpService {
//
//    @Value("${twilio.account-sid}")
//    private String ACCOUNT_SID;
//
//    @Value("${twilio.auth-token}")
//    private String AUTH_TOKEN;
//
//    @Value("${twilio.phone-number}")
//    private String TWILIO_NUMBER;
//
//    @PostConstruct
//    public void init() {
//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//    }
//
//    public void sendSms(String mobile, String otp) {
//        Message.creator(
//                new PhoneNumber("+91" + mobile),
//                new PhoneNumber(TWILIO_NUMBER),
//                "Your OTP is: " + otp
//        ).create();
//    }
//
//    public String generateOtp() {
//        return String.valueOf(new Random().nextInt(900000) + 100000);
//    }
//
//}
