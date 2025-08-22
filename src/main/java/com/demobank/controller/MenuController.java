package com.demobank.controller;

import com.demobank.entity.MenuConfig;
import com.demobank.service.MenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Tag(name = "Menu", description = "Menu operations")
@CrossOrigin(origins = "*")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuConfig>> getUserMenus(Principal principal) {
        List<MenuConfig> menus = menuService.getUserMenus(principal.getName());
        return ResponseEntity.ok(menus);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MenuConfig>> getAllMenus() {
        List<MenuConfig> menus = menuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuConfig> createMenu(@RequestBody MenuConfig menuConfig) {
        MenuConfig created = menuService.createMenu(menuConfig);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuConfig> updateMenu(@PathVariable Long id, @RequestBody MenuConfig menuConfig) {
        MenuConfig updated = menuService.updateMenu(id, menuConfig);
        return ResponseEntity.ok(updated);
    }
}
