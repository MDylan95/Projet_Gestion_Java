package com.pepiniere.ui;

import com.pepiniere.model.UserRole;
import com.pepiniere.util.SessionManager;
import com.pepiniere.dao.ArticleDAO;
import com.pepiniere.dao.ClientDAO;
import com.pepiniere.dao.CommandeDAO;
import com.pepiniere.dao.LivraisonDAO;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Fenetre principale avec sidebar et dashboard inspires Dasher
 */
public class MainFrame extends JFrame {
    
    private FadePanel contentPanel;
    private JPanel sidebarPanel;
    private List<JButton> sidebarButtons = new ArrayList<>();
    private String activeSection = "Accueil";
    
    public MainFrame() {
        initComponents();
        setupLayout();
    }
    
    private void initComponents() {
        setTitle("Pepiniere Plein de Foin - Gestion Commerciale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        
        contentPanel = new FadePanel();
    }
    
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        
        // --- Sidebar ---
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(UIStyles.SIDEBAR_BG);
        sidebarPanel.setPreferredSize(new Dimension(UIStyles.SIDEBAR_WIDTH, 0));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIStyles.BORDER_COLOR));
        
        buildSidebar();
        
        // --- Top bar ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIStyles.BORDER_COLOR));
        topBar.setPreferredSize(new Dimension(0, 56));
        
        SessionManager session = SessionManager.getInstance();
        
        JLabel lblPageTitle = new JLabel("  Tableau de bord");
        lblPageTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPageTitle.setForeground(UIStyles.TEXT_PRIMARY);
        lblPageTitle.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        userPanel.setOpaque(false);
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 16));
        
        // Avatar cercle avec initiale
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIStyles.PRIMARY_COLOR);
                g2.fillOval(0, 0, 36, 36);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                String initial = session.getUsername().substring(0, 1).toUpperCase();
                FontMetrics fm = g2.getFontMetrics();
                int x = (36 - fm.stringWidth(initial)) / 2;
                int y = (36 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(initial, x, y);
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(36, 36));
        
        JPanel userInfo = new JPanel();
        userInfo.setOpaque(false);
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        JLabel lblUsername = new JLabel(session.getUsername());
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUsername.setForeground(UIStyles.TEXT_PRIMARY);
        JLabel lblRole = new JLabel(session.getCurrentRole().getDisplayName());
        lblRole.setFont(UIStyles.FONT_SMALL);
        lblRole.setForeground(UIStyles.TEXT_SECONDARY);
        userInfo.add(lblUsername);
        userInfo.add(lblRole);
        
        userPanel.add(userInfo);
        userPanel.add(avatar);
        
        topBar.add(lblPageTitle, BorderLayout.WEST);
        topBar.add(userPanel, BorderLayout.EAST);
        
        // --- Content area ---
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UIStyles.BACKGROUND_COLOR);
        contentArea.add(topBar, BorderLayout.NORTH);
        contentArea.add(contentPanel, BorderLayout.CENTER);
        
        // Afficher le dashboard par defaut
        showDashboard();
        
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentArea, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void buildSidebar() {
        sidebarPanel.removeAll();
        SessionManager session = SessionManager.getInstance();
        boolean isResponsable = session.isResponsable();
        
        // Logo / Branding
        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        brandPanel.setMaximumSize(new Dimension(UIStyles.SIDEBAR_WIDTH, 70));
        
        JLabel lblLogo = new JLabel("Plein de Foin");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(UIStyles.PRIMARY_COLOR);
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblSub = new JLabel("Pepiniere");
        lblSub.setFont(UIStyles.FONT_SMALL);
        lblSub.setForeground(UIStyles.TEXT_MUTED);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        brandPanel.add(lblLogo);
        brandPanel.add(lblSub);
        sidebarPanel.add(brandPanel);
        sidebarPanel.add(Box.createVerticalStrut(10));
        
        // Separateur
        addSidebarSeparator("PRINCIPAL");
        
        addSidebarItem("Accueil", true, () -> { activeSection = "Accueil"; showDashboard(); });
        addSidebarItem("Articles", false, () -> { activeSection = "Articles"; showPanel(new ArticleListPanel(this)); });
        
        if (isResponsable) {
            addSidebarItem("Clients", false, () -> { activeSection = "Clients"; showPanel(new ClientListPanel(this)); });
            addSidebarItem("Commandes", false, () -> { activeSection = "Commandes"; showPanel(new CommandeListPanel(this)); });
            addSidebarItem("Livraisons", false, () -> { activeSection = "Livraisons"; showPanel(new LivraisonListPanel(this)); });
        } else {
            addSidebarItem("Clients", false, () -> { activeSection = "Clients"; showPanel(new ClientListPanel(this)); });
            addSidebarItem("Commandes", false, () -> { activeSection = "Commandes"; showPanel(new CommandeListPanel(this)); });
        }
        
        addSidebarSeparator("GESTION");
        
        addSidebarItem("Nouvel Article", false, () -> { activeSection = "Nouvel Article"; showPanel(new ArticleFormPanel(this)); });
        
        if (isResponsable) {
            addSidebarItem("Nouveau Client", false, () -> { activeSection = "Nouveau Client"; showPanel(new ClientFormPanel(this)); });
            addSidebarItem("Nouvelle Commande", false, () -> { activeSection = "Nouvelle Commande"; showPanel(new CommandeFormPanel(this)); });
            addSidebarItem("Nouvelle Livraison", false, () -> { activeSection = "Nouvelle Livraison"; showPanel(new LivraisonFormPanel(this)); });
        }
        
        if (isResponsable) {
            addSidebarSeparator("OUTILS");
            addSidebarItem("Augmenter les Prix", false, () -> showAugmenterPrixDialog());
            addSidebarItem("Valeur du Stock", false, () -> showValeurStock());
            addSidebarItem("Montant / Client", false, () -> { activeSection = "Montant / Client"; showPanel(new MontantClientPanel()); });
        }
        
        sidebarPanel.add(Box.createVerticalGlue());
        
        // Bouton deconnexion en bas
        addSidebarSeparator("");
        JButton btnLogout = UIStyles.createSidebarButton("Deconnexion", false);
        btnLogout.setForeground(UIStyles.DANGER_COLOR);
        btnLogout.addActionListener(e -> handleLogout());
        sidebarPanel.add(btnLogout);
        sidebarPanel.add(Box.createVerticalStrut(16));
        
        sidebarPanel.revalidate();
        sidebarPanel.repaint();
    }
    
    private void addSidebarSeparator(String label) {
        if (!label.isEmpty()) {
            JLabel lbl = new JLabel("  " + label);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
            lbl.setForeground(UIStyles.TEXT_MUTED);
            lbl.setBorder(BorderFactory.createEmptyBorder(16, 16, 6, 0));
            lbl.setMaximumSize(new Dimension(UIStyles.SIDEBAR_WIDTH, 36));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebarPanel.add(lbl);
        } else {
            JSeparator sep = new JSeparator();
            sep.setForeground(UIStyles.BORDER_COLOR);
            sep.setMaximumSize(new Dimension(UIStyles.SIDEBAR_WIDTH - 30, 1));
            sidebarPanel.add(Box.createVerticalStrut(8));
            sidebarPanel.add(sep);
            sidebarPanel.add(Box.createVerticalStrut(8));
        }
    }
    
    private void addSidebarItem(String text, boolean active, Runnable action) {
        JButton btn = UIStyles.createSidebarButton(text, active);
        btn.addActionListener(e -> {
            action.run();
            refreshSidebarSelection();
        });
        sidebarButtons.add(btn);
        sidebarPanel.add(btn);
    }
    
    private void refreshSidebarSelection() {
        for (JButton btn : sidebarButtons) {
            boolean isActive = btn.getText().equals(activeSection);
            btn.setBackground(isActive ? UIStyles.SIDEBAR_ACTIVE_BG : UIStyles.SIDEBAR_BG);
            btn.setFont(isActive ? UIStyles.FONT_SIDEBAR_ACTIVE : UIStyles.FONT_SIDEBAR);
            btn.setForeground(isActive ? UIStyles.PRIMARY_COLOR : UIStyles.TEXT_SECONDARY);
        }
        sidebarPanel.repaint();
    }
    
    /**
     * Affiche le dashboard avec les cartes de statistiques
     */
    private void showDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(UIStyles.BACKGROUND_COLOR);
        dashboard.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        
        // Titre
        JLabel lblDash = new JLabel("Vue d'ensemble");
        lblDash.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblDash.setForeground(UIStyles.TEXT_PRIMARY);
        lblDash.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Cartes de stats
        JPanel cardsRow = new JPanel(new GridLayout(1, 4, 18, 0));
        cardsRow.setOpaque(false);
        cardsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Charger les stats en arriere plan
        int nbArticles = 0, nbClients = 0, nbCommandes = 0, nbLivraisons = 0;
        try { nbArticles = new ArticleDAO().findAll().size(); } catch (Exception ignored) {}
        try { nbClients = new ClientDAO().findAll().size(); } catch (Exception ignored) {}
        try { nbCommandes = new CommandeDAO().findAll().size(); } catch (Exception ignored) {}
        try { nbLivraisons = new LivraisonDAO().findAll().size(); } catch (Exception ignored) {}
        
        cardsRow.add(UIStyles.createStatCard("Articles", String.valueOf(nbArticles), "En catalogue", UIStyles.CARD_GREEN));
        cardsRow.add(UIStyles.createStatCard("Clients", String.valueOf(nbClients), "Enregistres", UIStyles.CARD_BLUE));
        cardsRow.add(UIStyles.createStatCard("Commandes", String.valueOf(nbCommandes), "Passees", UIStyles.CARD_ORANGE));
        cardsRow.add(UIStyles.createStatCard("Livraisons", String.valueOf(nbLivraisons), "Effectuees", UIStyles.CARD_RED));
        
        JPanel topSection = new JPanel();
        topSection.setOpaque(false);
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.add(lblDash);
        topSection.add(cardsRow);
        topSection.add(Box.createVerticalStrut(24));
        
        // Section inferieure : raccourcis rapides
        JPanel quickActionsCard = UIStyles.createCard();
        quickActionsCard.setLayout(new BorderLayout());
        quickActionsCard.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        
        JLabel lblQuick = new JLabel("Actions rapides");
        lblQuick.setFont(UIStyles.FONT_SUBTITLE);
        lblQuick.setForeground(UIStyles.TEXT_PRIMARY);
        lblQuick.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        
        JPanel actionsGrid = new JPanel(new GridLayout(2, 3, 14, 14));
        actionsGrid.setOpaque(false);
        
        SessionManager session = SessionManager.getInstance();
        boolean isResponsable = session.isResponsable();
        
        actionsGrid.add(createQuickAction("Liste Articles", UIStyles.CARD_GREEN, () -> {
            activeSection = "Articles";
            showPanel(new ArticleListPanel(this));
            refreshSidebarSelection();
        }));
        actionsGrid.add(createQuickAction("Nouvel Article", UIStyles.CARD_GREEN, () -> {
            activeSection = "Nouvel Article";
            showPanel(new ArticleFormPanel(this));
            refreshSidebarSelection();
        }));
        actionsGrid.add(createQuickAction("Liste Clients", UIStyles.CARD_BLUE, () -> {
            activeSection = "Clients";
            showPanel(new ClientListPanel(this));
            refreshSidebarSelection();
        }));
        
        if (isResponsable) {
            actionsGrid.add(createQuickAction("Nouvelle Commande", UIStyles.CARD_ORANGE, () -> {
                activeSection = "Nouvelle Commande";
                showPanel(new CommandeFormPanel(this));
                refreshSidebarSelection();
            }));
            actionsGrid.add(createQuickAction("Nouvelle Livraison", UIStyles.CARD_RED, () -> {
                activeSection = "Nouvelle Livraison";
                showPanel(new LivraisonFormPanel(this));
                refreshSidebarSelection();
            }));
            actionsGrid.add(createQuickAction("Montant / Client", UIStyles.CARD_BLUE, () -> {
                activeSection = "Montant / Client";
                showPanel(new MontantClientPanel());
                refreshSidebarSelection();
            }));
        } else {
            actionsGrid.add(createQuickAction("Liste Commandes", UIStyles.CARD_ORANGE, () -> {
                activeSection = "Commandes";
                showPanel(new CommandeListPanel(this));
                refreshSidebarSelection();
            }));
        }
        
        quickActionsCard.add(lblQuick, BorderLayout.NORTH);
        quickActionsCard.add(actionsGrid, BorderLayout.CENTER);
        
        // Info role
        UIStyles.RoundedPanel roleCard = new UIStyles.RoundedPanel(UIStyles.CARD_RADIUS, UIStyles.PRIMARY_LIGHT, false);
        roleCard.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 12));
        roleCard.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        JLabel lblRoleInfo = new JLabel("Connecte en tant que : " + session.getUsername() + " (" + session.getCurrentRole().getDisplayName() + ")");
        lblRoleInfo.setFont(UIStyles.FONT_BODY);
        lblRoleInfo.setForeground(UIStyles.PRIMARY_DARK);
        roleCard.add(lblRoleInfo);
        
        JPanel bottomSection = new JPanel(new BorderLayout(0, 16));
        bottomSection.setOpaque(false);
        bottomSection.add(quickActionsCard, BorderLayout.CENTER);
        bottomSection.add(roleCard, BorderLayout.SOUTH);
        
        dashboard.add(topSection, BorderLayout.NORTH);
        dashboard.add(bottomSection, BorderLayout.CENTER);
        
        contentPanel.showWithFade(dashboard);
    }
    
    private JPanel createQuickAction(String title, Color accentColor, Runnable action) {
        UIStyles.RoundedPanel card = new UIStyles.RoundedPanel(12, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Barre coloree en haut
        JPanel colorBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
            }
        };
        colorBar.setOpaque(false);
        colorBar.setPreferredSize(new Dimension(0, 4));
        
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(UIStyles.TEXT_PRIMARY);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        card.add(colorBar, BorderLayout.NORTH);
        card.add(lbl, BorderLayout.CENTER);
        
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        
        return card;
    }
    
    public void showPanel(JPanel panel) {
        contentPanel.showWithFade(panel);
    }
    
    private void showAugmenterPrixDialog() {
        String input = JOptionPane.showInputDialog(this,
            "Entrez le pourcentage d'augmentation:",
            "Augmenter les Prix", JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            try {
                java.math.BigDecimal pourcentage = new java.math.BigDecimal(input.trim());
                LoadingSpinner.run(this, "Mise à jour des prix...",
                    () -> { new ArticleDAO().augmenterPrix(pourcentage); return pourcentage; },
                    p  -> ToastNotification.success(this, "Prix augmentés de " + p + "% avec succès."),
                    e  -> ToastNotification.error(this, "Erreur : " + e.getMessage())
                );
            } catch (NumberFormatException e) {
                ToastNotification.error(this, "Veuillez entrer un nombre valide.");
            }
        }
    }
    
    private void showValeurStock() {
        LoadingSpinner.run(this, "Calcul du stock...",
            () -> new ArticleDAO().getValeurStock(),
            valeur -> ToastNotification.info(this,
                "Valeur totale du stock : <b>" + valeur + " FCFA</b>"),
            e -> ToastNotification.error(this, "Erreur : " + e.getMessage())
        );
    }
    
    private void handleLogout() {
        if (UIStyles.showConfirmDialog(this, "Voulez-vous vraiment vous deconnecter?")) {
            SessionManager.getInstance().logout();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        }
    }
}
