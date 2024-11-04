package com.example.pos_system;

import view.ProductFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.awt.GraphicsEnvironment;
import javax.swing.SwingUtilities;

@SpringBootApplication
public class PosSystemApplication {
    public static void main(String[] args) {
        // Start the Spring Boot application
        SpringApplication.run(PosSystemApplication.class, args);

        // Check if the environment is headless
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("Running in headless mode. GUI components will not be displayed.");
            return; // Exit if running headless
        }

        // Launch the Swing GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                ProductFrame frame = new ProductFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

