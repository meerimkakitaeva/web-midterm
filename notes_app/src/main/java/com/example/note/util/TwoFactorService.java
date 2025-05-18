
package com.example.note.util;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TwoFactorService {
    private final ConcurrentHashMap<String, String> otpStore = new ConcurrentHashMap<>();

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit
        otpStore.put(email, otp);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        return otp.equals(otpStore.get(email));
    }
}
