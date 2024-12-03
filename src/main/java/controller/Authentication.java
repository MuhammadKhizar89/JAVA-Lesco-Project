package controller;
import view.SignInGUI;

public class Authentication {

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
