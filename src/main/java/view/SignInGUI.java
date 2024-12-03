package view;
import controller.Customer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import controller.Employee;
import utility.Constants;
import utility.Parsing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class SignInGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private Employee authenticatedEmployee;
    public SignInGUI() {
                initializeComponents();
    }
    public void SignInEmployeeGUI() {
        setTitle("LESCO - Sign In Employee");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    authenticatedEmployee = authenticateEmployee();
                } catch (IOException ex) {
                    displayError("Server Error 404");
                    return;
                }
                if (authenticatedEmployee != null) {
                    messageLabel.setText("Login Successful");
                    authenticatedEmployee.employeeMenu(); 
                    dispose();
                } else {
                    displayError("Invalid Username or Password");
                }
            }
        });
        setVisible(true);
    }

    public void SignInCustomerGUI() {
        setTitle("LESCO - Sign In Customer");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Customer authenticatedCustomer = null;
                try {
                    authenticatedCustomer = authenticateCustomer();
                } catch (IOException ex) {
                    displayError("Server Error 404");
                    return;
                }
                if (authenticatedCustomer != null) {
                    messageLabel.setText("Login Successful");
                    authenticatedCustomer.customerMenu();
                    dispose();
                } else {
                    displayError("Invalid Username or Password");
                }
            }
        });
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(null);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
setLocationRelativeTo(null);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 200, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 100, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 200, 30);
        add(loginButton);

        messageLabel = new JLabel();
        messageLabel.setBounds(150, 200, 200, 30);
        add(messageLabel);
    }

    private void displayError(String message) {
        messageLabel.setText(message);
        JOptionPane.showMessageDialog(this, message, "Login Failed", JOptionPane.ERROR_MESSAGE);
    }

    private Employee authenticateEmployee() throws IOException {
        String userName = usernameField.getText();
        String password = new String(passwordField.getPassword());
        Constants.client.connect();
        Constants.client.sendData("readEmployeeInfo#");
        String empResponse = Constants.client.waitForResponse();
        ArrayList<Employee> data = Parsing.parseEmployeeData(empResponse);
//        ArrayList<Employee> data = Reader.readEmployeeData(Constants.EMPLOYEESDATA);
        for (Employee e : data) {
            if (e.getUserName().equals(userName) && e.getPassword().equals(password)) {
                return e;
            }
        }
        return null;
    }

    private Customer authenticateCustomer() throws IOException {
        String userName = usernameField.getText();
        String password = new String(passwordField.getPassword());
        Constants.client.connect();
        Constants.client.sendData("readCustomerData#");
        String customerResponse = Constants.client.waitForResponse();
        ArrayList<Customer> data = Parsing.parseCustomerData(customerResponse);
        System.out.println(data);
        Constants.client.close();
        for (Customer c : data) {
            if (c.getCustomerId().equals(userName) && c.getCnic().equals(password)) {
                return c;
            }
        }
        return null;
    }

    public Employee getAuthenticatedEmployee() {
        return authenticatedEmployee;
    }
}
