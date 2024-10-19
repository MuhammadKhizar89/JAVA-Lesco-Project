package view;

import controller.Customer;
import controller.BillingInfo;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import model.Writer;
import utility.Constants;
import utility.Help;

public class PayBillGUI extends JFrame {

    private JTextField customerIdField;
    private JTextField billingMonthField;
    private JButton payBillButton;
    private JTable billTable;

    PayBillGUI(ArrayList<Customer> custList, ArrayList<BillingInfo> billList, String customerId, String billingMonth, Runnable onSuccess) {
        Customer foundCustomer = null;
        BillingInfo foundBill = null;
        int i = -1;
        for (Customer c : custList) {
            i++;
            if (customerId.equals(c.getCustomerId())) {
                foundCustomer = c;
                break;
            }
        }
        if (foundCustomer == null) {
            JOptionPane.showMessageDialog(this, "Customer Not Found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int j = -1;
        for (BillingInfo b : billList) {
            j++;
            if (b.getCustomerId().equals(customerId) && b.getBillingMonth().equals(billingMonth)) {
                if (b.getBillPaidStatus().equals("Paid")) {
                    JOptionPane.showMessageDialog(this, "Bill Already Paid.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                foundBill = b;
                break;
            }
        }
        if (foundBill == null) {
            JOptionPane.showMessageDialog(this, "Bill Not Found for the specified Date.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        foundBill.setBillPaidStatus("Paid");
        foundBill.setBillPaymentDate(LocalDate.now().toString());
        int reg = 0, peak = 0;
        for (BillingInfo b : billList) {
            if (foundCustomer.getCustomerId().equals(b.getCustomerId()) && b.getBillingMonth().equals(billingMonth)) {
                break;
            }
            if (foundCustomer.getCustomerId().equals(b.getCustomerId()) && !b.getBillingMonth().equals(billingMonth) && reg < b.getCurrentMeterReadingRegular()) {
                reg = b.getCurrentMeterReadingRegular();
            }
            if (foundCustomer.getCustomerId().equals(b.getCustomerId()) && peak < b.getCurrentMeterReadingPeak()) {
                peak = b.getCurrentMeterReadingPeak();
            }
        }

        LocalDate currentDate = LocalDate.now();
        String todayDate = currentDate.format(Help.Dateformatter);
        ArrayList<String> index = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();
        index.add("10");
        index.add("11");
        value.add("Paid");
        value.add(todayDate);
        Writer.updateBillFile(Constants.BILLINGINFO, foundCustomer.getCustomerId(), billingMonth, index, value);
        Writer.updateCustomerFile(Constants.CUSTOMERINFO, foundCustomer.getCustomerId(), foundBill.getCurrentMeterReadingRegular() - reg, foundBill.getCurrentMeterReadingPeak() - peak);
        custList.get(i).setRegularUnitsConsumed(Integer.toString((foundBill.getCurrentMeterReadingRegular() - reg) + Integer.parseInt(foundCustomer.getRegularUnitsConsumed())));
        custList.get(i).setPeakHourUnitsConsumed(Integer.toString((foundBill.getCurrentMeterReadingPeak() - peak) + Integer.parseInt(foundCustomer.getPeakHourUnitsConsumed())));
        billList.get(j).setBillPaidStatus("Paid");
        billList.get(j).setBillPaymentDate(todayDate);
        JOptionPane.showMessageDialog(this, "Bill Status Updated Successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        onSuccess.run();
    }
}
