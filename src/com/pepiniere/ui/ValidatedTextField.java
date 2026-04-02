package com.pepiniere.ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Predicate;

/**
 * Champ de texte avec validation en temps réel et feedback visuel
 */
public class ValidatedTextField extends JPanel {
    
    private JTextField textField;
    private JLabel feedbackLabel;
    private Predicate<String> validator;
    private String validationMessage;
    private boolean isValid = true;
    
    private static final Color VALID_COLOR = UIStyles.SUCCESS_COLOR;
    private static final Color INVALID_COLOR = UIStyles.DANGER_COLOR;
    private static final Color NEUTRAL_COLOR = UIStyles.BORDER_COLOR;
    
    public ValidatedTextField(String placeholder, Predicate<String> validator, String validationMessage) {
        this.validator = validator;
        this.validationMessage = validationMessage;
        
        setLayout(new BorderLayout(0, 6));
        setOpaque(false);
        
        // Champ de texte
        textField = UIStyles.createStyledTextField();
        textField.setText(placeholder);
        textField.setForeground(UIStyles.TEXT_SECONDARY);
        
        // Label de feedback
        feedbackLabel = new JLabel();
        feedbackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        feedbackLabel.setForeground(NEUTRAL_COLOR);
        
        // Listener pour validation en temps réel
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validate();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                validate();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                validate();
            }
        });
        
        add(textField, BorderLayout.CENTER);
        add(feedbackLabel, BorderLayout.SOUTH);
    }
    
    /**
     * Valide le contenu du champ
     */
    private void validate() {
        String text = textField.getText().trim();
        
        if (text.isEmpty()) {
            isValid = false;
            feedbackLabel.setText("");
            feedbackLabel.setForeground(NEUTRAL_COLOR);
            textField.setBorder(BorderFactory.createLineBorder(NEUTRAL_COLOR, 1));
            return;
        }
        
        isValid = validator.test(text);
        
        if (isValid) {
            feedbackLabel.setText("✓ Valide");
            feedbackLabel.setForeground(VALID_COLOR);
            textField.setBorder(BorderFactory.createLineBorder(VALID_COLOR, 1));
        } else {
            feedbackLabel.setText("✕ " + validationMessage);
            feedbackLabel.setForeground(INVALID_COLOR);
            textField.setBorder(BorderFactory.createLineBorder(INVALID_COLOR, 1));
        }
    }
    
    /**
     * Retourne le texte du champ
     */
    public String getText() {
        return textField.getText().trim();
    }
    
    /**
     * Définit le texte du champ
     */
    public void setText(String text) {
        textField.setText(text);
        validate();
    }
    
    /**
     * Retourne si le champ est valide
     */
    public boolean isValid() {
        return isValid && !getText().isEmpty();
    }
    
    /**
     * Réinitialise le champ
     */
    public void reset() {
        textField.setText("");
        feedbackLabel.setText("");
        isValid = false;
    }
    
    /**
     * Demande le focus au champ
     */
    @Override
    public void requestFocus() {
        textField.requestFocus();
    }
}
