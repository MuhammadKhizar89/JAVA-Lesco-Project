package view;

import controller.Customer;
import controller.NADRADB;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import model.Writer;

public class ViewCutomerInfoGUI extends JFrame {

    private ArrayList<NADRADB> nadraInfo;
    private ArrayList<Customer> custList;
    private JTable customerTable;
    private DefaultTableModel tableModel;

    public ViewCutomerInfoGUI(ArrayList<NADRADB> nadraInfo, ArrayList<Customer> custList) {
        this.nadraInfo = nadraInfo;
        this.custList = custList;
        init();
    }

    private void init() {
        setTitle("Customer Information");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set up the table model
        String[] columnNames = {
            "Customer ID", "CNIC", "Name", "Address", "Phone",
            "Customer Type", "Meter Type", "Connection Date",
            "Regular Units Consumed", "Peak Hour Units Consumed",
            "Update", "Remove"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);

        // Add custom cell editors and renderers for the buttons
        customerTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        customerTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), custList, "Update"));
        customerTable.getColumn("Remove").setCellRenderer(new ButtonRenderer());
        customerTable.getColumn("Remove").setCellEditor(new ButtonEditor(new JCheckBox(), custList, "Remove"));

        populateTable();

        // Add table to JScrollPane
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

        // Finalize frame setup
        setVisible(true);
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        for (Customer customer : custList) {
            Object[] rowData = {
                customer.getCustomerId(),
                customer.getCnic(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhone(),
                customer.getCustomerType(),
                customer.getMeterType(),
                customer.getConnectionDate(),
                customer.getRegularUnitsConsumed(),
                customer.getPeakHourUnitsConsumed(),
                "Update", // Placeholder for button text
                "Remove" // Placeholder for button text
            };
            tableModel.addRow(rowData);
        }
    }

    // Renderer for button cells
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Editor for button cells
    class ButtonEditor extends DefaultCellEditor {
        private String label;
        private boolean isPushed;
        private ArrayList<Customer> customerList;
        private String actionType;
        public ButtonEditor(JCheckBox checkBox, ArrayList<Customer> customerList, String actionType) {
            super(checkBox);
            this.customerList = customerList;
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
                    Customer customer = customerList.get(row);
                    if (actionType.equals("Update")) {
                        updateCustomer(customer);
                    } else if (actionType.equals("Remove")) {
                        removeCustomer(customer, row);
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
        private void updateCustomer(Customer customer) {
            new UpdCustInfoGUI(customer.getCustomerId(), nadraInfo, customerList, () -> populateTable());
        }
        private void removeCustomer(Customer customer, int row) {
            int confirm = JOptionPane.showConfirmDialog(ViewCutomerInfoGUI.this,
                    "Are you sure you want to remove Customer ID: " + customer.getCustomerId() + "?",
                    "Remove Customer", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Writer.deleteCustomer(customer.getCustomerId());
                customerList.remove(customer);
                tableModel.removeRow(row);
            }
        }
    }
}
