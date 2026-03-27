package com.pepiniere.ui;

import com.pepiniere.dao.ArticleDAO;
import com.pepiniere.model.Article;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/**
 * Formulaire de création/modification d'article
 */
public class ArticleFormPanel extends JPanel {
    
    private MainFrame parent;
    private Article article;
    private boolean editMode;
    
    private JTextField txtNoArticle;
    private JTextField txtDescription;
    private JTextField txtPrixUnitaire;
    private JTextField txtQuantiteStock;
    private JButton btnSave;
    private JButton btnCancel;
    
    public ArticleFormPanel(MainFrame parent) {
        this(parent, null);
    }
    
    public ArticleFormPanel(MainFrame parent, Article article) {
        this.parent = parent;
        this.article = article;
        this.editMode = (article != null);
        initComponents();
        setupLayout();
        setupListeners();
        if (editMode) {
            loadArticle();
        }
    }
    
    private void initComponents() {
        txtNoArticle = UIStyles.createStyledTextField();
        txtDescription = UIStyles.createStyledTextField();
        txtPrixUnitaire = UIStyles.createStyledTextField();
        txtQuantiteStock = UIStyles.createStyledTextField();
        btnSave = UIStyles.createPrimaryButton(editMode ? "Modifier" : "Créer");
        btnCancel = UIStyles.createOutlineButton("Annuler");
        
        txtNoArticle.setEditable(false);
        txtNoArticle.setBackground(UIStyles.BACKGROUND_COLOR);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UIStyles.BACKGROUND_COLOR);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitle = UIStyles.createTitleLabel(
            editMode ? "Modifier un Article" : "Nouvel Article");
        
        JPanel formCard = UIStyles.createCard();
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        if (editMode) {
            gbc.gridx = 0; gbc.gridy = row++;
            formCard.add(UIStyles.createLabel("N° Article"), gbc);
            gbc.gridy = row++;
            formCard.add(txtNoArticle, gbc);
            gbc.gridy = row++;
            gbc.insets = new Insets(20, 0, 12, 0);
        }
        
        formCard.add(UIStyles.createLabel("Description"), gbc);
        gbc.gridy = row++;
        gbc.insets = new Insets(12, 0, 12, 0);
        formCard.add(txtDescription, gbc);
        
        gbc.gridy = row++;
        gbc.insets = new Insets(20, 0, 12, 0);
        formCard.add(UIStyles.createLabel("Prix Unitaire (€)"), gbc);
        gbc.gridy = row++;
        gbc.insets = new Insets(12, 0, 12, 0);
        formCard.add(txtPrixUnitaire, gbc);
        
        gbc.gridy = row++;
        gbc.insets = new Insets(20, 0, 12, 0);
        formCard.add(UIStyles.createLabel("Quantité en Stock"), gbc);
        gbc.gridy = row++;
        gbc.insets = new Insets(12, 0, 12, 0);
        formCard.add(txtQuantiteStock, gbc);
        
        if (!editMode) {
            gbc.gridy = row++;
            gbc.insets = new Insets(20, 0, 12, 0);
            JLabel lblInfo = new JLabel("<html><i>💡 L'ID sera généré automatiquement (séquence Oracle)</i></html>");
            lblInfo.setFont(UIStyles.FONT_SMALL);
            lblInfo.setForeground(UIStyles.TEXT_SECONDARY);
            formCard.add(lblInfo, gbc);
        }
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        gbc.gridy = row++;
        gbc.insets = new Insets(30, 0, 0, 0);
        formCard.add(buttonPanel, gbc);
        
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formCard, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupListeners() {
        btnSave.addActionListener(e -> saveArticle());
        btnCancel.addActionListener(e -> parent.showPanel(new ArticleListPanel(parent)));
    }
    
    private void loadArticle() {
        txtNoArticle.setText(String.valueOf(article.getNoArticle()));
        txtDescription.setText(article.getDescription());
        txtPrixUnitaire.setText(article.getPrixUnitaire().toString());
        txtQuantiteStock.setText(String.valueOf(article.getQuantiteEnStock()));
    }
    
    private void saveArticle() {
        String description = txtDescription.getText().trim();
        String prixStr = txtPrixUnitaire.getText().trim();
        String quantiteStr = txtQuantiteStock.getText().trim();
        
        if (description.isEmpty() || prixStr.isEmpty() || quantiteStr.isEmpty()) {
            UIStyles.showErrorMessage(this, "Veuillez remplir tous les champs.");
            return;
        }
        
        try {
            BigDecimal prix = new BigDecimal(prixStr);
            int quantite = Integer.parseInt(quantiteStr);
            
            if (prix.compareTo(BigDecimal.ZERO) < 0 || quantite < 0) {
                UIStyles.showErrorMessage(this, "Le prix et la quantité doivent être positifs.");
                return;
            }
            
            ArticleDAO dao = new ArticleDAO();
            
            if (editMode) {
                article.setDescription(description);
                article.setPrixUnitaire(prix);
                article.setQuantiteEnStock(quantite);
                dao.update(article);
                UIStyles.showSuccessMessage(this, "Article modifié avec succès.");
            } else {
                Article newArticle = new Article();
                newArticle.setDescription(description);
                newArticle.setPrixUnitaire(prix);
                newArticle.setQuantiteEnStock(quantite);
                int newId = dao.insert(newArticle);
                UIStyles.showSuccessMessage(this, "Article créé avec succès (ID: " + newId + ").");
            }
            
            parent.showPanel(new ArticleListPanel(parent));
            
        } catch (NumberFormatException e) {
            UIStyles.showErrorMessage(this, "Veuillez entrer des valeurs numériques valides.");
        } catch (Exception e) {
            UIStyles.showErrorMessage(this, "Erreur: " + e.getMessage());
        }
    }
}
