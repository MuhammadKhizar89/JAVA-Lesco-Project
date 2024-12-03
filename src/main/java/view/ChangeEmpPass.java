
package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import utility.Constants;
public class ChangeEmpPass extends JFrame {
    private JTextField currentPasswordField;
    private JTextField newPasswordField;
    private JButton changePasswordButton;
    private String password;
    private String userName;
    public ChangeEmpPass(String userName, String password) {
        this.userName = userName;
        this.password = password;
        setTitle("Change Employee Password");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        currentPasswordField = new JTextField(20);
        newPasswordField = new JTextField(20);
        changePasswordButton = new JButton("Change Password");
        setLayout(new GridLayout(3, 2));
        add(new JLabel("Current Password:"));
        add(currentPasswordField);
        add(new JLabel("New Password:"));
        add(newPasswordField);
        add(changePasswordButton);
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });
        setVisible(true);
    }

    private void changePassword() {
        String currPasswordInput = currentPasswordField.getText();
        if (!currPasswordInput.equals(password)) {
            JOptionPane.showMessageDialog(this, "Wrong Current Password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String newPassword = newPasswordField.getText();
        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "New Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ArrayList<String> index = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();
        index.add("1"); // Assuming the password is at index 1
        value.add(newPassword);
        String dataToSend = "ChangePassword#" + userName + ";" + newPassword;
        try {
            Constants.client.connect();
            Constants.client.sendData(dataToSend);
            String serverResponse = Constants.client.waitForResponse();
            JOptionPane.showMessageDialog(this, serverResponse, "Server Response", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            Constants.client.close();
        }
//        Writer.updateFile(Constants.EMPLOYEESDATA, userName, index, value);
        JOptionPane.showMessageDialog(this, "Password Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
