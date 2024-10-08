package controller;

import utility.Constants;
import utility.Help;
import model.Reader;
import model.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Employee {

    private String userName;
    private String password;

    Employee() {
    }

   public Employee(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    void updateCustomerInfo(ArrayList<NADRADB> nadraInfo, ArrayList<Customer> custList) {
        Scanner customerInfo = new Scanner(System.in);
        ArrayList<String> customerDataList = new ArrayList<>();
        System.out.print("Enter customerId to Update Customer: ");
        String customerId = customerInfo.nextLine();
        Customer foundCustomer = null;
        int i = -1;
        for (Customer c : custList) {
            ++i;
            if (customerId.equals(c.getCustomerId())) {
                System.out.println("Customer Found: " + c.toString());
                foundCustomer = c;
                break;
            }
        }
        if (foundCustomer == null) {
            System.out.println("Customer Not Found.");
            return;
        }
        String cnic = "", name = "", address = "", phone = "", customerType = "", meterType = "", connectionDate = "";
        boolean isValid = false;
        int count = 0;
        while (!isValid) {
            System.out.print("Enter CNIC (13 digits, no dashes): ");
            cnic = customerInfo.nextLine();
            if (cnic.matches("\\d{13}")) {
                isValid = true;
                boolean isCNICPresentInNadra = false;
                for (NADRADB n : nadraInfo) {
                    if (n.getCNIC().equals(cnic)) {
                        isCNICPresentInNadra = true;
                    }
                }
                if (!isCNICPresentInNadra) {
                    System.out.print("This CNIC Not Found in Nadra DB ");
                    return;
                }
                for (Customer c : custList) {
                    if (cnic.equals(c.getCnic())) {
                        count++;
                    }
                    if (count == 3) {
                        System.out.println("Not Allowed! Maximum 3 meters allowed per CNIC.");
                        return;
                    }
                }
                custList.get(i).setCnic(cnic);
            } else {
                System.out.println("Invalid CNIC. Please enter exactly 13 digits without dashes.");
            }
        }

        isValid = false;
        while (!isValid) {
            System.out.print("Enter Customer Name: ");
            name = customerInfo.nextLine();
            if (!name.trim().isEmpty()) {
                isValid = true;
                custList.get(i).setName(name);
            } else {
                System.out.println("Name cannot be empty.");
            }
        }

        isValid = false;
        while (!isValid) {
            System.out.print("Enter Address: ");
            address = customerInfo.nextLine();
            if (!address.trim().isEmpty() && !address.contains(",")) {
                isValid = true;
                custList.get(i).setAddress(address);
            } else if (address.contains(",")) {
                System.out.println("Address should not contain any commas. Please use dashes.");
            } else {
                System.out.println("Address cannot be empty.");
            }
        }

        isValid = false;
        while (!isValid) {
            System.out.print("Enter Phone Number: ");
            phone = customerInfo.nextLine();
            if (phone.matches("\\d{10,15}")) {
                isValid = true;
                custList.get(i).setPhone(phone);
            } else {
                System.out.println("Invalid Phone Number. Enter between 10 and 15 digits.");
            }
        }

        isValid = false;
        while (!isValid) {
            System.out.print("Enter Customer Type (commercial/domestic): ");
            customerType = customerInfo.nextLine();
            if (customerType.equalsIgnoreCase("commercial") || customerType.equalsIgnoreCase("domestic")) {
                isValid = true;
                custList.get(i).setCustomerType(customerType);
            } else {
                System.out.println("Invalid Customer Type. Please enter either 'commercial' or 'domestic'.");
            }
        }

        isValid = false;
        while (!isValid) {
            System.out.print("Enter Meter Type (Single Phase/Three Phase): ");
            meterType = customerInfo.nextLine();
            if (meterType.equalsIgnoreCase("Single Phase") || meterType.equalsIgnoreCase("Three Phase")) {
                isValid = true;
                custList.get(i).setMeterType(meterType);
            } else {
                System.out.println("Invalid Meter Type. Please enter either 'Single Phase' or 'Three Phase'.");
            }
        }

        isValid = false;
        while (!isValid) {
            System.out.print("Enter Connection Date (DD/MM/YYYY): ");
            connectionDate = customerInfo.nextLine();
            if (connectionDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                isValid = true;
                custList.get(i).setConnectionDate(connectionDate);
            } else {
                System.out.println("Invalid Date format. Please enter in DD/MM/YYYY format.");
            }
        }
        ArrayList<String> index = new ArrayList<>();
        customerDataList.add(cnic);
        customerDataList.add(name);
        customerDataList.add(address);
        customerDataList.add(phone);
        customerDataList.add(customerType);
        customerDataList.add(meterType);
        customerDataList.add(connectionDate);
        index.add("1");
        index.add("2");
        index.add("3");
        index.add("4");
        index.add("5");
        index.add("6");
        index.add("7");
        Writer.updateFile(Constants.CUSTOMERINFO, foundCustomer.getCustomerId(), index, customerDataList);
        System.out.print("Customer added Successfully: ");

    }

    private void addCustomerInfo(ArrayList<NADRADB> nadraInfo, ArrayList<Customer> custList) {
        Scanner customerInfo = new Scanner(System.in);
        ArrayList<String> customerDataList = new ArrayList<>();
        String cnic = "", name = "", address = "", phone = "", customerType = "", meterType = "", connectionDate = "";
        boolean isValid = false;
        int count = 0;

        while (!isValid) {
            System.out.print("Enter CNIC (13 digits, no dashes): ");
            cnic = customerInfo.nextLine();
            if (cnic.matches("\\d{13}")) {
                isValid = true;
                boolean isCNICPresentInNadra = false;
                for (NADRADB n : nadraInfo) {
                    if (n.getCNIC().equals(cnic)) {
                        isCNICPresentInNadra = true;
                    }
                }
                if (!isCNICPresentInNadra) {
                    System.out.print("This CNIC Not Found in Nadra DB ");
                    return;
                }
                for (Customer c : custList) {
                    if (cnic.equals(c.getCnic())) {
                        count++;
                    }
                    if (count == 3) {
                        System.out.println("Not Allowed! Maximum 3 meters allowed per CNIC.");
                        return;
                    }
                }
            } else {
                System.out.println("Invalid CNIC. Please enter exactly 13 digits without dashes.");
            }
        }
        isValid = false;
        while (!isValid) {
            System.out.print("Enter Customer Name: ");
            name = customerInfo.nextLine();
            if (!name.trim().isEmpty()) {
                isValid = true;
            } else {
                System.out.println("Name cannot be empty.");
            }
        }
        isValid = false;
        while (!isValid) {
            System.out.print("Enter Address: ");
            address = customerInfo.nextLine();
            if (!address.trim().isEmpty() && !address.contains(",")) {
                isValid = true;
            } else if (address.contains(",")) {
                System.out.println("Address should not contain any commas. Please use dashes.");
            } else {
                System.out.println("Address cannot be empty.");
            }
        }
        isValid = false;
        while (!isValid) {
            System.out.print("Enter Phone Number: ");
            phone = customerInfo.nextLine();
            if (phone.matches("\\d{10,15}")) {
                isValid = true;
            } else {
                System.out.println("Invalid Phone Number. Enter between 10 and 15 digits.");
            }
        }
        isValid = false;
        while (!isValid) {
            System.out.print("Enter Customer Type (commercial/domestic): ");
            customerType = customerInfo.nextLine();
            if (customerType.equalsIgnoreCase("commercial") || customerType.equalsIgnoreCase("domestic")) {
                isValid = true;
            } else {
                System.out.println("Invalid Customer Type. Please enter either 'commercial' or 'domestic'.");
            }
        }
        isValid = false;
        while (!isValid) {
            System.out.print("Enter Meter Type (Single Phase/Three Phase): ");
            meterType = customerInfo.nextLine();
            if (meterType.equalsIgnoreCase("Single Phase") || meterType.equalsIgnoreCase("Three Phase")) {
                isValid = true;
            } else {
                System.out.println("Invalid Meter Type. Please enter either 'Single Phase' or 'Three Phase'.");
            }
        }
        isValid = false;
        while (!isValid) {
            System.out.print("Enter Connection Date (DD/MM/YYYY): ");
            connectionDate = customerInfo.nextLine();
            if (connectionDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                isValid = true;
            } else {
                System.out.println("Invalid Date format. Please enter in DD/MM/YYYY format.");
            }
        }
        String regularUnitsConsumed = "0";
        String peakHourUnitsConsumed = meterType.equalsIgnoreCase("Three Phase") ? "0" : "0";
        // Generate a unique customer ID
        String customerId = Help.generateCustomerId();
        // Add all the data to the ArrayList
        customerDataList.add(customerId);
        customerDataList.add(cnic);
        customerDataList.add(name);
        customerDataList.add(address);
        customerDataList.add(phone);
        customerDataList.add(customerType);
        customerDataList.add(meterType);
        customerDataList.add(connectionDate);
        customerDataList.add(regularUnitsConsumed);
        customerDataList.add(peakHourUnitsConsumed);
        custList.add(new Customer(customerId, cnic, name, address, phone, customerType, meterType, connectionDate, regularUnitsConsumed, peakHourUnitsConsumed));
        Writer.write(Constants.CUSTOMERINFO, customerDataList);
        System.out.println("Customer added successfully. Customer ID: " + customerId);
    }

    private void addBillingInfo(ArrayList<Customer> custList, ArrayList<BillingInfo> billList, ArrayList<TariffTaxInfo> rates) {

        Scanner billingInfo = new Scanner(System.in);
        System.out.print("Enter customerId to add bill info: ");
        String customerId = billingInfo.nextLine();
        Customer foundCustomer = null;
        int i = 0;
        for (Customer c : custList) {
            if (customerId.equals(c.getCustomerId())) {
                System.out.println("Customer Found: " + c.toString());
                foundCustomer = c;
                break;
            }
            ++i;
        }
        if (foundCustomer == null) {
            System.out.println("Customer Not Found.");
            return;
        }

        boolean isValid = false;
        String billingMonth = "";
        while (!isValid) {
            System.out.print("Enter Billing Month (MM/YYYY): ");
            billingMonth = billingInfo.nextLine();
            if (billingMonth.matches("\\d{2}/\\d{4}")) {
                isValid = true;
            } else {
                System.out.println("Follow the pattern MM/YYYY.");
            }
        }

        SimpleDateFormat billingMonthFormat = new SimpleDateFormat("MM/yyyy");
        SimpleDateFormat connectionDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date billingMonth1 = null;
        Date connectionDate = null;
        try {
            billingMonth1 = billingMonthFormat.parse(billingMonth);
            connectionDate = connectionDateFormat.parse(foundCustomer.getConnectionDate());
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error parsing dates");
            return;
        }
        for (BillingInfo b : billList) {

            if (foundCustomer.getCustomerId().equals(b.getCustomerId()) && b.getBillingMonth().equals(billingMonth)) {
                System.out.println("Bill for this month already Created");
                return;
            }

            if (foundCustomer.getCustomerId().equals(b.getCustomerId()) && billingMonth1.before(connectionDate)) {
                System.out.println("Cannot enter bill because billingMonth date is before connection date" + billingMonth + " " + b.getBillingMonth());
                return;
            }
        }
        if (billingMonth1.before(connectionDate)) {
            System.out.println("Cannot enter bill because billingMonth date is before connection date");
            return;
        }

        int reg = 0, peak = 0;
        for (BillingInfo b : billList) {
            if (foundCustomer.getCustomerId().equals(b.getCustomerId()) && reg < b.getCurrentMeterReadingRegular()) {
                reg = b.getCurrentMeterReadingRegular();
            }
            if (foundCustomer.getCustomerId().equals(b.getCustomerId()) && peak < b.getCurrentMeterReadingPeak()) {
                peak = b.getCurrentMeterReadingPeak();
            }
        }
        System.out.println(reg + " " + peak);
        int currentMeterReadingRegular = 0;
        while (true) {
            System.out.print("Enter Current Meter Reading (Regular): ");
            try {
                currentMeterReadingRegular = Integer.parseInt(billingInfo.nextLine());
                if (currentMeterReadingRegular >= 0) {
                    if (currentMeterReadingRegular < reg) {
                        System.out.println("Reading cannot be less than previous Meter Reading. Please enter a valid reading.");
                        continue;
                    }
                    break;
                } else {
                    System.out.println("Reading cannot be negative. Please enter a valid reading.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        int currentMeterReadingPeak = 0;
        if (foundCustomer.getMeterType().equalsIgnoreCase("Three Phase")) {
            while (true) {
                System.out.print("Enter Current Meter Reading (Peak): ");
                try {
                    currentMeterReadingPeak = Integer.parseInt(billingInfo.nextLine());
                    if (currentMeterReadingPeak >= 0) {
                        if (currentMeterReadingPeak < peak) {
                            System.out.println("Reading cannot be less than previous Meter Reading. Please enter a valid reading.");
                        }
                        break;
                    } else {
                        System.out.println("Reading cannot be negative. Please enter a valid reading.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate dateAfter7Days = currentDate.plusDays(7);
        String todayDate = currentDate.format(Help.Dateformatter);
        String dateAfter7DaysFormatted = dateAfter7Days.format(Help.Dateformatter);

        if (reg > 0) {
            currentMeterReadingRegular = currentMeterReadingRegular - reg;
        }
        if (peak > 0) {
            currentMeterReadingPeak = currentMeterReadingPeak - peak;
        }
        System.out.println("Today's date: " + todayDate);
        System.out.println("Date after 7 days: " + dateAfter7DaysFormatted);
        int costofElectricity;
        double salesTax = 0;
        int fixed = 0;
        int regularUnitsPrice = 0;
        int peakhourUnitsPrice = 0;
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

        costofElectricity = (regularUnitsPrice * currentMeterReadingRegular) + (peakhourUnitsPrice * currentMeterReadingPeak);
        double totalBilling = costofElectricity + ((costofElectricity / 100) * salesTax) + fixed;

        String billPaidStatus = "Unpaid";
        String billPaymentDate = "N/A";
        ArrayList<String> AllData = new ArrayList<>();
        AllData.add(foundCustomer.getCustomerId());
        AllData.add(billingMonth);
        AllData.add(Integer.toString(currentMeterReadingRegular + reg));
        AllData.add(Integer.toString(currentMeterReadingPeak + peak));
        AllData.add(todayDate);
        AllData.add(Integer.toString(costofElectricity));
        AllData.add(Double.toString(salesTax));
        AllData.add(Integer.toString(fixed));
        AllData.add(Double.toString(totalBilling));
        AllData.add(dateAfter7DaysFormatted);
        AllData.add(billPaidStatus);
        AllData.add(billPaymentDate);
        Writer.write(Constants.BILLINGINFO, AllData);

        billList.add(new BillingInfo(customerId, billingMonth, currentMeterReadingRegular + reg, currentMeterReadingPeak + peak,
                todayDate, costofElectricity, salesTax, fixed, totalBilling,
                dateAfter7DaysFormatted, billPaidStatus, billPaymentDate));
        System.out.println("Bill Info Addedd Sucessfully" + totalBilling);
    }

    private void payBill(ArrayList<Customer> custList, ArrayList<BillingInfo> billList) {
        Scanner billingInfo = new Scanner(System.in);
        for (BillingInfo e : billList) {
            System.out.println(e.toString());
        }

        System.out.print("Enter customerId to add bill info: ");
        String customerId = billingInfo.nextLine();
        int i = -1;
        Customer foundCustomer = null;
        for (Customer c : custList) {
            i++;
            if (customerId.equals(c.getCustomerId())) {
                foundCustomer = c;
                break;
            }
        }
        if (foundCustomer == null) {
            System.out.println("Customer Not Found.");
            return;
        }

        boolean isValid = false;
        String billingMonth = "";
        BillingInfo foundBill = null;
        int j = -1;
        while (!isValid) {
            System.out.print("Enter Bill Month (MM/YYYY): ");
            billingMonth = billingInfo.nextLine();
            if (billingMonth.matches("\\d{2}/\\d{4}")) {
                isValid = true;

                for (BillingInfo b : billList) {
                    j++;
                    if (b.getCustomerId().equals(foundCustomer.getCustomerId()) && b.getBillingMonth().equals(billingMonth)) {
                        if (b.getBillPaidStatus().equals("Paid")) {
                            System.out.println("Bill Already Paid.");
                            return;
                        }
                        foundBill = b;
                        break;
                    }

                }
            } else {
                System.out.println("Follow the pattern MM/YYYY.");
            }
        }
        if (foundBill == null) {
            System.out.println("Bill Not Found of such Date .");
            return;
        }
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
        System.out.println("Bill Status Updated SuccessFully.");
    }

    void viewBill(ArrayList<BillingInfo> billList) {
        Scanner billingInfo = new Scanner(System.in);
        System.out.print("Enter customerId to View All BIlls: ");
        String customerId = billingInfo.nextLine();
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

    void viewBillReports(ArrayList<BillingInfo> billList) {
        double paidBillSum = 0;
        double unPaidBillSum = 0;
        for (BillingInfo b : billList) {
            if (b.getBillPaidStatus().equals("Paid")) {
                paidBillSum += b.getTotalBillingAmount();
            }
            if (b.getBillPaidStatus().equals("Unpaid")) {
                unPaidBillSum += b.getTotalBillingAmount();
            }
        }
        System.out.print("Total Amount of UnPaid Bill " + unPaidBillSum + "\n");
        System.out.print("Total Amount of Paid Bill " + paidBillSum + "\n");
    }

    void viewCNICReports(ArrayList<NADRADB> nadraInfo) {
        // Get current date
        LocalDate currentDate = LocalDate.now();

        // Get the date 30 days ahead
        LocalDate within30Days = currentDate.plusDays(30);
        boolean isFound = false;
        // Iterate through the NADRA database list
        for (NADRADB n : nadraInfo) {
            // Parse the expiry date from the NADRA record (assuming it's stored as String in dd/MM/yyyy)
            LocalDate expiryDate = LocalDate.parse(n.getExpiryDate(), Help.Dateformatter);

            // Check if the expiry date is within the next 30 days
            if (!expiryDate.isBefore(currentDate) && !expiryDate.isAfter(within30Days)) {
                System.out.println("Customer with CNIC about to expire: " + n.getCNIC());
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No Customer Found");
        }
    }

    void updateTarrifInfo(ArrayList<TariffTaxInfo> tarrifInfo) {
        Scanner object = new Scanner(System.in);
        int i = 0;
        for (TariffTaxInfo t : tarrifInfo) {
            ++i;
            System.out.println("Press " + i + " To Edit " + t.toString());
        }
        int option = -1;
        boolean isValid = false;
        while (!isValid) {
            System.out.print("Enter option (1 to " + tarrifInfo.size() + "): ");
            option = object.nextInt();
            if (option > 0 && option <= tarrifInfo.size()) {
                isValid = true;
            } else {
                System.out.println("Invalid option. Please select a valid option.");
            }
        }
        int index = option - 1;
        System.out.print("Enter new regularUnitsPrice: ");
        int newRegularUnits = object.nextInt();
        int newPeakHourUnits = 0;
        if (tarrifInfo.get(index).getMeter_type().equals("3Phase")) {
            System.out.print("Enter new peakhourUnitsPrice: ");
            newPeakHourUnits = object.nextInt();
        }
        System.out.print("Enter new Sales Tax percentage: ");
        double newSalesTax = object.nextDouble();
        System.out.print("Enter new fixed charges: ");
        int newFixedCharges = object.nextInt();
        tarrifInfo.get(index).setRegularUnits(newRegularUnits);
        if (tarrifInfo.get(index).getMeter_type().equals("3Phase")) {
            tarrifInfo.get(index).setPeakhourUnits(newPeakHourUnits);
        } else {
            tarrifInfo.get(index).setPeakhourUnits(0);
        }
        tarrifInfo.get(index).setPercentage(newSalesTax);
        tarrifInfo.get(index).setFixedCharges(newFixedCharges);
        for (TariffTaxInfo t : tarrifInfo) {
            System.out.println(t.toString());
        }
        Writer.overwriteTarrifFile(Constants.TARIFFTAX, tarrifInfo);
        System.out.println("Tariff information updated successfully.");
    }

    void changeEmployeePassword() {
        Scanner authInfo = new Scanner(System.in);
        System.out.println("Enter Current Password");
        String currpassword = authInfo.nextLine();
        if (!currpassword.equals(password)) {
            System.out.println("Wrong Current Password");
            return;
        }
        System.out.println("Enter New Password");
        String newPassword = authInfo.nextLine();
        ArrayList<String> index = new ArrayList<>();
        ArrayList<String> value = new ArrayList<>();
        index.add("1");
        value.add(newPassword);
        Writer.updateFile(Constants.EMPLOYEESDATA, userName, index, value);
        System.out.println("Password Updated Successfully");
    }

    void updateBillInfo(ArrayList<BillingInfo> billList, ArrayList<Customer> custList, ArrayList<TariffTaxInfo> rates) {
        Scanner billingInfo = new Scanner(System.in);
        System.out.print("Enter CustomerID to update bill info: ");
        String customerId = billingInfo.nextLine();
        Customer foundCustomer = null;
        for (Customer c : custList) {
            if (customerId.equals(c.getCustomerId())) {
                System.out.println("Customer Found: " + c.toString());
                foundCustomer = c;
                break;
            }
        }
        if (foundCustomer == null) {
            System.out.println("Customer Not Found.");
            return;
        }
        String billingMonth = "";
        boolean isValid = false;
        while (!isValid) {
            System.out.print("Enter Billing Month (MM/YYYY): ");
            billingMonth = billingInfo.nextLine();
            if (billingMonth.matches("\\d{2}/\\d{4}")) {
                isValid = true;
            } else {
                System.out.println("Follow the pattern MM/YYYY.");
            }
        }

        BillingInfo foundBill = null;
        int j = -1;
        for (BillingInfo b : billList) {
            j++;
            if (b.getCustomerId().equals(customerId) && b.getBillingMonth().equals(billingMonth)) {
                foundBill = b;
                System.out.println("Current Bill Info: " + b.toString());
                break;
            }
        }

        if (foundBill == null) {
            System.out.println("Bill not found for the given month.");
            return;
        }

        int reg = 0, peak = 0;
        for (BillingInfo b : billList) {
            if (b.getBillingMonth().equals(billingMonth)) {
                break;
            }
            if (foundCustomer.getCustomerId().equals(b.getCustomerId()) && reg < b.getCurrentMeterReadingRegular()) {
                reg = b.getCurrentMeterReadingRegular();
            }
            if (foundCustomer.getCustomerId().equals(b.getCustomerId()) && peak < b.getCurrentMeterReadingPeak()) {
                peak = b.getCurrentMeterReadingPeak();
            }
        }
        int currentMeterReadingRegular = 0;
        while (true) {
            System.out.print("Enter New Current Meter Reading (Regular): ");
            try {
                currentMeterReadingRegular = Integer.parseInt(billingInfo.nextLine());
                if (currentMeterReadingRegular >= 0) {
                    if (currentMeterReadingRegular < reg) {
                        System.out.println("Reading cannot be less than previous Meter Reading. Please enter a valid reading.");
                        continue;
                    }
                    break;
                } else {
                    System.out.println("Reading cannot be negative. Please enter a valid reading.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        int currentMeterReadingPeak = 0;
        if (foundCustomer.getMeterType().equalsIgnoreCase("Three Phase")) {
            while (true) {
                System.out.print("Enter New Current Meter Reading (Peak): ");
                try {
                    currentMeterReadingPeak = Integer.parseInt(billingInfo.nextLine());
                    if (currentMeterReadingPeak >= 0) {
                        if (currentMeterReadingPeak < peak) {
                            System.out.println("Reading cannot be less than previous Meter Reading. Please enter a valid reading.");
                        }
                        break;
                    } else {
                        System.out.println("Reading cannot be negative. Please enter a valid reading.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
        }
        if (reg > 0) {
            currentMeterReadingRegular = currentMeterReadingRegular - reg;
        }
        if (peak > 0) {
            currentMeterReadingPeak = currentMeterReadingPeak - peak;
        }

        int costofElectricity;
        double salesTax = 0;
        int fixed = 0;
        int regularUnitsPrice = 0;
        int peakhourUnitsPrice = 0;
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

        costofElectricity = (regularUnitsPrice * currentMeterReadingRegular) + (peakhourUnitsPrice * currentMeterReadingPeak);
        double totalBilling = costofElectricity + ((costofElectricity / 100) * salesTax) + fixed;
        ArrayList<String> AllData = new ArrayList<>();
        AllData.add(Integer.toString(currentMeterReadingRegular + reg));
        AllData.add(Integer.toString(currentMeterReadingPeak + peak));
        AllData.add(Integer.toString(costofElectricity));
        AllData.add(Double.toString(salesTax));
        AllData.add(Integer.toString(fixed));
        AllData.add(Double.toString(totalBilling));
        ArrayList<String> Index = new ArrayList<>();
        Index.add("2");
        Index.add("3");
        Index.add("5");
        Index.add("6");
        Index.add("7");
        Index.add("8");
        Writer.updateBillFile(Constants.BILLINGINFO, foundCustomer.getCustomerId(), billingMonth, Index, AllData);
        billList.get(j).setSalesTax(salesTax);
        billList.get(j).setFixedCharges(fixed);
        billList.get(j).setTotalBillingAmount(totalBilling);
        billList.get(j).setCostOfElectricity(costofElectricity);
        billList.get(j).setCurrentMeterReadingPeak(currentMeterReadingPeak + peak);
        billList.get(j).setCurrentMeterReadingRegular(currentMeterReadingRegular + reg);
        System.out.println("Bill Updated SuccessFully");

    }

    public void employeeMenu() {
        ArrayList<Customer> custList = Reader.readCustomerData();
        ArrayList<BillingInfo> billList = Reader.readBillingInfo();
        ArrayList<TariffTaxInfo> rates = Reader.readTariffInfo();
        ArrayList<NADRADB> nadraInfo = Reader.readNADRAInfo();
        while (true) {
            Scanner employeMenu = new Scanner(System.in);
            System.out.println("Press 1 to Add Customer (New Meter)");
            System.out.println("Press 2 to Add Bill");
            System.out.println("Press 3 to Pay Bill");
            System.out.println("Press 4 to View Any Bill");
            System.out.println("Press 5 to View Report of Paid And UnPaid Bills");
            System.out.println("Press 6 to View Report of CNIC Expiry");
            System.out.println("Press 7 to Update TarrifInfo");
            System.out.println("Press 8 to Change Password");
            System.out.println("Press 9 to Update CustomerInfo(Meter)");
            System.out.println("Press 10 to Update Bill");
            System.out.println("Press -1 to Exit");
            int option = employeMenu.nextInt();
            if (option == 1) {
                addCustomerInfo(nadraInfo, custList);
            } else if (option == 2) {
                addBillingInfo(custList, billList, rates);
            } else if (option == 3) {
                payBill(custList, billList);
            } else if (option == 4) {
                viewBill(billList);
            } else if (option == 5) {
                viewBillReports(billList);
            } else if (option == 6) {
                viewCNICReports(nadraInfo);
            } else if (option == 7) {
                updateTarrifInfo(rates);
            } else if (option == 8) {
                changeEmployeePassword();
            } else if (option == 9) {
                updateCustomerInfo(nadraInfo, custList);
            } else if (option == 10) {
                updateBillInfo(billList, custList, rates);
            } else if (option == -1) {
                return;
            }
        }
    }

    @Override
    public String toString() {
        return "Employee{" + "userName='" + userName + '\'' + ", password='" + password + '\'' + '}';
    }
}
