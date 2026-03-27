package com.pepiniere.ui;

import com.pepiniere.model.UserRole;
import com.pepiniere.util.SessionManager;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre principale avec menus conditionnels selon le rôle utilisateur
 */
public class MainFrame extends JFrame {
    
    private JMenuBar menuBar;
    private JMenu menuArticles;
    private JMenu menuClients;
    private JMenu menuCommandes;
    private JMenu menuLivraisons;
    private JMenu menuOutils;
    private JMenu menuSession;
    
    private JMenuItem miNouvelArticle;
    private JMenuItem miListeArticles;
    private JMenuItem miAugmenterPrix;
    private JMenuItem miValeurStock;
    
    private JMenuItem miNouveauClient;
    private JMenuItem miListeClients;
    private JMenuItem miMontantClient;
    
    private JMenuItem miNouvelleCommande;
    private JMenuItem miListeCommandes;
    
    private JMenuItem miNouvelleLivraison;
    private JMenuItem miListeLivraisons;
    
    private JMenuItem miDeconnexion;
    private JMenuItem miQuitter;
    
    private JPanel contentPanel;
    private JLabel lblStatus;
    
    public MainFrame() {
        initComponents();
        setupMenuBar();
        setupLayout();
        applyRoleRestrictions();
    }
    
    private void initComponents() {
        setTitle("Pépinière Plein de Foin - Gestion Commerciale");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        lblStatus = new JLabel();
        lblStatus.setFont(UIStyles.FONT_BODY);
        lblStatus.setForeground(UIStyles.TEXT_SECONDARY);
        updateStatusBar();
    }
    
    private void setupMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIStyles.BORDER_COLOR));
        
        // Configuration générale pour les menus
        UIManager.put("Menu.font", UIStyles.FONT_BODY);
        UIManager.put("MenuItem.font", UIStyles.FONT_BODY);
        UIManager.put("Menu.margin", new Insets(8, 12, 8, 12));
        UIManager.put("MenuItem.margin", new Insets(8, 12, 8, 12));
        UIManager.put("Menu.selectionBackground", UIStyles.PRIMARY_LIGHT);
        UIManager.put("MenuItem.selectionBackground", UIStyles.PRIMARY_LIGHT);
        
        menuArticles = new JMenu("Articles");
        miNouvelArticle = new JMenuItem("Nouvel Article");
        miListeArticles = new JMenuItem("Liste des Articles");
        miAugmenterPrix = new JMenuItem("Augmenter les Prix");
        miValeurStock = new JMenuItem("Valeur du Stock");
        
        miNouvelArticle.addActionListener(e -> showPanel(new ArticleFormPanel(this)));
        miListeArticles.addActionListener(e -> showPanel(new ArticleListPanel(this)));
        miAugmenterPrix.addActionListener(e -> showAugmenterPrixDialog());
        miValeurStock.addActionListener(e -> showValeurStock());
        
        menuArticles.add(miNouvelArticle);
        menuArticles.add(miListeArticles);
        menuArticles.addSeparator();
        menuArticles.add(miAugmenterPrix);
        menuArticles.add(miValeurStock);
        
        menuClients = new JMenu("Clients");
        miNouveauClient = new JMenuItem("Nouveau Client");
        miListeClients = new JMenuItem("Liste des Clients");
        miMontantClient = new JMenuItem("Montant par Client");
        
        miNouveauClient.addActionListener(e -> showPanel(new ClientFormPanel(this)));
        miListeClients.addActionListener(e -> showPanel(new ClientListPanel(this)));
        miMontantClient.addActionListener(e -> showPanel(new MontantClientPanel()));
        
        menuClients.add(miNouveauClient);
        menuClients.add(miListeClients);
        menuClients.addSeparator();
        menuClients.add(miMontantClient);
        
        menuCommandes = new JMenu("Commandes");
        miNouvelleCommande = new JMenuItem("Nouvelle Commande");
        miListeCommandes = new JMenuItem("Liste des Commandes");
        
        miNouvelleCommande.addActionListener(e -> showPanel(new CommandeFormPanel(this)));
        miListeCommandes.addActionListener(e -> showPanel(new CommandeListPanel(this)));
        
        menuCommandes.add(miNouvelleCommande);
        menuCommandes.add(miListeCommandes);
        
        menuLivraisons = new JMenu("Livraisons");
        miNouvelleLivraison = new JMenuItem("Nouvelle Livraison");
        miListeLivraisons = new JMenuItem("Liste des Livraisons");
        
        miNouvelleLivraison.addActionListener(e -> showPanel(new LivraisonFormPanel(this)));
        miListeLivraisons.addActionListener(e -> showPanel(new LivraisonListPanel(this)));
        
        menuLivraisons.add(miNouvelleLivraison);
        menuLivraisons.add(miListeLivraisons);
        
        menuSession = new JMenu("Session");
        miDeconnexion = new JMenuItem("Déconnexion");
        miQuitter = new JMenuItem("Quitter");
        
        miDeconnexion.addActionListener(e -> handleLogout());
        miQuitter.addActionListener(e -> System.exit(0));
        
        menuSession.add(miDeconnexion);
        menuSession.addSeparator();
        menuSession.add(miQuitter);
        
        menuBar.add(menuArticles);
        menuBar.add(menuClients);
        menuBar.add(menuCommandes);
        menuBar.add(menuLivraisons);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuSession);
        
        setJMenuBar(menuBar);
    }
    
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(UIStyles.BACKGROUND_COLOR);
        
        JPanel welcomeCard = UIStyles.createCard();
        welcomeCard.setLayout(new BoxLayout(welcomeCard, BoxLayout.Y_AXIS));
        welcomeCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel lblWelcome = new JLabel("Bienvenue dans l'application de gestion");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblWelcome.setForeground(UIStyles.PRIMARY_COLOR);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel("Pépinière Plein de Foin");
        lblSubtitle.setFont(UIStyles.FONT_SUBTITLE);
        lblSubtitle.setForeground(UIStyles.TEXT_SECONDARY);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));
        
        JLabel lblInfo = new JLabel("<html><div style='text-align: center;'>Utilisez le menu ci-dessus pour accéder aux différentes fonctionnalités<br>de l'application selon votre profil utilisateur.</div></html>");
        lblInfo.setFont(UIStyles.FONT_BODY);
        lblInfo.setForeground(UIStyles.TEXT_SECONDARY);
        lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        welcomeCard.add(lblWelcome);
        welcomeCard.add(lblSubtitle);
        welcomeCard.add(lblInfo);
        
        welcomePanel.add(welcomeCard);
        contentPanel.add(welcomePanel, BorderLayout.CENTER);
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 8));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIStyles.BORDER_COLOR));
        statusPanel.add(lblStatus);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void applyRoleRestrictions() {
        SessionManager session = SessionManager.getInstance();
        boolean isResponsable = session.isResponsable();
        
        miAugmenterPrix.setEnabled(isResponsable);
        miValeurStock.setEnabled(isResponsable);
        miMontantClient.setEnabled(isResponsable);
        
        miNouveauClient.setEnabled(isResponsable);
        miNouvelleCommande.setEnabled(isResponsable);
        miNouvelleLivraison.setEnabled(isResponsable);
        
        menuLivraisons.setEnabled(isResponsable);
        
        if (!isResponsable) {
            miAugmenterPrix.setToolTipText("Accès réservé au Responsable");
            miValeurStock.setToolTipText("Accès réservé au Responsable");
            miMontantClient.setToolTipText("Accès réservé au Responsable");
            miNouveauClient.setToolTipText("Accès réservé au Responsable");
            miNouvelleCommande.setToolTipText("Accès réservé au Responsable");
        }
    }
    
    private void updateStatusBar() {
        SessionManager session = SessionManager.getInstance();
        lblStatus.setText(" " + session.getUsername() + 
                         "  •  " + session.getCurrentRole().getDisplayName());
    }
    
    public void showPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showAugmenterPrixDialog() {
        String input = JOptionPane.showInputDialog(this,
            "Entrez le pourcentage d'augmentation:",
            "Augmenter les Prix", JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.trim().isEmpty()) {
            try {
                java.math.BigDecimal pourcentage = new java.math.BigDecimal(input.trim());
                new com.pepiniere.dao.ArticleDAO().augmenterPrix(pourcentage);
                UIStyles.showSuccessMessage(this,
                    "Les prix ont été augmentés de " + pourcentage + "%");
            } catch (NumberFormatException e) {
                UIStyles.showErrorMessage(this, "Veuillez entrer un nombre valide.");
            } catch (Exception e) {
                UIStyles.showErrorMessage(this, "Erreur: " + e.getMessage());
            }
        }
    }
    
    private void showValeurStock() {
        try {
            java.math.BigDecimal valeur = new com.pepiniere.dao.ArticleDAO().getValeurStock();
            UIStyles.showSuccessMessage(this,
                "Valeur totale du stock: " + valeur + " €");
        } catch (Exception e) {
            UIStyles.showErrorMessage(this, "Erreur: " + e.getMessage());
        }
    }
    
    private void handleLogout() {
        if (UIStyles.showConfirmDialog(this, "Voulez-vous vraiment vous déconnecter?")) {
            SessionManager.getInstance().logout();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        }
    }
}
