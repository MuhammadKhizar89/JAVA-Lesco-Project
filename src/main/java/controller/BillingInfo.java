package controller;
public class BillingInfo {
    private String customerId;
    private String billingMonth;
    private int currentMeterReadingRegular;
    private int currentMeterReadingPeak;
    private String billingDate;
    private int costOfElectricity;
    private double salesTax;
    private int fixedCharges;
    private double totalBillingAmount;
    private String dueDate;
    private String billPaidStatus;
    private String billPaymentDate;
    public BillingInfo(String customerId, String billingMonth, int currentMeterReadingRegular, int currentMeterReadingPeak, 
                       String billingDate, int costOfElectricity, double salesTax, int fixedCharges, double totalBillingAmount, 
                       String dueDate, String billPaidStatus, String billPaymentDate) {
        this.customerId = customerId;
        this.billingMonth = billingMonth;
        this.currentMeterReadingRegular = currentMeterReadingRegular;
        this.currentMeterReadingPeak = currentMeterReadingPeak;
        this.billingDate = billingDate;
        this.costOfElectricity = costOfElectricity;
        this.salesTax = salesTax;
        this.fixedCharges = fixedCharges;
        this.totalBillingAmount = totalBillingAmount;
        this.dueDate = dueDate;
        this.billPaidStatus = billPaidStatus;
        this.billPaymentDate = billPaymentDate;
    }

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public int getCurrentMeterReadingRegular() {
        return currentMeterReadingRegular;
    }

    public void setCurrentMeterReadingRegular(int currentMeterReadingRegular) {
        this.currentMeterReadingRegular = currentMeterReadingRegular;
    }

    public int getCurrentMeterReadingPeak() {
        return currentMeterReadingPeak;
    }

    public void setCurrentMeterReadingPeak(int currentMeterReadingPeak) {
        this.currentMeterReadingPeak = currentMeterReadingPeak;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    public int getCostOfElectricity() {
        return costOfElectricity;
    }

    public void setCostOfElectricity(int costOfElectricity) {
        this.costOfElectricity = costOfElectricity;
    }

    public double getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(double salesTax) {
        this.salesTax = salesTax;
    }

    public int getFixedCharges() {
        return fixedCharges;
    }

    public void setFixedCharges(int fixedCharges) {
        this.fixedCharges = fixedCharges;
    }

    public double getTotalBillingAmount() {
        return totalBillingAmount;
    }

    public void setTotalBillingAmount(double totalBillingAmount) {
        this.totalBillingAmount = totalBillingAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getBillPaidStatus() {
        return billPaidStatus;
    }

    public void setBillPaidStatus(String billPaidStatus) {
        this.billPaidStatus = billPaidStatus;
    }

    public String getBillPaymentDate() {
        return billPaymentDate;
    }

    public void setBillPaymentDate(String billPaymentDate) {
        this.billPaymentDate = billPaymentDate;
    }

    @Override
    public String toString() {
        return "BillingInfo{" +
                "customerId='" + customerId + '\'' +
                ", billingMonth='" + billingMonth + '\'' +
                ", currentMeterReadingRegular=" + currentMeterReadingRegular +
                ", currentMeterReadingPeak=" + currentMeterReadingPeak +
                ", billingDate='" + billingDate + '\'' +
                ", costOfElectricity=" + costOfElectricity +
                ", salesTax=" + salesTax +
                ", fixedCharges=" + fixedCharges +
                ", totalBillingAmount=" + totalBillingAmount +
                ", dueDate='" + dueDate + '\'' +
                ", billPaidStatus='" + billPaidStatus + '\'' +
                ", billPaymentDate='" + billPaymentDate + '\'' +
                '}';
    }
}
