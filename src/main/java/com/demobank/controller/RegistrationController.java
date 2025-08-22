package com.demobank.controller;

import com.demobank.dto.ApiResponse;
import com.demobank.dto.UserRegistrationRequest;
import com.demobank.entity.User;
import com.demobank.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Registration", description = "Registration operations")
@CrossOrigin(origins = "*")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody UserRegistrationRequest request) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setFullName(request.getFullName());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setRole(request.getRole());

            User createdUser = userService.createUser(user);
            createdUser.setPassword(null); // Don't send password back

            return ResponseEntity.ok(ApiResponse.<User>builder()
                    .success(true)
                    .message("User registered successfully")
                    .data(createdUser)
                    .status(200)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.<User>builder()
                    .success(false)
                    .message(e.getMessage())
                    .data(null)
                    .status(400)
                    .build());
        }
    }
}