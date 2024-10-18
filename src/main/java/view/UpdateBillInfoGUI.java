package view;

import controller.BillingInfo;
import controller.Customer;
import controller.TariffTaxInfo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import model.Writer;
import utility.Constants;

public class UpdateBillInfoGUI {

    private JFrame frame;
    private JTextField customerIdField;
    private JTextField billingMonthField;
    private JTextField currentMeterReadingRegularField;
    private JTextField currentMeterReadingPeakField;

    private ArrayList<BillingInfo> billList;
    private ArrayList<Customer> custList;
    private ArrayList<TariffTaxInfo> rates;

    public UpdateBillInfoGUI(ArrayList<BillingInfo> billList, ArrayList<Customer> custList, ArrayList<TariffTaxInfo> rates) {
        this.billList = billList;
        this.custList = custList;
        this.rates = rates;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Update Billing Info");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(0, 2));
        frame.add(new JLabel("Customer ID:"));
        customerIdField = new JTextField();
        frame.add(customerIdField);
        frame.add(new JLabel("Billing Month (MM/YYYY):"));
        billingMonthField = new JTextField();
        frame.add(billingMonthField);
        frame.add(new JLabel("Current Meter Reading (Regular):"));
        currentMeterReadingRegularField = new JTextField();
        frame.add(currentMeterReadingRegularField);
        frame.add(new JLabel("Current Meter Reading (Peak):"));
        currentMeterReadingPeakField = new JTextField();
        frame.add(currentMeterReadingPeakField);
frame.setLocationRelativeTo(null);
        JButton updateButton = new JButton("Update Bill Info");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateBillInfo();
            }
        });
        frame.add(updateButton);
        frame.setVisible(true);
    }

    private void updateBillInfo() {
        String customerId = customerIdField.getText().trim();
        String billingMonth = billingMonthField.getText().trim();
        int currentMeterReadingRegular;
        int currentMeterReadingPeak = 0;
        try {
            // Fetch the bill for the customer
            Customer foundCustomer = null;
            for (Customer c : custList) {
                if (customerId.equals(c.getCustomerId())) {
                    foundCustomer = c;
                    break;
                }
            }

            if (foundCustomer == null) {
                JOptionPane.showMessageDialog(frame, "Customer Not Found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate billing month format
            if (!billingMonth.matches("\\d{2}/\\d{4}")) {
                JOptionPane.showMessageDialog(frame, "Invalid Billing Month. Follow the pattern MM/YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BillingInfo foundBill = null;
            int previousReadingRegular =0;
            int previousReadingPeak = 0;
            int indexToUpdate = -1;
            for (int i = 0; i < billList.size(); i++) {
                BillingInfo b = billList.get(i);
                if (b.getCustomerId().equals(customerId) && !b.getBillingMonth().equals(billingMonth)) {
                    previousReadingRegular = b.getCurrentMeterReadingRegular();
                    previousReadingPeak = b.getCurrentMeterReadingPeak();
                }
                if (b.getCustomerId().equals(customerId) && b.getBillingMonth().equals(billingMonth)) {
                    foundBill = b;
                    indexToUpdate = i;
                    break;
                }
            }
            if (foundBill == null) {
                JOptionPane.showMessageDialog(frame, "Bill not found for the given month.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentMeterReadingRegular = Integer.parseInt(currentMeterReadingRegularField.getText());
            if (currentMeterReadingPeakField.getText().trim().isEmpty()) {
                currentMeterReadingPeak = 0;
            } else {
                currentMeterReadingPeak = Integer.parseInt(currentMeterReadingPeakField.getText());
            }

            if (currentMeterReadingRegular < previousReadingRegular) {
                JOptionPane.showMessageDialog(frame, "Current meter reading (Regular) cannot be less than the previous reading.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (foundCustomer.getMeterType().equalsIgnoreCase("Three Phase") && currentMeterReadingPeak < previousReadingPeak) {
                JOptionPane.showMessageDialog(frame, "Current meter reading (Peak) cannot be less than the previous reading.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calculation logic remains similar
            int costofElectricity;
            double salesTax = 0;
            int fixed = 0;
            int regularUnitsPrice = 0;
            int peakhourUnitsPrice = 0;

            // Rates and charges based on customer type and meter type
            if (foundCustomer.getMeterType().equalsIgnoreCase("single phase")) {
                if (foundCustomer.getCustomerType().equalsIgnoreCase("domestic")) {
                    salesTax = rates.get(0).getPercentage();
                    fixed = rates.get(0).getFixedCharges();
                    regularUnitsPrice = rates.get(0).getRegularUnits();
                    peakhourUnitsPrice = rates.get(0).getPeakhourUnits();
                } else {
                    salesTax = rates.get(1).getPercentage();
                    fixed = rates.get(1).getFixedCharges();
                    regularUnitsPrice = rates.get(1).getRegularUnits();
                    peakhourUnitsPrice = rates.get(1).getPeakhourUnits();
                }
            } else {
                if (foundCustomer.getCustomerType().equalsIgnoreCase("domestic")) {
                    salesTax = rates.get(2).getPercentage();
                    fixed = rates.get(2).getFixedCharges();
                    regularUnitsPrice = rates.get(2).getRegularUnits();
                    peakhourUnitsPrice = rates.get(2).getPeakhourUnits();
                } else {
                    salesTax = rates.get(3).getPercentage();
                    fixed = rates.get(3).getFixedCharges();
                    regularUnitsPrice = rates.get(3).getRegularUnits();
                    peakhourUnitsPrice = rates.get(3).getPeakhourUnits();
                }
            }

            // Update cost calculation
            costofElectricity = (regularUnitsPrice * (currentMeterReadingRegular - previousReadingRegular))
                    + (peakhourUnitsPrice * (currentMeterReadingPeak - previousReadingPeak));
            double totalBilling = costofElectricity + ((costofElectricity / 100) * salesTax) + fixed;

            // Update billing information
            foundBill.setSalesTax(salesTax);
            foundBill.setFixedCharges(fixed);
            foundBill.setTotalBillingAmount(totalBilling);
            foundBill.setCostOfElectricity(costofElectricity);
            foundBill.setCurrentMeterReadingRegular(currentMeterReadingRegular);
            foundBill.setCurrentMeterReadingPeak(currentMeterReadingPeak);

            // Write updated billing information to file
            ArrayList<String> allData = new ArrayList<>();
            allData.add(Integer.toString(currentMeterReadingRegular));
            allData.add(Integer.toString(currentMeterReadingPeak));
            allData.add(Integer.toString(costofElectricity));
            allData.add(Double.toString(salesTax));
            allData.add(Integer.toString(fixed));
            allData.add(Double.toString(totalBilling));
            ArrayList<String> index = new ArrayList<>();
            index.add("2"); // Adjust these indices based on your requirements
            index.add("3");
            index.add("5");
            index.add("6");
            index.add("7");
            index.add("8");

            Writer.updateBillFile(Constants.BILLINGINFO, foundCustomer.getCustomerId(), billingMonth, index, allData);

            JOptionPane.showMessageDialog(frame, "Bill Updated Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers for meter readings.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
