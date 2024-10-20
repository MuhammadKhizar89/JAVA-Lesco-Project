package model;

import controller.BillingInfo;
import controller.Customer;
import controller.TariffTaxInfo;
import java.io.*;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import static model.Reader.readCustomerData;
import utility.Constants;

public class Writer {

    public static void write(String filename, ArrayList<String> employeeArray) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            for (int i = 0; i < employeeArray.size(); i++) {
                writer.write(employeeArray.get(i));
                if (i < employeeArray.size() - 1) {
                    writer.write(",");
                }
            }
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while appending to the file.");
            e.printStackTrace();
        }
    }

    public static void updateCustomerFile(String filename, String id, int currentMeterReadingRegular, int currentMeterReadingPeak) {
        File inputFile = new File(filename);
        File tempFile = new File("temp.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] customerData = currentLine.split(",");
                if (customerData[0].equals(id)) {
                    int oldMeterReadingRegular = Integer.parseInt(customerData[8]);
                    int oldMeterReadingPeak = Integer.parseInt(customerData[9]);
                    int newMeterReadingRegular = oldMeterReadingRegular + currentMeterReadingRegular;
                    int newMeterReadingPeak = oldMeterReadingPeak + currentMeterReadingPeak;
                    customerData[8] = Integer.toString(newMeterReadingRegular);
                    customerData[9] = Integer.toString(newMeterReadingPeak);
                    currentLine = String.join(",", customerData);
                }
                writer.write(currentLine);
                writer.newLine();
            }
            writer.close();
            reader.close();
            if (!inputFile.delete()) {
                System.out.println("Could not delete original file");
                return;
            }

            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename temp file");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the file.");
            e.printStackTrace();
        }
    }

    public static void updateFile(String filename, String id, ArrayList<String> index, ArrayList<String> value) {
        File inputFile = new File(filename);
        File tempFile = new File("temp.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)); BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] customerData = currentLine.split(",");
                if (customerData[0].equals(id)) {
                    for (int i = 0; i < index.size(); i++) {
                        int updateIndex = Integer.parseInt(index.get(i));
                        if (updateIndex >= 0 && updateIndex < customerData.length) {
                            customerData[updateIndex] = value.get(i);
                        }
                    }
                    currentLine = String.join(",", customerData);
                }
                writer.write(currentLine);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("An error occurred while updating the file.");
            e.printStackTrace();
            return;
        }
        if (!inputFile.delete()) {
            System.out.println("Could not delete original file");
            return;
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename temp file");
        }
    }

    public static void overwriteTarrifFile(String filename, ArrayList<TariffTaxInfo> tarrifInfo) {
        try {
            FileWriter writer = new FileWriter(filename, false);
            for (TariffTaxInfo t : tarrifInfo) {
                writer.write(t.getMeter_type());
                writer.write(",");
                writer.write(Integer.toString(t.getRegularUnits()));
                writer.write(",");
                if (t.getPeakhourUnits() != 0) {
                    writer.write(Integer.toString(t.getPeakhourUnits()));
                }
                writer.write(",");
                writer.write(Double.toString(t.getPercentage()));
                writer.write(",");
                writer.write(Integer.toString(t.getFixedCharges()));
                writer.write("\n");
            }
            writer.close();
            System.out.println("File has been successfully overwritten.");
        } catch (IOException e) {
            System.out.println("An error occurred while overwriting the file.");
            e.printStackTrace();
        }
    }

    public static void updateBillFile(String filename, String id, String date, ArrayList<String> index, ArrayList<String> value) {
        File inputFile = new File(filename);
        File tempFile = new File("temp.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)); BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] customerData = currentLine.split(",");
                if (customerData[0].equals(id) && customerData[1].equals(date)) {
                    for (int i = 0; i < index.size(); i++) {
                        int updateIndex = Integer.parseInt(index.get(i));
                        if (updateIndex >= 0 && updateIndex < customerData.length) {
                            customerData[updateIndex] = value.get(i);
                        }
                    }
                    currentLine = String.join(",", customerData);
                }
                writer.write(currentLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the file.");
            e.printStackTrace();
            return;
        }
        if (!inputFile.delete()) {
            System.out.println("Could not delete original file");
            return;
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename temp file");
        }
    }

    public static void deleteCustomer(String customerId) {
        ArrayList<Customer> customerList = readCustomerData();
        boolean customerFound = false;
        for (int i = 0; i < customerList.size(); i++) {
            if (customerList.get(i).getCustomerId().equals(customerId)) {
                customerList.remove(i);
                customerFound = true;
                break;
            }
        }
        if (!customerFound) {
            System.out.println("Customer with ID " + customerId + " not found.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.CUSTOMERINFO))) {
            for (Customer customer : customerList) {
                String line = customer.getCustomerId() + ","
                        + customer.getCnic() + ","
                        + customer.getName() + ","
                        + customer.getAddress() + ","
                        + customer.getPhone() + ","
                        + customer.getCustomerType() + ","
                        + customer.getMeterType() + ","
                        + customer.getConnectionDate() + ","
                        + customer.getRegularUnitsConsumed() + ","
                        + (customer.getPeakHourUnitsConsumed().isEmpty() ? "-1" : customer.getPeakHourUnitsConsumed());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }

        System.out.println("Customer with ID " + customerId + " has been deleted successfully.");
    }

    public static void deleteBill(String customerId, String billingMonth) {
        ArrayList<BillingInfo> billList = Reader.readBillingInfo();// Read all bills
        boolean billDeleted = false;
        for (int i = 0; i < billList.size(); i++) {
            BillingInfo bill = billList.get(i);
            if (bill.getCustomerId().equals(customerId) && bill.getBillingMonth().equals(billingMonth)) {
                billList.remove(i);
                billDeleted = true;
                break;
            }
        }

        if (billDeleted) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.BILLINGINFO))) {
                for (BillingInfo bill : billList) {
                    writer.write(String.join(",",
                            bill.getCustomerId(),
                            bill.getBillingMonth(),
                            String.valueOf(bill.getCurrentMeterReadingRegular()),
                            String.valueOf(bill.getCurrentMeterReadingPeak()),
                            bill.getBillingDate(),
                            String.valueOf(bill.getCostOfElectricity()),
                            String.valueOf(bill.getSalesTax()),
                            String.valueOf(bill.getFixedCharges()),
                            String.valueOf(bill.getTotalBillingAmount()),
                            bill.getDueDate(),
                            bill.getBillPaidStatus(),
                            bill.getBillPaymentDate()
                    ));
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing updated billing info to file.");
            }
            System.out.println("Bill deleted successfully.");
        } else {
            System.out.println("Bill not found.");
        }
    }

}
