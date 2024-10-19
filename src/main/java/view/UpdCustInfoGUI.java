package view;
import controller.Customer;
import controller.NADRADB;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import model.Writer;
import utility.Constants;

public class UpdCustInfoGUI extends JFrame {
    private JTextField customerIdField;
    private JTextField cnicField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField customerTypeField;
    private JTextField meterTypeField;
    private JTextField connectionDateField;
    private JButton updateButton;
    private ArrayList<NADRADB> nadraInfo;
    private ArrayList<Customer> custList;
     private Runnable onUpdateSuccess;
    String CustId;
    public UpdCustInfoGUI(String CustId,ArrayList<NADRADB> nadraInfo, ArrayList<Customer> custList,Runnable onUpdateSuccess) {
        this.nadraInfo = nadraInfo;
        this.custList = custList;
        this.CustId=CustId;
        this.onUpdateSuccess = onUpdateSuccess; 
        setTitle("Update Customer Information");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        customerIdField = new JTextField(20);
        customerIdField.setText(CustId);
        customerIdField.setEditable(false);
        cnicField = new JTextField(13);
        nameField = new JTextField(20);
        addressField = new JTextField(20);
        phoneField = new JTextField(11);
        customerTypeField = new JTextField(20);
        meterTypeField = new JTextField(20);
        connectionDateField = new JTextField(20);
        updateButton = new JButton("Update Customer");
        setLayout(new GridLayout(9, 2));
        add(new JLabel("Customer ID:"));
        add(customerIdField);
        add(new JLabel("CNIC (13 digits):"));
        add(cnicField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Address:"));
        add(addressField);
        add(new JLabel("Phone Number:"));
        add(phoneField);
        add(new JLabel("Customer Type:"));
        add(customerTypeField);
        add(new JLabel("Meter Type:"));
        add(meterTypeField);
        add(new JLabel("Connection Date (DD/MM/YYYY):"));
        add(connectionDateField);
        add(updateButton);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCustomerInfo();
            }
        });
        setVisible(true);
    }

    private void updateCustomerInfo() {
        String customerId = CustId;
        Customer foundCustomer = null;
        int index = -1;
        for (int i = 0; i < custList.size(); i++) {
            if (customerId.equals(custList.get(i).getCustomerId())) {
                foundCustomer = custList.get(i);
                index = i;
                break;
            }
        }

        if (foundCustomer == null) {
            JOptionPane.showMessageDialog(this, "Customer Not Found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String cnic = cnicField.getText().trim();
        if (!cnic.matches("\\d{13}") || !isCNICPresentInNadra(cnic)) {
            JOptionPane.showMessageDialog(this, "Invalid CNIC or CNIC not found in Nadra DB.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (isMaxMetersExceeded(cnic)) {
            JOptionPane.showMessageDialog(this, "Not Allowed! Maximum 3 meters allowed per CNIC.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        foundCustomer.setCnic(cnic);
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        foundCustomer.setName(name);
        String address = addressField.getText().trim();
        if (address.isEmpty() || address.contains(",")) {
            JOptionPane.showMessageDialog(this, "Invalid Address. Please use dashes and ensure it's not empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        foundCustomer.setAddress(address);
        String phone = phoneField.getText().trim();
        if (!phone.matches("\\d{10,15}")) {
            JOptionPane.showMessageDialog(this, "Invalid Phone Number. Enter between 10 and 15 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        foundCustomer.setPhone(phone);
        String customerType = customerTypeField.getText().trim();
        if (!customerType.equalsIgnoreCase("commercial") && !customerType.equalsIgnoreCase("domestic")) {
            JOptionPane.showMessageDialog(this, "Invalid Customer Type. Please enter either 'commercial' or 'domestic'.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        foundCustomer.setCustomerType(customerType);
        String meterType = meterTypeField.getText().trim();
        if (!meterType.equalsIgnoreCase("Single Phase") && !meterType.equalsIgnoreCase("Three Phase")) {
            JOptionPane.showMessageDialog(this, "Invalid Meter Type. Please enter either 'Single Phase' or 'Three Phase'.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        foundCustomer.setMeterType(meterType);

        String connectionDate = connectionDateField.getText().trim();
        if (!connectionDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Invalid Date format. Please enter in DD/MM/YYYY format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        foundCustomer.setConnectionDate(connectionDate);

        // Update customer info in the data file
        ArrayList<String> indexList = new ArrayList<>();
        ArrayList<String> customerDataList = new ArrayList<>();
        indexList.add("1"); // CNIC
        indexList.add("2"); // Name
        indexList.add("3"); // Address
        indexList.add("4"); // Phone
        indexList.add("5"); // Customer Type
        indexList.add("6"); // Meter Type
        indexList.add("7"); // Connection Date
        custList.get(index).setCnic(cnic);
        custList.get(index).setName(name);
        custList.get(index).setAddress(address);
        custList.get(index).setPhone(phone);
        custList.get(index).setCustomerType(customerType);
        custList.get(index).setMeterType(meterType);
        custList.get(index).setConnectionDate(connectionDate);
        customerDataList.add(cnic);
        customerDataList.add(name);
        customerDataList.add(address);
        customerDataList.add(phone);
        customerDataList.add(customerType);
        customerDataList.add(meterType);
        customerDataList.add(connectionDate);

        Writer.updateFile(Constants.CUSTOMERINFO, foundCustomer.getCustomerId(), indexList, customerDataList);
        JOptionPane.showMessageDialog(this, "Customer Updated Successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
         onUpdateSuccess.run();
        dispose(); // Close the window after updating
    }

    private boolean isCNICPresentInNadra(String cnic) {
        for (NADRADB n : nadraInfo) {
            if (n.getCNIC().equals(cnic)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMaxMetersExceeded(String cnic) {
        int count = 0;
        for (Customer c : custList) {
            if (cnic.equals(c.getCnic())) {
                count++;
            }
        }
        return count >= 3;
    }
}
