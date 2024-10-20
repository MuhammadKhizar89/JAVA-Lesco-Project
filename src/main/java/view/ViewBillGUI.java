package view;

import controller.BillingInfo;
import controller.Customer;
import controller.TariffTaxInfo;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.Writer;

public class ViewBillGUI extends JFrame {

    private JTextField customerIdField;
    private JTable billTable;
    private DefaultTableModel tableModel;
    private ArrayList<BillingInfo> billList;
    private ArrayList<Customer> custList;
    private ArrayList<TariffTaxInfo> rates;

    public ViewBillGUI(ArrayList<BillingInfo> billList, ArrayList<Customer> custList, ArrayList<TariffTaxInfo> rates) {
        this.billList = billList;
        this.custList = custList;
        this.rates = rates;
        setTitle("View Bills");
        setSize(900, 400);  // Increased width to fit the new column
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel customerIdLabel = new JLabel("Customer ID:");
        customerIdField = new JTextField(15);

        // Added the new "Delete Bill" column
        String[] columnNames = {"Customer ID", "Billing Month", "Meter Reading (Regular)", "Meter Reading (Peak)",
            "Billing Date", "Total Amount", "Due Date", "Paid Status", "Payment Date", "Update Bill", "Pay Bill", "Delete Bill"};
        tableModel = new DefaultTableModel(columnNames, 0);
        billTable = new JTable(tableModel);

        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.add(customerIdLabel);
        inputPanel.add(customerIdField);
        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(billTable), BorderLayout.CENTER);

        customerIdField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterBills();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterBills();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterBills();
            }
        });

        loadAllBills();

        billTable.getColumn("Update Bill").setCellRenderer(new ButtonRenderer());
        billTable.getColumn("Update Bill").setCellEditor(new ButtonEditor(new JCheckBox(), billList, "Update"));

        billTable.getColumn("Pay Bill").setCellRenderer(new ButtonRenderer());
        billTable.getColumn("Pay Bill").setCellEditor(new ButtonEditor(new JCheckBox(), billList, "Pay"));

        // Set up delete column
        billTable.getColumn("Delete Bill").setCellRenderer(new ButtonRenderer());
        billTable.getColumn("Delete Bill").setCellEditor(new ButtonEditor(new JCheckBox(), billList, "Delete"));

        setVisible(true);
    }

    private void loadAllBills() {
    tableModel.setRowCount(0); // Clear existing rows

    // Create a map to store the last bill for each customer ID
    Map<String, BillingInfo> lastBillMap = new HashMap<>();

    // Populate the map with the last bill for each customer ID
    for (BillingInfo bill : billList) {
        lastBillMap.put(bill.getCustomerId(), bill);
    }

    // Now load the bills into the table
    for (BillingInfo bill : billList) {
        // Check if this is the last bill for the customer
        boolean isLastBill = bill.equals(lastBillMap.get(bill.getCustomerId()));
        Object[] rowData = {
            bill.getCustomerId(),
            bill.getBillingMonth(),
            bill.getCurrentMeterReadingRegular(),
            bill.getCurrentMeterReadingPeak(),
            bill.getBillingDate(),
            bill.getTotalBillingAmount(),
            bill.getDueDate(),
            bill.getBillPaidStatus(),
            bill.getBillPaymentDate(),
            isLastBill ? "Update" : "Disabled",  // Enable Update only for the last bill
            "Pay",
            isLastBill ? "Delete" : "Disabled"   // Enable Delete only if Update is enabled
        };
        tableModel.addRow(rowData);
    }
}


    private void filterBills() {
        String searchText = customerIdField.getText().toLowerCase();
        tableModel.setRowCount(0);
        Map<String, BillingInfo> lastBillMap = new HashMap<>();
        for (BillingInfo bill : billList) {
            lastBillMap.put(bill.getCustomerId(), bill);
        }
        if (searchText.isEmpty()) {
            loadAllBills();
        } else {
            for (BillingInfo bill : billList) {
                if (bill.getCustomerId().equals(searchText)) {
                    Object[] rowData = {
                        bill.getCustomerId(),
                        bill.getBillingMonth(),
                        bill.getCurrentMeterReadingRegular(),
                        bill.getCurrentMeterReadingPeak(),
                        bill.getBillingDate(),
                        bill.getTotalBillingAmount(),
                        bill.getDueDate(),
                        bill.getBillPaidStatus(),
                        bill.getBillPaymentDate(),
                        bill.equals(lastBillMap.get(bill.getCustomerId())) ? "Update" : "Disabled",
                        "Pay",
                        bill.getBillPaidStatus().equalsIgnoreCase("unpaid") ? "Delete" : "Disabled"
                    };
                    tableModel.addRow(rowData);
                }
            }
        }
        if (tableModel.getRowCount() == 0) {
            Object[] emptyRow = {"No results found", "", "", "", "", "", "", "", "", "", "", ""};
            tableModel.addRow(emptyRow);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String buttonText = (value == null) ? "" : value.toString();
            setText(buttonText);
            setEnabled(!buttonText.equals("Disabled")); // Enable or disable based on the text
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {

        private String label;
        private boolean isPushed;
        private ArrayList<BillingInfo> billList;
        private String actionType;

        public ButtonEditor(JCheckBox checkBox, ArrayList<BillingInfo> billList, String actionType) {
            super(checkBox);
            this.billList = billList;
            this.actionType = actionType;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    String customerId = (String) table.getValueAt(row, 0);
                    String date = (String) table.getValueAt(row, 1);
                    if (actionType.equals("Update") && !label.equals("Disabled")) {
                        updateBill(customerId, date);
                    } else if (actionType.equals("Pay")) {
                        payBill(customerId, date);
                    } else if (actionType.equals("Delete") && !label.equals("Disabled")) {
                        deleteBill(customerId, date);
                    }
                }
            });
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        private void updateBill(String customerId, String date) {
            new UpdateBillInfoGUI(billList, custList, rates, customerId, date);
        }

        private void payBill(String customerId, String date) {
            new PayBillGUI(custList, billList, customerId, date, () -> loadAllBills());
        }

        private void deleteBill(String customerId, String date) {
            boolean deleted = false;
            for (int i = 0; i < billList.size(); i++) {
                BillingInfo bill = billList.get(i);
                if (bill.getCustomerId().equals(customerId) && bill.getBillingMonth().equals(date)) {
                    billList.remove(i);
                    deleted = true;
                    break;
                }
            }
            Writer.deleteBill(customerId, date);
            if (deleted) {
                JOptionPane.showMessageDialog(null, "Bill deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Bill not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            loadAllBills();
        }
    }
}
