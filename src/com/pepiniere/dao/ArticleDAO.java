package com.pepiniere.dao;

import com.pepiniere.model.Article;
import com.pepiniere.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour la gestion des articles
 */
public class ArticleDAO {
    
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }
    
    public List<Article> findAll() throws SQLException {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM Article ORDER BY noArticle";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Article article = new Article();
                article.setNoArticle(rs.getInt(1));
                article.setDescription(rs.getString(2));
                article.setPrixUnitaire(rs.getBigDecimal(3));
                article.setQuantiteEnStock(rs.getInt(4));
                articles.add(article);
            }
        }
        return articles;
    }
    
    public Article findById(int noArticle) throws SQLException {
        String sql = "SELECT noArticle, description, prixUnitaire, quantiteEnStock FROM Article WHERE noArticle = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, noArticle);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Article article = new Article();
                    article.setNoArticle(rs.getInt("noArticle"));
                    article.setDescription(rs.getString("description"));
                    article.setPrixUnitaire(rs.getBigDecimal("prixUnitaire"));
                    article.setQuantiteEnStock(rs.getInt("quantiteEnStock"));
                    return article;
                }
            }
        }
        return null;
    }
    
    /**
     * Insère un nouvel article en utilisant la séquence Oracle seq_article
     */
    public int insert(Article article) throws SQLException {
        String sql = "INSERT INTO Article (noArticle, description, prixUnitaire, quantiteEnStock) " +
                     "VALUES (seq_article.NEXTVAL, ?, ?, ?)";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, new String[]{"noArticle"})) {
            pstmt.setString(1, article.getDescription());
            pstmt.setBigDecimal(2, article.getPrixUnitaire());
            pstmt.setInt(3, article.getQuantiteEnStock());
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }
    
    public void update(Article article) throws SQLException {
        String sql = "UPDATE Article SET description = ?, prixUnitaire = ?, quantiteEnStock = ? WHERE noArticle = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, article.getDescription());
            pstmt.setBigDecimal(2, article.getPrixUnitaire());
            pstmt.setInt(3, article.getQuantiteEnStock());
            pstmt.setInt(4, article.getNoArticle());
            pstmt.executeUpdate();
        }
    }
    
    public void delete(int noArticle) throws SQLException {
        String sql = "DELETE FROM Article WHERE noArticle = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, noArticle);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Appelle la procédure Oracle pour augmenter les prix
     */
    public void augmenterPrix(BigDecimal pourcentage) throws SQLException {
        String sql = "{call augmenter_prix(?)}";
        
        try (CallableStatement cstmt = getConnection().prepareCall(sql)) {
            cstmt.setBigDecimal(1, pourcentage);
            cstmt.execute();
        }
    }
    
    /**
     * Récupère la valeur totale du stock via la vue STVALEUR
     */
    public BigDecimal getValeurStock() throws SQLException {
        String sql = "SELECT * FROM STVALEUR";
        
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }
}
