package utility;

import controller.*;

import java.util.ArrayList;

public class Parsing {
    public static ArrayList<Customer> parseCustomerData(String response) {
        ArrayList<Customer> customers = new ArrayList<>();
        String[] customerEntries = response.split("\n"); // Each customer is on a new line
        for (String entry : customerEntries) {
            String[] fields = entry.split(";"); // Fields separated by `;`
            if (fields.length == 10) { // Ensure correct number of fields
                Customer customer = new Customer(
                        fields[0], // Customer ID
                        fields[1], // CNIC
                        fields[2], // Name
                        fields[3], // Address
                        fields[4], // Phone
                        fields[5], // Customer Type
                        fields[6], // Meter Type
                        fields[7], // Connection Date
                        fields[8], // Regular Units Consumed
                        fields[9]  // Peak Hour Units Consumed
                );
                customers.add(customer);
            }
        }
        return customers;
    }
    public static ArrayList<BillingInfo> parseBillingData(String response) {
        ArrayList<BillingInfo> billingList = new ArrayList<>();
        String[] billingEntries = response.split("\n"); // Each billing record is on a new line

        for (String entry : billingEntries) {
            String[] fields = entry.split(";"); // Fields separated by `;`
            if (fields.length == 12) { // Ensure correct number of fields
                BillingInfo bill = new BillingInfo(
                        fields[0], // Customer ID
                        fields[1], // Billing Month
                        Integer.parseInt(fields[2]), // Current Meter Reading Regular
                        Integer.parseInt(fields[3]), // Current Meter Reading Peak
                        fields[4], // Billing Date
                        Integer.parseInt(fields[5]), // Cost of Electricity
                        Double.parseDouble(fields[6]), // Sales Tax
                        Integer.parseInt(fields[7]), // Fixed Charges
                        Double.parseDouble(fields[8]), // Total Billing Amount
                        fields[9], // Due Date
                        fields[10], // Bill Paid Status
                        fields[11]  // Bill Payment Date
                );
                billingList.add(bill);
            }
        }
        return billingList;
    }
    public static ArrayList<NADRADB> parseNADRAData(String response) {
        ArrayList<NADRADB> nadraList = new ArrayList<>();
        String[] nadraEntries = response.split("\n"); // Each NADRA record is on a new line

        for (String entry : nadraEntries) {
            String[] fields = entry.split(";"); // Fields separated by `;`
            if (fields.length == 3) { // Ens
                System.out.println(fields[0]+ // CNIC
                        fields[1]+// Issue Date
                        fields[2]  );
                NADRADB nadra = new NADRADB(
                        fields[0], // CNIC
                        fields[1], // Issue Date
                        fields[2]  // Expiry Date
                );
                nadraList.add(nadra);
            }
        }
        return nadraList;
    }
    public static ArrayList<TariffTaxInfo> parseTariffData(String response) {
        ArrayList<TariffTaxInfo> ratesList = new ArrayList<>();
        String[] ratesEntries = response.split("\n"); // Each NADRA record is on a new line

        for (String entry : ratesEntries) {
            String[] fields = entry.split(";"); // Fields separated by `;`
            if (fields.length == 5) {
                TariffTaxInfo rate = new TariffTaxInfo(
                        fields[0],
                        Integer.parseInt(fields[1]),
                        Integer.parseInt(fields[2]),
                        Double.parseDouble(fields[3]),
                        Integer.parseInt(fields[4])
                );
                ratesList.add(rate);
            }
        }
        return ratesList;
    }
    public static ArrayList<Employee> parseEmployeeData(String response) {
        ArrayList<Employee> emp = new ArrayList<>();
        String[] empEntries = response.split("\n"); // Each NADRA record is on a new line

        for (String entry : empEntries) {
            String[] fields = entry.split(";"); // Fields separated by `;`
            if (fields.length == 2) {
                Employee e = new Employee(
                        fields[0],
                        fields[1]
                );
                emp.add(e);
            }
        }
        return emp;
    }

}
