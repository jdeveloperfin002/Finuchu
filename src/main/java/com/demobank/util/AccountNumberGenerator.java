package com.demobank.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AccountNumberGenerator {

    private static final String PREFIX = "ACC";
    private static final SecureRandom random = new SecureRandom();

    public String generateAccountNumber() {
        // Format: ACC + YYYYMMDD + 5 random digits
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%05d", random.nextInt(100000));
        return PREFIX + dateStr + randomStr;
    }

    public String generateReferenceNumber() {
        // Format: REF + timestamp + 4 random digits
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomStr = String.format("%04d", random.nextInt(10000));
        return "REF" + timestamp.substring(timestamp.length() - 8) + randomStr;
    }
}