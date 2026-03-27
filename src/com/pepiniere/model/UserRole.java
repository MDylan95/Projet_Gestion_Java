package com.pepiniere.model;

/**
 * Enumération des rôles utilisateur
 */
public enum UserRole {
    MAGASINIER("Magasinier"),
    RESPONSABLE("Responsable");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
