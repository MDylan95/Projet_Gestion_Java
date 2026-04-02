package com.pepiniere.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Panneau conteneur qui applique un effet de fondu (fade-in)
 * à chaque fois qu'un nouveau sous-panneau est affiché.
 *
 * Remplace le contentPanel standard dans MainFrame.
 *
 * Usage :
 *   FadePanel content = new FadePanel();
 *   content.showWithFade(myPanel);   // remplace removeAll/add/revalidate
 */
public class FadePanel extends JPanel {

    private JPanel  current;
    private float   alpha      = 1f;
    private Timer   fadeTimer;
    private boolean fading     = false;

    private static final int STEPS    = 18;   // nombre de pas
    private static final int DELAY_MS = 14;   // ~70fps

    public FadePanel() {
        super(new BorderLayout());
        setBackground(UIStyles.BACKGROUND_COLOR);
        setOpaque(true);
    }

    /** Affiche newPanel avec un fondu entrant */
    public void showWithFade(JPanel newPanel) {
        if (fading) {
            // Si une animation est déjà en cours, on remplace directement
            if (fadeTimer != null) fadeTimer.stop();
            swap(newPanel);
            alpha = 1f;
            fading = false;
            repaint();
            return;
        }

        fading = true;
        alpha  = 0f;
        swap(newPanel);

        fadeTimer = new Timer(DELAY_MS, null);
        fadeTimer.addActionListener(e -> {
            alpha = Math.min(1f, alpha + 1f / STEPS);
            repaint();
            if (alpha >= 1f) {
                fadeTimer.stop();
                fading = false;
            }
        });
        fadeTimer.start();
    }

    private void swap(JPanel newPanel) {
        removeAll();
        current = newPanel;
        if (newPanel != null) add(newPanel, BorderLayout.CENTER);
        revalidate();
    }

    @Override
    protected void paintChildren(Graphics g) {
        if (fading && alpha < 1f) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.paintChildren(g2);
            g2.dispose();
        } else {
            super.paintChildren(g);
        }
    }
}
