package com.pepiniere.util;

import com.pepiniere.model.UserRole;

/**
 * Gestionnaire de session utilisateur
 */
public class SessionManager {
    
    private static SessionManager instance;
    private UserRole currentRole;
    private String username;
    
    private SessionManager() {}
    
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void login(String username, UserRole role) {
        this.username = username;
        this.currentRole = role;
    }
    
    public void logout() {
        this.username = null;
        this.currentRole = null;
        DatabaseConnection.getInstance().closeConnection();
    }
    
    public UserRole getCurrentRole() {
        return currentRole;
    }
    
    public String getUsername() {
        return username;
    }
    
    public boolean isLoggedIn() {
        return currentRole != null;
    }
    
    public boolean isResponsable() {
        return currentRole == UserRole.RESPONSABLE;
    }
    
    public boolean isMagasinier() {
        return currentRole == UserRole.MAGASINIER;
    }
}
