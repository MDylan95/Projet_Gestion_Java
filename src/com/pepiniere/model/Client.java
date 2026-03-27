package com.pepiniere.model;

/**
 * Classe représentant un client de la pépinière
 */
public class Client {
    private int noClient;
    private String nomClient;
    private String noTelephone;

    public Client() {}

    public Client(int noClient, String nomClient, String noTelephone) {
        this.noClient = noClient;
        this.nomClient = nomClient;
        this.noTelephone = noTelephone;
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

    public String getNoTelephone() {
        return noTelephone;
    }

    public void setNoTelephone(String noTelephone) {
        this.noTelephone = noTelephone;
    }

    @Override
    public String toString() {
        return noClient + " - " + nomClient;
    }
}
