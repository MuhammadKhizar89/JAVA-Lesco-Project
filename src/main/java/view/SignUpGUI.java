package view;
import controller.Employee;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import model.Reader;
import model.Writer;
import utility.Constants;

public class SignUpGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    public SignUpGUI() {
        setTitle("Employee Sign-Up");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 20, 80, 25);
        add(usernameLabel);
        usernameField = new JTextField();
        usernameField.setBounds(120, 20, 150, 25);
        add(usernameField);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 60, 80, 25);
        add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(120, 60, 150, 25);
        add(passwordField);
        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(90, 110, 120, 30);
        add(signUpButton);
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpEmployee();
            }
        });
        setVisible(true);
    }
    private void signUpEmployee() {
        String userName = usernameField.getText();
        String password = new String(passwordField.getPassword());
        ArrayList<Employee> data = Reader.readEmployeeData(Constants.EMPLOYEESDATA);
        boolean userExists = false;
        for (Employee e : data) {
            if (e.getUserName().equals(userName)) {
                JOptionPane.showMessageDialog(this, "Username Already Exists, Try Another", "Error", JOptionPane.ERROR_MESSAGE);
                userExists = true;
                break;
            }
        }
        if (!userExists) {
            ArrayList<String> employeeArray = new ArrayList<>();
            employeeArray.add(userName);
            employeeArray.add(password);
            Writer.write(Constants.EMPLOYEESDATA, employeeArray);
            JOptionPane.showMessageDialog(this, "Account Created Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
