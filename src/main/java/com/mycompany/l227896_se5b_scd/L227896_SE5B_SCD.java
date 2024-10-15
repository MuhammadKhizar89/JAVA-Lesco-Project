package com.mycompany.l227896_se5b_scd;

import controller.Authentication;
import controller.Customer;
import controller.Employee;
import java.util.Scanner;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import view.AuthMenu;

public class L227896_SE5B_SCD {

    public static void atuhMenu() {
//        while (true) {
//            Scanner authmenu = new Scanner(System.in);
//            System.out.println("Press 1 to Login as Employee");
//            System.out.println("Press 2 to Signup as Employee");
//            System.out.println("Press 3 to Login as Customer");
//            System.out.println("Press -1 to Exit");
//            int menuNumber = authmenu.nextInt();
//            Authentication authObject = new Authentication();
//            if (menuNumber == 1) {
//                Employee signInUser = authObject.signInEmployee();
//                if (signInUser != null) {
//                    signInUser.employeeMenu();
//                }
//            } else if (menuNumber == 2) {
//                authObject.signUpEmployee();
//            } else if (menuNumber == 3) {
//                Customer signInCust = authObject.signInCustomer();
//                if (signInCust != null) {
//                    signInCust.customerMenu();
//                }
//            } else if (menuNumber == -1) {
//                break;
//            }
//        }
    }

    public static void main(String[] args) {
//        atuhMenu();
        AuthMenu obj = new AuthMenu();
    }
}
