package com.demobank.config;

import com.demobank.entity.MenuConfig;
import com.demobank.entity.User;
import com.demobank.enums.UserRole;
import com.demobank.repository.MenuConfigRepository;
import com.demobank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuConfigRepository menuConfigRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@demobank.com");
            admin.setFullName("System Administrator");
            admin.setPhoneNumber("1234567890");
            admin.setAccountNumber("ACC0000000001");
            admin.setBalance(1000000.0);
            admin.setRole(UserRole.ADMIN);
            admin.setIsActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            userRepository.save(admin);
        }

        // Create sample customer if not exists
        if (!userRepository.existsByUsername("customer")) {
            User customer = new User();
            customer.setUsername("customer");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setEmail("customer@demobank.com");
            customer.setFullName("John Doe");
            customer.setPhoneNumber("9876543210");
            customer.setAccountNumber("ACC0000000002");
            customer.setBalance(50000.0);
            customer.setRole(UserRole.CUSTOMER);
            customer.setIsActive(true);
            customer.setCreatedAt(LocalDateTime.now());
            customer.setUpdatedAt(LocalDateTime.now());
            userRepository.save(customer);
        }

        // Initialize menu items
        initializeMenus();
    }

    private void initializeMenus() {
        if (menuConfigRepository.count() == 0) {
            // Dashboard
            MenuConfig dashboard = new MenuConfig();
            dashboard.setMenuName("Dashboard");
            dashboard.setMenuIcon("fas fa-tachometer-alt");
            dashboard.setMenuUrl("/dashboard");
            dashboard.setParentId(null);
            dashboard.setMenuOrder(1);
            dashboard.setIsActive(true);
            dashboard.setRequiredRole(UserRole.CUSTOMER);
            menuConfigRepository.save(dashboard);

            // Accounts
            MenuConfig accounts = new MenuConfig();
            accounts.setMenuName("Accounts");
            accounts.setMenuIcon("fas fa-university");
            accounts.setMenuUrl("/accounts");
            accounts.setParentId(null);
            accounts.setMenuOrder(2);
            accounts.setIsActive(true);
            accounts.setRequiredRole(UserRole.CUSTOMER);
            menuConfigRepository.save(accounts);

            // Transactions
            MenuConfig transactions = new MenuConfig();
            transactions.setMenuName("Transactions");
            transactions.setMenuIcon("fas fa-exchange-alt");
            transactions.setMenuUrl("/transactions");
            transactions.setParentId(null);
            transactions.setMenuOrder(3);
            transactions.setIsActive(true);
            transactions.setRequiredRole(UserRole.CUSTOMER);
            menuConfigRepository.save(transactions);

            // Transfer Money
            MenuConfig transfer = new MenuConfig();
            transfer.setMenuName("Transfer Money");
            transfer.setMenuIcon("fas fa-paper-plane");
            transfer.setMenuUrl("/transfer");
            transfer.setParentId(null);
            transfer.setMenuOrder(4);
            transfer.setIsActive(true);
            transfer.setRequiredRole(UserRole.CUSTOMER);
            menuConfigRepository.save(transfer);

            // Admin Panel
            MenuConfig adminPanel = new MenuConfig();
            adminPanel.setMenuName("Admin Panel");
            adminPanel.setMenuIcon("fas fa-cog");
            adminPanel.setMenuUrl("/admin");
            adminPanel.setParentId(null);
            adminPanel.setMenuOrder(5);
            adminPanel.setIsActive(true);
            adminPanel.setRequiredRole(UserRole.ADMIN);
            menuConfigRepository.save(adminPanel);

            // User Management (sub-menu of Admin Panel)
            MenuConfig userMgmt = new MenuConfig();
            userMgmt.setMenuName("User Management");
            userMgmt.setMenuIcon("fas fa-users");
            userMgmt.setMenuUrl("/admin/users");
            userMgmt.setParentId(adminPanel.getId());
            userMgmt.setMenuOrder(1);
            userMgmt.setIsActive(true);
            userMgmt.setRequiredRole(UserRole.ADMIN);
            menuConfigRepository.save(userMgmt);
        }
    }
}