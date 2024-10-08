package model;

import controller.BillingInfo;
import utility.Constants;
import controller.Customer;
import controller.Employee;
import controller.NADRADB;
import controller.TariffTaxInfo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Reader {

    public static ArrayList<Employee> readEmployeeData(String filename) {
        ArrayList<Employee> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String userName = parts[0];
                    String password = parts[1];
                    Employee employee = new Employee(userName, password);
                    data.add(employee);
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
                    System.out.println("File not  Exist");
        }
        return data;
    }

    public static ArrayList<Customer> readCustomerData() {
        ArrayList<Customer> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.CUSTOMERINFO))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split each line by comma to get customer data
                String[] parts = line.split(",");
                if (parts.length == 10) { // Ensure there are exactly 10 fields in the file
                    String customerId = parts[0].trim();
                    String cnic = parts[1].trim();
                    String name = parts[2].trim();
                    String address = parts[3].trim();
                    String phone = parts[4].trim();
                    String customerType = parts[5].trim();
                    String meterType = parts[6].trim();
                    String connectionDate = parts[7].trim();
                    String regularUnitsConsumed = parts[8].trim();
                    String peakHourUnitsConsumed = !"-1".equals(parts[9].trim()) ? parts[9].trim() : "";
                    // Create a new Customer object using the parameterized constructor
                    Customer customer = new Customer(customerId, cnic, name, address, phone, customerType, meterType, connectionDate, regularUnitsConsumed, peakHourUnitsConsumed);
                    // Add the customer to the ArrayList
                    data.add(customer);
                } else {
                    System.out.println("Invalid data format in the file: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("File not  Exist");
        }
        return data;
    }

    public static ArrayList<TariffTaxInfo> readTariffInfo() {
        ArrayList<TariffTaxInfo> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.TARIFFTAX))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split each line by comma to get customer data
                String[] parts = line.split(",");
                if (parts.length == 5) { // Ensure there are exactly 10 fields in the file
                    String meterType = parts[0].trim();
                    int regularUnits = Integer.parseInt(parts[1].trim());
                    int peakedUnits = parts[2].trim() != "" ? Integer.parseInt(parts[2].trim()) : 0;
                    double tax = Double.parseDouble(parts[3].trim());
                    int fixed = Integer.parseInt(parts[4].trim());
                    data.add(new TariffTaxInfo(meterType, regularUnits, peakedUnits, tax, fixed));
                } else {
                    System.out.println("Invalid data format in the file: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("File not  Exist");
        }
        return data;
    }

    public static ArrayList<BillingInfo> readBillingInfo() {
        ArrayList<BillingInfo> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.BILLINGINFO))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Split each line by comma to get the individual customer fields
                String[] fields = line.split(",");
                if (fields.length != 12) {
                    // Handle case where the number of fields does not match the expected number (12)
                    System.out.println("Invalid data format: " + line);
                    continue;
                }
                // Parse each field and convert them to the correct data types for BillingInfo
                String customerId = fields[0];
                String billingMonth = fields[1];
                int currentMeterReadingRegular = Integer.parseInt(fields[2]);
                int currentMeterReadingPeak = Integer.parseInt(fields[3]);
                String billingDate = fields[4];
                int costOfElectricity = Integer.parseInt(fields[5]);
                double salesTax = Double.parseDouble(fields[6]);
                int fixedCharges = Integer.parseInt(fields[7]);
                double totalBillingAmount = Double.parseDouble(fields[8]);
                String dueDate = fields[9];
                String billPaidStatus = fields[10];
                String billPaymentDate = fields[11];

                // Create a new BillingInfo object
                BillingInfo billingInfo = new BillingInfo(customerId, billingMonth, currentMeterReadingRegular, currentMeterReadingPeak,
                        billingDate, costOfElectricity, salesTax, fixedCharges, totalBillingAmount,
                        dueDate, billPaidStatus, billPaymentDate);

                // Add the BillingInfo object to the list
                data.add(billingInfo);
            }
        } catch (IOException e) {
            System.out.println("Error reading billing info file.");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing numeric values in the billing info.");
            e.printStackTrace();
        }

        return data;
    }

     public static ArrayList<NADRADB> readNADRAInfo(){
       ArrayList<NADRADB> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.NADRA))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Split each line by comma to get the individual customer fields
                String[] fields = line.split(",");
                if (fields.length != 3) {
                    // Handle case where the number of fields does not match the expected number (12)
                    System.out.println("Invalid data format: " + line);
                    continue;
                }
                String CNIC = fields[0];
                String issueDate = fields[1];
                String expiryDate = fields[2];
                data.add(new NADRADB(CNIC,issueDate,expiryDate));
            }
        } catch (IOException e) {
            System.out.println("Error reading billing info file.");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing numeric values in the billing info.");
            e.printStackTrace();
        }
        return data;
     }
}
