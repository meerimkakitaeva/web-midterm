package com.example.note.service.impl;

import com.example.note.dto.AuthRequest;
import com.example.note.dto.AuthResponse;
import com.example.note.dto.RegisterRequest;
import com.example.note.entity.RefreshToken;
import com.example.note.entity.User;
import com.example.note.repository.RefreshTokenRepository;
import com.example.note.repository.UserRepository;
import com.example.note.security.JwtUtil;
import com.example.note.service.AuthService;
import com.example.note.util.EmailService;
import com.example.note.util.TwoFactorService;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtUtil jwtUtil;

    private final EmailService emailService;

    private final TwoFactorService twoFactorService;

    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository, EmailService emailService, TwoFactorService twoFactorService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.emailService = emailService;
        this.twoFactorService = twoFactorService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String initiateLogin(AuthRequest authRequest) throws MessagingException {
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found by username %s ", authRequest.getUsername())));
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            return "Invalid username or password";
        }
        String otp = twoFactorService.generateOtp(user.getEmail());
        emailService.sendEmail(user.getEmail(), "Your 2FA Code", "Your login code is: " + otp);
        return "OTP sent to email. Please verify.";
    }

    @Override
    public AuthResponse verifyOtp(String username, String otp) {
        User user = userRepository.findByUsername(username).orElseThrow();
        if (!twoFactorService.verifyOtp(user.getEmail(), otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        String accessToken = jwtUtil.generateToken(user.getUsername());
        String refreshToken = UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken(refreshToken, Instant.now().plusMillis(604800000), user);
        refreshTokenRepository.save(token);
        return new AuthResponse(accessToken, refreshToken);
    }

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already registered.";
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return "Username already taken.";
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return "User registered successfully.";
    }

    public AuthResponse refreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow();
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired.");
        }
        String accessToken = jwtUtil.generateToken(refreshToken.getUser().getUsername());
        return new AuthResponse(accessToken, refreshToken.getToken());
    }
}
