package com.demobank.entity;

import com.demobank.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MENU_CONFIG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_seq")
    @SequenceGenerator(name = "menu_seq", sequenceName = "MENU_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_icon")
    private String menuIcon;

    @Column(name = "menu_url")
    private String menuUrl;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "menu_order")
    private Integer menuOrder;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    private UserRole requiredRole;
}