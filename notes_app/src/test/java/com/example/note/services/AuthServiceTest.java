package com.example.note.services;

import com.example.note.dto.AuthRequest;
import com.example.note.dto.AuthResponse;
import com.example.note.dto.RegisterRequest;
import com.example.note.entity.RefreshToken;
import com.example.note.entity.User;
import com.example.note.repository.RefreshTokenRepository;
import com.example.note.repository.UserRepository;
import com.example.note.security.JwtUtil;
import com.example.note.service.AuthService;
import com.example.note.service.impl.AuthServiceImpl;
import com.example.note.util.EmailService;
import com.example.note.util.TwoFactorService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private JwtUtil jwtUtil;
    private EmailService emailService;
    private TwoFactorService twoFactorService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        jwtUtil = mock(JwtUtil.class);
        emailService = mock(EmailService.class);
        twoFactorService = mock(TwoFactorService.class);
        passwordEncoder = mock(PasswordEncoder.class);

        authService = new AuthServiceImpl(
                userRepository,
                jwtUtil,
                refreshTokenRepository,
                emailService,
                twoFactorService,
                passwordEncoder
        );
    }

    @Test
    void testInitiateLoginSuccess() throws MessagingException {
        AuthRequest request = new AuthRequest("john", "password");
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(twoFactorService.generateOtp("john@example.com")).thenReturn("123456");

        String result = authService.initiateLogin(request);

        assertEquals("OTP sent to email. Please verify.", result);
        verify(emailService).sendEmail(eq("john@example.com"), anyString(), contains("123456"));
    }

    @Test
    void testVerifyOtpSuccess() {
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(twoFactorService.verifyOtp("john@example.com", "123456")).thenReturn(true);
        when(jwtUtil.generateToken("john")).thenReturn("access-token");

        AuthResponse response = authService.verifyOtp("john", "123456");

        assertEquals("access-token", response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest req = new RegisterRequest("newuser", "new@example.com", "securepass");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("newuser")).thenReturn(false);

        String response = authService.register(req);

        assertEquals("User registered successfully.", response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRefreshTokenValid() {
        User user = new User();
        user.setUsername("john");
        RefreshToken token = new RefreshToken(UUID.randomUUID().toString(), Instant.now().plusSeconds(60), user);

        when(refreshTokenRepository.findByToken(token.getToken())).thenReturn(Optional.of(token));
        when(jwtUtil.generateToken("john")).thenReturn("new-access-token");

        AuthResponse response = authService.refreshToken(token.getToken());

        assertEquals("new-access-token", response.getAccessToken());
        assertEquals(token.getToken(), response.getRefreshToken());
    }
}
