package com.mycompany.l227896_se5b_scd;
import controller.*;
import model.Reader;
import model.Writer;
import utility.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class SocketServer {
    private int port;

    public SocketServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                new ClientHandler(socket).start(); // Handle each client in a new thread
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String receivedData = inputStream.readUTF();
                    System.out.println("Received from client: " + receivedData);

                    if (receivedData.startsWith("AddCustomer#")) {
                        handleAddCustomer(receivedData.substring("AddCustomer#".length()));
                    } else if (receivedData.startsWith("AddBill#")) {
                        handleAddBill(receivedData.substring("AddBill#".length()));
                    }else if (receivedData.startsWith("ChangePassword#")) {
                        handleChangePassword(receivedData.substring("ChangePassword#".length()), outputStream);
                    } else if (receivedData.startsWith("readCustomerData#")) {
                        ArrayList<Customer> custList = Reader.readCustomerData();
                        System.out.println(custList);
                        sendResponse(outputStream, formatCustomerData(custList));
//                        return;/
                    } else if (receivedData.startsWith("readBillingInfo#")) {
                        ArrayList<BillingInfo> billList = Reader.readBillingInfo();
                        System.out.println(billList);
                        sendResponse(outputStream, formatBillingData(billList));
//                        return;
                    } else if (receivedData.startsWith("readNADRAInfo#")) {
                        ArrayList<NADRADB> nadraInfo = Reader.readNADRAInfo();
                        System.out.println(nadraInfo);
                        sendResponse(outputStream, formatNADRAData(nadraInfo));
//                        return;
                    }else if (receivedData.startsWith("UpdateCNICExpiry#")) {
                        String[] parts = receivedData.split("#")[1].split(";");
                        String cnic = parts[0];  // Extract CNIC
                        String newExpiryDate = parts[1];  // Extract new expiry date
                        ArrayList<String> index = new ArrayList<>();
                        ArrayList<String> value = new ArrayList<>();
                        index.add("2");
                        value.add(newExpiryDate);
                        Writer.updateFile(Constants.NADRA, cnic, index, value);
                    } else if (receivedData.startsWith("readtariffInfo")) {
                        ArrayList<TariffTaxInfo> rates = Reader.readTariffInfo();
                        System.out.println(rates);
                        sendResponse(outputStream, formatTariffData(rates));
                    }else if(receivedData.startsWith("updateNADRA")){
                        String[] parts = receivedData.split("#")[1].split(";");
                        String cnic = parts[0];
                        String issueDate = parts[1];
                        String expiryDate = parts[2];
                        ArrayList<String> index = new ArrayList<>();
                        index.add("1");
                        index.add("2");
                        ArrayList<String> value = new ArrayList<>();
                        value.add(issueDate);
                        value.add(expiryDate);
                        Writer.updateFile(Constants.NADRA, cnic, index, value);
                    }else if (receivedData.startsWith("payBill#")) {
                        String[] parts = receivedData.split("#")[1].split(";");
                        String customerId = parts[0];
                        String billingMonth = parts[1];
                        String status = parts[2];
                        String paidDate = parts[3];

                        ArrayList<String> index = new ArrayList<>();
                        ArrayList<String> value = new ArrayList<>();
                        index.add("10"); // Assuming "Paid" status is at index 10
                        index.add("11"); // Assuming payment date is at index 11
                        value.add(status);
                        value.add(paidDate);
                        Writer.updateBillFile(Constants.BILLINGINFO, customerId, billingMonth, index, value);
                    }
                    else if (receivedData.startsWith("updateCustomerBillInfo#")) {
                        String[] parts = receivedData.split("#")[1].split(";");
                        String customerId = parts[0];
                        int regularReading = Integer.parseInt(parts[1]);
                        int peakReading = Integer.parseInt(parts[2]);
                        Writer.updateCustomerFile(Constants.CUSTOMERINFO, customerId, regularReading, peakReading);
                    }else if(receivedData.startsWith("readEmployeeInfo")){
                        ArrayList<Employee> employees = Reader.readEmployeeData(Constants.EMPLOYEESDATA);
                        System.out.println(employees);
                        sendResponse(outputStream, formatEmployeeData(employees));
                    }else if (receivedData.startsWith("updateBill#")) {
                        String[] parts = receivedData.split("#")[1].split(";");
                        String customerId = parts[0];
                        String billingMonth = parts[1];
                        ArrayList<String> index = new ArrayList<>(Arrays.asList("2", "3", "5", "6", "7", "8"));
                        ArrayList<String> value = new ArrayList<>(Arrays.asList(parts).subList(2, parts.length));
                        Writer.updateBillFile(Constants.BILLINGINFO, customerId, billingMonth, index, value);
                    }else if (receivedData.startsWith("updateTariff#")) {
                        String[] parts = receivedData.split("#")[1].split(";");
                        String meterType = parts[0];
                        int regularUnits = Integer.parseInt(parts[1]);
                        double percentage = Double.parseDouble(parts[2]);
                        int fixedCharges = Integer.parseInt(parts[3]);
                        int peakHourUnits = Integer.parseInt(parts[4]);
                        int row = Integer.parseInt(parts[5]);
                        System.out.println("hehe"+row);
                        ArrayList<TariffTaxInfo> tariffList = Reader.readTariffInfo();
                        int i=0;
                        for (TariffTaxInfo tariff : tariffList) {
                            if (i==row) {
                                tariff.setRegularUnits(regularUnits);
                                tariff.setPercentage(percentage);
                                tariff.setFixedCharges(fixedCharges);
                                tariff.setPeakhourUnits(peakHourUnits);
                                break;
                            }
                            i++;
                        }
                        Writer.overwriteTarrifFile(Constants.TARIFFTAX, tariffList);
                    } else if (receivedData.startsWith("updateCustomer#")) {
                        String[] parts = receivedData.split("#")[1].split(";", 3);
                        String customerId = parts[0];
                        String[] indexList = parts[1].split(",");
                        String[] customerDataList = parts[2].split(";");
                        ArrayList<String> indexArray = new ArrayList<>(Arrays.asList(indexList));
                        ArrayList<String> customerDataArray = new ArrayList<>(Arrays.asList(customerDataList));
                        Writer.updateFile(Constants.CUSTOMERINFO, customerId, indexArray, customerDataArray);
                    }else if (receivedData.startsWith("deleteBill#")) {
                        String[] parts = receivedData.split("#")[1].split(";", 2);  // Split after '#', then split by ';'
                        String customerId = parts[0];
                        String date = parts[1];
                        Writer.deleteBill(customerId, date);
                    }else if (receivedData.startsWith("deleteCustomer#")) {
                        String customerId = receivedData.split("#")[1];  // Extract the customer ID
                        Writer.deleteCustomer(customerId);
                    }
                    String response = "Server received: " + receivedData.toUpperCase();
                    outputStream.writeUTF(response);
                    outputStream.flush();
                    System.out.println("Response sent to client: " + response);
                }
            } catch (IOException ex) {
                System.out.println("Client disconnected");
            } finally {
                closeConnection();
            }
        }
        private void sendResponse(DataOutputStream outputStream, String data) throws IOException {
            System.out.println("hehe"+data);
            outputStream.writeUTF(data);
        }
        private String formatEmployeeData(ArrayList<Employee> emp){
            StringBuilder sb = new StringBuilder();
            for (Employee e : emp) {
                sb.append(String.join(";", e.getUserName(),e.getPassword()));
                sb.append("\n");
            }
            return sb.toString().trim();
        }
        private String formatTariffData(ArrayList<TariffTaxInfo> rates){
            StringBuilder sb = new StringBuilder();
            for (TariffTaxInfo rate : rates) {
                sb.append(String.join(";", rate.getMeter_type(), String.valueOf(rate.getRegularUnits()),String.valueOf(rate.getPeakhourUnits()),String.valueOf(rate.getPercentage()),
                        String.valueOf(rate.getFixedCharges())));
                sb.append("\n");
            }
            return sb.toString().trim();
        }

        private String formatCustomerData(ArrayList<Customer> customers) {
            StringBuilder sb = new StringBuilder();
            for (Customer customer : customers) {
                sb.append(String.join(";", customer.getCustomerId(), customer.getCnic(), customer.getName(), customer.getAddress(),
                        customer.getPhone(), customer.getCustomerType(), customer.getMeterType(), customer.getConnectionDate(),
                        customer.getRegularUnitsConsumed(), customer.getPeakHourUnitsConsumed()));
                System.out.println(customer.getPhone());
                sb.append("\n");
            }
            return sb.toString().trim();
        }
        private String formatBillingData(ArrayList<BillingInfo> billingInfo) {
            StringBuilder sb = new StringBuilder();
            for (BillingInfo bill : billingInfo) {
                sb.append(String.join(";",
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
                sb.append("\n");
            }
            return sb.toString().trim();
        }

        private String formatNADRAData(ArrayList<NADRADB> nadraInfo) {
            StringBuilder sb = new StringBuilder();
            for (NADRADB nadra : nadraInfo) {
                sb.append(String.join(";",
                        nadra.getCNIC(),
                        nadra.getIssueDate(),
                        nadra.getExpiryDate()
                ));
                sb.append("\n");
            }
            return sb.toString().trim();
        }

        private void handleChangePassword(String data, DataOutputStream outputStream) {
            String[] parts = data.split(";");
            if (parts.length == 2) {
                String userName = parts[0];
                String newPassword = parts[1];

                ArrayList<String> index = new ArrayList<>();
                ArrayList<String> value = new ArrayList<>();
                index.add("1");
                value.add(newPassword);
                Writer.updateFile(Constants.EMPLOYEESDATA, userName, index, value);
            } else {
                try {
                    outputStream.writeUTF("Invalid data format for password change.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleAddCustomer(String data) {
            String[] parts = data.split(";");
            ArrayList<String> customerDataList = new ArrayList<>(Arrays.asList(parts));
            System.out.println("Customer data to save: " + customerDataList);
            Writer.write(Constants.CUSTOMERINFO, customerDataList);
        }
        private void handleAddBill(String data) {
            String[] parts = data.split(";");
            ArrayList<String> billInfo = new ArrayList<>(Arrays.asList(parts));
            if (billInfo.size() > 0) {
                Writer.write(Constants.BILLINGINFO, billInfo);
                System.out.println("Bill information saved: " + billInfo);
            } else {
                System.out.println("Invalid billing data received.");
            }
        }

        private void closeConnection() {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
                if (socket != null) socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}