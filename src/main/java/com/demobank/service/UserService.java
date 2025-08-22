package com.demobank.service;

import com.demobank.entity.User;
import com.demobank.enums.UserRole;
import com.demobank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Double getBalance(String username) {
        User user = getUserByUsername(username);
        return user.getBalance();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAccountNumber(generateAccountNumber());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        if (user.getRole() == null) {
            user.setRole(UserRole.CUSTOMER);
        }

        return userRepository.save(user);
    }

    public void toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsActive(!user.getIsActive());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    private String generateAccountNumber() {
        String prefix = "ACC";
        Random random = new Random();
        String suffix = String.format("%010d", random.nextInt(1000000000));
        String accountNumber = prefix + suffix;

        // Ensure uniqueness
        while (userRepository.existsByAccountNumber(accountNumber)) {
            suffix = String.format("%010d", random.nextInt(1000000000));
            accountNumber = prefix + suffix;
        }

        return accountNumber;
    }
}