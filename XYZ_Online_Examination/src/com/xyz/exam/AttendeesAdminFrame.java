package com.xyz.exam;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AttendeesAdminFrame extends JFrame {
    public AttendeesAdminFrame() {
        setTitle("Attendees Marks - XYZ Online Examination");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        panel.setLayout(new BorderLayout());

        // JTable
        String[] columns = {"Name", "Email", "Marks"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Fetch data
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT name, email, test_score FROM Attendees ORDER BY test_score DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("name"), rs.getString("email"), rs.getInt("test_score")});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error!");
        }

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendeesAdminFrame().setVisible(true));
    }
}