package view;
import controller.BillingInfo;
import controller.Customer;
import controller.NADRADB;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import model.Reader;
import model.Writer;
import utility.Constants;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
public class CustomerMenuGUI extends JFrame {
    private ArrayList<Customer> custList;
    private ArrayList<BillingInfo> billList;
    private ArrayList<NADRADB> nadraInfo;
    String CNIC, customerId;
    public CustomerMenuGUI(String CNIC, String customerId) {
        this.CNIC = CNIC;
        this.customerId = customerId;
        custList = Reader.readCustomerData();
        billList = Reader.readBillingInfo();
        nadraInfo = Reader.readNADRAInfo();
        setTitle("Customer Menu");
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton viewBillButton = new JButton("View Bill");
        viewBillButton.setBounds(100, 50, 200, 30);
        viewBillButton.addActionListener(e -> viewBill());
        add(viewBillButton);
        JButton updateCNICButton = new JButton("Update CNIC Expiry");
        updateCNICButton.setBounds(100, 100, 200, 30);
        updateCNICButton.addActionListener(e -> updateCNICexpiry(nadraInfo));
        add(updateCNICButton);
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(100, 150, 200, 30);
        exitButton.addActionListener(e -> dispose());
        add(exitButton);
        setVisible(true);
    }
    private void viewBill() {
        Customer foundCustomer = null;
        for (Customer c : custList) {
            if (customerId.equals(c.getCustomerId())) {
                if (CNIC.equals(c.getCnic())) {
                    foundCustomer = c;
                    break;
                } else {
                    JOptionPane.showMessageDialog(this, "No such Customer ID found with your CNIC", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        if (foundCustomer == null) {
            JOptionPane.showMessageDialog(this, "No customer found for this Customer ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ArrayList<BillingInfo> customerBills = new ArrayList<>();
        for (BillingInfo b : billList) {
            if (b.getCustomerId().equals(customerId)) {
                customerBills.add(b);
            }
        }
        if (!customerBills.isEmpty()) {
            String[] columnNames = {
                "Customer ID", "Billing Month", "Current Meter Reading (Regular)",
                "Current Meter Reading (Peak)", "Billing Date", "Cost of Electricity",
                "Sales Tax", "Fixed Charges", "Total Billing Amount", "Due Date",
                "Bill Paid Status", "Bill Payment Date"
            };
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            for (BillingInfo bill : customerBills) {
                Object[] rowData = {
                    bill.getCustomerId(),
                    bill.getBillingMonth(),
                    bill.getCurrentMeterReadingRegular(),
                    bill.getCurrentMeterReadingPeak(),
                    bill.getBillingDate(),
                    bill.getCostOfElectricity(),
                    bill.getSalesTax(),
                    bill.getFixedCharges(),
                    bill.getTotalBillingAmount(),
                    bill.getDueDate(),
                    bill.getBillPaidStatus(),
                    bill.getBillPaymentDate()
                };
                tableModel.addRow(rowData);
            }
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(1000, 500));
            table.setFillsViewportHeight(true);
            JFrame frame = new JFrame("Bill Details");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(scrollPane);
            frame.pack();
            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No bill found for this Customer ID.", "No Bills", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateCNICexpiry(ArrayList<NADRADB> nadraData) {
        NADRADB foundNadra = null;
        for (NADRADB n : nadraData) {
            if (CNIC.equals(n.getCNIC())) {
//                JOptionPane.showMessageDialog(this, "Your Current Info: " + n.toString(), "NADRA Info", JOptionPane.INFORMATION_MESSAGE);
                foundNadra = n;
                break;
            }
        }
        if (foundNadra == null) {
            JOptionPane.showMessageDialog(this, "No data found for your CNIC in NADRA DB", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate issueDate;
        try {
            issueDate = LocalDate.parse(foundNadra.getIssueDate(), formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid issue date format in NADRA DB.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean isValid = false;
        String expiryDateInfo = "";
        while (!isValid) {
            expiryDateInfo = JOptionPane.showInputDialog(this, "Enter Expiry Date (DD/MM/YYYY)--Current Expiry :"+foundNadra.getExpiryDate());
            if (expiryDateInfo == null) {
                return; 
            }
            try {
                LocalDate expiryDate = LocalDate.parse(expiryDateInfo, formatter);
                if (expiryDate.isAfter(issueDate)) {
                    isValid = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Expiry date must be after the issue date (" + issueDate.format(formatter) + ").", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please enter a valid date in DD/MM/YYYY format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        ArrayList<String> index = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();
        index.add("2");
        value.add(expiryDateInfo);
        Writer.updateFile(Constants.NADRA, CNIC, index, value);
        foundNadra.setExpiryDate(expiryDateInfo);
        JOptionPane.showMessageDialog(this, "CNIC expiry date updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
