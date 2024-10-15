package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import controller.TariffTaxInfo;
import model.Writer;
import utility.Constants;
public class UpdateTariffInfoGUI extends JFrame {
    private JComboBox<String> tariffComboBox;
    private JTextField regularUnitsField;
    private JTextField peakHourUnitsField;
    private JTextField salesTaxField;
    private JTextField fixedChargesField;
    private JButton updateButton;
    private ArrayList<TariffTaxInfo> tariffInfo;
    public UpdateTariffInfoGUI(ArrayList<TariffTaxInfo> tariffInfo) 
    {
        this.tariffInfo = tariffInfo;
        setTitle("Update Tariff Information");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        tariffComboBox = new JComboBox<>();
        for (TariffTaxInfo t : tariffInfo) {
            tariffComboBox.addItem(t.toString());
        }
        regularUnitsField = new JTextField(10);
        peakHourUnitsField = new JTextField(10);
        salesTaxField = new JTextField(10);
        fixedChargesField = new JTextField(10);
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Select Tariff:"));
        inputPanel.add(tariffComboBox);
        inputPanel.add(new JLabel("Regular Units Price:"));
        inputPanel.add(regularUnitsField);
        inputPanel.add(new JLabel("Peak Hour Units Price:"));
        inputPanel.add(peakHourUnitsField);
        inputPanel.add(new JLabel("Sales Tax (%):"));
        inputPanel.add(salesTaxField);
        inputPanel.add(new JLabel("Fixed Charges:"));
        inputPanel.add(fixedChargesField);
        updateButton = new JButton("Update Tariff");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTariff();
            }
        });
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.CENTER);
        add(updateButton, BorderLayout.SOUTH);
        tariffComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSelectedTariffData();
            }
        });
        loadSelectedTariffData();
        setVisible(true);
    }

    private void loadSelectedTariffData() {
        int index = tariffComboBox.getSelectedIndex();
        TariffTaxInfo selectedTariff = tariffInfo.get(index);
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

    private void updateTariff() {
        int index = tariffComboBox.getSelectedIndex();
        TariffTaxInfo selectedTariff = tariffInfo.get(index);
        selectedTariff.setRegularUnits(Integer.parseInt(regularUnitsField.getText()));
        selectedTariff.setPercentage(Double.parseDouble(salesTaxField.getText()));
        selectedTariff.setFixedCharges(Integer.parseInt(fixedChargesField.getText()));
        if (selectedTariff.getMeter_type().equals("3Phase")) {
            selectedTariff.setPeakhourUnits(Integer.parseInt(peakHourUnitsField.getText()));
        } else {
            selectedTariff.setPeakhourUnits(0);
        }
        Writer.overwriteTarrifFile(Constants.TARIFFTAX, tariffInfo);
        JOptionPane.showMessageDialog(this, "Tariff information updated successfully.");
    }
}
