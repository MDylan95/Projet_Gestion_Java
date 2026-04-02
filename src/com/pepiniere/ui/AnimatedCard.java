package com.pepiniere.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Carte avec animations slide-in et hover effects
 */
public class AnimatedCard extends JPanel {
    
    private float scale = 1.0f;
    private float targetScale = 1.0f;
    private Timer animationTimer;
    private JPanel contentPanel;
    
    public AnimatedCard(JPanel content) {
        this.contentPanel = content;
        setOpaque(false);
        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);
        
        // Hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                targetScale = 1.05f;
                startAnimation();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                targetScale = 1.0f;
                startAnimation();
            }
        });
    }
    
    /**
     * Démarre l'animation de scale
     */
    private void startAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
        }
        
        animationTimer = new Timer(16, e -> {
            // Interpolation linéaire vers la cible
            float diff = targetScale - scale;
            if (Math.abs(diff) > 0.001f) {
                scale += diff * 0.15f;
                repaint();
            } else {
                scale = targetScale;
                ((Timer) e.getSource()).stop();
            }
        });
        animationTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Appliquer la transformation de scale
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        g2.translate(centerX, centerY);
        g2.scale(scale, scale);
        g2.translate(-centerX, -centerY);
        
        // Ombre
        g2.setColor(new Color(0, 0, 0, 15));
        g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 12, 12);
        
        // Fond blanc
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 12, 12);
        
        // Bordure
        g2.setColor(UIStyles.BORDER_COLOR);
        g2.setStroke(new BasicStroke(1.0f));
        g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 12, 12);
        
        g2.dispose();
        super.paintComponent(g);
    }
}
