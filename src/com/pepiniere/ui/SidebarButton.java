package com.pepiniere.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Bouton de sidebar avec icône et animation hover
 */
public class SidebarButton extends JButton {
    
    private String icon;
    private boolean isActive = false;
    private float hoverAlpha = 0.0f;
    private Timer hoverTimer;
    
    public SidebarButton(String icon, String text) {
        this.icon = icon;
        setText(icon + "  " + text);
        setFont(UIStyles.FONT_SIDEBAR);
        setForeground(UIStyles.TEXT_SECONDARY);
        setBackground(UIStyles.SIDEBAR_BG);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        setPreferredSize(new Dimension(UIStyles.SIDEBAR_WIDTH, 44));
        setMaximumSize(new Dimension(UIStyles.SIDEBAR_WIDTH, 44));
        
        // Hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startHoverAnimation(0.15f);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                startHoverAnimation(0.0f);
            }
        });
    }
    
    /**
     * Démarre l'animation de hover
     */
    private void startHoverAnimation(float targetAlpha) {
        if (hoverTimer != null) {
            hoverTimer.stop();
        }
        
        float finalTargetAlpha = targetAlpha;
        hoverTimer = new Timer(16, e -> {
            float diff = finalTargetAlpha - hoverAlpha;
            if (Math.abs(diff) > 0.001f) {
                hoverAlpha += diff * 0.2f;
                repaint();
            } else {
                hoverAlpha = finalTargetAlpha;
                ((Timer) e.getSource()).stop();
            }
        });
        hoverTimer.start();
    }
    
    /**
     * Définit si le bouton est actif
     */
    public void setActive(boolean active) {
        this.isActive = active;
        if (active) {
            setFont(UIStyles.FONT_SIDEBAR_ACTIVE);
            setForeground(UIStyles.PRIMARY_COLOR);
        } else {
            setFont(UIStyles.FONT_SIDEBAR);
            setForeground(UIStyles.TEXT_SECONDARY);
        }
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fond hover
        if (hoverAlpha > 0.01f || isActive) {
            Color bgColor = isActive ? UIStyles.SIDEBAR_ACTIVE_BG : UIStyles.SIDEBAR_HOVER_BG;
            g2.setColor(new Color(
                bgColor.getRed(),
                bgColor.getGreen(),
                bgColor.getBlue(),
                (int) (255 * (isActive ? 1.0f : hoverAlpha))
            ));
            g2.fillRoundRect(8, 4, getWidth() - 16, getHeight() - 8, 8, 8);
        }
        
        // Barre de sélection à gauche
        if (isActive) {
            g2.setColor(UIStyles.PRIMARY_COLOR);
            g2.fillRoundRect(0, 8, 4, getHeight() - 16, 2, 2);
        }
        
        g2.dispose();
        super.paintComponent(g);
    }
}
