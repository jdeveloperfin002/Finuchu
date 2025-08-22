package com.demobank.service;

import com.demobank.dto.LoginRequest;
import com.demobank.dto.LoginResponse;
import com.demobank.dto.OtpVerificationRequest;
import com.demobank.entity.OtpToken;
import com.demobank.entity.User;
import com.demobank.repository.OtpTokenRepository;
import com.demobank.repository.UserRepository;
import com.demobank.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpTokenRepository otpTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Generate and send OTP
            String otpCode = generateOTP();
            saveOtpToken(request.getUsername(), otpCode);

            return LoginResponse.builder()
                    .success(true)
                    .message("OTP sent successfully")
                    .requiresOtp(true)
                    .build();

        } catch (Exception e) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Invalid credentials")
                    .requiresOtp(false)
                    .build();
        }
    }

    public LoginResponse verifyOtp(OtpVerificationRequest request) {
        Optional<OtpToken> otpTokenOpt = otpTokenRepository
                .findByUsernameAndOtpCodeAndIsUsedFalseAndExpiresAtAfter(
                        request.getUsername(), request.getOtpCode(), LocalDateTime.now());

        if (otpTokenOpt.isPresent()) {
            OtpToken otpToken = otpTokenOpt.get();
            otpToken.setIsUsed(true);
            otpTokenRepository.save(otpToken);

            // Generate JWT token
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String jwtToken = jwtTokenUtil.generateToken(userDetails);

            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

            return LoginResponse.builder()
                    .success(true)
                    .message("Login successful")
                    .token(jwtToken)
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .role(user.getRole().toString())
                    .accountNumber(user.getAccountNumber())
                    .requiresOtp(false)
                    .build();
        }

        return LoginResponse.builder()
                .success(false)
                .message("Invalid or expired OTP")
                .requiresOtp(true)
                .build();
    }

    private String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }

    private void saveOtpToken(String username, String otpCode) {
        // Delete existing unused OTP tokens for this user
        otpTokenRepository.deleteByUsernameAndIsUsedFalse(username);

        OtpToken otpToken = new OtpToken();
        otpToken.setUsername(username);
        otpToken.setOtpCode(otpCode);
        otpToken.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // 5 minutes expiry
        otpTokenRepository.save(otpToken);

        // Here you would integrate with SMS/Email service to send OTP
        System.out.println("OTP for " + username + ": " + otpCode);
    }
}