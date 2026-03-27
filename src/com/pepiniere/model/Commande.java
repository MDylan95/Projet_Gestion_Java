package com.pepiniere.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une commande
 */
public class Commande {
    private int noCommande;
    private Date dateCommande;
    private int noClient;
    private String nomClient;
    private List<LigneCommande> lignes;

    public Commande() {
        this.lignes = new ArrayList<>();
    }

    public Commande(int noCommande, Date dateCommande, int noClient) {
        this.noCommande = noCommande;
        this.dateCommande = dateCommande;
        this.noClient = noClient;
        this.lignes = new ArrayList<>();
    }

    public int getNoCommande() {
        return noCommande;
    }

    public void setNoCommande(int noCommande) {
        this.noCommande = noCommande;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public int getNoClient() {
        return noClient;
    }

    public void setNoClient(int noClient) {
        this.noClient = noClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public List<LigneCommande> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCommande> lignes) {
        this.lignes = lignes;
    }

    public void addLigne(LigneCommande ligne) {
        this.lignes.add(ligne);
    }
}
