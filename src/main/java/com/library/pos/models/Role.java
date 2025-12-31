package com.library.pos.models;

/**
 * Enum representing user roles in the system.
 */
public enum Role {
    ADMIN("Admin"),
    MANAGER("Manager");
    
    private final String displayName;
    
    Role(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Convert a string to Role enum.
     * @param value the string value to convert
     * @return the corresponding Role, or null if not found
     */
    public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        return null;
    }
}
