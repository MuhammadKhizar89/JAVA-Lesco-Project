package view;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import controller.NADRADB;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import model.Writer;
import utility.Constants;
public class UpdateNADRAGUI extends JFrame {
    private JTextField issueDateField;
    private JTextField expiryDateField;
    private NADRADB obj;
    private Runnable obj1;
    public UpdateNADRAGUI(NADRADB obj, Runnable obj1) {
        this.obj1 = obj1;
        this.obj = obj;
        setTitle("Update NADRA");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        JLabel cnicLabel = new JLabel("CNIC:");
        JLabel cnicValueLabel = new JLabel(obj.getCNIC());
        cnicValueLabel.setEnabled(false);
        JLabel issueDateLabel = new JLabel("Issue Date:");
        issueDateField = new JTextField();
        JLabel expiryDateLabel = new JLabel("Expiry Date:");
        expiryDateField = new JTextField();
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateNADRA();
            }
        });
        panel.add(cnicLabel);
        panel.add(cnicValueLabel);
        panel.add(issueDateLabel);
        panel.add(issueDateField);
        panel.add(expiryDateLabel);
        panel.add(expiryDateField);
        panel.add(new JLabel());
        panel.add(updateButton);
        add(panel);
        setVisible(true);
    }
    private void updateNADRA() {
        String issueDate = issueDateField.getText();
        String expiryDate = expiryDateField.getText();
        String datePattern = "^\\d{2}/\\d{2}/\\d{4}$";
        if (!issueDate.matches(datePattern)) {
            JOptionPane.showMessageDialog(this, "Invalid Issue Date format. Please use DD/MM/YYYY.");
            return;
        }
        if (!expiryDate.matches(datePattern)) {
            JOptionPane.showMessageDialog(this, "Invalid Expiry Date format. Please use DD/MM/YYYY.");
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date issueDateParsed = sdf.parse(issueDate);
            Date expiryDateParsed = sdf.parse(expiryDate);
            if (expiryDateParsed.before(issueDateParsed)) {
                JOptionPane.showMessageDialog(this, "Expiry Date cannot be before Issue Date.");
                return;
            }
            obj.setExpiryDate(expiryDate);
            obj.setIssueDate(issueDate);
            ArrayList<String> index = new ArrayList<>();
            index.add("1");
            index.add("2");
            ArrayList<String> value = new ArrayList<>();
            value.add(issueDate);
            value.add(expiryDate);
            Writer.updateFile(Constants.NADRA, obj.getCNIC(), index, value);
            JOptionPane.showMessageDialog(this, "Updated successfully.");
            obj1.run();
            dispose();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Error parsing dates. Please ensure they are in DD/MM/YYYY format.");
        }
    }
}
