/**
* Create a class CustomerDetail that extends from the class Customer.
*/ 
class CustomerDetail extends Customer{
    /**
    * Construct these Strings name customerPhone, customerPassword and customerStatus.
    */
    private String customerPhone;
    private String customerPassword;
    private String customerStatus = "Normal"; // set default status as 'Normal'.
    public CustomerDetail(){}
    public CustomerDetail(String customerName, String customerPhone, String customerPassword, String customerStatus){
        super(customerName);
        this.customerPhone = customerPhone;
        this.customerPassword = customerPassword;
        this.customerStatus= customerStatus;
    }
    public void setCustomerStatus(String customerStatus){
        this.customerStatus = customerStatus; 
    }
    public String getCustomerName() {
        return customerName;
    }
    public String getCustomerPhone() {
        return customerPhone;
    }
    public String getCustomerStatus() {
        return customerStatus;
    }

    public String toString() {
        return customerName + " " + customerPhone + " " + customerPassword + " " + customerStatus;
    }
    public String toCSVString() {
        return customerName + "," + customerPhone + "," + customerPassword + "," + customerStatus;
    }

}