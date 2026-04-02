package com.pepiniere.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

/**
 * Spinner de chargement animé à afficher pendant les requêtes Oracle.
 *
 * Usage :
 *   LoadingSpinner spinner = new LoadingSpinner("Connexion en cours...");
 *   spinner.show(parentFrame);
 *   // ... opération longue ...
 *   spinner.hide();
 */
public class LoadingSpinner extends JDialog {

    private Timer animTimer;
    private int   angle = 0;

    public LoadingSpinner(Window owner, String message) {
        super(owner, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(220, 130);
        setLocationRelativeTo(owner);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                // Fond blanc arrondi avec légère ombre
                g2.setColor(new Color(0, 0, 0, 25));
                g2.fillRoundRect(4, 4, getWidth() - 5, getHeight() - 5, 20, 20);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 20, 20);

                // Arc de chargement
                int cx = getWidth() / 2;
                int r  = 24;
                int x  = cx - r - 2;
                int y  = 18;
                g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                // Piste grise
                g2.setColor(new Color(232, 236, 241));
                g2.drawOval(x, y, r * 2, r * 2);
                // Arc animé vert
                g2.setColor(UIStyles.PRIMARY_COLOR);
                g2.draw(new Arc2D.Double(x, y, r * 2, r * 2, angle, 270, Arc2D.OPEN));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(78, 16, 16, 16));

        JLabel lbl = new JLabel(message, SwingConstants.CENTER);
        lbl.setFont(UIStyles.FONT_BODY);
        lbl.setForeground(UIStyles.TEXT_SECONDARY);
        panel.add(lbl, BorderLayout.CENTER);
        add(panel);

        animTimer = new Timer(30, e -> {
            angle = (angle + 8) % 360;
            panel.repaint();
        });
    }

    /** Affiche le spinner dans un thread séparé pour ne pas bloquer l'EDT */
    public void showSpinner() {
        animTimer.start();
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    /** Cache et détruit le spinner */
    public void hideSpinner() {
        animTimer.stop();
        SwingUtilities.invokeLater(this::dispose);
    }

    /**
     * Utilitaire : exécute une tâche longue avec spinner puis callback sur l'EDT.
     *
     * Exemple :
     *   LoadingSpinner.run(frame, "Chargement...", () -> dao.findAll(), result -> {
     *       // utiliser result
     *   }, error -> ToastNotification.error(frame, error.getMessage()));
     */
    public static <T> void run(Window owner, String message,
                               ThrowingSupplier<T> task,
                               java.util.function.Consumer<T> onSuccess,
                               java.util.function.Consumer<Exception> onError) {
        LoadingSpinner spinner = new LoadingSpinner(owner, message);
        SwingWorker<T, Void> worker = new SwingWorker<>() {
            @Override protected T doInBackground() throws Exception { return task.get(); }
            @Override protected void done() {
                spinner.hideSpinner();
                try {
                    onSuccess.accept(get());
                } catch (Exception e) {
                    onError.accept(e);
                }
            }
        };
        worker.execute();
        spinner.showSpinner();
    }

    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }
}
