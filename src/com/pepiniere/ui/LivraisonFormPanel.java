package com.pepiniere.ui;

import com.pepiniere.dao.CommandeDAO;
import com.pepiniere.dao.LivraisonDAO;
import com.pepiniere.model.Commande;
import com.pepiniere.model.DetailLivraison;
import com.pepiniere.model.LigneCommande;
import com.pepiniere.model.Livraison;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

/**
 * Formulaire de création de livraison
 */
public class LivraisonFormPanel extends JPanel {
    
    private MainFrame parent;
    
    private JTextField txtNoLivraison;
    private JComboBox<String> cmbCommande;
    private JTable tableLignesCmd;
    private JTable tableDetails;
    private DefaultTableModel lignesCmdModel;
    private DefaultTableModel detailsModel;
    
    private JTextField txtQuantiteLivree;
    private JButton btnAddDetail;
    private JButton btnRemoveDetail;
    private JButton btnSave;
    private JButton btnCancel;
    
    private List<Commande> commandes;
    
    public LivraisonFormPanel(MainFrame parent) {
        this.parent = parent;
        initComponents();
        setupLayout();
        setupListeners();
        loadCommandes();
    }
    
    private void initComponents() {
        txtNoLivraison = UIStyles.createStyledTextField();
        cmbCommande = new JComboBox<>();
        cmbCommande.setFont(UIStyles.FONT_BODY);
        cmbCommande.setBackground(Color.WHITE);
        txtQuantiteLivree = UIStyles.createStyledTextField();
        
        String[] columnsLignes = {"N° Article", "Description", "Quantité Cmd"};
        lignesCmdModel = new DefaultTableModel(columnsLignes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableLignesCmd = new JTable(lignesCmdModel);
        tableLignesCmd.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIStyles.styleTable(tableLignesCmd);
        
        String[] columnsDetails = {"N° Commande", "N° Article", "Quantité Livrée"};
        detailsModel = new DefaultTableModel(columnsDetails, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDetails = new JTable(detailsModel);
        UIStyles.styleTable(tableDetails);
        
        btnAddDetail = UIStyles.createSecondaryButton("Ajouter");
        btnRemoveDetail = UIStyles.createDangerButton("Supprimer");
        btnSave = UIStyles.createPrimaryButton("Enregistrer Livraison");
        btnCancel = UIStyles.createOutlineButton("Annuler");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UIStyles.BACKGROUND_COLOR);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitle = UIStyles.createTitleLabel("Nouvelle Livraison");
        
        JPanel contentCard = UIStyles.createCard();
        contentCard.setLayout(new BorderLayout(0, 20));
        contentCard.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // --- En-tête de la livraison ---
        JPanel headerFormPanel = new JPanel(new GridBagLayout());
        headerFormPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        headerFormPanel.add(UIStyles.createLabel("N° Livraison:"), gbc);
        gbc.gridx = 1;
        txtNoLivraison.setPreferredSize(new Dimension(150, 38));
        headerFormPanel.add(txtNoLivraison, gbc);
        
        gbc.gridx = 2;
        headerFormPanel.add(UIStyles.createLabel("Commande:"), gbc);
        gbc.gridx = 3;
        cmbCommande.setPreferredSize(new Dimension(250, 38));
        headerFormPanel.add(cmbCommande, gbc);
        
        // --- Zone centrale : Lignes de commande -> Ajout -> Détails de livraison ---
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(Color.WHITE);
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.fill = GridBagConstraints.BOTH;
        gbcCenter.weighty = 1.0;
        
        // Lignes de la commande
        JPanel leftPanel = new JPanel(new BorderLayout(0, 5));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(UIStyles.createHeadingLabel("Articles de la commande"), BorderLayout.NORTH);
        JScrollPane scrollLignes = new JScrollPane(tableLignesCmd);
        scrollLignes.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER_COLOR));
        scrollLignes.getViewport().setBackground(Color.WHITE);
        leftPanel.add(scrollLignes, BorderLayout.CENTER);
        
        // Zone du milieu (Ajout)
        JPanel middlePanel = new JPanel(new GridBagLayout());
        middlePanel.setBackground(Color.WHITE);
        middlePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        GridBagConstraints gbcMid = new GridBagConstraints();
        gbcMid.insets = new Insets(5, 0, 5, 0);
        gbcMid.gridx = 0;
        
        gbcMid.gridy = 0;
        middlePanel.add(UIStyles.createLabel("Qté livrée:"), gbcMid);
        gbcMid.gridy = 1;
        txtQuantiteLivree.setPreferredSize(new Dimension(80, 38));
        middlePanel.add(txtQuantiteLivree, gbcMid);
        gbcMid.gridy = 2;
        middlePanel.add(btnAddDetail, gbcMid);
        
        // Détails de la livraison
        JPanel rightPanel = new JPanel(new BorderLayout(0, 5));
        rightPanel.setBackground(Color.WHITE);
        
        JPanel rightHeader = new JPanel(new BorderLayout());
        rightHeader.setBackground(Color.WHITE);
        rightHeader.add(UIStyles.createHeadingLabel("Articles à livrer"), BorderLayout.WEST);
        rightHeader.add(btnRemoveDetail, BorderLayout.EAST);
        
        rightPanel.add(rightHeader, BorderLayout.NORTH);
        JScrollPane scrollDetails = new JScrollPane(tableDetails);
        scrollDetails.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER_COLOR));
        scrollDetails.getViewport().setBackground(Color.WHITE);
        rightPanel.add(scrollDetails, BorderLayout.CENTER);
        
        // Assemblage de la zone centrale
        gbcCenter.gridx = 0; gbcCenter.weightx = 0.45;
        centerWrapper.add(leftPanel, gbcCenter);
        gbcCenter.gridx = 1; gbcCenter.weightx = 0.1;
        centerWrapper.add(middlePanel, gbcCenter);
        gbcCenter.gridx = 2; gbcCenter.weightx = 0.45;
        centerWrapper.add(rightPanel, gbcCenter);
        
        contentCard.add(headerFormPanel, BorderLayout.NORTH);
        contentCard.add(centerWrapper, BorderLayout.CENTER);
        
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
        cmbCommande.addActionListener(e -> loadLignesCommande());
        btnAddDetail.addActionListener(e -> addDetail());
        btnRemoveDetail.addActionListener(e -> removeDetail());
        btnSave.addActionListener(e -> saveLivraison());
        btnCancel.addActionListener(e -> parent.showPanel(new LivraisonListPanel(parent)));
    }
    
    private void loadCommandes() {
        try {
            CommandeDAO dao = new CommandeDAO();
            commandes = dao.findAll();
            for (Commande c : commandes) {
                cmbCommande.addItem("Cmd " + c.getNoCommande() + " - " + c.getNomClient());
            }
        } catch (Exception e) {
            ToastNotification.error(parent, "Erreur: " + e.getMessage());
        }
    }
    
    private void loadLignesCommande() {
        lignesCmdModel.setRowCount(0);
        int idx = cmbCommande.getSelectedIndex();
        if (idx < 0 || idx >= commandes.size()) return;
        
        try {
            Commande cmd = commandes.get(idx);
            CommandeDAO dao = new CommandeDAO();
            List<LigneCommande> lignes = dao.findLignesCommande(cmd.getNoCommande());
            for (LigneCommande l : lignes) {
                lignesCmdModel.addRow(new Object[]{
                    l.getNoArticle(),
                    l.getDescriptionArticle(),
                    l.getQuantite()
                });
            }
        } catch (Exception e) {
            ToastNotification.error(parent, "Erreur: " + e.getMessage());
        }
    }
    
    private void addDetail() {
        int row = tableLignesCmd.getSelectedRow();
        int idx = cmbCommande.getSelectedIndex();
        String qteStr = txtQuantiteLivree.getText().trim();
        
        if (row < 0 || idx < 0 || qteStr.isEmpty()) {
            ToastNotification.error(parent, "Sélectionnez une ligne et entrez une quantité.");
            return;
        }
        
        try {
            int qte = Integer.parseInt(qteStr);
            if (qte <= 0) {
                ToastNotification.error(parent, "La quantité doit être positive.");
                return;
            }
            
            int noCommande = commandes.get(idx).getNoCommande();
            int noArticle = (int) lignesCmdModel.getValueAt(row, 0);
            
            detailsModel.addRow(new Object[]{noCommande, noArticle, qte});
            txtQuantiteLivree.setText("");
            
        } catch (NumberFormatException e) {
            ToastNotification.error(parent, "Quantité invalide.");
        }
    }
    
    private void removeDetail() {
        int row = tableDetails.getSelectedRow();
        if (row >= 0) {
            detailsModel.removeRow(row);
        }
    }
    
    private void saveLivraison() {
        String noLivraisonStr = txtNoLivraison.getText().trim();
        
        if (noLivraisonStr.isEmpty()) {
            ToastNotification.error(parent, "Veuillez entrer un numéro de livraison.");
            return;
        }
        
        if (detailsModel.getRowCount() == 0) {
            ToastNotification.error(parent, "Veuillez ajouter au moins un détail de livraison.");
            return;
        }
        
        try {
            int noLivraison = Integer.parseInt(noLivraisonStr);
            
            Livraison livraison = new Livraison();
            livraison.setNoLivraison(noLivraison);
            livraison.setDateLivraison(new Date(System.currentTimeMillis()));
            
            LivraisonDAO dao = new LivraisonDAO();
            dao.insert(livraison);
            
            for (int i = 0; i < detailsModel.getRowCount(); i++) {
                DetailLivraison detail = new DetailLivraison();
                detail.setNoLivraison(noLivraison);
                detail.setNoCommande((int) detailsModel.getValueAt(i, 0));
                detail.setNoArticle((int) detailsModel.getValueAt(i, 1));
                detail.setQuantiteLivree((int) detailsModel.getValueAt(i, 2));
                dao.insertDetailLivraison(detail);
            }
            
            JOptionPane.showMessageDialog(this,
                "Livraison enregistrée avec succès.",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            parent.showPanel(new LivraisonListPanel(parent));
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Le numéro de livraison doit être un nombre.",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
