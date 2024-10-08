package utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Help {
    public static DateTimeFormatter Dateformatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String generateCustomerId() {
          int maxCustomerId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.CUSTOMERINFO))) {
            String line=reader.readLine();
            while (line!= null) {
                String[] data = line.split(",");
                if (data.length > 0) {
                    System.out.println(data);
                    String customerIdStr = data[0].trim();
                    int customerId = Integer.parseInt(customerIdStr);
                    // Update maxCustomerId if current customerId is larger
                        maxCustomerId = customerId;
                }
                line = reader.readLine(); 
            }
        } catch (IOException e) {
            System.out.println("File Not Exist");
        }
        int nextCustomerId = maxCustomerId + 1;
        return String.format("%04d", nextCustomerId);
    }
}
