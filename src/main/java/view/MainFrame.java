package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainFrame() {
        setTitle("POS System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize CardLayout and main card panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create panels for Dashboard, Inventory, and Sales Register
        JPanel dashboardPanel = createDashboardPanel();
        ProductFrame inventoryPanel = createInventoryPanel(); // Assuming ProductFrame extends JPanel
        JPanel salesRegisterPanel = createSalesRegisterPanel();

        // Add panels to the cardPanel
        cardPanel.add(dashboardPanel, "Dashboard");
        cardPanel.add(inventoryPanel, "Inventory");
        cardPanel.add(salesRegisterPanel, "Sales Register");

        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton inventoryButton = new JButton("Go to Inventory");
        JButton salesRegisterButton = new JButton("Go to Sales Register");

        inventoryButton.addActionListener(e -> cardLayout.show(cardPanel, "Inventory"));
        salesRegisterButton.addActionListener(e -> cardLayout.show(cardPanel, "Sales Register"));

        panel.add(inventoryButton);
        panel.add(salesRegisterButton);
        return panel;
    }

    private ProductFrame createInventoryPanel() {
        ProductFrame panel = new ProductFrame();
        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Dashboard"));
        panel.add(backButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createSalesRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Sales Register Page", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Dashboard"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
