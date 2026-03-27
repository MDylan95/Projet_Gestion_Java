package com.pepiniere.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class TestDB {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            DatabaseMetaData metaData = conn.getMetaData();
            
            System.out.println("=== COLONNES DE LA TABLE CLIENT ===");
            ResultSet rs = metaData.getColumns(null, null, "CLIENT", null);
            while (rs.next()) {
                System.out.println("- " + rs.getString("COLUMN_NAME"));
            }
            
            System.out.println("\n=== COLONNES DE LA TABLE ARTICLE ===");
            ResultSet rs2 = metaData.getColumns(null, null, "ARTICLE", null);
            while (rs2.next()) {
                System.out.println("- " + rs2.getString("COLUMN_NAME"));
            }
            
            System.out.println("\n=== COLONNES DE LA TABLE LIGNECOMMANDE ===");
            ResultSet rs3 = metaData.getColumns(null, null, "LIGNECOMMANDE", null);
            while (rs3.next()) {
                System.out.println("- " + rs3.getString("COLUMN_NAME"));
            }
            
            System.out.println("\n=== COLONNES DE LA TABLE DETAILLIVRAISON ===");
            ResultSet rs4 = metaData.getColumns(null, null, "DETAILLIVRAISON", null);
            while (rs4.next()) {
                System.out.println("- " + rs4.getString("COLUMN_NAME"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
