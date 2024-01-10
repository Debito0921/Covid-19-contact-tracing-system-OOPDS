/**
 * A class representing a customer.
 */
class Customer{
    protected String customerName;
    /**
     * Construct a String name customerName. 
    */

    public Customer(){}
    /**
    * Construct a Customer method with the function customerName.
    * 
    * @param customerName with the String of the name of a customer.
    */
    public Customer(String customerName){
        this.customerName = customerName;
    }
    /**
    * returns customerName in Customer class. 
    *
    * @return customerName in the Customer class.
    */
    public String getCustomerName(){
        return customerName;
    }
}