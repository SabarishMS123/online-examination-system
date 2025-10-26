package com.xyz.exam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomeFrame extends JFrame {
    public WelcomeFrame() {
        setTitle("XYZ Online Examination");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel with gradient background
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

        // Title
        JLabel titleLabel = new JLabel("XYZ Online Examination");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(150, 20, 300, 30);
        panel.add(titleLabel);

        // About Section
        JLabel aboutLabel = new JLabel("<html><b>About:</b><br>" +
                "1. We’re providing the best online service to the examining system<br>" +
                "2. Don’t cheat<br>" +
                "3. You’re here to test, not to downlift you<br>" +
                "4. Timer is set by the staff for n number of minutes. Please Login or Register to attend the exam</html>");
        aboutLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        aboutLabel.setForeground(Color.BLACK);
        aboutLabel.setBounds(50, 60, 500, 150);
        panel.add(aboutLabel);

        // Buttons
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 250, 100, 30);
        loginButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        panel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(300, 250, 100, 30);
        registerButton.addActionListener(e -> {
            dispose();
            new RegisterFrame().setVisible(true);
        });
        panel.add(registerButton);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeFrame().setVisible(true));
    }
}