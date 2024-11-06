package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
public class DashboardFrame extends JFrame {
    public DashboardFrame() {
        setTitle("Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        buttonPanel.setBackground(Color.DARK_GRAY);

        JButton inventoryButton = new JButton("Inventory");
        inventoryButton.setFont(new Font("Arial", Font.BOLD, 18));
        inventoryButton.setForeground(Color.WHITE);
        inventoryButton.setBackground(new Color(70, 130, 180));
        inventoryButton.setFocusPainted(false);
        inventoryButton.setBorderPainted(false);
        inventoryButton.setOpaque(true);
        inventoryButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        inventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame inventoryFrame = new JFrame("Product Inventory");
                inventoryFrame.setSize(400, 300);
                inventoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Add components to the ProductFrame as needed (e.g., labels, text fields, tables)
                JLabel productLabel = new JLabel("Product Name:");
                JTextField productField = new JTextField(20);
                JButton addProductButton = new JButton("Add Product");

                inventoryFrame.add(productLabel);
                inventoryFrame.add(productField);
                inventoryFrame.add(addProductButton);

                inventoryFrame.setLayout(new FlowLayout());
                inventoryFrame.setVisible(true);
            }
        });

        JButton salesRegisterButton = new JButton("Sales Register");
        salesRegisterButton.setFont(new Font("Arial", Font.BOLD, 18));
        salesRegisterButton.setForeground(Color.WHITE);
        salesRegisterButton.setBackground(new Color(70, 130, 180));
        salesRegisterButton.setFocusPainted(false);
        salesRegisterButton.setBorderPainted(false);
        salesRegisterButton.setOpaque(true);
        salesRegisterButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        salesRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame salesRegisterFrame = new JFrame("Sales Register");
                salesRegisterFrame.setSize(400, 300);
                salesRegisterFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Add components to the SalesRegisterFrame as needed (e.g., labels, text fields, tables)
                JLabel itemLabel = new JLabel("Item:");
                JTextField itemField = new JTextField(20);
                JButton addItemButton = new JButton("Add Item");

                salesRegisterFrame.add(itemLabel);
                salesRegisterFrame.add(itemField);
                salesRegisterFrame.add(addItemButton);

                salesRegisterFrame.setLayout(new FlowLayout());
                salesRegisterFrame.setVisible(true);
            }
        });

        buttonPanel.add(inventoryButton);
        buttonPanel.add(salesRegisterButton);

        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 18));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(70, 130, 180));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setOpaque(true);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        add(logoutButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DashboardFrame();
        });
    }
}