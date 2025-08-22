package com.demobank.service;

import com.demobank.entity.MenuConfig;
import com.demobank.entity.User;
import com.demobank.repository.MenuConfigRepository;
import com.demobank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuConfigRepository menuConfigRepository;

    @Autowired
    private UserRepository userRepository;

    public List<MenuConfig> getUserMenus(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return menuConfigRepository.findByIsActiveTrueAndRequiredRoleOrderByMenuOrder(user.getRole());
    }

    public List<MenuConfig> getAllMenus() {
        return menuConfigRepository.findByIsActiveTrueOrderByMenuOrder();
    }

    public MenuConfig createMenu(MenuConfig menuConfig) {
        return menuConfigRepository.save(menuConfig);
    }

    public MenuConfig updateMenu(Long id, MenuConfig menuConfig) {
        MenuConfig existing = menuConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        existing.setMenuName(menuConfig.getMenuName());
        existing.setMenuIcon(menuConfig.getMenuIcon());
        existing.setMenuUrl(menuConfig.getMenuUrl());
        existing.setParentId(menuConfig.getParentId());
        existing.setMenuOrder(menuConfig.getMenuOrder());
        existing.setIsActive(menuConfig.getIsActive());
        existing.setRequiredRole(menuConfig.getRequiredRole());

        return menuConfigRepository.save(existing);
    }
}