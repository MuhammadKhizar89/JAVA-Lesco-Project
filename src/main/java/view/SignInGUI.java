package view;
import controller.Customer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import controller.Employee;
import model.Reader;
import utility.Constants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
                authenticatedEmployee = authenticateEmployee();
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
                Customer authenticatedCustomer = authenticateCustomer();
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

    private Employee authenticateEmployee() {
        String userName = usernameField.getText();
        String password = new String(passwordField.getPassword());
        ArrayList<Employee> data = Reader.readEmployeeData(Constants.EMPLOYEESDATA);
        for (Employee e : data) {
            if (e.getUserName().equals(userName) && e.getPassword().equals(password)) {
                return e;
            }
        }
        return null;
    }

    private Customer authenticateCustomer() {
        String userName = usernameField.getText();
        String password = new String(passwordField.getPassword());
        ArrayList<Customer> data = Reader.readCustomerData();
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
