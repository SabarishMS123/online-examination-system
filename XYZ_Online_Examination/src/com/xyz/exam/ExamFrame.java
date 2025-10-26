package com.xyz.exam;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class ExamFrame extends JFrame {
    private String userEmail;
    private JRadioButton[][] optionButtons;
    private int timeLeft = 300; // 5 minutes in seconds
    private JLabel timerLabel;

    public ExamFrame(String email) {
        this.userEmail = email;
        setTitle("Exam - XYZ Online Examination");
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
        panel.setPreferredSize(new Dimension(780, 1600)); // Maintains sufficient height for all content

        // Timer
        timerLabel = new JLabel("Time Left: 5:00");
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setBounds(600, 20, 100, 30);
        panel.add(timerLabel);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeLeft--;
                int minutes = timeLeft / 60;
                int seconds = timeLeft % 60;
                timerLabel.setText(String.format("Time Left: %d:%02d", minutes, seconds));
                if (timeLeft <= 0) {
                    timer.cancel();
                    submitExam();
                }
            }
        }, 1000, 1000);

        // Questions
        JTextArea[] questionAreas = new JTextArea[5];
        optionButtons = new JRadioButton[5][4];
        String[][] options = new String[5][4];
        String[] correctAnswers = new String[5];
        int[] questionIds = new int[5];

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Questions LIMIT 5";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int i = 0;
            while (rs.next() && i < 5) {
                questionIds[i] = rs.getInt("question_id");
                questionAreas[i] = new JTextArea(rs.getString("question_text"));
                questionAreas[i].setEditable(false);
                questionAreas[i].setLineWrap(true);
                questionAreas[i].setWrapStyleWord(true);
                JScrollPane questionScroll = new JScrollPane(questionAreas[i]);
                questionScroll.setBounds(20, 50 + i * 200, 700, 60); // 200-pixel spacing
                panel.add(questionScroll);

                options[i][0] = rs.getString("option1");
                options[i][1] = rs.getString("option2");
                options[i][2] = rs.getString("option3");
                options[i][3] = rs.getString("option4");
                correctAnswers[i] = rs.getString("correct_answer");

                ButtonGroup group = new ButtonGroup();
                for (int j = 0; j < 4; j++) {
                    optionButtons[i][j] = new JRadioButton(options[i][j]);
                    optionButtons[i][j].setBounds(20, 120 + i * 200 + j * 30, 700, 30); // 200-pixel spacing
                    optionButtons[i][j].setOpaque(false);
                    optionButtons[i][j].setForeground(Color.BLACK);
                    group.add(optionButtons[i][j]);
                    panel.add(optionButtons[i][j]);
                }
                i++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!");
        }

        // Submit Button (placed dynamically below the last question)
        int lastQuestionY = 120 + 4 * 30 + 4 * 200; // Y-position of the last option + spacing adjustment
        JButton saveButton = new JButton("Submit");
        saveButton.setBounds(300, lastQuestionY + 50, 100, 30); // 50 pixels below the last option
        saveButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to submit the exam?", "Confirm Submission", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                timer.cancel();
                submitExam();
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

    private void submitExam() {
        int score = 0;
        StringBuilder resultMessage = new StringBuilder("Your Score: ");
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Questions LIMIT 5";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int i = 0;
            while (rs.next() && i < 5) {
                String correctAnswer = rs.getString("correct_answer");
                String selectedAnswer = null;
                for (int j = 0; j < 4; j++) {
                    if (optionButtons[i][j].isSelected()) {
                        selectedAnswer = optionButtons[i][j].getText();
                        break;
                    }
                }
                boolean isCorrect = selectedAnswer != null && selectedAnswer.equals(correctAnswer);
                if (isCorrect) score += 20; // 100/5 questions = 20 points per question

                // Save result
                String insertQuery = "INSERT INTO Exam_Results (email, question_id, selected_answer, is_correct) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, userEmail);
                insertStmt.setInt(2, rs.getInt("question_id"));
                insertStmt.setString(3, selectedAnswer != null ? selectedAnswer : "");
                insertStmt.setBoolean(4, isCorrect);
                insertStmt.executeUpdate();

                resultMessage.append("\nQuestion ").append(i + 1).append(": ")
                        .append(isCorrect ? "Correct" : "Wrong (Correct: " + correctAnswer + ")");
                i++;
            }

            // Update attendee score
            String updateQuery = "UPDATE Attendees SET test_score = ? WHERE email = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, score);
            updateStmt.setString(2, userEmail);
            updateStmt.executeUpdate();

            resultMessage.insert(0, "Your Score: " + score + "\n");
            JOptionPane.showMessageDialog(this, resultMessage.toString());
            dispose();
            new WelcomeFrame().setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExamFrame("attendee@example.com").setVisible(true));
    }
}