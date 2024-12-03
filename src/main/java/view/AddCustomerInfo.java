package view;
import controller.Customer;
import controller.NADRADB;
import utility.Constants;
import utility.Help;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
public class AddCustomerInfo extends JFrame {
    private JTextField cnicField, nameField, addressField, phoneField, customerTypeField, meterTypeField, connectionDateField;
    private JButton addButton;
    public AddCustomerInfo(ArrayList<Customer> custList, ArrayList<NADRADB> nadraInfo) {
        setTitle("Add Customer Information");
        setSize(350, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        JLabel cnicLabel = new JLabel("CNIC:");
        cnicLabel.setBounds(30, 30, 80, 25);
        add(cnicLabel);
        cnicField = new JTextField();
        cnicField.setBounds(150, 30, 150, 25);
        add(cnicField);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 70, 80, 25);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(150, 70, 150, 25);
        add(nameField);
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(30, 110, 80, 25);
        add(addressLabel);
        addressField = new JTextField();
        addressField.setBounds(150, 110, 150, 25);
        add(addressField);
        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(30, 150, 80, 25);
        add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBounds(150, 150, 150, 25);
        add(phoneField);
        JLabel customerTypeLabel = new JLabel("Customer Type:");
        customerTypeLabel.setBounds(30, 190, 100, 25);
        add(customerTypeLabel);
        customerTypeField = new JTextField();
        customerTypeField.setBounds(150, 190, 150, 25);
        add(customerTypeField);
        JLabel meterTypeLabel = new JLabel("Meter Type:");
        meterTypeLabel.setBounds(30, 230, 100, 25);
        add(meterTypeLabel);
        meterTypeField = new JTextField();
        meterTypeField.setBounds(150, 230, 150, 25);
        add(meterTypeField);
        JLabel connectionDateLabel = new JLabel("Connection Date:");
        connectionDateLabel.setBounds(30, 270, 120, 25);
        add(connectionDateLabel);
        connectionDateField = new JTextField();
        connectionDateField.setBounds(150, 270, 150, 25);
        add(connectionDateField);
        addButton = new JButton("Add Customer");
        addButton.setBounds(100, 320, 150, 30);
        add(addButton);
        setVisible(true);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cnic = cnicField.getText();
                String name = nameField.getText();
                String address = addressField.getText();
                String phone = phoneField.getText();
                String customerType = customerTypeField.getText();
                String meterType = meterTypeField.getText();
                String connectionDate = connectionDateField.getText();
                if (!cnic.matches("\\d{13}")) {
                    JOptionPane.showMessageDialog(null, "Invalid CNIC. Please enter exactly 13 digits without dashes.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean isCNICPresentInNadra = false;
                for (NADRADB n : nadraInfo) {
                    if (n.getCNIC().equals(cnic)) {
                        isCNICPresentInNadra = true;
                        break;
                    }
                }
                if (!isCNICPresentInNadra) {
                    JOptionPane.showMessageDialog(null, "This CNIC Not Found in NADRA DB.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int meterCount = 0;
                for (Customer c : custList) {
                    if (c.getCnic().equals(cnic)) {
                        meterCount++;
                    }
                }
                if (meterCount >= 3) {
                    JOptionPane.showMessageDialog(null, "Not Allowed! Maximum 3 meters allowed per CNIC.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (address.trim().isEmpty() || address.contains(",")) {
                    JOptionPane.showMessageDialog(null, "Address cannot be empty and should not contain commas.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!phone.matches("\\d{10,15}")) {
                    JOptionPane.showMessageDialog(null, "Invalid Phone Number. Enter between 10 and 15 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!customerType.equalsIgnoreCase("commercial") && !customerType.equalsIgnoreCase("domestic")) {
                    JOptionPane.showMessageDialog(null, "Invalid Customer Type. Please enter 'commercial' or 'domestic'.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!meterType.equalsIgnoreCase("Single Phase") && !meterType.equalsIgnoreCase("Three Phase")) {
                    JOptionPane.showMessageDialog(null, "Invalid Meter Type. Please enter 'Single Phase' or 'Three Phase'.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!connectionDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    JOptionPane.showMessageDialog(null, "Invalid Date format. Please enter in DD/MM/YYYY format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String customerId = Help.generateCustomerId();
                ArrayList<String> customerDataList = new ArrayList<>();
                customerDataList.add(customerId);
                customerDataList.add(cnic);
                customerDataList.add(name);
                customerDataList.add(address);
                customerDataList.add(phone);
                customerDataList.add(customerType);
                customerDataList.add(meterType);
                customerDataList.add(connectionDate);
                customerDataList.add("0");
                customerDataList.add(meterType.equalsIgnoreCase("Three Phase") ? "0" : "0");
                custList.add(new Customer(customerId, cnic, name, address, phone, customerType, meterType, connectionDate, "0", "0"));
                String dataToSend = "AddCustomer#"+ String.join(";", customerDataList);
                try {
                    Constants.client.connect();
                    Constants.client.sendData(dataToSend);
                    Constants.client.waitForResponse();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    Constants.client.close();
                }
                JOptionPane.showMessageDialog(null, "Customer added successfully. Customer ID: " + customerId, "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });
    }
}
