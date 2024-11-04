package view;

import javax.swing.*;
import java.awt.*;

public class SalesRegisterFrame extends JPanel{
    public SalesRegisterFrame() {
        // setTitle("Sales Register");
        setSize(600, 400);
        // setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this frame, not the entire app
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Sales Register Page", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
