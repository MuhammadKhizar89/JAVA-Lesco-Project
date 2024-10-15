package controller;
import utility.Constants;
import controller.Customer;
import controller.Employee;
import model.Reader;
import model.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import view.SignInGUI;
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
        SignInGUI signInEmployee = new SignInGUI();
        signInEmployee.SignInEmployeeGUI();
        return signInEmployee.getAuthenticatedEmployee();
    }

    public static void signInCustomer() {
        SignInGUI signIncust = new SignInGUI();
        signIncust.SignInCustomerGUI();
        
    }
}
