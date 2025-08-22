package com.demobank.controller;

import com.demobank.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
@Tag(name = "Public", description = "Public operations")
@CrossOrigin(origins = "*")
public class PublicController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Demo Bank API is running")
                .data("OK")
                .status(200)
                .build());
    }

    @GetMapping("/contact")
    public ResponseEntity<ApiResponse<String>> getContactInfo() {
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Contact information")
                .data("Demo Bank - Customer Service: 1-800-DEMO-BANK")
                .status(200)
                .build());
    }
}