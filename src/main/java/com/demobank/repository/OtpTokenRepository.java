package com.demobank.repository;

import com.demobank.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByUsernameAndOtpCodeAndIsUsedFalseAndExpiresAtAfter(
            String username, String otpCode, LocalDateTime currentTime);
    void deleteByUsernameAndIsUsedFalse(String username);
}