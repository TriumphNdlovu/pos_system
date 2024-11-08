package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.jar.Attributes.Name;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.example.pos_system.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ProductFrame extends JPanel {
    private JTable productTable;
    private DefaultTableModel model;

    public ProductFrame() {
    setLayout(new BorderLayout());  // Set layout to BorderLayout
    setBackground(new Color(240, 255, 255)); // Light cyan background

    // Add title label
    JLabel titleLabel = new JLabel("Product Inventory", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
    add(titleLabel, BorderLayout.NORTH); // Add title at the top

    // Create table for product data
    productTable = new JTable();
    model = new DefaultTableModel(new String[]{"Barcode", "Name", "Price", "Quantity", "Actions"}, 0);
    productTable.setModel(model);
    productTable.setRowHeight(25); // Set row height
    productTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for table content

    // Customize table header
    productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
    productTable.getTableHeader().setBackground(new Color(0, 128, 128)); // Dark teal color
    productTable.getTableHeader().setForeground(Color.WHITE);
    productTable.getTableHeader().setOpaque(false);

    // Set grid color and visibility
    productTable.setGridColor(new Color(200, 200, 200)); // Light gray grid lines
    productTable.setShowGrid(true);

    // Alternating row colors for better readability
    productTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                                                                boolean isSelected, boolean hasFocus,
                                                                int row, int column) {
            java.awt.Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                cell.setBackground(row % 2 == 0 ? Color.WHITE : new Color(230, 240, 255)); // Light blue for alternating rows
            }
            return cell;
        }
    });

    JScrollPane scrollPane = new JScrollPane(productTable); // Make table scrollable
    add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center

    // Load products into the table
    loadProducts();

    // Create a panel for buttons
    JPanel buttonPanel = new JPanel();
    JButton addButton = createStyledButton("Add Product");
    addButton.addActionListener(e -> showAddProductDialog());
    buttonPanel.add(addButton);

    // Add navigation button back to Dashboard
    JButton backButton = createStyledButton("Back to Dashboard");
    backButton.addActionListener(e -> {
        CardLayout cl = (CardLayout)(getParent().getLayout());
        cl.show(getParent(), "Dashboard");
    });
    buttonPanel.add(backButton);

    JButton DeleteButton = createStyledButton("Delete Product");
    DeleteButton.addActionListener(e -> {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String barcode = (String) model.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + model.getValueAt(selectedRow, 1), "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            deleteProduct(barcode);
        }
    });
    buttonPanel.add(DeleteButton);

    // Add the button panel to the bottom
    add(buttonPanel, BorderLayout.SOUTH);

}



private void loadProducts() {
    
    try {
        List<Product> products = fetchProducts();
            model.setRowCount(0); 
            for (Product product : products) {
                model.addRow(new Object[]{product.getBarcode(), product.getName(), product.getPrice(), product.getQuantity(), "Edit/Delete"});

            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load products.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct(String barcode) {
        try {
            String apiUrl = "http://localhost:8080/api/products/" + barcode; 
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .DELETE()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Deleting product: " + response.body());
            
            loadProducts(); 

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete product.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Product> fetchProducts() throws Exception {
        String apiUrl = "http://localhost:8080/api/products"; 
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new Gson().fromJson(response.body(), new TypeToken<List<Product>>() {}.getType());
    }

    private void addProduct(Product product) {
        CompletableFuture<Product> future = CompletableFuture.supplyAsync(() -> {
            try {
                String apiUrl = "http://localhost:8080/api/products"; 
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(product)))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                return new Gson().fromJson(response.body(), Product.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });

        future.thenAccept(result -> {
            if (result != null) {
                // Optionally, add product to the table if the addProduct call was successful
                model.addRow(new Object[]{result.getBarcode(), result.getName(), result.getPrice(), result.getQuantity()});
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

private void showAddProductDialog() {
    JDialog dialog = new JDialog();
    dialog.setTitle("Add Product");
    dialog.setSize(350, 300);
    dialog.setLayout(new BorderLayout(10, 10));
    dialog.setLocationRelativeTo(null);

    // Create the form panel with a grid layout
    JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
    formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Add input fields
    JTextField barcodeField = new JTextField();
    JTextField nameField = new JTextField();
    JTextField priceField = new JTextField();
    JTextField productSizeField = new JTextField();
    JTextField quantityField = new JTextField();

    formPanel.add(new JLabel("Barcode:"));
    formPanel.add(barcodeField);
    formPanel.add(new JLabel("Name:"));
    formPanel.add(nameField);
    formPanel.add(new JLabel("Price:"));
    formPanel.add(priceField);
    formPanel.add(new JLabel("Product Size:"));
    formPanel.add(productSizeField);
    formPanel.add(new JLabel("Quantity:"));
    formPanel.add(quantityField);

    // Create the button panel and set its layout
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    JButton addButton = createStyledButton("Add");
    JButton cancelButton = createStyledButton("Cancel");

    // Add action listeners
    addButton.addActionListener(e -> {
        String barcode = barcodeField.getText().trim();
        String name = nameField.getText().trim();
        String productSize = productSizeField.getText().trim();
        double price;
        int quantity;

        if (name.isEmpty() || barcode.isEmpty() || productSize.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            price = Double.parseDouble(priceField.getText().trim());
            quantity = Integer.parseInt(quantityField.getText().trim());

            Product product = new Product(name, price, quantity, productSize, barcode);
            addProduct(product);

            JOptionPane.showMessageDialog(dialog, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Invalid price or quantity. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, "Failed to add product. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    // Add buttons to the button panel
    buttonPanel.add(addButton);
    buttonPanel.add(cancelButton);

    // Add panels to the dialog
    dialog.add(formPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    dialog.pack();

    dialog.setResizable(false);
    dialog.setVisible(true);
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

