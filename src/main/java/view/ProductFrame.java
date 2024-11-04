

package view;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.example.pos_system.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.table.DefaultTableModel;

public class ProductFrame extends JPanel {
    private JTable productTable;

    public ProductFrame() {
        setLayout(new BorderLayout());  // Set layout to BorderLayout
        setBackground(new Color(240, 255, 255)); // Light cyan background

        // Add title label
        JLabel titleLabel = new JLabel("Product Inventory", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH); // Add title at the top

        // Create table for product data
        productTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(productTable); // Make table scrollable
        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center

        // Load products into the table
        loadProducts();
        
        // Add navigation button back to Dashboard
        JButton backButton = createStyledButton("Back to Dashboard");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout)(getParent().getLayout());
            cl.show(getParent(), "Dashboard");
        });
        add(backButton, BorderLayout.SOUTH); // Add the back button at the bottom
    }

    private void loadProducts() {
        try {
            List<Product> products = fetchProducts();
            DefaultTableModel model = new DefaultTableModel(new String[]{"Barcode", "Name", "Price", "Quantity"}, 0);
            for (Product product : products) {
                model.addRow(new Object[]{product.getBarcode(), product.getName(), product.getPrice(), product.getQuantity()});
            }
            productTable.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load products.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Product> fetchProducts() throws Exception {
        String apiUrl = "http://localhost:8080/api/products"; // Adjust as needed
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<List<Product>>() {}.getType());
    }
    
    // Method to create styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(60, 179, 113)); // Medium Sea Green color
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(200, 50)); // Consistent size
        return button;
    }
}
