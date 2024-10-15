package controller;

import utility.Constants;
import controller.NADRADB;
import model.Reader;
import model.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import view.CustomerMenuGUI;

public class Customer {

    private String customerId;
    private String cnic;
    private String name;
    private String address;
    private String phone;
    private String customerType;
    private String meterType;
    private String connectionDate;
    private String regularUnitsConsumed;
    private String peakHourUnitsConsumed;

    public Customer(String customerId, String cnic, String name, String address, String phone,
            String customerType, String meterType, String connectionDate,
            String regularUnitsConsumed, String peakHourUnitsConsumed) {
        this.customerId = customerId;
        this.cnic = cnic;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.customerType = customerType;
        this.meterType = meterType;
        this.connectionDate = connectionDate;
        this.regularUnitsConsumed = regularUnitsConsumed;
        this.peakHourUnitsConsumed = peakHourUnitsConsumed;
    }

// Getter and Setter for customerId
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    // Getter and Setter for cnic
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter and Setter for phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter and Setter for customerType
    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    // Getter and Setter for meterType
    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    // Getter and Setter for connectionDate
    public String getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(String connectionDate) {
        this.connectionDate = connectionDate;
    }

    // Getter and Setter for regularUnitsConsumed
    public String getRegularUnitsConsumed() {
        return regularUnitsConsumed;
    }

    public void setRegularUnitsConsumed(String regularUnitsConsumed) {
        this.regularUnitsConsumed = regularUnitsConsumed;
    }

    // Getter and Setter for peakHourUnitsConsumed
    public String getPeakHourUnitsConsumed() {
        return peakHourUnitsConsumed;
    }

    public void setPeakHourUnitsConsumed(String peakHourUnitsConsumed) {
        this.peakHourUnitsConsumed = peakHourUnitsConsumed;
    }

    @Override
    public String toString() {
        return "Customer{"
                + "customerId='" + customerId + '\''
                + ", cnic='" + cnic + '\''
                + ", name='" + name + '\''
                + ", address='" + address + '\''
                + ", phone='" + phone + '\''
                + ", customerType='" + customerType + '\''
                + ", meterType='" + meterType + '\''
                + ", connectionDate='" + connectionDate + '\''
                + ", regularUnitsConsumed='" + regularUnitsConsumed + '\''
                + ", peakHourUnitsConsumed='" + peakHourUnitsConsumed + '\''
                + '}';
    }

    void viewBill(ArrayList<Customer> custList, ArrayList<BillingInfo> billList) {
        Scanner custInfo = new Scanner(System.in);
        System.out.print("Enter customerId to View All BIlls: ");
        String customerId = custInfo.nextLine();
        Customer foundCustomer = null;

        for (Customer c : custList) {
            if (customerId.equals(c.getCustomerId())) {
                if (cnic.equals(c.getCnic())) {
                    System.out.println("Customer Found: " + c.toString());
                    foundCustomer = c;
                    break;
                } else {
                    System.out.println("NO such customerID found on your CNIC");
                    return;
                }
            }
        }
        if (foundCustomer == null) {
            System.out.println("No bill found for this Customer ID.");
            return;
        }
        boolean flag = false;

        for (BillingInfo b : billList) {
            if (b.getCustomerId().equals(customerId)) {
                System.out.print(b.toString() + '\n');
                flag = true;
            }
        }
        if (!flag) {
            System.out.print("No Bill Found of Such id \n");
        }
    }

    void updateCNICexpiry(ArrayList<NADRADB> nadraInfo) {
        NADRADB found_nadra = null;
        for (NADRADB n : nadraInfo) {
            if (n.getCNIC().equals(cnic)) {
                System.out.println("Your Current Info " + n.toString());
                found_nadra = n;
            }
        }

        if (found_nadra == null) {
            System.out.println("No data Found for your CNIC in NADRA DB");
            return;  // Exit if no record is found
        }

        Scanner dateInfo = new Scanner(System.in);
        String expiryDateInfo = "";
        String issueDateInfo = found_nadra.getIssueDate();

        // Parse issue date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate issueDate;
        try {
            issueDate = LocalDate.parse(issueDateInfo, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid issue date format in NADRA DB.");
            return;
        }

        boolean isValid = false;
        while (!isValid) {
            System.out.print("Enter Expiry Date (DD/MM/YYYY): ");
            expiryDateInfo = dateInfo.nextLine();

            // Validate format
            if (expiryDateInfo.matches("\\d{2}/\\d{2}/\\d{4}")) {
                try {
                    LocalDate expiryDate = LocalDate.parse(expiryDateInfo, formatter);

                    // Check if expiry date is after issue date
                    if (expiryDate.isAfter(issueDate)) {
                        isValid = true;
                    } else {
                        System.out.println("Expiry date must be after the issue date (" + issueDateInfo + ").");
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter a valid date.");
                }
            } else {
                System.out.println("Invalid Date format. Please enter in DD/MM/YYYY format.");
            }
        }

        // Continue with updating the file
        ArrayList<String> index = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();
        index.add("2");
        value.add(expiryDateInfo);
        Writer.updateFile(Constants.NADRA, cnic, index, value);
    }

    public void customerMenu() {
        CustomerMenuGUI custmenuGUI=new  CustomerMenuGUI(cnic,customerId);        
    }

}
