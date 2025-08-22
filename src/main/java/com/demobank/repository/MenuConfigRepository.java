package com.demobank.repository;

import com.demobank.entity.MenuConfig;
import com.demobank.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuConfigRepository extends JpaRepository<MenuConfig, Long> {
    List<MenuConfig> findByIsActiveTrueAndRequiredRoleOrderByMenuOrder(UserRole role);
    List<MenuConfig> findByIsActiveTrueOrderByMenuOrder();
}