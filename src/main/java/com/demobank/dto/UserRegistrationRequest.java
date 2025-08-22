package com.demobank.dto;

import com.demobank.enums.UserRole;
import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private UserRole role;
}