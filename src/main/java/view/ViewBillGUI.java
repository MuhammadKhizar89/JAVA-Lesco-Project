package view;

import controller.BillingInfo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewBillGUI extends JFrame {
    private JTextField customerIdField;
    private JButton viewBillsButton;
    private JTable billTable;
    private DefaultTableModel tableModel;

    public ViewBillGUI(ArrayList<BillingInfo> billList) {
        setTitle("View Bills");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel customerIdLabel = new JLabel("Customer ID:");
        customerIdField = new JTextField(15);
        viewBillsButton = new JButton("View Bills");

        // Setup the table model and table
        String[] columnNames = {"Customer ID", "Billing Month", "Meter Reading (Regular)", "Meter Reading (Peak)",
                                "Billing Date", "Total Amount", "Due Date", "Paid Status", "Payment Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        billTable = new JTable(tableModel);

        // Set layout and add components
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.add(customerIdLabel);
        inputPanel.add(customerIdField);
        inputPanel.add(viewBillsButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(billTable), BorderLayout.CENTER);

        // Add action listener for the button
        viewBillsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerId = customerIdField.getText();
                viewBill(billList, customerId);
            }
        });

        setVisible(true);
    }

    private void viewBill(ArrayList<BillingInfo> billList, String customerId) {
        tableModel.setRowCount(0); // Clear existing rows
        boolean found = false;
        for (BillingInfo bill : billList) {
            if (bill.getCustomerId().equals(customerId)) {
                Object[] rowData = {
                    bill.getCustomerId(),
                    bill.getBillingMonth(),
                    bill.getCurrentMeterReadingRegular(),
                    bill.getCurrentMeterReadingPeak(),
                    bill.getBillingDate(),
                    bill.getTotalBillingAmount(),
                    bill.getDueDate(),
                    bill.getBillPaidStatus(),
                    bill.getBillPaymentDate()
                };
                tableModel.addRow(rowData);
                found = true;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(this, "No Bill Found for Customer ID: " + customerId, "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
