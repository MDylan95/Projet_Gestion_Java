package com.pepiniere.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe singleton pour la gestion de la connexion à la base de données Oracle
 */
public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String DEFAULT_USER = "Dev";
    private static final String DEFAULT_PASSWORD = "Dev";
    
    private String currentUser;
    private String currentPassword;
    
    private DatabaseConnection() {}
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Établit une connexion à la base de données avec les identifiants par défaut
     */
    public Connection getConnection() throws SQLException {
        return getConnection(DEFAULT_USER, DEFAULT_PASSWORD);
    }
    
    /**
     * Établit une connexion à la base de données avec des identifiants spécifiques
     */
    public Connection getConnection(String user, String password) throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, user, password);
                this.currentUser = user;
                this.currentPassword = password;
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver Oracle JDBC non trouvé: " + e.getMessage());
            }
        }
        return connection;
    }
    
    /**
     * Teste la connexion avec les identifiants fournis
     */
    public boolean testConnection(String user, String password) {
        try {
            Class.forName(DRIVER);
            Connection testConn = DriverManager.getConnection(URL, user, password);
            testConn.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Ferme la connexion actuelle
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Reconnecte avec les identifiants actuels
     */
    public void reconnect() throws SQLException {
        closeConnection();
        getConnection(currentUser, currentPassword);
    }
    
    public String getCurrentUser() {
        return currentUser;
    }
}
