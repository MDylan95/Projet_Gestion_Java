package com.pepiniere.model;

import java.math.BigDecimal;

/**
 * Classe représentant un article de la pépinière
 */
public class Article {
    private int noArticle;
    private String description;
    private BigDecimal prixUnitaire;
    private int quantiteEnStock;

    public Article() {}

    public Article(int noArticle, String description, BigDecimal prixUnitaire, int quantiteEnStock) {
        this.noArticle = noArticle;
        this.description = description;
        this.prixUnitaire = prixUnitaire;
        this.quantiteEnStock = quantiteEnStock;
    }

    public int getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(int noArticle) {
        this.noArticle = noArticle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public int getQuantiteEnStock() {
        return quantiteEnStock;
    }

    public void setQuantiteEnStock(int quantiteEnStock) {
        this.quantiteEnStock = quantiteEnStock;
    }

    @Override
    public String toString() {
        return noArticle + " - " + description;
    }
}
