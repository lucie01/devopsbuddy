package com.devopsbuddy.enums;

import lombok.Getter;

/**
 * Defines the possible roles.
 */
@Getter
public enum RolesEnum {
    BASIC(1, "ROLE_BASIC"),
    PRO(2, "ROLE_PRO"),
    ADMIN(3, "ROLE_ADMIN");

    private final int id;
    private final String roleName;

    RolesEnum(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
}
