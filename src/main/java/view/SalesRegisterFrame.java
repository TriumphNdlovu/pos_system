package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.example.pos_system.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SalesRegisterFrame extends JPanel {
    private List<Product> products = new ArrayList<>();
    private DefaultListModel<String> scannedProductsModel;
    private JTextField barcodeField;
    private JTextField quantityField;
    private JLabel totalLabel;
    private double totalPrice = 0.0;
    private List<Product> scannedProductsList = new ArrayList<>(); // Stores scanned products for sale

    public SalesRegisterFrame() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245)); // Light gray background

        GridBagConstraints c = new GridBagConstraints();

        // Title Label
        JLabel titleLabel = new JLabel("Sales Register", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER; // Span all columns
        c.insets = new Insets(20, 10, 10, 10);
        add(titleLabel, c);

        // Input Panel with Barcode and Quantity fields
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        barcodeField = new JTextField(15);
        inputPanel.add(new JLabel("Barcode:"));
        inputPanel.add(barcodeField);

        quantityField = new JTextField("1", 3);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);

        JButton scanButton = createStyledButton("Scan");
        scanButton.addActionListener(e -> scanProduct());
        inputPanel.add(scanButton);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 10);
        add(inputPanel, c);

        // Scanned Products List with scroll pane
        scannedProductsModel = new DefaultListModel<>();
        JList<String> scannedProductsListUI = new JList<>(scannedProductsModel);
        JScrollPane scrollPane = new JScrollPane(scannedProductsListUI);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Scanned Products"));
        scrollPane.setPreferredSize(new Dimension(400, 200));
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.6;
        c.weighty = 1;
        c.gridwidth = 1;
        c.insets = new Insets(10, 10, 10, 10);
        add(scrollPane, c);

        // Panel for Total Price and Buttons (Complete Sale & Back to Dashboard)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        // Total price display
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(totalLabel);

        // Complete Sale button
        JButton completeSaleButton = createStyledButton("Complete Sale");
        completeSaleButton.addActionListener(e -> completeSale());
        bottomPanel.add(completeSaleButton);

        // Back to Dashboard button
        JButton backButton = createStyledButton("Back to Dashboard");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) getParent().getLayout();
            cl.show(getParent(), "Dashboard");
        });
        bottomPanel.add(backButton);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.4;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 10, 10);
add(bottomPanel, c);

// Key binding setup
setupKeyBindings();

// Fetch products when the frame is initialized
fetchProducts();

// Request focus on barcode field when the panel is displayed
SwingUtilities.invokeLater(() -> barcodeField.requestFocusInWindow());
}

private void fetchProducts() {
    try {
        String apiUrl = "http://localhost:8080/api/products";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        products = new Gson().fromJson(response.body(), new TypeToken<List<Product>>() {}.getType());
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to load products.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void scanProduct() {
    String barcode = barcodeField.getText().trim();
    int quantity;

    try {
        quantity = Integer.parseInt(quantityField.getText().trim());
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    Product foundProduct = products.stream()
            .filter(product -> product.getBarcode().equals(barcode))
            .findFirst()
            .orElse(null);

    if (foundProduct != null) {
        double itemTotal = foundProduct.getPrice() * quantity;
        totalPrice += itemTotal;

        Product scannedProduct = new Product(foundProduct.getName(), foundProduct.getPrice(), quantity, foundProduct.getProductSize(), foundProduct.getBarcode());
        scannedProductsList.add(scannedProduct);

        String productInfo = String.format("%s - %s - $%.2f - Size: %s - Qty: %d",
                foundProduct.getBarcode(), foundProduct.getName(), foundProduct.getPrice(), foundProduct.getProductSize(), quantity);
        scannedProductsModel.addElement(productInfo);
        totalLabel.setText(String.format("Total: $%.2f", totalPrice));

        barcodeField.setText("");
        quantityField.setText("1");
        barcodeField.requestFocusInWindow();
    } else {
        JOptionPane.showMessageDialog(this, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void completeSale() {
    try {
        String apiUrl = "http://localhost:8080/api/sale";
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        String saleDataJson = gson.toJson(scannedProductsList);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .POST(HttpRequest.BodyPublishers.ofString(saleDataJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JOptionPane.showMessageDialog(this, "Sale completed successfully!", "Sale", JOptionPane.INFORMATION_MESSAGE);
            scannedProductsModel.clear();
            totalPrice = 0.0;
            totalLabel.setText("Total: $0.00");
            scannedProductsList.clear();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to complete sale.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "An error occurred while completing the sale.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void setupKeyBindings() {
    barcodeField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "scanProduct");
    barcodeField.getActionMap().put("scanProduct", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            scanProduct();
        }
    });

    barcodeField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "focusQuantityField");
    barcodeField.getActionMap().put("focusQuantityField", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            quantityField.requestFocusInWindow();
        }
    });

    quantityField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK), "focusBarcodeField");
    quantityField.getActionMap().put("focusBarcodeField", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            barcodeField.requestFocusInWindow();
        }
    });

    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), "completeSale");
    getActionMap().put("completeSale", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            completeSale();
        }
    });

    // Key binding to navigate back to the dashboard
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK), "goToDashboard");
    getActionMap().put("goToDashboard", new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout cl = (CardLayout) getParent().getLayout();
            cl.show(getParent(), "Dashboard");
        }
    });
}

private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("Arial", Font.BOLD, 16));
    button.setForeground(Color.WHITE);
    button.setBackground(new Color(60, 179, 113));
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setOpaque(true);
    button.setPreferredSize(new Dimension(150, 40));
    return button;
}
}
// The Enter key is bound to trigger the scan action when the barcode field is focused.
// Tab and Shift + Tab navigate between the barcode and quantity fields.
// Ctrl + F triggers the "Complete Sale" action.
// Ctrl + D navigates back to the dashboard.