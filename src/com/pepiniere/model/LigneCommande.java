package com.pepiniere.model;

/**
 * Classe représentant une ligne de commande
 */
public class LigneCommande {
    private int noCommande;
    private int noArticle;
    private int quantite;
    private String descriptionArticle;

    public LigneCommande() {}

    public LigneCommande(int noCommande, int noArticle, int quantite) {
        this.noCommande = noCommande;
        this.noArticle = noArticle;
        this.quantite = quantite;
    }

    public int getNoCommande() {
        return noCommande;
    }

    public void setNoCommande(int noCommande) {
        this.noCommande = noCommande;
    }

    public int getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(int noArticle) {
        this.noArticle = noArticle;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getDescriptionArticle() {
        return descriptionArticle;
    }

    public void setDescriptionArticle(String descriptionArticle) {
        this.descriptionArticle = descriptionArticle;
    }
}
