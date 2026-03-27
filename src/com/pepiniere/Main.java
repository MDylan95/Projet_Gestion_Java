package com.pepiniere;

import com.pepiniere.ui.LoginFrame;

import javax.swing.*;

/**
 * Point d'entrée de l'application Pépinière "Plein de Foin"
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
