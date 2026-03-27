package com.pepiniere.ui;

import com.pepiniere.dao.ClientDAO;
import com.pepiniere.dao.CommandeDAO;
import com.pepiniere.model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Panel pour afficher le montant total par client (fonction Oracle montant_client)
 */
public class MontantClientPanel extends JPanel {
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;
    private JButton btnCalculer;
    private JLabel lblTotal;
    
    public MontantClientPanel() {
        initComponents();
        setupLayout();
        setupListeners();
        loadData();
    }
    
    private void initComponents() {
        String[] columns = {"N° Client", "Nom", "Téléphone", "Montant Total (€)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UIStyles.styleTable(table);
        
        btnRefresh = UIStyles.createSecondaryButton("Actualiser");
        btnCalculer = UIStyles.createPrimaryButton("Calculer Montants");
        lblTotal = UIStyles.createTitleLabel("Total général: 0.00 €");
        lblTotal.setForeground(UIStyles.PRIMARY_COLOR);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        setBackground(UIStyles.BACKGROUND_COLOR);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        
        JLabel lblTitle = UIStyles.createTitleLabel("Montant Total par Client");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UIStyles.BACKGROUND_COLOR);
        buttonPanel.add(btnCalculer);
        buttonPanel.add(btnRefresh);
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        JPanel contentCard = UIStyles.createCard();
        contentCard.setLayout(new BorderLayout(0, 20));
        contentCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER_COLOR));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        contentCard.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIStyles.BORDER_COLOR));
        bottomPanel.add(lblTotal);
        
        contentCard.add(bottomPanel, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentCard, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void setupListeners() {
        btnRefresh.addActionListener(e -> loadData());
        btnCalculer.addActionListener(e -> calculerMontants());
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        try {
            ClientDAO dao = new ClientDAO();
            List<Client> clients = dao.findAll();
            for (Client c : clients) {
                tableModel.addRow(new Object[]{
                    c.getNoClient(),
                    c.getNomClient(),
                    c.getNoTelephone(),
                    "-"
                });
            }
        } catch (Exception e) {
            UIStyles.showErrorMessage(this, "Erreur lors du chargement: " + e.getMessage());
        }
    }
    
    private void calculerMontants() {
        BigDecimal totalGeneral = BigDecimal.ZERO;
        CommandeDAO commandeDAO = new CommandeDAO();
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        try {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int noClient = (int) tableModel.getValueAt(i, 0);
                BigDecimal montant = commandeDAO.getMontantClient(noClient);
                if (montant == null) montant = BigDecimal.ZERO;
                tableModel.setValueAt(montant, i, 3);
                totalGeneral = totalGeneral.add(montant);
            }
            
            lblTotal.setText("Total général: " + totalGeneral + " €");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du calcul: " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }
}
