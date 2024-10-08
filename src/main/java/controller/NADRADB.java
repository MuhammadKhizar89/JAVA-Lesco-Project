/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author Admin
 */
public class NADRADB {
    private String CNIC;
    private String issueDate;

    @Override
    public String toString() {
        return "NADRADB{" + "CNIC=" + CNIC + ", issueDate=" + issueDate + ", expiryDate=" + expiryDate + '}';
    }
    private String expiryDate;

    public NADRADB(String CNIC, String issueDate, String expiryDate) {
        this.CNIC = CNIC;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
   
}
