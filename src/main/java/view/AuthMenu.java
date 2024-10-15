package view;
import controller.Authentication;
import controller.Customer;
import controller.Employee;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class AuthMenu extends JFrame {
    public AuthMenu() {
        setLayout(null);
        setSize(400, 400);
        setTitle("LESCO");
        JButton button1 = new JButton("Login as Employee");
        JButton button2 = new JButton("Signup as Employee");
        JButton button3 = new JButton("Login as Customer");
        JButton button4 = new JButton("Exit App");
        button1.setBounds(0, 0, 400, 50);
        button2.setBounds(0, 50, 400, 50);
        button3.setBounds(0, 100, 400, 50);
        button4.setBounds(0, 150, 400, 50);
        Authentication authObject = new Authentication();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Employee signInUser = authObject.signInEmployee();
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authObject.signUpEmployee();
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authObject.signInCustomer();
            }
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(button1);
        add(button2);
        add(button3);
        add(button4);
        setVisible(true);
    }
}