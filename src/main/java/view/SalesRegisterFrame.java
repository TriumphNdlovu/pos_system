package view;

import javax.swing.*;
import java.awt.*;

public class SalesRegisterFrame extends JPanel {
    public SalesRegisterFrame() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 230, 250)); // Lavender background
        
        JLabel titleLabel = new JLabel("Sales Register", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Add a placeholder for sales register components here
        JTextArea textArea = new JTextArea("Sales Register functionality will go here.");
        textArea.setEditable(false);
        add(textArea, BorderLayout.CENTER);

        // Add navigation button back to Dashboard
        JButton backButton = createStyledButton("Back to Dashboard");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout)(getParent().getLayout());
            cl.show(getParent(), "Dashboard");
        });
        add(backButton, BorderLayout.SOUTH);
    }

    // Method to create styled buttons (can be used in all frames)
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(60, 179, 113)); // Medium Sea Green color
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }
}
