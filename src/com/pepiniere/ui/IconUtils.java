package com.pepiniere.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Utilitaire pour créer des icônes textuelles stylisées
 * Utilise des caractères Unicode pour les icônes
 */
public class IconUtils {
    
    // Icônes d'actions (caractères simples et universels)
    public static final String ICON_ADD = "[+]";
    public static final String ICON_EDIT = "[E]";
    public static final String ICON_DELETE = "[X]";
    public static final String ICON_SAVE = "[S]";
    public static final String ICON_CANCEL = "[C]";
    public static final String ICON_SEARCH = "[?]";
    public static final String ICON_FILTER = "[F]";
    public static final String ICON_REFRESH = "[R]";
    public static final String ICON_DOWNLOAD = "[↓]";
    public static final String ICON_UPLOAD = "[↑]";
    
    // Icônes de statut
    public static final String ICON_SUCCESS = "✓";
    public static final String ICON_ERROR = "✕";
    public static final String ICON_WARNING = "!";
    public static final String ICON_INFO = "i";
    
    // Icônes de navigation
    public static final String ICON_HOME = "[H]";
    public static final String ICON_BACK = "[<]";
    public static final String ICON_FORWARD = "[>]";
    public static final String ICON_MENU = "[≡]";
    public static final String ICON_CLOSE = "[X]";
    
    // Icônes de données
    public static final String ICON_ARTICLE = "[A]";
    public static final String ICON_CLIENT = "[C]";
    public static final String ICON_COMMANDE = "[O]";
    public static final String ICON_LIVRAISON = "[L]";
    public static final String ICON_STOCK = "[S]";
    
    /**
     * Crée un bouton avec icône et texte
     */
    public static JButton createIconButton(String icon, String text, Runnable action) {
        JButton btn = new JButton(icon + " " + text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.addActionListener(e -> action.run());
        return btn;
    }
    
    /**
     * Crée un label avec icône et texte
     */
    public static JLabel createIconLabel(String icon, String text) {
        JLabel lbl = new JLabel(icon + " " + text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lbl;
    }
    
    /**
     * Crée un label avec icône colorée
     */
    public static JLabel createColoredIconLabel(String icon, String text, Color color) {
        JLabel lbl = createIconLabel(icon, text);
        lbl.setForeground(color);
        return lbl;
    }
}
