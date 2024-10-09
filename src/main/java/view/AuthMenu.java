/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Admin
 */
public class AuthMenu extends JFrame {

    public AuthMenu() {
        JFrame window = new JFrame();
    window.setLayout(null);  // Disable the layout manager to allow manual positioning
    window.setVisible(true);
    window.setSize(400, 400);
    window.setTitle("This is my GUI");

    JPanel header = new JPanel();
    header.setBackground(Color.BLACK);
    header.setBounds(10, 10, 380, 50); // Position (10, 10), width 380, height 50
    window.add(header);
    }
}
