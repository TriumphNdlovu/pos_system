package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.example.pos_system.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SalesRegisterFrame extends JPanel {
    private List<Product> products = new ArrayList<>();
    private DefaultListModel<String> scannedProductsModel;
    private JTextField barcodeField;
    private JTextField quantityField;
    private JLabel totalPriceLabel;
    private List<Product> scannedProducts = new ArrayList<>();

    public SalesRegisterFrame() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(230, 230, 250)); // Lavender background

        // Title Label
        JLabel titleLabel = new JLabel("Sales Register", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Input panel for barcode and quantity
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.add(new JLabel("Barcode:"));
        barcodeField = new JTextField(15);
        inputPanel.add(barcodeField);
        
        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField("1", 3);
        inputPanel.add(quantityField);

        JButton scanButton = createStyledButton("Scan");
        scanButton.addActionListener(new ScanButtonListener());
        inputPanel.add(scanButton);

        add(inputPanel, BorderLayout.NORTH);

        // List of scanned products
        scannedProductsModel = new DefaultListModel<>();
        JList<String> scannedProductsList = new JList<>(scannedProductsModel);
        JScrollPane scrollPane = new JScrollPane(scannedProductsList);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        add(scrollPane, BorderLayout.CENTER);

        // Panel for total price and complete sale button
        JPanel summaryPanel = new JPanel(new BorderLayout());
        totalPriceLabel = new JLabel("Total Price: $0.00");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        summaryPanel.add(totalPriceLabel, BorderLayout.WEST);

        JButton completeSaleButton = createStyledButton("Complete Sale");
        completeSaleButton.addActionListener(new CompleteSaleButtonListener());
        summaryPanel.add(completeSaleButton, BorderLayout.EAST);

        add(summaryPanel, BorderLayout.SOUTH);

        // Fetch products when the frame is initialized
        fetchProducts();
        barcodeField.requestFocusInWindow();
    }

    private void fetchProducts() {
        try {
            String apiUrl = "http://localhost:8080/api/products"; // Adjust as needed
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            products = new Gson().fromJson(response.body(), new TypeToken<List<Product>>() {}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load products.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class ScanButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String barcode = barcodeField.getText().trim();
            int quantity;

            try {
                quantity = Integer.parseInt(quantityField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(SalesRegisterFrame.this, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product foundProduct = products.stream()
                    .filter(product -> product.getBarcode().equals(barcode))
                    .findFirst()
                    .orElse(null);

            if (foundProduct != null) {
                scannedProducts.add(foundProduct);
                double itemTotal = foundProduct.getPrice() * quantity;
                String productInfo = String.format("%s - %s - $%.2f - Size: %s - Qty: %d",
                        foundProduct.getBarcode(), foundProduct.getName(), foundProduct.getPrice(), foundProduct.getProductSize(), quantity);
                scannedProductsModel.addElement(productInfo);
                updateTotalPrice();
                barcodeField.setText("");
                quantityField.setText("1");
                barcodeField.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(SalesRegisterFrame.this, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class CompleteSaleButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (scannedProducts.isEmpty()) {
                JOptionPane.showMessageDialog(SalesRegisterFrame.this, "No products to complete the sale.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String apiUrl = "http://localhost:8080/api/sale"; // Replace with your actual endpoint
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(scannedProducts)))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    JOptionPane.showMessageDialog(SalesRegisterFrame.this, "Sale completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    scannedProductsModel.clear();
                    scannedProducts.clear();
                    totalPriceLabel.setText("Total Price: R0.00");
                } else {
                    JOptionPane.showMessageDialog(SalesRegisterFrame.this, "Failed to complete the sale.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(SalesRegisterFrame.this, "An error occurred while processing the sale.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTotalPrice() {
        double total = scannedProducts.stream()
                .mapToDouble(product -> product.getPrice() * Integer.parseInt(quantityField.getText().trim()))
                .sum();
        totalPriceLabel.setText(String.format("Total Price: $%.2f", total));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(60, 179, 113)); // Medium Sea Green color
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }
}
