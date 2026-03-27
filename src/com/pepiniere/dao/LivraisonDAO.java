package com.pepiniere.dao;

import com.pepiniere.model.DetailLivraison;
import com.pepiniere.model.Livraison;
import com.pepiniere.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des livraisons
 */
public class LivraisonDAO {
    
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }
    
    public List<Livraison> findAll() throws SQLException {
        List<Livraison> livraisons = new ArrayList<>();
        String sql = "SELECT noLivraison, dateLivraison FROM Livraison ORDER BY noLivraison";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Livraison livraison = new Livraison();
                livraison.setNoLivraison(rs.getInt("noLivraison"));
                livraison.setDateLivraison(rs.getDate("dateLivraison"));
                livraisons.add(livraison);
            }
        }
        return livraisons;
    }
    
    public Livraison findById(int noLivraison) throws SQLException {
        String sql = "SELECT noLivraison, dateLivraison FROM Livraison WHERE noLivraison = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, noLivraison);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Livraison livraison = new Livraison();
                    livraison.setNoLivraison(rs.getInt("noLivraison"));
                    livraison.setDateLivraison(rs.getDate("dateLivraison"));
                    livraison.setDetails(findDetailsLivraison(noLivraison));
                    return livraison;
                }
            }
        }
        return null;
    }
    
    public List<DetailLivraison> findDetailsLivraison(int noLivraison) throws SQLException {
        List<DetailLivraison> details = new ArrayList<>();
        String sql = "SELECT noLivraison, noCommande, noArticle, quantiteLivree " +
                     "FROM DetailLivraison WHERE noLivraison = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, noLivraison);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DetailLivraison detail = new DetailLivraison();
                    detail.setNoLivraison(rs.getInt("noLivraison"));
                    detail.setNoCommande(rs.getInt("noCommande"));
                    detail.setNoArticle(rs.getInt("noArticle"));
                    detail.setQuantiteLivree(rs.getInt("quantiteLivree"));
                    details.add(detail);
                }
            }
        }
        return details;
    }
    
    public void insert(Livraison livraison) throws SQLException {
        String sql = "INSERT INTO Livraison (noLivraison, dateLivraison) VALUES (?, ?)";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, livraison.getNoLivraison());
            pstmt.setDate(2, livraison.getDateLivraison());
            pstmt.executeUpdate();
        }
    }
    
    public void insertDetailLivraison(DetailLivraison detail) throws SQLException {
        String sql = "INSERT INTO DetailLivraison (noLivraison, noCommande, noArticle, quantiteLivree) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, detail.getNoLivraison());
            pstmt.setInt(2, detail.getNoCommande());
            pstmt.setInt(3, detail.getNoArticle());
            pstmt.setInt(4, detail.getQuantiteLivree());
            pstmt.executeUpdate();
        }
    }
    
    public void delete(int noLivraison) throws SQLException {
        String sqlDetails = "DELETE FROM DetailLivraison WHERE noLivraison = ?";
        String sqlLivraison = "DELETE FROM Livraison WHERE noLivraison = ?";
        
        try (PreparedStatement pstmt1 = getConnection().prepareStatement(sqlDetails);
             PreparedStatement pstmt2 = getConnection().prepareStatement(sqlLivraison)) {
            pstmt1.setInt(1, noLivraison);
            pstmt1.executeUpdate();
            
            pstmt2.setInt(1, noLivraison);
            pstmt2.executeUpdate();
        }
    }
}
