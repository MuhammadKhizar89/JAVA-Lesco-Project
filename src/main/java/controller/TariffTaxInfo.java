package controller;
public class TariffTaxInfo {
private String meter_type;
private int regularUnits;
private int peakhourUnits;
private double percentage;
private int fixedCharges;

    public TariffTaxInfo(String meter_type, int regularUnits, int peakhourUnits, double percentage, int fixedCharges) {
        this.meter_type = meter_type;
        this.regularUnits = regularUnits;
        this.peakhourUnits = peakhourUnits;
        this.percentage = percentage;
        this.fixedCharges = fixedCharges;
    }

    @Override
    public String toString() {
        return "meter_type=" + meter_type + ", regularUnits=" + regularUnits + ", peakhourUnits=" + peakhourUnits + ", percentage=" + percentage + ", fixedCharges=" + fixedCharges;
    }

    public String getMeter_type() {
        return meter_type;
    }

    public void setMeter_type(String meter_type) {
        this.meter_type = meter_type;
    }

    public int getRegularUnits() {
        return regularUnits;
    }

    public void setRegularUnits(int regularUnits) {
        this.regularUnits = regularUnits;
    }

    public int getPeakhourUnits() {
        return peakhourUnits;
    }

    public void setPeakhourUnits(int peakhourUnits) {
        this.peakhourUnits = peakhourUnits;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public int getFixedCharges() {
        return fixedCharges;
    }

    public void setFixedCharges(int fixedCharges) {
        this.fixedCharges = fixedCharges;
    }

    
}
