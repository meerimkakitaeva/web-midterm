package com.example.note.service;

import com.example.note.dto.AuthRequest;
import com.example.note.dto.AuthResponse;
import com.example.note.dto.RegisterRequest;
import jakarta.mail.MessagingException;

public interface AuthService {
    public String initiateLogin(AuthRequest request) throws MessagingException;
    public AuthResponse verifyOtp(String username, String otp);
    public String register(RegisterRequest request);
    public AuthResponse refreshToken(String token);

}
