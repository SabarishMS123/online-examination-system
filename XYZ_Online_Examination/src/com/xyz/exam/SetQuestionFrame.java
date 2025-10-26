package com.xyz.exam;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SetQuestionFrame extends JFrame {
    public SetQuestionFrame() {
        setTitle("Set Questions - XYZ Online Examination");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel with gradient background
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
        panel.setPreferredSize(new Dimension(780, 1300)); // Set a larger preferred size to accommodate all questions

        // Array to hold question text areas, option fields, and correct answer fields
        JTextArea[] questionAreas = new JTextArea[5];
        JTextField[][] optionFields = new JTextField[5][4];
        JTextField[] correctAnswerFields = new JTextField[5];

        int y = 20;
        for (int i = 0; i < 5; i++) {
            // Question
            JLabel questionLabel = new JLabel("Question " + (i + 1) + ":");
            questionLabel.setForeground(Color.BLACK);
            questionLabel.setBounds(20, y, 100, 30);
            panel.add(questionLabel);

            questionAreas[i] = new JTextArea();
            questionAreas[i].setLineWrap(true);
            questionAreas[i].setWrapStyleWord(true);
            JScrollPane questionScroll = new JScrollPane(questionAreas[i]);
            questionScroll.setBounds(120, y, 600, 60);
            panel.add(questionScroll);

            // Options
            for (int j = 0; j < 4; j++) {
                JLabel optionLabel = new JLabel("Option " + (j + 1) + ":");
                optionLabel.setForeground(Color.BLACK);
                optionLabel.setBounds(120, y + 70 + j * 30, 100, 30);
                panel.add(optionLabel);

                optionFields[i][j] = new JTextField();
                optionFields[i][j].setBounds(220, y + 70 + j * 30, 500, 30);
                panel.add(optionFields[i][j]);
            }

            // Correct Answer
            JLabel correctLabel = new JLabel("Correct Answer:");
            correctLabel.setForeground(Color.BLACK);
            correctLabel.setBounds(120, y + 190, 100, 30);
            panel.add(correctLabel);

            correctAnswerFields[i] = new JTextField();
            correctAnswerFields[i].setBounds(220, y + 190, 500, 30);
            panel.add(correctAnswerFields[i]);

            y += 250; // Increased spacing to avoid overlap
        }

        // Save Button
        JButton saveButton = new JButton("Save Questions");
        saveButton.setBounds(300, y, 150, 30);
        saveButton.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO Questions (question_text, option1, option2, option3, option4, correct_answer) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                for (int i = 0; i < 5; i++) {
                    stmt.setString(1, questionAreas[i].getText());
                    stmt.setString(2, optionFields[i][0].getText());
                    stmt.setString(3, optionFields[i][1].getText());
                    stmt.setString(4, optionFields[i][2].getText());
                    stmt.setString(5, optionFields[i][3].getText());
                    stmt.setString(6, correctAnswerFields[i].getText());
                    stmt.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "Questions saved successfully!");
                dispose();
                new AdminFrame().setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error!");
            }
        });
        panel.add(saveButton);

        // Add the panel to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        // Ensure the frame packs to fit the scroll pane
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SetQuestionFrame().setVisible(true));
    }
}