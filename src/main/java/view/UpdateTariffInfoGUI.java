package view;
import controller.TariffTaxInfo;
import model.Writer;
import utility.Constants;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.table.TableCellRenderer;
public class UpdateTariffInfoGUI extends JFrame {
    private JTextField regularUnitsField;
    private JTextField peakHourUnitsField;
    private JTextField salesTaxField;
    private JTextField fixedChargesField;
    private JButton updateButton;
    private JTable tariffTable;
    private DefaultTableModel tableModel;
    private ArrayList<TariffTaxInfo> tariffInfo;

    public UpdateTariffInfoGUI(ArrayList<TariffTaxInfo> tariffInfo) {
        this.tariffInfo = tariffInfo;
        setTitle("Update Tariff Information");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        regularUnitsField = new JTextField(10);
        peakHourUnitsField = new JTextField(10);
        salesTaxField = new JTextField(10);
        fixedChargesField = new JTextField(10);
        String[] columnNames = {"Meter Type","Type", "Regular Units Price", "Peak Hour Units Price", "Sales Tax (%)", "Fixed Charges", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tariffTable = new JTable(tableModel);
        loadTariffData();
        setLayout(new BorderLayout());
        add(new JScrollPane(tariffTable), BorderLayout.CENTER);
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Regular Units Price:"));
        inputPanel.add(regularUnitsField);
        inputPanel.add(new JLabel("Peak Hour Units Price:"));
        inputPanel.add(peakHourUnitsField);
        inputPanel.add(new JLabel("Sales Tax (%):"));
        inputPanel.add(salesTaxField);
        inputPanel.add(new JLabel("Fixed Charges:"));
        inputPanel.add(fixedChargesField);
        JPanel buttonPanel = new JPanel();
        updateButton = new JButton("Update Selected Tariff");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTariff();
            }
        });
        buttonPanel.add(updateButton);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);
        tariffTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tariffTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));
        setVisible(true);
    }

    private void loadTariffData() {
        tableModel.setRowCount(0);
        int i=0;
        for (TariffTaxInfo tariff : tariffInfo) {
            Object[] rowData = {
                tariff.getMeter_type(),
                i%2==0?"Domestic":"Commercial",
                tariff.getRegularUnits(),
                tariff.getPeakhourUnits(),
                tariff.getPercentage(),
                tariff.getFixedCharges(),
                "Update"
            };
            i++;
            tableModel.addRow(rowData);
        }
    }

    private void updateTariff() {
    int selectedRow = tariffTable.getSelectedRow();
    if (selectedRow >= 0) {
        TariffTaxInfo selectedTariff = tariffInfo.get(selectedRow);
        selectedTariff.setRegularUnits(Integer.parseInt(regularUnitsField.getText()));
        selectedTariff.setPercentage(Double.parseDouble(salesTaxField.getText()));
        selectedTariff.setFixedCharges(Integer.parseInt(fixedChargesField.getText()));

        if (selectedTariff.getMeter_type().equals("3Phase")) {
            selectedTariff.setPeakhourUnits(Integer.parseInt(peakHourUnitsField.getText()));
        } else {
            selectedTariff.setPeakhourUnits(0);
        }
        int currentRow = selectedRow;
        loadTariffData(); 
        tariffTable.setRowSelectionInterval(currentRow, currentRow);
        Writer.overwriteTarrifFile(Constants.TARIFFTAX, tariffInfo);
        JOptionPane.showMessageDialog(this, "Tariff information updated successfully.");
    } else {
        JOptionPane.showMessageDialog(this, "Please select a tariff to update.");
    }
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
        private JButton button;
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = tariffTable.getSelectedRow();
                    if (row >= 0) {
                        TariffTaxInfo selectedTariff = tariffInfo.get(row);
                        loadFieldsFromSelectedTariff(selectedTariff);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            button.setText((value == null) ? "" : value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }

    private void loadFieldsFromSelectedTariff(TariffTaxInfo selectedTariff) {
        regularUnitsField.setText(String.valueOf(selectedTariff.getRegularUnits()));
        salesTaxField.setText(String.valueOf(selectedTariff.getPercentage()));
        fixedChargesField.setText(String.valueOf(selectedTariff.getFixedCharges()));
        if (selectedTariff.getMeter_type().equals("3Phase")) {
            peakHourUnitsField.setEnabled(true);
            peakHourUnitsField.setText(String.valueOf(selectedTariff.getPeakhourUnits()));
        } else {
            peakHourUnitsField.setEnabled(false);
            peakHourUnitsField.setText("N/A");
        }
    }
}
