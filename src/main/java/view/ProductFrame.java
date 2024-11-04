package view;

import java.awt.BorderLayout;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.example.pos_system.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ProductFrame extends JFrame {
    private JTable productTable;

    public ProductFrame() {
        setTitle("Product List");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        productTable = new JTable();
        add(new JScrollPane(productTable), BorderLayout.CENTER);
        loadProducts();
    }

    private void loadProducts() {
        try {
            List<Product> products = fetchProducts();
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Quantity"}, 0);
            for (Product product : products) {
                model.addRow(new Object[]{product.getId(), product.getName(), product.getPrice(), product.getQuantity()});
            }
            productTable.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load products.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Product> fetchProducts() throws Exception {
        String apiUrl = "http://localhost:8080/api/products"; // Adjust this URL to your server
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        return gson.fromJson(response.body(), new TypeToken<List<Product>>() {}.getType());
    }

    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         ProductFrame frame = new ProductFrame();
    //         frame.setVisible(true);
    //     });
    // }
}



