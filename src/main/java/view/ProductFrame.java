// package view;

// import java.awt.BorderLayout;
// import java.awt.Color;
// import java.awt.Dimension;
// import java.awt.Font;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.util.List;
// import javax.swing.JButton;
// import javax.swing.JFrame;
// import javax.swing.JOptionPane;
// import javax.swing.JPanel;
// import javax.swing.JLabel;
// import javax.swing.JScrollPane;
// import javax.swing.JTable;
// import javax.swing.SwingUtilities;
// import javax.swing.table.DefaultTableModel;
// import javax.swing.table.JTableHeader;

// import com.example.pos_system.model.Product;
// import com.google.gson.Gson;
// import com.google.gson.reflect.TypeToken;

// public class ProductFrame extends JFrame {
//     private JTable productTable;

//     public ProductFrame() {
//         setTitle("Product List");
//         setSize(800, 600);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLayout(new BorderLayout());

//         // Create header label
//         JPanel headerPanel = new JPanel();
//         headerPanel.add(createTitleLabel());
//         add(headerPanel, BorderLayout.NORTH);

//         // Initialize the table and load products
//         productTable = new JTable();
//         productTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for the table
//         JTableHeader tableHeader = productTable.getTableHeader();
//         tableHeader.setFont(new Font("Arial", Font.BOLD, 16)); // Set font for the header
//         tableHeader.setBackground(Color.LIGHT_GRAY); // Set header background color
//         tableHeader.setForeground(Color.BLACK); // Set header text color

//         add(new JScrollPane(productTable), BorderLayout.CENTER);
//         loadProducts();

//         // Create and add button panel
//         JPanel buttonPanel = new JPanel();
//         JButton refreshButton = new JButton("Refresh");
//         JButton exitButton = new JButton("Exit");

//         refreshButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 loadProducts();
//             }
//         });

//         exitButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 System.exit(0);
//             }
//         });

//         buttonPanel.add(refreshButton);
//         buttonPanel.add(exitButton);
//         add(buttonPanel, BorderLayout.SOUTH);
//     }

//     private JLabel createTitleLabel() {
//         JLabel titleLabel = new JLabel("Product List");
//         titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Title font
//         return titleLabel;
//     }

//     private void loadProducts() {
//         try {
//             List<Product> products = fetchProducts();
//             DefaultTableModel model = new DefaultTableModel(new String[]{"Barcode", "Name", "Price", "Quantity"}, 0);
//             for (Product product : products) {
//                 model.addRow(new Object[]{product.getBarcode(), product.getName(), product.getPrice(), product.getQuantity()});
//             }
//             productTable.setModel(model);
//             setColumnWidths(); // Set column widths after loading data
//         } catch (Exception e) {
//             e.printStackTrace();
//             JOptionPane.showMessageDialog(this, "Failed to load products.", "Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }

//     private void setColumnWidths() {
//         productTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Barcode
//         productTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
//         productTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Price
//         productTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Quantity
//     }

//     private List<Product> fetchProducts() throws Exception {
//         String apiUrl = "http://localhost:8080/api/products"; // Adjust this URL to your server
//         HttpClient client = HttpClient.newHttpClient();
//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(URI.create(apiUrl))
//                 .GET()
//                 .build();

//         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//         Gson gson = new Gson();
//         return gson.fromJson(response.body(), new TypeToken<List<Product>>() {}.getType());
//     }



//     // public static void main(String[] args) {
//     //     SwingUtilities.invokeLater(() -> {
//     //         ProductFrame frame = new ProductFrame();
//     //         frame.setVisible(true);
//     //     });
//     // }
// }



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
public class ProductFrame extends JPanel { // Changed from JFrame to JPanel
    private JTable productTable;

    public ProductFrame() {
        setLayout(new BorderLayout());
        productTable = new JTable();
        add(new JScrollPane(productTable), BorderLayout.CENTER);
        loadProducts();
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
}
