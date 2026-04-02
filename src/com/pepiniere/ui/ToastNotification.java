package com.pepiniere.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Notification toast non-bloquante qui s'affiche en bas à droite
 * et disparaît automatiquement après quelques secondes.
 * Remplace les JOptionPane intrusifs pour les messages de succès/info.
 */
public class ToastNotification extends JWindow {

    public enum Type { SUCCESS, ERROR, INFO, WARNING }

    private static final int WIDTH  = 320;
    private static final int HEIGHT = 64;
    private static final int MARGIN = 20;
    private static final int DURATION_MS = 3000;
    private static final int FADE_STEPS  = 30;
    private static final int FADE_DELAY  = 16; // ~60fps

    private float opacity = 0f;
    private final Type type;
    private final String message;

    private ToastNotification(Window owner, String message, Type type) {
        super(owner);
        this.message = message;
        this.type    = type;
        setSize(WIDTH, HEIGHT);
        setOpacity(0f);
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                // Ombre
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(4, 4, getWidth() - 4, getHeight() - 4, 16, 16);
                // Fond
                g2.setColor(backgroundColor());
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 16, 16);
                // Barre colorée gauche
                g2.setColor(accentColor());
                g2.fillRoundRect(0, 0, 5, getHeight() - 4, 4, 4);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(12, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        // Icône
        JLabel icon = new JLabel(iconText());
        icon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        icon.setForeground(accentColor());

        // Message
        JLabel lbl = new JLabel("<html><body style='width:210px'>" + message + "</body></html>");
        lbl.setFont(UIStyles.FONT_BODY);
        lbl.setForeground(UIStyles.TEXT_PRIMARY);

        panel.add(icon, BorderLayout.WEST);
        panel.add(lbl,  BorderLayout.CENTER);
        add(panel);
    }

    private Color backgroundColor() {
        return switch (type) {
            case SUCCESS -> new Color(236, 252, 243);
            case ERROR   -> new Color(255, 235, 235);
            case WARNING -> new Color(255, 251, 230);
            default      -> Color.WHITE;
        };
    }

    private Color accentColor() {
        return switch (type) {
            case SUCCESS -> UIStyles.SUCCESS_COLOR;
            case ERROR   -> UIStyles.DANGER_COLOR;
            case WARNING -> UIStyles.WARNING_COLOR;
            default      -> UIStyles.SECONDARY_COLOR;
        };
    }

    private String iconText() {
        return switch (type) {
            case SUCCESS -> "✓";
            case ERROR   -> "!";
            case WARNING -> "!";
            default      -> "i";
        };
    }

    /** Positionne le toast en bas à droite de la fenêtre propriétaire */
    private void position(Window owner) {
        Rectangle screen = owner != null
            ? owner.getBounds()
            : GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        int x = screen.x + screen.width  - WIDTH  - MARGIN;
        int y = screen.y + screen.height - HEIGHT - MARGIN;
        setLocation(x, y);
    }

    private void showAndFade() {
        setVisible(true);
        // Fade-in
        Timer fadeIn = new Timer(FADE_DELAY, null);
        fadeIn.addActionListener(e -> {
            opacity = Math.min(1f, opacity + 1f / FADE_STEPS);
            setOpacity(opacity);
            if (opacity >= 1f) fadeIn.stop();
        });
        fadeIn.start();

        // Attente puis fade-out
        Timer hold = new Timer(DURATION_MS, e -> {
            Timer fadeOut = new Timer(FADE_DELAY, null);
            fadeOut.addActionListener(ev -> {
                opacity = Math.max(0f, opacity - 1f / FADE_STEPS);
                setOpacity(opacity);
                if (opacity <= 0f) {
                    fadeOut.stop();
                    dispose();
                }
            });
            fadeOut.start();
        });
        hold.setRepeats(false);
        hold.start();
    }

    // ─── API publique ────────────────────────────────────────────────────────

    public static void show(Window owner, String message, Type type) {
        SwingUtilities.invokeLater(() -> {
            ToastNotification toast = new ToastNotification(owner, message, type);
            toast.position(owner);
            toast.showAndFade();
        });
    }

    public static void success(Window owner, String message) {
        show(owner, message, Type.SUCCESS);
    }

    public static void error(Window owner, String message) {
        show(owner, message, Type.ERROR);
    }

    public static void info(Window owner, String message) {
        show(owner, message, Type.INFO);
    }

    public static void warning(Window owner, String message) {
        show(owner, message, Type.WARNING);
    }
}
