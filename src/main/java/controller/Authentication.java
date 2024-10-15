package controller;
import utility.Constants;
import controller.Customer;
import controller.Employee;
import model.Reader;
import model.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import view.SignInGUI;
import view.SignUpGUI;
public class Authentication {

    public static void signUpEmployee() {
        SignUpGUI signupEmployee = new SignUpGUI();
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
