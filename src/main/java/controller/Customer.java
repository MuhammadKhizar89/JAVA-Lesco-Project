package controller;

public class Customer {

    private String customerId;
    private String cnic;
    private String name;
    private String address;
    private String phone;
    private String customerType;
    private String meterType;
    private String connectionDate;
    private String regularUnitsConsumed;
    private String peakHourUnitsConsumed;

    public Customer(String customerId, String cnic, String name, String address, String phone,
            String customerType, String meterType, String connectionDate,
            String regularUnitsConsumed, String peakHourUnitsConsumed) {
        this.customerId = customerId;
        this.cnic = cnic;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.customerType = customerType;
        this.meterType = meterType;
        this.connectionDate = connectionDate;
        this.regularUnitsConsumed = regularUnitsConsumed;
        this.peakHourUnitsConsumed = peakHourUnitsConsumed;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter and Setter for phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter and Setter for customerType
    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    // Getter and Setter for meterType
    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    // Getter and Setter for connectionDate
    public String getConnectionDate() {
        return connectionDate;
    }

    public void setConnectionDate(String connectionDate) {
        this.connectionDate = connectionDate;
    }

    // Getter and Setter for regularUnitsConsumed
    public String getRegularUnitsConsumed() {
        return regularUnitsConsumed;
    }

    public void setRegularUnitsConsumed(String regularUnitsConsumed) {
        this.regularUnitsConsumed = regularUnitsConsumed;
    }

    // Getter and Setter for peakHourUnitsConsumed
    public String getPeakHourUnitsConsumed() {
        return peakHourUnitsConsumed;
    }

    public void setPeakHourUnitsConsumed(String peakHourUnitsConsumed) {
        this.peakHourUnitsConsumed = peakHourUnitsConsumed;
    }

    @Override
    public String toString() {
        return "Customer{"
                + "customerId='" + customerId + '\''
                + ", cnic='" + cnic + '\''
                + ", name='" + name + '\''
                + ", address='" + address + '\''
                + ", phone='" + phone + '\''
                + ", customerType='" + customerType + '\''
                + ", meterType='" + meterType + '\''
                + ", connectionDate='" + connectionDate + '\''
                + ", regularUnitsConsumed='" + regularUnitsConsumed + '\''
                + ", peakHourUnitsConsumed='" + peakHourUnitsConsumed + '\''
                + '}';
    }



}
