package com.xyz.exam;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("Login - XYZ Online Examination");
        setSize(600, 400);
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

        // Username
        JLabel usernameLabel = new JLabel("Username (Email):");
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setBounds(100, 100, 150, 30);
        panel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(250, 100, 200, 30);
        panel.add(usernameField);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setBounds(100, 150, 150, 30);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(250, 150, 200, 30);
        panel.add(passwordField);

        // Buttons
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 200, 100, 30);
        loginButton.addActionListener(e -> {
            String email = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try (Connection conn = DatabaseConnection.getConnection()) {
                // Check Admin table
                String adminQuery = "SELECT * FROM Admin WHERE email = ? AND password = ?";
                PreparedStatement adminStmt = conn.prepareStatement(adminQuery);
                adminStmt.setString(1, email);
                adminStmt.setString(2, password);
                ResultSet adminRs = adminStmt.executeQuery();

                if (adminRs.next()) {
                    dispose();
                    new AdminFrame().setVisible(true);
                    return;
                }

                // Check Attendees table
                String attendeeQuery = "SELECT * FROM Attendees WHERE email = ? AND password = ?";
                PreparedStatement attendeeStmt = conn.prepareStatement(attendeeQuery);
                attendeeStmt.setString(1, email);
                attendeeStmt.setString(2, password);
                ResultSet attendeeRs = attendeeStmt.executeQuery();

                if (attendeeRs.next()) {
                    dispose();
                    new ExamFrame(email).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error!");
            }
        });
        panel.add(loginButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(300, 200, 100, 30);
        backButton.addActionListener(e -> {
            dispose();
            new WelcomeFrame().setVisible(true);
        });
        panel.add(backButton);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}