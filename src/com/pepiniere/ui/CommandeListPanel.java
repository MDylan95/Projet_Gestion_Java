package com.pepiniere.ui;

import com.pepiniere.dao.CommandeDAO;
import com.pepiniere.model.Commande;
import com.pepiniere.model.LigneCommande;
import com.pepiniere.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel d'affichage de la liste des commandes
 */
public class CommandeListPanel extends JPanel {
    
    private MainFrame parent;
    private JTable tableCommandes;
    private JTable tableLignes;
    private DefaultTableModel commandeModel;
    private DefaultTableModel ligneModel;
    private JButton btnRefresh;
    private JButton btnDelete;
    private JButton btnNew;
    
    public CommandeListPanel(MainFrame parent) {
        this.parent = parent;
        initComponents();
        setupLayout();
        setupListeners();
        loadData();
    }
    
    private void initComponents() {
        String[] columnsCmd = {"N° Commande", "Date", "N° Client", "Nom Client"};
        commandeModel = new DefaultTableModel(columnsCmd, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableCommandes = new JTable(commandeModel);
        tableCommandes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIStyles.styleTable(tableCommandes);
        
        String[] columnsLigne = {"N° Article", "Description", "Quantité"};
        ligneModel = new DefaultTableModel(columnsLigne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableLignes = new JTable(ligneModel);
        UIStyles.styleTable(tableLignes);
        
        btnRefresh = UIStyles.createSecondaryButton("Actualiser");
        btnDelete = UIStyles.createDangerButton("Supprimer");
        btnNew = UIStyles.createPrimaryButton("Nouvelle");
        
        boolean isResponsable = SessionManager.getInstance().isResponsable();
        btnNew.setEnabled(isResponsable);
        btnDelete.setEnabled(isResponsable);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UIStyles.BACKGROUND_COLOR);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        
        JLabel lblTitle = UIStyles.createTitleLabel("Liste des Commandes");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        buttonPanel.add(btnNew);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel contentCard = UIStyles.createCard();
        contentCard.setLayout(new BorderLayout(0, 20));
        contentCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel commandesWrapper = new JPanel(new BorderLayout(0, 10));
        commandesWrapper.setBackground(Color.WHITE);
        commandesWrapper.add(UIStyles.createHeadingLabel("Commandes"), BorderLayout.NORTH);
        JScrollPane scrollCommandes = new JScrollPane(tableCommandes);
        scrollCommandes.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER_COLOR));
        scrollCommandes.getViewport().setBackground(Color.WHITE);
        scrollCommandes.setPreferredSize(new Dimension(700, 250));
        commandesWrapper.add(scrollCommandes, BorderLayout.CENTER);
        
        JPanel lignesWrapper = new JPanel(new BorderLayout(0, 10));
        lignesWrapper.setBackground(Color.WHITE);
        lignesWrapper.add(UIStyles.createHeadingLabel("Détails de la commande sélectionnée"), BorderLayout.NORTH);
        JScrollPane scrollLignes = new JScrollPane(tableLignes);
        scrollLignes.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER_COLOR));
        scrollLignes.getViewport().setBackground(Color.WHITE);
        scrollLignes.setPreferredSize(new Dimension(700, 200));
        lignesWrapper.add(scrollLignes, BorderLayout.CENTER);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, commandesWrapper, lignesWrapper);
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(10);
        splitPane.setBorder(null);
        splitPane.setBackground(Color.WHITE);
        
        contentCard.add(splitPane, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentCard, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupListeners() {
        btnRefresh.addActionListener(e -> loadData());
        btnNew.addActionListener(e -> parent.showPanel(new CommandeFormPanel(parent)));
        btnDelete.addActionListener(e -> deleteSelected());
        
        tableCommandes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadLignesCommande();
            }
        });
    }
    
    private void loadData() {
        commandeModel.setRowCount(0);
        ligneModel.setRowCount(0);
        try {
            CommandeDAO dao = new CommandeDAO();
            List<Commande> commandes = dao.findAll();
            for (Commande c : commandes) {
                commandeModel.addRow(new Object[]{
                    c.getNoCommande(),
                    c.getDateCommande(),
                    c.getNoClient(),
                    c.getNomClient()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadLignesCommande() {
        ligneModel.setRowCount(0);
        int row = tableCommandes.getSelectedRow();
        if (row < 0) return;
        
        try {
            int noCommande = (int) commandeModel.getValueAt(row, 0);
            CommandeDAO dao = new CommandeDAO();
            List<LigneCommande> lignes = dao.findLignesCommande(noCommande);
            for (LigneCommande l : lignes) {
                ligneModel.addRow(new Object[]{
                    l.getNoArticle(),
                    l.getDescriptionArticle(),
                    l.getQuantite()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteSelected() {
        int row = tableCommandes.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une commande.",
                "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Voulez-vous vraiment supprimer cette commande et ses lignes?",
            "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int noCommande = (int) commandeModel.getValueAt(row, 0);
                CommandeDAO dao = new CommandeDAO();
                dao.delete(noCommande);
                loadData();
                JOptionPane.showMessageDialog(this,
                    "Commande supprimée avec succès.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
