package com.pepiniere.dao;

import com.pepiniere.model.Client;
import com.pepiniere.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des clients
 */
public class ClientDAO {
    
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }
    
    public List<Client> findAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Client ORDER BY noClient";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Client client = new Client();
                client.setNoClient(rs.getInt(1));
                client.setNomClient(rs.getString(2));
                client.setNoTelephone(rs.getString(3));
                clients.add(client);
            }
        }
        return clients;
    }
    
    public Client findById(int noClient) throws SQLException {
        String sql = "SELECT noClient, nomClient, noTelephone FROM Client WHERE noClient = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, noClient);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client();
                    client.setNoClient(rs.getInt("noClient"));
                    client.setNomClient(rs.getString("nomClient"));
                    client.setNoTelephone(rs.getString("noTelephone"));
                    return client;
                }
            }
        }
        return null;
    }
    
    public void insert(Client client) throws SQLException {
        String sql = "INSERT INTO Client (noClient, nomClient, noTelephone) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, client.getNoClient());
            pstmt.setString(2, client.getNomClient());
            pstmt.setString(3, client.getNoTelephone());
            pstmt.executeUpdate();
        }
    }
    
    public void update(Client client) throws SQLException {
        String sql = "UPDATE Client SET nomClient = ?, noTelephone = ? WHERE noClient = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, client.getNomClient());
            pstmt.setString(2, client.getNoTelephone());
            pstmt.setInt(3, client.getNoClient());
            pstmt.executeUpdate();
        }
    }
    
    public void delete(int noClient) throws SQLException {
        String sql = "DELETE FROM Client WHERE noClient = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, noClient);
            pstmt.executeUpdate();
        }
    }
}
