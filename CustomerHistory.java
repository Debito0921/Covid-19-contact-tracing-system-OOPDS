/**
 * This CustomerHistory class has the index of customer, date and time and name of shop that
 * customer visit at the day. 
 * 
 * @author Hooi Thing Hong
 */
class CustomerHistory extends Customer{
    private String customerIndex;
    private String dateTime;
    private String shopName;
    /**
    * Construct Strings name customerIndex, dateTime and shopName.
    */
    public CustomerHistory(){}
    /**
    * Construct the CustomerHistory.
    *
    * @param customerName the customer 
    * @param customerIndex comes with the index of a customer in the format of String.
    * @param dateTime is the date and time when the customer checks in. 
    * @param shopName with the name of the shop that customer had checked in.
    */
    public CustomerHistory(String customerIndex, String customerName, String dateTime, String shopName){
        super(customerName);
        this.customerIndex = customerIndex;
        this.dateTime = dateTime;
        this.shopName = shopName;
    }
    /**
    * return the String customerIndex in CustomerHistory class.
    * @return customerIndex
    */ 
    public String getCustomerIndex() {
        return customerIndex;
    }
    /**
    * return the String customerName in CustomerHistory class.
    * @return customerName
    */ 
    public String getCustomerName() {
        return customerName;
    }
    /**
    * return the String dateTime in CustomerHistory class.
    * @return dateTime
    */ 
    public String getDateTime(){
        return dateTime;
    }
    /**
    * return the String shopName in CustomerHistory class.
    * @return shopName
    */ 
    public String getShopName(){
        return shopName;
    }
    /**
    * return the String toCSVString in CustomerHistory class.
    * @return toCSVString
    */
    public String toCSVString() {
        return customerIndex + "," +  customerName  + "," + dateTime + "," + shopName;
    }
}