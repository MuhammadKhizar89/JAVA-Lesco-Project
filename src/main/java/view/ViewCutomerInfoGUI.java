package view;
import controller.Customer;
import controller.NADRADB;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import utility.Constants;

public class ViewCutomerInfoGUI extends JFrame {

    private ArrayList<NADRADB> nadraInfo;
    private ArrayList<Customer> custList;
    private ArrayList<Customer> filteredCustList;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public ViewCutomerInfoGUI(ArrayList<NADRADB> nadraInfo, ArrayList<Customer> custList) {
        this.nadraInfo = nadraInfo;
        this.custList = custList;
        this.filteredCustList = new ArrayList<>(custList);
        init();
    }

    private void init() {
        setTitle("Customer Information");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });
        searchPanel.add(new JLabel("Search: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);
        String[] columnNames = {
            "Customer ID", "CNIC", "Name", "Address", "Phone",
            "Customer Type", "Meter Type", "Connection Date",
            "Regular Units Consumed", "Peak Hour Units Consumed",
            "Update", "Remove"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        customerTable = new JTable(tableModel);
        customerTable.getColumn("Update").setCellRenderer(new ButtonRenderer());
        customerTable.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox(), filteredCustList, "Update"));
        customerTable.getColumn("Remove").setCellRenderer(new ButtonRenderer());
        customerTable.getColumn("Remove").setCellEditor(new ButtonEditor(new JCheckBox(), filteredCustList, "Remove"));
        populateTable();
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private void populateTable() {
        tableModel.setRowCount(0);
        for (Customer customer : filteredCustList) {
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
                "Update",
                "Remove"
            };
            tableModel.addRow(rowData);
        }
    }

    private void filterTable() {
        String query = searchField.getText().toLowerCase();
        filteredCustList = (ArrayList<Customer>) custList.stream()
                .filter(c -> c.getName().toLowerCase().contains(query)
                || c.getCnic().toLowerCase().contains(query)
                || c.getCustomerId().toLowerCase().contains(query))
                .collect(Collectors.toList());
        populateTable();
    }

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
            new UpdCustInfoGUI(customer.getCustomerId(), nadraInfo, custList, () -> populateTable());
        }

        private void removeCustomer(Customer customer, int row) {
            int confirm = JOptionPane.showConfirmDialog(ViewCutomerInfoGUI.this,
                    "Are you sure you want to remove Customer ID: " + customer.getCustomerId() + "?",
                    "Remove Customer", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String dataToSend = "deleteCustomer#" + customer.getCustomerId();
                try {
                    Constants.client.connect();
                    Constants.client.sendData(dataToSend);
                    String response = Constants.client.waitForResponse();
                    System.out.println(response);  // Log server response
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    Constants.client.close();
                }
//                Writer.deleteCustomer(customer.getCustomerId());
                custList.remove(customer);
                tableModel.removeRow(row);
            }
        }
    }
}
