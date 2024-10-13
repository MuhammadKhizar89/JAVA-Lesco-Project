package controller;

import utility.Constants;
import controller.Customer;
import controller.Employee;
import model.Reader;
import model.Writer;
import java.util.ArrayList;
import java.util.Scanner;

public class Authentication {

    public static void signUpEmployee() {
        Scanner employeeInputData = new Scanner(System.in);
        ArrayList<Employee> Data = Reader.readEmployeeData(Constants.EMPLOYEESDATA);
        while (true) {
            System.out.println("Enter UserName");
            String userName = employeeInputData.nextLine();
            boolean userExists = false;
            for (Employee e : Data) {
                if (e.getUserName().equals(userName)) {
                    System.out.println("Username Already Exists, Try Another");
                    userExists = true;
                    break;
                }
            }
            if (!userExists) {
                System.out.println("Enter Password");
                String password = employeeInputData.nextLine();
                ArrayList<String> employeeArray = new ArrayList<>();
                employeeArray.add(userName);
                employeeArray.add(password);
                Writer.write(Constants.EMPLOYEESDATA, employeeArray);
                System.out.println("Account Created Successfully");
                break;
            }
        }
    }

    public static Employee signInEmployee() {
        
        Scanner employeeInputData = new Scanner(System.in);
        ArrayList<Employee> Data = Reader.readEmployeeData(Constants.EMPLOYEESDATA);
        while (true) {
            System.out.println("Enter UserName");
            String userName = employeeInputData.nextLine();
            System.out.println("Enter Password");
            String password = employeeInputData.nextLine();
            for (Employee e : Data) {
                if (e.getUserName().equals(userName)&&e.getPassword().equals(password)) {
                    System.out.println("Signup Sucessfully");
                    return new Employee(userName,password);
                }
            }
            System.out.println("Invalid Username or Password");
            break;
        }
        return null;
    }
     public static Customer signInCustomer() {
        Scanner customerInputData = new Scanner(System.in);
        ArrayList<Customer> Data = Reader.readCustomerData();
        while (true) {
            System.out.println("Enter UserName");
            String userName = customerInputData.nextLine();
            System.out.println("Enter Password");
            String password = customerInputData.nextLine();
            for (Customer e : Data) {
                if (e.getCustomerId().equals(userName)&&e.getCnic().equals(password)) {
                    System.out.println("SignIn Sucessfully");
                    return e;
                }
            }
            System.out.println("Invalid Username or Password");
            break;
        }
        return null;
    }
}
