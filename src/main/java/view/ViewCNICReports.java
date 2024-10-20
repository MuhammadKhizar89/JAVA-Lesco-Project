package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import controller.NADRADB;
import utility.Help;
public class ViewCNICReports extends JFrame {
    private JTextArea resultTextArea;
    public ViewCNICReports(ArrayList<NADRADB> nadraInfo) {
        setTitle("CNIC Expiry Notification");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        resultTextArea = new JTextArea(10, 30);
        resultTextArea.setEditable(false);
        setLayout(new BorderLayout());
        add(new JScrollPane(resultTextArea), BorderLayout.CENTER);
        checkExpiry(nadraInfo);
        setVisible(true);
    }
    private void checkExpiry(ArrayList<NADRADB> nadraInfo) {
        LocalDate currentDate = LocalDate.now();
        LocalDate within30Days = currentDate.plusDays(30);
        boolean isFound = false;
        StringBuilder resultText = new StringBuilder();
        for (NADRADB n : nadraInfo) {
            LocalDate expiryDate = LocalDate.parse(n.getExpiryDate(), Help.Dateformatter);
            if (!expiryDate.isBefore(currentDate) && !expiryDate.isAfter(within30Days)) {
                resultText.append("Customer with CNIC about to expire: ").append(n.getCNIC()).append("\n");
                isFound = true;
            }
        }
        if (isFound) {
            resultTextArea.setText(resultText.toString());
        } else {
            resultTextArea.setText("No Customer Found");
        }
    }
}
