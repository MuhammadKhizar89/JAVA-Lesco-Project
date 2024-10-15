package view;
import controller.BillingInfo;
import controller.Customer;
import controller.NADRADB;
import controller.TariffTaxInfo;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import model.Reader;
import model.Writer;
import utility.Constants;
import utility.Help;

public class EmployeeMenu extends JFrame {

     private String userName;
    private String password;
    private ArrayList<Customer> custList;
    private ArrayList<BillingInfo> billList;
    private ArrayList<TariffTaxInfo> rates;
    private ArrayList<NADRADB> nadraInfo;

    public EmployeeMenu(String userName,String password) {
        this.userName=userName;
        this.password=password;
        custList = Reader.readCustomerData();
        billList = Reader.readBillingInfo();
        rates = Reader.readTariffInfo();
        nadraInfo = Reader.readNADRAInfo();
        init();
    }

    public void init() {
        setTitle("Employee Menu");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(11, 1, 5, 5));
        addButton("Add Customer (New Meter)", e -> addCustomer());
        addButton("Add Bill", e -> addBillingInfo());
        addButton("Pay Bill", e -> payBill());
        addButton("View Any Bill", e -> viewBill());
        addButton("View Report of Paid and Unpaid Bills", e -> viewBillReports());
        addButton("View Report of CNIC Expiry", e -> viewCNICReports());
        addButton("Update Tariff Info", e -> updateTariffInfo());
        addButton("Change Password", e -> changeEmployeePassword());
        addButton("Update Customer Info (Meter)", e -> updateCustomerInfo());
        addButton("Update Bill", e -> updateBillInfo());
        addButton("Back", e -> backButton());
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void backButton() {
        dispose();
    }

    private void addButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        add(button);
    }

    private void addCustomer() {
        new AddCustomerInfo(custList, nadraInfo);
    }

    private void addBillingInfo() {
        new BillingInfoGUI(custList, billList, rates);
    }

    private void payBill() {
        new PayBillGUI(custList, billList);
    }

    private void viewBill() {
        new ViewBillGUI(billList);
    }

    private void viewBillReports() {
        new ViewBillReportsGUI(billList);
    }

    private void viewCNICReports() {
        new ViewCNICReports(nadraInfo);
    }

    private void updateTariffInfo() {
        new UpdateTariffInfoGUI(rates);
    }

    private void changeEmployeePassword() {
        new ChangeEmpPass(userName,password);
    }

    private void updateCustomerInfo() {
        new UpdCustInfoGUI(nadraInfo,custList);
    }

    private void updateBillInfo() {
        new UpdateBillInfoGUI(billList,custList,rates);
    }
}
