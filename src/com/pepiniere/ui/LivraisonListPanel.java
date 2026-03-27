package com.pepiniere.ui;

import com.pepiniere.dao.LivraisonDAO;
import com.pepiniere.model.DetailLivraison;
import com.pepiniere.model.Livraison;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel d'affichage de la liste des livraisons
 */
public class LivraisonListPanel extends JPanel {
    
    private MainFrame parent;
    private JTable tableLivraisons;
    private JTable tableDetails;
    private DefaultTableModel livraisonModel;
    private DefaultTableModel detailModel;
    private JButton btnRefresh;
    private JButton btnDelete;
    private JButton btnNew;
    
    public LivraisonListPanel(MainFrame parent) {
        this.parent = parent;
        initComponents();
        setupLayout();
        setupListeners();
        loadData();
    }
    
    private void initComponents() {
        String[] columnsLiv = {"N° Livraison", "Date Livraison"};
        livraisonModel = new DefaultTableModel(columnsLiv, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableLivraisons = new JTable(livraisonModel);
        tableLivraisons.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIStyles.styleTable(tableLivraisons);
        
        String[] columnsDetail = {"N° Commande", "N° Article", "Quantité Livrée"};
        detailModel = new DefaultTableModel(columnsDetail, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDetails = new JTable(detailModel);
        UIStyles.styleTable(tableDetails);
        
        btnRefresh = UIStyles.createSecondaryButton("Actualiser");
        btnDelete = UIStyles.createDangerButton("Supprimer");
        btnNew = UIStyles.createPrimaryButton("Nouvelle");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UIStyles.BACKGROUND_COLOR);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        
        JLabel lblTitle = UIStyles.createTitleLabel("Liste des Livraisons");
        
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
        
        JPanel livraisonsWrapper = new JPanel(new BorderLayout(0, 10));
        livraisonsWrapper.setBackground(Color.WHITE);
        livraisonsWrapper.add(UIStyles.createHeadingLabel("Livraisons"), BorderLayout.NORTH);
        JScrollPane scrollLivraisons = new JScrollPane(tableLivraisons);
        scrollLivraisons.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER_COLOR));
        scrollLivraisons.getViewport().setBackground(Color.WHITE);
        scrollLivraisons.setPreferredSize(new Dimension(700, 250));
        livraisonsWrapper.add(scrollLivraisons, BorderLayout.CENTER);
        
        JPanel detailsWrapper = new JPanel(new BorderLayout(0, 10));
        detailsWrapper.setBackground(Color.WHITE);
        detailsWrapper.add(UIStyles.createHeadingLabel("Détails de la livraison sélectionnée"), BorderLayout.NORTH);
        JScrollPane scrollDetails = new JScrollPane(tableDetails);
        scrollDetails.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER_COLOR));
        scrollDetails.getViewport().setBackground(Color.WHITE);
        scrollDetails.setPreferredSize(new Dimension(700, 200));
        detailsWrapper.add(scrollDetails, BorderLayout.CENTER);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, livraisonsWrapper, detailsWrapper);
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
        btnNew.addActionListener(e -> parent.showPanel(new LivraisonFormPanel(parent)));
        btnDelete.addActionListener(e -> deleteSelected());
        
        tableLivraisons.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadDetails();
            }
        });
    }
    
    private void loadData() {
        livraisonModel.setRowCount(0);
        detailModel.setRowCount(0);
        try {
            LivraisonDAO dao = new LivraisonDAO();
            List<Livraison> livraisons = dao.findAll();
            for (Livraison l : livraisons) {
                livraisonModel.addRow(new Object[]{
                    l.getNoLivraison(),
                    l.getDateLivraison()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadDetails() {
        detailModel.setRowCount(0);
        int row = tableLivraisons.getSelectedRow();
        if (row < 0) return;
        
        try {
            int noLivraison = (int) livraisonModel.getValueAt(row, 0);
            LivraisonDAO dao = new LivraisonDAO();
            List<DetailLivraison> details = dao.findDetailsLivraison(noLivraison);
            for (DetailLivraison d : details) {
                detailModel.addRow(new Object[]{
                    d.getNoCommande(),
                    d.getNoArticle(),
                    d.getQuantiteLivree()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteSelected() {
        int row = tableLivraisons.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une livraison.",
                "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Voulez-vous vraiment supprimer cette livraison et ses détails?",
            "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int noLivraison = (int) livraisonModel.getValueAt(row, 0);
                LivraisonDAO dao = new LivraisonDAO();
                dao.delete(noLivraison);
                loadData();
                JOptionPane.showMessageDialog(this,
                    "Livraison supprimée avec succès.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
