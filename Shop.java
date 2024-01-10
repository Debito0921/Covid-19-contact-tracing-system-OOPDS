class Shop{
    private String shopName;
    private String phone;
    private String managerName;
    private String shopStatus = "Normal"; // set default status as 'Normal'.

    public Shop(){}
    public Shop(String shopName, String phone, String managerName, String shopStatus){
        this.shopName = shopName;
        this.phone = phone;
        this.managerName = managerName;
        this.shopStatus = shopStatus;
    }

    public void setShopStatus(String status){
        this.shopStatus = status;
    }
    public String getShopName(){
        return shopName;
    }
    public String getPhone(){
        return phone;
    }
    public String getManagerName() {
        return managerName;
    }
    public String getShopStatus(){
        return shopStatus;
    }
    public String toString(){
        return shopName + " " + phone + " " + managerName + " " + shopStatus;
    }
    public String toCSVString(){
        return shopName + "," + phone + "," + managerName + "," + shopStatus;
    }
}
