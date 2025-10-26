package com.xyz.exam;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {
    public AdminFrame() {
        setTitle("Admin - XYZ Online Examination");
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

        JButton viewMarksButton = new JButton("View Attendees Marks");
        viewMarksButton.setBounds(150, 100, 300, 30);
        viewMarksButton.addActionListener(e -> {
            new AttendeesAdminFrame().setVisible(true);
        });
        panel.add(viewMarksButton);

        JButton setQuestionButton = new JButton("Set Question");
        setQuestionButton.setBounds(150, 150, 300, 30);
        setQuestionButton.addActionListener(e -> {
            dispose();
            new SetQuestionFrame().setVisible(true);
        });
        panel.add(setQuestionButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 200, 300, 30);
        logoutButton.addActionListener(e -> {
            dispose();
            new WelcomeFrame().setVisible(true);
        });
        panel.add(logoutButton);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminFrame().setVisible(true));
    }
}