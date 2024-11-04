package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainFrame() {
        setTitle("POS System Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize CardLayout and main card panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.LIGHT_GRAY);

        // Create panels for Dashboard, Inventory, and Sales Register
        JPanel dashboardPanel = createDashboardPanel();
        JPanel inventoryPanel = new ProductFrame(); // Create ProductFrame directly here
        JPanel salesRegisterPanel = new SalesRegisterFrame();

        // Add panels to the cardPanel
        cardPanel.add(dashboardPanel, "Dashboard");
        cardPanel.add(inventoryPanel, "Inventory"); // Correctly added
        cardPanel.add(salesRegisterPanel, "Sales Register");

        // Add cardPanel to the frame
        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245)); // Light grey background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Padding between buttons
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Styled "Inventory" button
        JButton inventoryButton = createStyledButton("Inventory");
        inventoryButton.addActionListener(e -> cardLayout.show(cardPanel, "Inventory"));
        panel.add(inventoryButton, gbc);

        // Styled "Sales Register" button
        gbc.gridy++;
        JButton salesRegisterButton = createStyledButton("Sales Register");
        salesRegisterButton.addActionListener(e -> cardLayout.show(cardPanel, "Sales Register"));
        panel.add(salesRegisterButton, gbc);

        return panel;
    }

    private JPanel createSalesRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(230, 230, 250)); // Lavender background

        JLabel label = new JLabel("Sales Register Page", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 24));
        panel.add(label, BorderLayout.CENTER);

        // Navigation button back to Dashboard
        JButton backButton = createStyledButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Dashboard"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    // Method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(60, 179, 113)); // Medium Sea Green color
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(200, 50)); // Consistent size
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
