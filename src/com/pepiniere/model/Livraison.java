package com.pepiniere.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une livraison
 */
public class Livraison {
    private int noLivraison;
    private Date dateLivraison;
    private List<DetailLivraison> details;

    public Livraison() {
        this.details = new ArrayList<>();
    }

    public Livraison(int noLivraison, Date dateLivraison) {
        this.noLivraison = noLivraison;
        this.dateLivraison = dateLivraison;
        this.details = new ArrayList<>();
    }

    public int getNoLivraison() {
        return noLivraison;
    }

    public void setNoLivraison(int noLivraison) {
        this.noLivraison = noLivraison;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public List<DetailLivraison> getDetails() {
        return details;
    }

    public void setDetails(List<DetailLivraison> details) {
        this.details = details;
    }

    public void addDetail(DetailLivraison detail) {
        this.details.add(detail);
    }
}
