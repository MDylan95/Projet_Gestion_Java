package com.pepiniere.dao;

import com.pepiniere.model.Commande;
import com.pepiniere.model.LigneCommande;
import com.pepiniere.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des commandes
 */
public class CommandeDAO {
    
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }
    
    public List<Commande> findAll() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT c.noCommande, c.dateCommande, c.noClient, cl.nomClient " +
                     "FROM Commande c " +
                     "LEFT JOIN Client cl ON c.noClient = cl.noClient " +
                     "ORDER BY c.noCommande";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Commande commande = new Commande();
                commande.setNoCommande(rs.getInt("noCommande"));
                commande.setDateCommande(rs.getDate("dateCommande"));
                commande.setNoClient(rs.getInt("noClient"));
                commande.setNomClient(rs.getString("nomClient"));
                commandes.add(commande);
            }
        }
        return commandes;
    }
    
    public Commande findById(int noCommande) throws SQLException {
        String sql = "SELECT c.noCommande, c.dateCommande, c.noClient, cl.nomClient " +
                     "FROM Commande c " +
                     "LEFT JOIN Client cl ON c.noClient = cl.noClient " +
                     "WHERE c.noCommande = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, noCommande);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Commande commande = new Commande();
                    commande.setNoCommande(rs.getInt("noCommande"));
                    commande.setDateCommande(rs.getDate("dateCommande"));
                    commande.setNoClient(rs.getInt("noClient"));
                    commande.setNomClient(rs.getString("nomClient"));
                    commande.setLignes(findLignesCommande(noCommande));
                    return commande;
                }
            }
        }
        return null;
    }
    
    public List<LigneCommande> findLignesCommande(int noCommande) throws SQLException {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT lc.noCommande, lc.noArticle, lc.quantite, a.description " +
                     "FROM LigneCommande lc " +
                     "LEFT JOIN Article a ON lc.noArticle = a.noArticle " +
                     "WHERE lc.noCommande = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, noCommande);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LigneCommande ligne = new LigneCommande();
                    ligne.setNoCommande(rs.getInt("noCommande"));
                    ligne.setNoArticle(rs.getInt("noArticle"));
                    ligne.setQuantite(rs.getInt("quantite"));
                    ligne.setDescriptionArticle(rs.getString("description"));
                    lignes.add(ligne);
                }
            }
        }
        return lignes;
    }
    
    public void insert(Commande commande) throws SQLException {
        String sql = "INSERT INTO Commande (NOCOMMANDE, DATECOMMANDE, NOCLIENT) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, commande.getNoCommande());
            pstmt.setDate(2, commande.getDateCommande());
            pstmt.setInt(3, commande.getNoClient());
            pstmt.executeUpdate();
        }
    }
    
    public void insertLigneCommande(LigneCommande ligne) throws SQLException {
        String sql = "INSERT INTO LigneCommande (NOCOMMANDE, NOARTICLE, QUANTITE) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, ligne.getNoCommande());
            pstmt.setInt(2, ligne.getNoArticle());
            pstmt.setInt(3, ligne.getQuantite());
            pstmt.executeUpdate();
        }
    }
    
    public void delete(int noCommande) throws SQLException {
        String sqlLignes = "DELETE FROM LigneCommande WHERE NOCOMMANDE = ?";
        String sqlCommande = "DELETE FROM Commande WHERE NOCOMMANDE = ?";
        
        try (PreparedStatement pstmt1 = getConnection().prepareStatement(sqlLignes);
             PreparedStatement pstmt2 = getConnection().prepareStatement(sqlCommande)) {
            pstmt1.setInt(1, noCommande);
            pstmt1.executeUpdate();
            
            pstmt2.setInt(1, noCommande);
            pstmt2.executeUpdate();
        }
    }
    
    /**
     * Appelle la fonction Oracle montant_client pour calculer le montant total d'un client
     */
    public BigDecimal getMontantClient(int noClient) throws SQLException {
        String sql = "{? = call montant_client(?)}";
        
        try (CallableStatement cstmt = getConnection().prepareCall(sql)) {
            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.setInt(2, noClient);
            cstmt.execute();
            return cstmt.getBigDecimal(1);
        }
    }
}
