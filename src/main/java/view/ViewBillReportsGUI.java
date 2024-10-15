package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import controller.BillingInfo;

public class ViewBillReportsGUI extends JFrame {

    private JLabel paidBillLabel;
    private JLabel unpaidBillLabel;

    public ViewBillReportsGUI(ArrayList<BillingInfo> billList) {
        setTitle("Bill Report");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        paidBillLabel = new JLabel("Total Amount of Paid Bills: ");
        unpaidBillLabel = new JLabel("Total Amount of Unpaid Bills: ");
        setLayout(new GridLayout(3, 1));
        add(paidBillLabel);
        add(unpaidBillLabel);
        viewBillReports(billList);

        setVisible(true);
    }

    private void viewBillReports(ArrayList<BillingInfo> billList) {
        double paidBillSum = 0;
        double unPaidBillSum = 0;

        for (BillingInfo b : billList) {
            if ("Paid".equals(b.getBillPaidStatus())) {
                paidBillSum += b.getTotalBillingAmount();
            } else if ("Unpaid".equals(b.getBillPaidStatus())) {
                unPaidBillSum += b.getTotalBillingAmount();
            }
        }

        // Updating labels with calculated sums
        paidBillLabel.setText("Total Amount of Paid Bills: " + paidBillSum);
        unpaidBillLabel.setText("Total Amount of Unpaid Bills: " + unPaidBillSum);
    }

}
