package com.pepiniere.ui;

import com.pepiniere.dao.ArticleDAO;
import com.pepiniere.dao.ClientDAO;
import com.pepiniere.dao.CommandeDAO;
import com.pepiniere.model.Article;
import com.pepiniere.model.Client;
import com.pepiniere.model.Commande;
import com.pepiniere.model.LigneCommande;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

/**
 * Formulaire de création de commande
 */
public class CommandeFormPanel extends JPanel {
    
    private MainFrame parent;
    
    private JTextField txtNoCommande;
    private JComboBox<Client> cmbClient;
    private JComboBox<Article> cmbArticle;
    private JTextField txtQuantite;
    private JTable tableLignes;
    private DefaultTableModel tableModel;
    
    private JButton btnAddLigne;
    private JButton btnRemoveLigne;
    private JButton btnSave;
    private JButton btnCancel;
    
    public CommandeFormPanel(MainFrame parent) {
        this.parent = parent;
        initComponents();
        setupLayout();
        setupListeners();
        loadComboData();
    }
    
    private void initComponents() {
        txtNoCommande = UIStyles.createStyledTextField();
        cmbClient = new JComboBox<>();
        cmbClient.setFont(UIStyles.FONT_BODY);
        cmbClient.setBackground(Color.WHITE);
        
        cmbArticle = new JComboBox<>();
        cmbArticle.setFont(UIStyles.FONT_BODY);
        cmbArticle.setBackground(Color.WHITE);
        
        txtQuantite = UIStyles.createStyledTextField();
        
        String[] columns = {"N° Article", "Description", "Quantité"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableLignes = new JTable(tableModel);
        tableLignes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIStyles.styleTable(tableLignes);
        
        btnAddLigne = UIStyles.createSecondaryButton("Ajouter Ligne");
        btnRemoveLigne = UIStyles.createDangerButton("Supprimer Ligne");
        btnSave = UIStyles.createPrimaryButton("Enregistrer Commande");
        btnCancel = UIStyles.createOutlineButton("Annuler");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UIStyles.BACKGROUND_COLOR);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitle = UIStyles.createTitleLabel("Nouvelle Commande");
        
        JPanel contentCard = UIStyles.createCard();
        contentCard.setLayout(new BorderLayout(0, 20));
        contentCard.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // --- En-tête de la commande ---
        JPanel headerFormPanel = new JPanel(new GridBagLayout());
        headerFormPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        headerFormPanel.add(UIStyles.createLabel("N° Commande:"), gbc);
        gbc.gridx = 1;
        txtNoCommande.setPreferredSize(new Dimension(150, 38));
        headerFormPanel.add(txtNoCommande, gbc);
        
        gbc.gridx = 2;
        headerFormPanel.add(UIStyles.createLabel("Client:"), gbc);
        gbc.gridx = 3;
        cmbClient.setPreferredSize(new Dimension(300, 38));
        headerFormPanel.add(cmbClient, gbc);
        
        // --- Ajout de ligne ---
        JPanel addLignePanel = new JPanel(new GridBagLayout());
        addLignePanel.setBackground(UIStyles.BACKGROUND_COLOR);
        addLignePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyles.BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbcLigne = new GridBagConstraints();
        gbcLigne.insets = new Insets(5, 10, 5, 10);
        gbcLigne.anchor = GridBagConstraints.CENTER;
        
        gbcLigne.gridx = 0; gbcLigne.gridy = 0;
        addLignePanel.add(UIStyles.createLabel("Article:"), gbcLigne);
        gbcLigne.gridx = 1;
        cmbArticle.setPreferredSize(new Dimension(250, 38));
        addLignePanel.add(cmbArticle, gbcLigne);
        
        gbcLigne.gridx = 2;
        addLignePanel.add(UIStyles.createLabel("Quantité:"), gbcLigne);
        gbcLigne.gridx = 3;
        txtQuantite.setPreferredSize(new Dimension(100, 38));
        addLignePanel.add(txtQuantite, gbcLigne);
        
        gbcLigne.gridx = 4;
        addLignePanel.add(btnAddLigne, gbcLigne);
        
        // --- Tableau des lignes ---
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(Color.WHITE);
        
        JPanel tableHeaderPanel = new JPanel(new BorderLayout());
        tableHeaderPanel.setBackground(Color.WHITE);
        tableHeaderPanel.add(UIStyles.createHeadingLabel("Lignes de la commande"), BorderLayout.WEST);
        tableHeaderPanel.add(btnRemoveLigne, BorderLayout.EAST);
        
        JScrollPane scrollPane = new JScrollPane(tableLignes);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER_COLOR));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(700, 200));
        
        tablePanel.add(tableHeaderPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Assemblage de la carte
        JPanel topWrapper = new JPanel(new BorderLayout(0, 20));
        topWrapper.setBackground(Color.WHITE);
        topWrapper.add(headerFormPanel, BorderLayout.NORTH);
        topWrapper.add(addLignePanel, BorderLayout.CENTER);
        
        contentCard.add(topWrapper, BorderLayout.NORTH);
        contentCard.add(tablePanel, BorderLayout.CENTER);
        
        // --- Boutons d'action ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        btnSave.setPreferredSize(new Dimension(200, 40));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(contentCard, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupListeners() {
        btnAddLigne.addActionListener(e -> addLigne());
        btnRemoveLigne.addActionListener(e -> removeLigne());
        btnSave.addActionListener(e -> saveCommande());
        btnCancel.addActionListener(e -> parent.showPanel(new CommandeListPanel(parent)));
    }
    
    private void loadComboData() {
        try {
            ClientDAO clientDAO = new ClientDAO();
            List<Client> clients = clientDAO.findAll();
            for (Client c : clients) {
                cmbClient.addItem(c);
            }
            
            ArticleDAO articleDAO = new ArticleDAO();
            List<Article> articles = articleDAO.findAll();
            for (Article a : articles) {
                cmbArticle.addItem(a);
            }
        } catch (Exception e) {
            ToastNotification.error(parent, "Erreur lors du chargement des données: " + e.getMessage());
        }
    }
    
    private void addLigne() {
        Article article = (Article) cmbArticle.getSelectedItem();
        String quantiteStr = txtQuantite.getText().trim();
        
        if (article == null || quantiteStr.isEmpty()) {
            ToastNotification.error(parent, "Veuillez sélectionner un article et entrer une quantité.");
            return;
        }
        
        try {
            int quantite = Integer.parseInt(quantiteStr);
            if (quantite <= 0) {
                ToastNotification.error(parent, "La quantité doit être positive.");
                return;
            }
            
            tableModel.addRow(new Object[]{
                article.getNoArticle(),
                article.getDescription(),
                quantite
            });
            txtQuantite.setText("");
            
        } catch (NumberFormatException e) {
            ToastNotification.error(parent, "Quantité invalide.");
        }
    }
    
    private void removeLigne() {
        int row = tableLignes.getSelectedRow();
        if (row >= 0) {
            tableModel.removeRow(row);
        }
    }
    
    private void saveCommande() {
        String noCommandeStr = txtNoCommande.getText().trim();
        Client client = (Client) cmbClient.getSelectedItem();
        
        if (noCommandeStr.isEmpty() || client == null) {
            ToastNotification.error(parent, "Veuillez remplir le numéro de commande et sélectionner un client.");
            return;
        }
        
        if (tableModel.getRowCount() == 0) {
            ToastNotification.error(parent, "Veuillez ajouter au moins une ligne de commande.");
            return;
        }
        
        try {
            int noCommande = Integer.parseInt(noCommandeStr);
            
            Commande commande = new Commande();
            commande.setNoCommande(noCommande);
            commande.setDateCommande(new Date(System.currentTimeMillis()));
            commande.setNoClient(client.getNoClient());
            
            CommandeDAO dao = new CommandeDAO();
            dao.insert(commande);
            
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                LigneCommande ligne = new LigneCommande();
                ligne.setNoCommande(noCommande);
                ligne.setNoArticle((int) tableModel.getValueAt(i, 0));
                ligne.setQuantite((int) tableModel.getValueAt(i, 2));
                dao.insertLigneCommande(ligne);
            }
            
            ToastNotification.success(parent, "Commande enregistrée avec succès.");
            parent.showPanel(new CommandeListPanel(parent));
            
        } catch (NumberFormatException e) {
            ToastNotification.error(parent, "Le numéro de commande doit être un nombre.");
        } catch (Exception e) {
            ToastNotification.error(parent, "Erreur: " + e.getMessage());
        }
    }
}
