package model;

import controller.TariffTaxInfo;
import java.io.*;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

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

            // Iterate over each line in the file
            while ((currentLine = reader.readLine()) != null) {
                String[] customerData = currentLine.split(",");

                // If the ID matches, update the readings
                if (customerData[0].equals(id)) {
                    int oldMeterReadingRegular = Integer.parseInt(customerData[8]);
                    int oldMeterReadingPeak = Integer.parseInt(customerData[9]);

                    // Add the old and new readings together
                    int newMeterReadingRegular = oldMeterReadingRegular + currentMeterReadingRegular;
                    int newMeterReadingPeak = oldMeterReadingPeak + currentMeterReadingPeak;

                    // Update the line with new readings
                    customerData[8] = Integer.toString(newMeterReadingRegular);
                    customerData[9] = Integer.toString(newMeterReadingPeak);

                    // Join the array back into a string
                    currentLine = String.join(",", customerData);
                }

                // Write the updated line to the temp file
                writer.write(currentLine);
                writer.newLine();
            }

            writer.close();
            reader.close();

            // Replace the original file with the updated file
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

            // Iterate over each line in the file
            while ((currentLine = reader.readLine()) != null) {
                String[] customerData = currentLine.split(",");

                // If the ID matches, update the specific fields
                if (customerData[0].equals(id)) {
                    // Iterate over the indexes and update only the specific ones
                    for (int i = 0; i < index.size(); i++) {
                        int updateIndex = Integer.parseInt(index.get(i));

                        // Ensure the index is within bounds
                        if (updateIndex >= 0 && updateIndex < customerData.length) {
                            customerData[updateIndex] = value.get(i);
                        }
                    }
                    // Join the updated array back into a string
                    currentLine = String.join(",", customerData);
                }

                // Write the updated or original line to the temp file
                writer.write(currentLine);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("An error occurred while updating the file.");
            e.printStackTrace();
            return;
        }

        // Replace the original file with the updated temp file
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
            // Open the file in overwrite mode (false as second argument)
            FileWriter writer = new FileWriter(filename, false);

            // Iterate through the TariffTaxInfo list and write to the file
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

            writer.close(); // Close the file writer
            System.out.println("File has been successfully overwritten.");
        } catch (IOException e) {
            System.out.println("An error occurred while overwriting the file.");
            e.printStackTrace();
        }
    }

    public static void updateBillFile(String filename, String id,String date, ArrayList<String> index, ArrayList<String> value) {
        File inputFile = new File(filename);
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)); BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;

            // Iterate over each line in the file
            while ((currentLine = reader.readLine()) != null) {
                String[] customerData = currentLine.split(",");

                // If the ID matches, update the specific fields
                if (customerData[0].equals(id)&&customerData[1].equals(date)) {
                    // Iterate over the indexes and update only the specific ones
                    for (int i = 0; i < index.size(); i++) {
                        int updateIndex = Integer.parseInt(index.get(i));

                        // Ensure the index is within bounds
                        if (updateIndex >= 0 && updateIndex < customerData.length) {
                            customerData[updateIndex] = value.get(i);
                        }
                    }
                    // Join the updated array back into a string
                    currentLine = String.join(",", customerData);
                }

                // Write the updated or original line to the temp file
                writer.write(currentLine);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("An error occurred while updating the file.");
            e.printStackTrace();
            return;
        }

        // Replace the original file with the updated temp file
        if (!inputFile.delete()) {
            System.out.println("Could not delete original file");
            return;
        }

        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename temp file");
        }
    }

    
}
