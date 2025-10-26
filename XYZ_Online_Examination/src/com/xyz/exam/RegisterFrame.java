package com.xyz.exam;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterFrame extends JFrame {
    public RegisterFrame() {
        setTitle("Register - XYZ Online Examination");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Changed gradient to ocean light blue and soft yellow
                GradientPaint gp = new GradientPaint(0, 0, new Color(173, 216, 230), 0, getHeight(), new Color(255, 245, 157));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);

        // Fields
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setBounds(100, 50, 150, 30);
        panel.add(nameLabel);
        JTextField nameField = new JTextField();
        nameField.setBounds(250, 50, 200, 30);
        panel.add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.BLACK);
        emailLabel.setBounds(100, 100, 150, 30);
        panel.add(emailLabel);
        JTextField emailField = new JTextField();
        emailField.setBounds(250, 100, 200, 30);
        panel.add(emailField);

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setForeground(Color.BLACK);
        contactLabel.setBounds(100, 150, 150, 30);
        panel.add(contactLabel);
        JTextField contactField = new JTextField();
        contactField.setBounds(250, 150, 200, 30);
        panel.add(contactField);

        JLabel universityLabel = new JLabel("University/School:");
        universityLabel.setForeground(Color.BLACK);
        universityLabel.setBounds(100, 200, 150, 30);
        panel.add(universityLabel);
        JTextField universityField = new JTextField();
        universityField.setBounds(250, 200, 200, 30);
        panel.add(universityField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setForeground(Color.BLACK);
        genderLabel.setBounds(100, 250, 150, 30);
        panel.add(genderLabel);
        JRadioButton maleButton = new JRadioButton("Male");
        maleButton.setBounds(250, 250, 100, 30);
        maleButton.setOpaque(false);
        maleButton.setForeground(Color.BLACK);
        JRadioButton femaleButton = new JRadioButton("Female");
        femaleButton.setBounds(350, 250, 100, 30);
        femaleButton.setOpaque(false);
        femaleButton.setForeground(Color.BLACK);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        panel.add(maleButton);
        panel.add(femaleButton);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setForeground(Color.BLACK);
        roleLabel.setBounds(100, 300, 150, 30);
        panel.add(roleLabel);
        String[] roles = {"Admin", "Attendee"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        roleCombo.setBounds(250, 300, 200, 30);
        panel.add(roleCombo);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setBounds(100, 350, 150, 30);
        panel.add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(250, 350, 200, 30);
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setForeground(Color.BLACK);
        confirmPasswordLabel.setBounds(100, 400, 150, 30);
        panel.add(confirmPasswordLabel);
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(250, 400, 200, 30);
        panel.add(confirmPasswordField);

        // Buttons
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(100, 450, 100, 30);
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String contact = contactField.getText();
            String university = universityField.getText();
            String gender = maleButton.isSelected() ? "Male" : femaleButton.isSelected() ? "Female" : "";
            String role = (String) roleCombo.getSelectedItem();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!");
                return;
            }

            try (Connection conn = DatabaseConnection.getConnection()) {
                String table = role.equals("Admin") ? "Admin" : "Attendees";
                String query = "INSERT INTO " + table + " (name, email, contact, university, gender, password) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, contact);
                stmt.setString(4, university);
                stmt.setString(5, gender);
                stmt.setString(6, password);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose();
                new LoginFrame().setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: Email already exists or database issue!");
            }
        });
        panel.add(saveButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(250, 450, 100, 30);
        backButton.addActionListener(e -> {
            dispose();
            new WelcomeFrame().setVisible(true);
        });
        panel.add(backButton);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(400, 450, 100, 30);
        loginButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        panel.add(loginButton);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterFrame().setVisible(true));
    }
}