package com.pepiniere;

import com.pepiniere.ui.LoginFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Point d'entrée de l'application Pépinière "Plein de Foin"
 */
public class Main {

    public static void main(String[] args) {
        // --- FlatLaf : Look & Feel moderne (optionnel) ---
        boolean flatLafLoaded = false;
        try {
            Class<?> flatLafClass = Class.forName("com.formdev.flatlaf.FlatLightLaf");
            java.lang.reflect.Method setupMethod = flatLafClass.getMethod("setup");
            setupMethod.invoke(null);
            flatLafLoaded = true;

            // Personnalisation globale (équivalent d'un CSS global)
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.width", 8);
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("Component.innerFocusWidth", 0);
            UIManager.put("Table.rowHeight", 38);
            UIManager.put("TableHeader.height", 44);
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("ToolTip.background", new Color(44, 62, 80));
            UIManager.put("ToolTip.foreground", Color.WHITE);

        } catch (Exception e) {
            // FlatLaf non disponible, utiliser le look & feel système
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
