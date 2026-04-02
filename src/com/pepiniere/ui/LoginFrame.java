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
    
    public LoginFrame() {
        initComponents();
        setupLayout();
        setupListeners();
    }
    
    private void initComponents() {
        setTitle("Pepiniere Plein de Foin - Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        txtUsername = UIStyles.createStyledTextField();
        txtPassword = UIStyles.createStyledPasswordField();
        
        cmbRole = new JComboBox<>(new String[]{
            UserRole.MAGASINIER.getDisplayName(),
            UserRole.RESPONSABLE.getDisplayName()
        });
        cmbRole.setFont(UIStyles.FONT_BODY);
        cmbRole.setPreferredSize(new Dimension(280, 42));
        cmbRole.setBackground(Color.WHITE);
        
        btnLogin = UIStyles.createPrimaryButton("Se connecter");
        btnLogin.setPreferredSize(new Dimension(280, 44));
        btnLogin.setMaximumSize(new Dimension(280, 44));
        
        txtUsername.setText("Dev");
    }
    
    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        
        // --- Panneau gauche : gradient vert avec branding ---
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(39, 135, 66), 0, getHeight(), new Color(52, 168, 83));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        leftPanel.setPreferredSize(new Dimension(340, 0));
        leftPanel.setLayout(new GridBagLayout());
        
        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        
        // Cercle decoratif
        JPanel circlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillOval(10, 10, 80, 80);
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fillOval(25, 25, 50, 50);
                g2.dispose();
            }
        };
        circlePanel.setOpaque(false);
        circlePanel.setPreferredSize(new Dimension(100, 100));
        circlePanel.setMaximumSize(new Dimension(100, 100));
        circlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblBrand = new JLabel("Pepiniere");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblBrand2 = new JLabel("Plein de Foin");
        lblBrand2.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblBrand2.setForeground(Color.WHITE);
        lblBrand2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTagline = new JLabel("Gestion Commerciale");
        lblTagline.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblTagline.setForeground(new Color(255, 255, 255, 200));
        lblTagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTagline.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        
        // Ligne decorative
        JPanel line = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 80));
                g2.fillRoundRect(0, 0, getWidth(), 2, 2, 2);
                g2.dispose();
            }
        };
        line.setOpaque(false);
        line.setPreferredSize(new Dimension(180, 2));
        line.setMaximumSize(new Dimension(180, 2));
        line.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblDesc = new JLabel("<html><div style='text-align:center;'>Gerez vos articles, clients,<br>commandes et livraisons</div></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(255, 255, 255, 160));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDesc.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        
        brandPanel.add(circlePanel);
        brandPanel.add(Box.createVerticalStrut(20));
        brandPanel.add(lblBrand);
        brandPanel.add(lblBrand2);
        brandPanel.add(lblTagline);
        brandPanel.add(Box.createVerticalStrut(20));
        brandPanel.add(line);
        brandPanel.add(lblDesc);
        
        leftPanel.add(brandPanel);
        
        // --- Panneau droit : formulaire de connexion ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        rightPanel.setPreferredSize(new Dimension(420, 0));
        
        JPanel formWrapper = new JPanel();
        formWrapper.setOpaque(false);
        formWrapper.setLayout(new BoxLayout(formWrapper, BoxLayout.Y_AXIS));
        formWrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        
        JLabel lblWelcome = new JLabel("Bienvenue !");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblWelcome.setForeground(UIStyles.TEXT_PRIMARY);
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblConnectMsg = new JLabel("Connectez-vous pour continuer");
        lblConnectMsg.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblConnectMsg.setForeground(UIStyles.TEXT_SECONDARY);
        lblConnectMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblConnectMsg.setBorder(BorderFactory.createEmptyBorder(6, 0, 30, 0));
        
        JLabel lblUser = new JLabel("Utilisateur");
        lblUser.setFont(UIStyles.FONT_HEADING);
        lblUser.setForeground(UIStyles.TEXT_PRIMARY);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblPass = new JLabel("Mot de passe");
        lblPass.setFont(UIStyles.FONT_HEADING);
        lblPass.setForeground(UIStyles.TEXT_PRIMARY);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblProfil = new JLabel("Profil");
        lblProfil.setFont(UIStyles.FONT_HEADING);
        lblProfil.setForeground(UIStyles.TEXT_PRIMARY);
        lblProfil.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cmbRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        cmbRole.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        
        formWrapper.add(lblWelcome);
        formWrapper.add(lblConnectMsg);
        formWrapper.add(lblUser);
        formWrapper.add(Box.createVerticalStrut(8));
        formWrapper.add(txtUsername);
        formWrapper.add(Box.createVerticalStrut(18));
        formWrapper.add(lblPass);
        formWrapper.add(Box.createVerticalStrut(8));
        formWrapper.add(txtPassword);
        formWrapper.add(Box.createVerticalStrut(18));
        formWrapper.add(lblProfil);
        formWrapper.add(Box.createVerticalStrut(8));
        formWrapper.add(cmbRole);
        formWrapper.add(Box.createVerticalStrut(28));
        formWrapper.add(btnLogin);
        
        rightPanel.add(formWrapper);
        
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        setSize(760, 520);
        setLocationRelativeTo(null);
    }
    
    private void setupListeners() {
        btnLogin.addActionListener(this::handleLogin);
        txtPassword.addActionListener(this::handleLogin);
    }
    
    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String selectedRole = (String) cmbRole.getSelectedItem();
        
        if (username.isEmpty()) {
            ToastNotification.error(this, "Veuillez saisir un nom d'utilisateur.");
            return;
        }
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        btnLogin.setEnabled(false);
        
        LoadingSpinner spinner = new LoadingSpinner(this, "Connexion en cours...");
        
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return DatabaseConnection.getInstance().testConnection(username, password);
            }
            
            @Override
            protected void done() {
                spinner.hideSpinner();
                setCursor(Cursor.getDefaultCursor());
                btnLogin.setEnabled(true);
                
                try {
                    boolean success = get();
                    if (success) {
                        UserRole role = selectedRole.equals(UserRole.RESPONSABLE.getDisplayName()) 
                            ? UserRole.RESPONSABLE : UserRole.MAGASINIER;
                        
                        SessionManager.getInstance().login(username, role);
                        DatabaseConnection.getInstance().getConnection(username, password);
                        
                        ToastNotification.success(LoginFrame.this, "Connexion reussie !");
                        
                        SwingUtilities.invokeLater(() -> {
                            MainFrame mainFrame = new MainFrame();
                            mainFrame.setVisible(true);
                            dispose();
                        });
                    } else {
                        ToastNotification.error(LoginFrame.this,
                            "Identifiants incorrects ou base de donnees inaccessible.");
                        txtPassword.setText("");
                        txtPassword.requestFocus();
                    }
                } catch (Exception ex) {
                    ToastNotification.error(LoginFrame.this, "Erreur: " + ex.getMessage());
                }
            }
        };
        spinner.showSpinner();
        worker.execute();
    }
}
