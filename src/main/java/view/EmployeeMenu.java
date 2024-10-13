package view;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class EmployeeMenu extends JFrame {
    public EmployeeMenu() {
        init();
    }
    public void init() {
        setTitle("Employee Menu");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(11, 1, 5, 5)); 
        addButton("Add Customer (New Meter)", e -> addCustomerInfo());
        addButton("Add Bill", e -> addBillingInfo());
        addButton("Pay Bill", e -> payBill());
        addButton("View Any Bill", e -> viewBill());
        addButton("View Report of Paid and Unpaid Bills", e -> viewBillReports());
        addButton("View Report of CNIC Expiry", e -> viewCNICReports());
        addButton("Update Tariff Info", e -> updateTariffInfo());
        addButton("Change Password", e -> changeEmployeePassword());
        addButton("Update Customer Info (Meter)", e -> updateCustomerInfo());
        addButton("Update Bill", e -> updateBillInfo());
        addButton("Exit", e -> dispose());
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        add(button);
    }

    private void addCustomerInfo() {
        JOptionPane.showMessageDialog(this, "Add Customer Info functionality triggered.");
    }

    private void addBillingInfo() {
        JOptionPane.showMessageDialog(this, "Add Billing Info functionality triggered.");
        // Add logic to open add billing info form or dialog
    }

    private void payBill() {
        JOptionPane.showMessageDialog(this, "Pay Bill functionality triggered.");
        // Add logic to handle bill payment
    }

    private void viewBill() {
        JOptionPane.showMessageDialog(this, "View Bill functionality triggered.");
        // Add logic to view a specific bill
    }

    private void viewBillReports() {
        JOptionPane.showMessageDialog(this, "View Bill Reports functionality triggered.");
        // Add logic to view report of paid and unpaid bills
    }

    private void viewCNICReports() {
        JOptionPane.showMessageDialog(this, "View CNIC Reports functionality triggered.");
        // Add logic to view CNIC expiry report
    }

    private void updateTariffInfo() {
        JOptionPane.showMessageDialog(this, "Update Tariff Info functionality triggered.");
        // Add logic to update tariff info
    }

    private void changeEmployeePassword() {
        JOptionPane.showMessageDialog(this, "Change Password functionality triggered.");
        // Add logic to change employee password
    }

    private void updateCustomerInfo() {
        JOptionPane.showMessageDialog(this, "Update Customer Info functionality triggered.");
        // Add logic to update customer info
    }

    private void updateBillInfo() {
        JOptionPane.showMessageDialog(this, "Update Bill Info functionality triggered.");
        // Add logic to update bill info
    }
}
