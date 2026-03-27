package com.pepiniere.ui;

import com.pepiniere.model.UserRole;
import com.pepiniere.util.DatabaseConnection;
import com.pepiniere.util.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Fenêtre de connexion avec gestion des profils MAGASINIER et RESPONSABLE
 */
public class LoginFrame extends JFrame {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JButton btnLogin;
    private JButton btnCancel;
    
    public LoginFrame() {
        initComponents();
        setupLayout();
        setupListeners();
    }
    
    private void initComponents() {
        setTitle("Pépinière Plein de Foin - Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        txtUsername = UIStyles.createStyledTextField();
        txtPassword = new JPasswordField();
        txtPassword.setFont(UIStyles.FONT_BODY);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyles.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtPassword.setPreferredSize(new Dimension(250, 38));
        
        cmbRole = new JComboBox<>(new String[]{
            UserRole.MAGASINIER.getDisplayName(),
            UserRole.RESPONSABLE.getDisplayName()
        });
        cmbRole.setFont(UIStyles.FONT_BODY);
        cmbRole.setPreferredSize(new Dimension(250, 38));
        
        btnLogin = UIStyles.createPrimaryButton("Connexion");
        btnCancel = UIStyles.createOutlineButton("Annuler");
        
        txtUsername.setText("Dev");
    }
    
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UIStyles.PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel lblTitle = new JLabel("Pépinière Plein de Foin");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel("Gestion Commerciale");
        lblSubtitle.setFont(UIStyles.FONT_BODY);
        lblSubtitle.setForeground(new Color(230, 255, 230));
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        headerPanel.add(lblTitle);
        headerPanel.add(lblSubtitle);
        
        JPanel cardPanel = UIStyles.createCard();
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(30, 40, 30, 40),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        cardPanel.add(UIStyles.createLabel("Utilisateur"), gbc);
        
        gbc.gridy = 1;
        cardPanel.add(txtUsername, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        cardPanel.add(UIStyles.createLabel("Mot de passe"), gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 10, 0);
        cardPanel.add(txtPassword, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 0, 10, 0);
        cardPanel.add(UIStyles.createLabel("Profil"), gbc);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 20, 0);
        cardPanel.add(cmbRole, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnCancel);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 0, 0, 0);
        cardPanel.add(buttonPanel, gbc);
        
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(UIStyles.BACKGROUND_COLOR);
        centerWrapper.add(cardPanel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);
        
        add(mainPanel);
        setSize(500, 600);
        setLocationRelativeTo(null);
    }
    
    private void setupListeners() {
        btnLogin.addActionListener(this::handleLogin);
        btnCancel.addActionListener(e -> System.exit(0));
        
        txtPassword.addActionListener(this::handleLogin);
    }
    
    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String selectedRole = (String) cmbRole.getSelectedItem();
        
        if (username.isEmpty()) {
            UIStyles.showErrorMessage(this, "Veuillez saisir un nom d'utilisateur.");
            return;
        }
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        btnLogin.setEnabled(false);
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return DatabaseConnection.getInstance().testConnection(username, password);
            }
            
            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                btnLogin.setEnabled(true);
                
                try {
                    boolean success = get();
                    if (success) {
                        UserRole role = selectedRole.equals(UserRole.RESPONSABLE.getDisplayName()) 
                            ? UserRole.RESPONSABLE : UserRole.MAGASINIER;
                        
                        SessionManager.getInstance().login(username, role);
                        DatabaseConnection.getInstance().getConnection(username, password);
                        
                        MainFrame mainFrame = new MainFrame();
                        mainFrame.setVisible(true);
                        dispose();
                    } else {
                        UIStyles.showErrorMessage(LoginFrame.this,
                            "Identifiants incorrects ou base de données inaccessible.");
                        txtPassword.setText("");
                        txtPassword.requestFocus();
                    }
                } catch (Exception ex) {
                    UIStyles.showErrorMessage(LoginFrame.this, "Erreur: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }
}
