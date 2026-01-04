package com.library.pos.models;

/**
 * Enum representing user roles in the system.
 */
public enum UserRole {
    ADMIN("ADMIN"),
    MANAGER("MANAGER");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Convert string to UserRole enum.
     * 
     * @param role String representation of role
     * @return UserRole enum value
     */
    public static UserRole fromString(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.value.equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }
}
