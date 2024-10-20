package view;

import controller.BillingInfo;
import controller.Customer;
import controller.TariffTaxInfo;
import utility.Help;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import model.Writer;
import utility.Constants;

public class BillingInfoGUI {

    private JFrame frame;
    private JTextField billingMonthField;
    private JTextField currentMeterReadingRegularField;
    private JTextField currentMeterReadingPeakField;
    private JLabel resultLabel;
    private JComboBox<String> customerComboBox;
    private ArrayList<Customer> custList;
    private ArrayList<BillingInfo> billList;
    private ArrayList<TariffTaxInfo> rates;

    public BillingInfoGUI(ArrayList<Customer> custList, ArrayList<BillingInfo> billList, ArrayList<TariffTaxInfo> rates) {
        this.custList = custList;
        this.billList = billList;
        this.rates = rates;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Add Billing Information");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(7, 2));
        customerComboBox = new JComboBox<>();
        for (Customer customer : custList) {
            customerComboBox.addItem(customer.getCustomerId());
        }
        billingMonthField = new JTextField();
        currentMeterReadingRegularField = new JTextField();
        currentMeterReadingPeakField = new JTextField();
        resultLabel = new JLabel("");
        frame.add(new JLabel("Select Customer ID:"));
        frame.add(customerComboBox);
        frame.add(new JLabel("Billing Month (MM/YYYY):"));
        frame.add(billingMonthField);
        frame.add(new JLabel("Current Meter Reading (Regular):"));
        frame.add(currentMeterReadingRegularField);
        frame.add(new JLabel("Current Meter Reading (Peak):"));
        frame.add(currentMeterReadingPeakField);
        frame.setLocationRelativeTo(null);
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBillingInfo();
            }
        });
        frame.add(submitButton);
        frame.add(resultLabel);
        frame.setVisible(true);
    }

    private void addBillingInfo() {
        String customerId = (String) customerComboBox.getSelectedItem();
        String billingMonth = billingMonthField.getText();
        String meterReadingRegularText = currentMeterReadingRegularField.getText();
        String meterReadingPeakText = currentMeterReadingPeakField.getText();
        if (!billingMonth.matches("\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(frame, "Follow the pattern MM/YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Customer foundCustomer = null;
        for (Customer c : custList) {
            if (customerId.equals(c.getCustomerId())) {
                foundCustomer = c;
                break;
            }
        }
        if (foundCustomer == null) {
            JOptionPane.showMessageDialog(frame, "Customer not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Date billingMonthDate;
        Date connectionDate;
        try {
            SimpleDateFormat billingMonthFormat = new SimpleDateFormat("MM/yyyy");
            SimpleDateFormat connectionDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            billingMonthDate = billingMonthFormat.parse(billingMonth);
            connectionDate = connectionDateFormat.parse(foundCustomer.getConnectionDate());
            if (billingMonthDate.before(connectionDate)) {
                JOptionPane.showMessageDialog(frame, "Billing month cannot be before the connection date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, "Error parsing dates.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (BillingInfo b : billList) {
            if (customerId.equals(b.getCustomerId()) && billingMonth.equals(b.getBillingMonth())) {
                JOptionPane.showMessageDialog(frame, "Bill for this month already created.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        int reg = 0, peak = 0;
        for (BillingInfo b : billList) {
            if (customerId.equals(b.getCustomerId())) {
                reg = Math.max(reg, b.getCurrentMeterReadingRegular());
                peak = Math.max(peak, b.getCurrentMeterReadingPeak());
            }
        }
        int currentMeterReadingRegular;
        int currentMeterReadingPeak;
        try {
            currentMeterReadingRegular = Integer.parseInt(meterReadingRegularText);
            if (currentMeterReadingRegular <= reg) {
                JOptionPane.showMessageDialog(frame, "Reading cannot be less than the previous meter reading.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (foundCustomer.getMeterType().equalsIgnoreCase("Three Phase")) {
                currentMeterReadingPeak = Integer.parseInt(meterReadingPeakText);
                if (currentMeterReadingPeak <= peak) {
                    JOptionPane.showMessageDialog(frame, "Reading cannot be less than previous peak reading.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                currentMeterReadingPeak = 0;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid meter readings.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        currentMeterReadingRegular = currentMeterReadingRegular - reg;
        currentMeterReadingPeak = currentMeterReadingPeak - peak;
        double salesTax;
        int fixed, regularUnitsPrice, peakhourUnitsPrice;
        TariffTaxInfo rateInfo;
        if (foundCustomer.getMeterType().equalsIgnoreCase("single phase")) {
            rateInfo = foundCustomer.getCustomerType().equalsIgnoreCase("domestic") ? rates.get(0) : rates.get(1);
        } else {
            rateInfo = foundCustomer.getCustomerType().equalsIgnoreCase("domestic") ? rates.get(2) : rates.get(3);
        }
        salesTax = rateInfo.getPercentage();
        fixed = rateInfo.getFixedCharges();
        regularUnitsPrice = rateInfo.getRegularUnits();
        peakhourUnitsPrice = rateInfo.getPeakhourUnits();
        int costofElectricity = (regularUnitsPrice * currentMeterReadingRegular) + (peakhourUnitsPrice * currentMeterReadingPeak);
        double totalBilling = costofElectricity + ((costofElectricity / 100.0) * salesTax) + fixed;
        String todayDate = LocalDate.now().format(Help.Dateformatter);
        String dateAfter7DaysFormatted = LocalDate.now().plusDays(7).format(Help.Dateformatter);
        ArrayList<String> AllData = new ArrayList<>();
        AllData.add(foundCustomer.getCustomerId());
        AllData.add(billingMonth);
        currentMeterReadingRegular = currentMeterReadingRegular + reg;
        currentMeterReadingPeak = currentMeterReadingPeak + peak;
        AllData.add(Integer.toString(currentMeterReadingRegular));
        AllData.add(Integer.toString(currentMeterReadingPeak));
        AllData.add(todayDate);
        AllData.add(Integer.toString(costofElectricity));
        AllData.add(Double.toString(salesTax));
        AllData.add(Integer.toString(fixed));
        AllData.add(Double.toString(totalBilling));
        AllData.add(dateAfter7DaysFormatted);
        AllData.add("Unpaid");
        AllData.add("N/A");
        Writer.write(Constants.BILLINGINFO, AllData);
        billList.add(new BillingInfo(
                customerId, billingMonth, currentMeterReadingRegular, currentMeterReadingPeak, todayDate, costofElectricity,
                salesTax, fixed, totalBilling, dateAfter7DaysFormatted, "Unpaid", "N/A"));
        frame.dispose();
        JOptionPane.showMessageDialog(frame, "Bill info added successfully. Total billing: " + totalBilling);
    }

}
