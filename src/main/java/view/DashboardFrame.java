package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DashboardFrame extends JFrame {
    public DashboardFrame() {
        setTitle("Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1, 20, 20)); // Grid layout with more spacing

        // Create a panel to hold buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        buttonPanel.setBackground(Color.DARK_GRAY);

        // Button to open the Inventory page
        JButton inventoryButton = createStyledButton("Inventory");
        inventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the Inventory frame
                ProductFrame inventoryFrame = new ProductFrame();
                inventoryFrame.setVisible(true);
            }
        });

        // Button to open the Sales Register page
        JButton salesRegisterButton = createStyledButton("Sales Register");
        salesRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SalesRegisterFrame salesRegisterFrame = new SalesRegisterFrame();
                salesRegisterFrame.setVisible(true);
            }
        });

        // Add buttons to the panel
        buttonPanel.add(inventoryButton);
        buttonPanel.add(salesRegisterButton);

        // Add panel to the frame
        add(buttonPanel);
    }

    // Method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180)); // Light steel blue color
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DashboardFrame dashboard = new DashboardFrame();
            dashboard.setVisible(true);
        });
    }
}
