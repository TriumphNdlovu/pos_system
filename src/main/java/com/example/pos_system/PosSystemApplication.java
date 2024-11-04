package com.example.pos_system;

import view.MainFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.swing.SwingUtilities;

@SpringBootApplication
public class PosSystemApplication {
    public static void main(String[] args) {
        // Start the Spring Boot application
        SpringApplication.run(PosSystemApplication.class, args);
        System.setProperty("java.awt.headless", "false");
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame mainframe = new MainFrame();
                mainframe.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

