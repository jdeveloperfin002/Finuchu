package com.demobank.controller;

import com.demobank.dto.ApiResponse;
import com.demobank.util.SessionManager;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Logout", description = "Logout operations")
@CrossOrigin(origins = "*")
public class LogoutController {

    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(Principal principal) {
        if (principal != null) {
            sessionManager.removeSession(principal.getName());
        }

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Logged out successfully")
                .data("Session terminated")
                .status(200)
                .build());
    }
}