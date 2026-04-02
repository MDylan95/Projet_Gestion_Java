package com.pepiniere.ui;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire des raccourcis clavier pour l'application
 */
public class KeyboardShortcuts {
    
    private static final Map<String, Integer> SHORTCUTS = new HashMap<>();
    
    static {
        // Raccourcis globaux
        SHORTCUTS.put("new_article", KeyEvent.VK_N);
        SHORTCUTS.put("search", KeyEvent.VK_F);
        SHORTCUTS.put("save", KeyEvent.VK_S);
        SHORTCUTS.put("delete", KeyEvent.VK_D);
        SHORTCUTS.put("refresh", KeyEvent.VK_R);
        SHORTCUTS.put("logout", KeyEvent.VK_Q);
        SHORTCUTS.put("help", KeyEvent.VK_H);
    }
    
    /**
     * Enregistre un raccourci clavier global
     */
    public static void registerGlobalShortcut(JFrame frame, String action, int keyCode, int modifiers, Runnable callback) {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && 
                e.getKeyCode() == keyCode && 
                e.getModifiers() == modifiers) {
                callback.run();
                return true;
            }
            return false;
        });
    }
    
    /**
     * Enregistre un raccourci clavier pour un composant
     */
    public static void registerComponentShortcut(JComponent component, String action, int keyCode, int modifiers, Runnable callback) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
        component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, action);
        component.getActionMap().put(action, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                callback.run();
            }
        });
    }
    
    /**
     * Retourne le code clavier pour une action
     */
    public static Integer getShortcutKeyCode(String action) {
        return SHORTCUTS.get(action);
    }
    
    /**
     * Retourne la description du raccourci
     */
    public static String getShortcutDescription(String action) {
        Integer keyCode = SHORTCUTS.get(action);
        if (keyCode != null) {
            return "Ctrl+" + KeyEvent.getKeyText(keyCode);
        }
        return "";
    }
}
