package com.pepiniere.ui;

import com.pepiniere.dao.ArticleDAO;
import com.pepiniere.model.Article;
import com.pepiniere.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel d'affichage de la liste des articles
 */
public class ArticleListPanel extends JPanel {
    
    private MainFrame parent;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnNew;
    
    public ArticleListPanel(MainFrame parent) {
        this.parent = parent;
        initComponents();
        setupLayout();
        setupListeners();
        loadData();
    }
    
    private void initComponents() {
        String[] columns = {"N° Article", "Description", "Prix Unitaire", "Quantité en Stock"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        UIStyles.styleTable(table);
        
        btnRefresh = UIStyles.createSecondaryButton("Actualiser");
        btnEdit = UIStyles.createPrimaryButton("Modifier");
        btnDelete = UIStyles.createDangerButton("Supprimer");
        btnNew = UIStyles.createPrimaryButton("Nouveau");
        
        boolean isResponsable = SessionManager.getInstance().isResponsable();
        btnEdit.setEnabled(isResponsable);
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
        
        JLabel lblTitle = UIStyles.createTitleLabel("Liste des Articles");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        buttonPanel.add(btnNew);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel tableCard = UIStyles.createCard();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tableCard, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupListeners() {
        btnRefresh.addActionListener(e -> loadData());
        btnNew.addActionListener(e -> parent.showPanel(new ArticleFormPanel(parent)));
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        try {
            ArticleDAO dao = new ArticleDAO();
            List<Article> articles = dao.findAll();
            for (Article a : articles) {
                tableModel.addRow(new Object[]{
                    a.getNoArticle(),
                    a.getDescription(),
                    a.getPrixUnitaire(),
                    a.getQuantiteEnStock()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un article.",
                "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            int noArticle = (int) tableModel.getValueAt(row, 0);
            ArticleDAO dao = new ArticleDAO();
            Article article = dao.findById(noArticle);
            if (article != null) {
                parent.showPanel(new ArticleFormPanel(parent, article));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un article.",
                "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Voulez-vous vraiment supprimer cet article?",
            "Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int noArticle = (int) tableModel.getValueAt(row, 0);
                ArticleDAO dao = new ArticleDAO();
                dao.delete(noArticle);
                loadData();
                JOptionPane.showMessageDialog(this,
                    "Article supprimé avec succès.",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur: " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
