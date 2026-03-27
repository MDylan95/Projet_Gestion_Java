package com.pepiniere.ui;

import com.pepiniere.dao.ClientDAO;
import com.pepiniere.model.Client;

import javax.swing.*;
import java.awt.*;

/**
 * Formulaire de création/modification de client
 */
public class ClientFormPanel extends JPanel {
    
    private MainFrame parent;
    private Client client;
    private boolean editMode;
    
    private JTextField txtNoClient;
    private JTextField txtNomClient;
    private JTextField txtTelephone;
    private JButton btnSave;
    private JButton btnCancel;
    
    public ClientFormPanel(MainFrame parent) {
        this(parent, null);
    }
    
    public ClientFormPanel(MainFrame parent, Client client) {
        this.parent = parent;
        this.client = client;
        this.editMode = (client != null);
        initComponents();
        setupLayout();
        setupListeners();
        if (editMode) {
            loadClient();
        }
    }
    
    private void initComponents() {
        txtNoClient = UIStyles.createStyledTextField();
        txtNomClient = UIStyles.createStyledTextField();
        txtTelephone = UIStyles.createStyledTextField();
        btnSave = UIStyles.createPrimaryButton(editMode ? "Modifier" : "Créer");
        btnCancel = UIStyles.createOutlineButton("Annuler");
        
        if (editMode) {
            txtNoClient.setEditable(false);
            txtNoClient.setBackground(UIStyles.BACKGROUND_COLOR);
        }
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UIStyles.BACKGROUND_COLOR);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitle = UIStyles.createTitleLabel(
            editMode ? "Modifier un Client" : "Nouveau Client");
        
        JPanel formCard = UIStyles.createCard();
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formCard.add(UIStyles.createLabel("N° Client"), gbc);
        gbc.gridy = 1;
        formCard.add(txtNoClient, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 12, 0);
        formCard.add(UIStyles.createLabel("Nom du Client"), gbc);
        gbc.gridy = 3;
        gbc.insets = new Insets(12, 0, 12, 0);
        formCard.add(txtNomClient, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 0, 12, 0);
        formCard.add(UIStyles.createLabel("Téléphone"), gbc);
        gbc.gridy = 5;
        gbc.insets = new Insets(12, 0, 12, 0);
        formCard.add(txtTelephone, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(30, 0, 0, 0);
        formCard.add(buttonPanel, gbc);
        
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formCard, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupListeners() {
        btnSave.addActionListener(e -> saveClient());
        btnCancel.addActionListener(e -> parent.showPanel(new ClientListPanel(parent)));
    }
    
    private void loadClient() {
        txtNoClient.setText(String.valueOf(client.getNoClient()));
        txtNomClient.setText(client.getNomClient());
        txtTelephone.setText(client.getNoTelephone());
    }
    
    private void saveClient() {
        String noClientStr = txtNoClient.getText().trim();
        String nomClient = txtNomClient.getText().trim();
        String telephone = txtTelephone.getText().trim();
        
        if (noClientStr.isEmpty() || nomClient.isEmpty()) {
            UIStyles.showErrorMessage(this, "Veuillez remplir le numéro et le nom du client.");
            return;
        }
        
        try {
            int noClient = Integer.parseInt(noClientStr);
            
            ClientDAO dao = new ClientDAO();
            
            if (editMode) {
                client.setNomClient(nomClient);
                client.setNoTelephone(telephone);
                dao.update(client);
                UIStyles.showSuccessMessage(this, "Client modifié avec succès.");
            } else {
                Client newClient = new Client(noClient, nomClient, telephone);
                dao.insert(newClient);
                UIStyles.showSuccessMessage(this, "Client créé avec succès.");
            }
            
            parent.showPanel(new ClientListPanel(parent));
            
        } catch (NumberFormatException e) {
            UIStyles.showErrorMessage(this, "Le numéro de client doit être un nombre valide.");
        } catch (Exception e) {
            UIStyles.showErrorMessage(this, "Erreur: " + e.getMessage());
        }
    }
}
