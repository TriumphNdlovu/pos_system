package view;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import java.net.URL;

public class RegisterPage extends JPanel {
    private JTextField usernameField;
    private JTextField roleField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegisterPage() {
        // setTitle("User Registration");
        setSize(400, 300);
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setLocationRelativeTo(null);

        // Create components
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        JLabel roleLabel = new JLabel("Role:");
        roleField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        registerButton = new JButton("Register");

        // Layout setup
        setLayout(new GridLayout(4, 2, 10, 10));
        add(usernameLabel);
        add(usernameField);
        add(roleLabel);
        add(roleField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty space
        add(registerButton);

        // Register button action
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });

        //Nav back to login page
        JButton backButton = new JButton("Back to Login");
        add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) getParent().getLayout();
                cl.show(getParent(), "Login Page");
            }
        });
        setVisible(true);
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String role = roleField.getText();
        String password = new String(passwordField.getPassword());

        // Send the data to the backend (this part will be handled with HTTP requests)
        if (!username.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
            // Logic to call the backend service
            System.out.println("Registering user: " + username);
           sendRegistrationData(username, role, password);

        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sendRegistrationData(String username, String role, String password) {
    try {
        URL url = new URL("http://localhost:8080/users/register");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = String.format("{\"username\": \"%s\", \"role\": \"%s\", \"password\": \"%s\"}", 
                                                username, role, password);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Navigate to the login page
            CardLayout cl = (CardLayout) getParent().getLayout();
            cl.show(getParent(), "Login Page");

        } else {
            JOptionPane.showMessageDialog(this, "Registration failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "An error occurred!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    public static void main(String[] args) {
        new RegisterPage();
    }
}
