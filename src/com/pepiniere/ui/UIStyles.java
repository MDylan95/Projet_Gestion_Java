package com.pepiniere.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class UIStyles {
    
    // Palette inspirée Dasher - tons verts/menthe
    public static final Color PRIMARY_COLOR = new Color(52, 168, 83);
    public static final Color PRIMARY_DARK = new Color(39, 135, 66);
    public static final Color PRIMARY_LIGHT = new Color(232, 245, 233);
    public static final Color ACCENT_MINT = new Color(209, 243, 224);
    public static final Color SECONDARY_COLOR = new Color(66, 133, 244);
    public static final Color SUCCESS_COLOR = new Color(52, 168, 83);
    public static final Color DANGER_COLOR = new Color(234, 67, 53);
    public static final Color WARNING_COLOR = new Color(251, 188, 4);
    public static final Color ORANGE_ACCENT = new Color(255, 138, 101);
    
    // Backgrounds
    public static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color SIDEBAR_BG = new Color(255, 255, 255);
    public static final Color SIDEBAR_ACTIVE_BG = new Color(232, 245, 233);
    public static final Color SIDEBAR_HOVER_BG = new Color(245, 245, 245);
    
    // Text
    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    public static final Color TEXT_MUTED = new Color(170, 183, 184);
    public static final Color BORDER_COLOR = new Color(232, 236, 241);
    
    // Card accent colors for dashboard
    public static final Color CARD_GREEN = new Color(209, 243, 224);
    public static final Color CARD_BLUE = new Color(212, 230, 255);
    public static final Color CARD_ORANGE = new Color(255, 234, 210);
    public static final Color CARD_RED = new Color(255, 220, 218);
    
    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_STAT_NUMBER = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font FONT_STAT_LABEL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SIDEBAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SIDEBAR_ACTIVE = new Font("Segoe UI", Font.BOLD, 14);
    
    // Dimensions
    public static final int SIDEBAR_WIDTH = 220;
    public static final int CARD_RADIUS = 16;
    
    /**
     * Panneau avec coins arrondis et ombre portée
     */
    public static class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        private boolean drawShadow;
        
        public RoundedPanel(int radius, Color bgColor, boolean drawShadow) {
            this.radius = radius;
            this.bgColor = bgColor;
            this.drawShadow = drawShadow;
            setOpaque(false);
        }
        
        public RoundedPanel(int radius, Color bgColor) {
            this(radius, bgColor, true);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (drawShadow) {
                g2.setColor(new Color(0, 0, 0, 15));
                g2.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, radius, radius);
            }
            
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(PRIMARY_DARK.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(PRIMARY_DARK);
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(FONT_BODY);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }
    
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(SECONDARY_COLOR.darker());
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(FONT_BODY);
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }
    
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(DANGER_COLOR.darker());
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(FONT_BODY);
        button.setBackground(DANGER_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }
    
    public static JButton createOutlineButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(PRIMARY_LIGHT);
                } else {
                    g2.setColor(CARD_BACKGROUND);
                }
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setColor(PRIMARY_COLOR);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(FONT_BODY);
        button.setForeground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }
    
    /**
     * Bouton de sidebar inspiré Dasher
     */
    public static JButton createSidebarButton(String text, boolean active) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover() && !getBackground().equals(SIDEBAR_ACTIVE_BG)) {
                    g2.setColor(SIDEBAR_HOVER_BG);
                } else {
                    g2.setColor(getBackground());
                }
                g2.fillRoundRect(4, 2, getWidth() - 8, getHeight() - 4, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(active ? FONT_SIDEBAR_ACTIVE : FONT_SIDEBAR);
        button.setBackground(active ? SIDEBAR_ACTIVE_BG : SIDEBAR_BG);
        button.setForeground(active ? PRIMARY_COLOR : TEXT_SECONDARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 15));
        button.setMaximumSize(new Dimension(SIDEBAR_WIDTH, 44));
        button.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 44));
        return button;
    }
    
    public static JTextField createStyledTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hasFocus()) {
                    g2.setColor(PRIMARY_COLOR);
                    g2.setStroke(new BasicStroke(1.5f));
                } else {
                    g2.setColor(BORDER_COLOR);
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        field.setFont(FONT_BODY);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        field.setPreferredSize(new Dimension(280, 42));
        return field;
    }
    
    public static JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hasFocus()) {
                    g2.setColor(PRIMARY_COLOR);
                    g2.setStroke(new BasicStroke(1.5f));
                } else {
                    g2.setColor(BORDER_COLOR);
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        field.setFont(FONT_BODY);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        field.setPreferredSize(new Dimension(280, 42));
        return field;
    }
    
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    public static JLabel createHeadingLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_HEADING);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    public static JPanel createCard() {
        RoundedPanel panel = new RoundedPanel(CARD_RADIUS, CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }
    
    /**
     * Crée une carte de statistique style Dasher
     */
    public static JPanel createStatCard(String title, String value, String subtitle, Color accentColor) {
        RoundedPanel card = new RoundedPanel(CARD_RADIUS, CARD_BACKGROUND);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_STAT_LABEL);
        lblTitle.setForeground(TEXT_SECONDARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(FONT_STAT_NUMBER);
        lblValue.setForeground(TEXT_PRIMARY);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblValue.setBorder(BorderFactory.createEmptyBorder(6, 0, 4, 0));
        
        JLabel lblSubtitle = new JLabel(subtitle);
        lblSubtitle.setFont(FONT_SMALL);
        lblSubtitle.setForeground(PRIMARY_COLOR);
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(lblTitle);
        textPanel.add(lblValue);
        textPanel.add(lblSubtitle);
        
        // Icône colorée à droite
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, 48, 48, 14, 14);
                g2.dispose();
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(48, 48));
        
        JPanel iconWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        iconWrapper.setOpaque(false);
        iconWrapper.add(iconPanel);
        
        card.add(textPanel, BorderLayout.CENTER);
        card.add(iconWrapper, BorderLayout.EAST);
        
        return card;
    }
    
    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setBackground(CARD_BACKGROUND);
        table.setBorder(null);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(FONT_HEADING);
        table.getTableHeader().setBackground(new Color(250, 251, 252));
        table.getTableHeader().setForeground(TEXT_SECONDARY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        table.getTableHeader().setPreferredSize(new Dimension(0, 44));
        table.getTableHeader().setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 251, 252));
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
                return c;
            }
        });

        // Highlight de ligne au survol
        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            int lastRow = -1;
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != lastRow) {
                    lastRow = row;
                    table.repaint();
                }
            }
        });
    }
    
    /**
     * Style un JScrollPane avec coins arrondis
     */
    public static JScrollPane createStyledScrollPane(Component view) {
        JScrollPane sp = new JScrollPane(view);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(CARD_BACKGROUND);
        return sp;
    }
    
    public static void showSuccessMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Succes", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Erreur", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    public static boolean showConfirmDialog(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Confirmation",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
