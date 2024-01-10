import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.List;
import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.ParseException; 
import java.text.SimpleDateFormat;  
import java.util.Date; 

public class Main{    
    public static void main(String[] args){
             mainMenu();
    }   
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void mainMenu(){
        Scanner input = new Scanner (System.in);
        System.out.println("____________________________________________________________");
        System.out.println("|                                                          |");
        System.out.println("|     ======= COVID-19 contact tracing system ========     |");
        System.out.println("|                                                          |");
        System.out.println("|             CUSTOMER --------------------[1]             |");
        System.out.println("|             SHOP ------------------------[2]             |");
        System.out.println("|             ADMIN -----------------------[3]             |");
        System.out.println("|             EXIT ------------------------[4]             |");
        System.out.println("|                                                          |");
        System.out.println("|__________________________________________________________|"); 
        System.out.println();
        do{
            try {
                System.out.print("Please Enter Your Choice: ");
                int mainMenuInput = input.nextInt(); 
                if (mainMenuInput == 1)
                    customerMainMenu(input);
                else if (mainMenuInput == 2)
                    shopMainMenu(input);
                else if (mainMenuInput == 3)
                    adminMainMenu(input);
                else if (mainMenuInput == 4)
                    System.exit(0); 
                else 
                    throw new IllegalArgumentException("Input Error: Input Must Between 1 - 4 Only.");
            } 
            catch(InputMismatchException ex){
                System.out.println("Input Error: Input Must Between 1 - 4 Only.");
                input.nextLine();
                System.out.println();
            }
            catch(IllegalArgumentException ex){
                System.out.println(ex.getMessage());
                input.nextLine();
                System.out.println();
            }
        } while(true);
    }
    ////////////////////////////////////////////////// CUSTOMER //////////////////////////////////////////////////////////////////
    static void customerMainMenu(Scanner input){
        System.out.println("____________________________________________________________");
        System.out.println("|                                                          |");
        System.out.println("|          ============== CUSTOMER ==============          |");
        System.out.println("|                                                          |");
        System.out.println("|             REGISTER --------------------[1]             |");
        System.out.println("|             SIGN IN  --------------------[2]             |");
        System.out.println("|             EXIT ------------------------[3]             |");
        System.out.println("|                                                          |");
        System.out.println("|__________________________________________________________|"); 
        System.out.println(); 
        
        do {
            try{
                System.out.print("Please Enter Your Choice: ");
                int customerMainMenuInput = input.nextInt();
                if (customerMainMenuInput == 1)
                    customerRegistration(input);
                else if (customerMainMenuInput == 2)
                    customerSignIn(input);
                else if (customerMainMenuInput == 3)
                    mainMenu();
                else
                    throw new IllegalArgumentException("Input Error: Input Must Between 1 - 3 Only.");
            }
            catch(InputMismatchException ex){
                System.out.println("Input Error: Input Must Between 1 - 3 Only.");
                input.nextLine();
                System.out.println();
            }
            catch(IllegalArgumentException ex){
                System.out.println(ex.getMessage());
                input.nextLine();
                System.out.println();
            }
        } while(true); 
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void customerRegistration(Scanner input){
        String fileName = "Customer.csv";
        CustomerDetail d = new CustomerDetail();
        System.out.println();  
        System.out.println("=========== Customer Registration ============");
        do{
            try{
                BufferedReader myFile = new BufferedReader(new FileReader(fileName));
                if (myFile.readLine() == null) {  // check whether the file is empty.
                    System.out.println();
                    System.out.print("Name            : ");
                    String customerName = input.nextLine();
                    customerName = input.nextLine();
                    System.out.print("Phone           : +60");
                    int phone = input.nextInt();
                    String customerPhone = Integer.toString(phone);
                    System.out.print("Password        : ");
                    String customerPassword = input.next();
                    ArrayList<CustomerDetail> customer = new ArrayList<CustomerDetail>();
                    customer.add(new CustomerDetail(customerName, customerPhone, customerPassword, d.getCustomerStatus()));
                    saveCustomerToFiles(customer, fileName);  // save data to csv file
                    System.out.println("Your Account Has Been Created Successfully");
                    pressEnterToContinue();
                    customerMainMenu(input);
                }
                else{
                    System.out.println();
                    System.out.print("Name            : ");
                    String customerName = input.nextLine();
                    customerName = input.nextLine();
                    System.out.print("Phone           : +60");
                    int phone = input.nextInt();
                    String customerPhone = Integer.toString(phone);
                    int customerNameAvailability = checkAvailability(0, customerName, fileName);
                    int customerPhoneAvailability = checkAvailability(1, customerPhone, fileName); // check the existence of customerPhone number
                    if (customerNameAvailability == -1 && customerPhoneAvailability == -1) {
                        System.out.print("Password        : ");
                        String customerPassword = input.next(); // read away unwanted newline.
                        ArrayList<CustomerDetail> customer = readCustomerFromFile(fileName);
                        customer.add(new CustomerDetail(customerName, customerPhone, customerPassword, d.getCustomerStatus()));
                        saveCustomerToFiles(customer, fileName);  // save data to csv file
                        System.out.println("Your Account Has Been Created Successfully");
                        pressEnterToContinue();
                        customerMainMenu(input);
                    }
                    else 
                        throw new IllegalArgumentException("Input Error: This Name or Phone Number Has Been Registered!");     
                }
            }
            catch (FileNotFoundException ex){
                System.out.println("Error: File Does Not Exists.");
                System.exit(0);
            }
            catch (InputMismatchException ex){
                System.out.println ("Input Error: Invalid Phone Number." );     
            }
            catch(IllegalArgumentException ex){
                System.out.println(ex.getMessage());
                System.exit(0);
            }
            catch (ArrayIndexOutOfBoundsException ex){
                System.out.println("Error: File Data Format Incorrect.");
                System.exit(0);
            }
            catch (IOException ex){
                System.out.println("Error: " + ex.getMessage());
                System.exit(0);
            }
        } while(true);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static ArrayList<CustomerDetail> readCustomerFromFile(String fileName) throws IOException{
        ArrayList<CustomerDetail> customer = new ArrayList<CustomerDetail>();
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] items = lines.get(i).split(",");
            customer.add (new CustomerDetail(items[0], items[1], items[2], items[3]));
        }
        return customer;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void saveCustomerToFiles(ArrayList<CustomerDetail> customer, String fileName) throws IOException{
        StringBuilder sb = new StringBuilder();
        FileWriter myFile = new FileWriter(fileName);
        for(int i = 0; i < customer.size(); i++){
            sb.append(customer.get(i).toCSVString() + "\n");
        }
        myFile.write(sb.toString());
        myFile.close();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void customerSignIn(Scanner input){
        String fileName = "Customer.csv";
        System.out.println();
        System.out.println("=========== Customer Sign In ============");
        do{
            try{
                BufferedReader myFile = new BufferedReader(new FileReader(fileName));
                myFile.close();
                System.out.println();
                System.out.print("Phone          : +60");
                int phone = input.nextInt(); 
                String customerPhone = Integer.toString(phone);
                int customerPhoneIndex = checkAvailability(1, customerPhone, fileName); // check the existence of the phone number
                if (customerPhoneIndex >= 0){
                    System.out.print("Password       : ");
                    String customerPassword = input.next();
                    int customerPasswordIndex = checkAvailability(2, customerPassword, fileName);
                    
                    if (customerPhoneIndex == customerPasswordIndex){
                        System.out.println("Sign In Successfully");
                        pressEnterToContinue();
                        customerMenu(customerPhoneIndex, input);
                    }
                    else 
                        throw new IllegalArgumentException("Input Error: Incorrect Password!");
                }
                else 
                    throw new IllegalArgumentException("Input Error: Incorrect Phone Number!");
            }
            catch (FileNotFoundException ex){
                System.out.println("Error: File Does Not Exists.");
                System.exit(0);
            }
            catch (InputMismatchException ex){
                System.out.println("Input Error: Invalid Phone Number.");
                input.nextLine(); // clear the wrong input
            }
            catch (IllegalArgumentException ex){
                System.out.println(ex.getMessage());
                input.nextLine();
            }
           catch (ArrayIndexOutOfBoundsException ex){
                System.out.println("Error: File Data Format Incorrect.");
                System.exit(0);
            }
            catch (IOException ex){
                System.out.println("Error: " + ex.getMessage());
                System.exit(0);
            }
        } while(true);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static int checkAvailability(int arrayIndex, String input, String fileName) throws IOException{
        List <String> lines = Files.readAllLines(Paths.get(fileName));
        int inputIndex = -1; // set default to -1, which means no data in the file
        for (int i = 0; i < lines.size(); i++) {
            String[] items = lines.get(i).split(",");
            if (items[arrayIndex].equals(input))
                inputIndex = i;
        }
        return inputIndex; // return the index of the input 
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void customerMenu(int customerIndex, Scanner input){
        System.out.println("____________________________________________________________");
        System.out.println("|                                                          |");
        System.out.println("|          ============== CUSTOMER ==============          |");
        System.out.println("|                                                          |");
        System.out.println("|             CHECK IN SHOP ---------------[1]             |");
        System.out.println("|             VIEW HISTORY  ---------------[2]             |");
        System.out.println("|             VIEW STATUS -----------------[3]             |");
        System.out.println("|             SIGN OUT --------------------[4]             |");
        System.out.println("|                                                          |");
        System.out.println("|__________________________________________________________|"); 
        System.out.println();   
        do{      
            try{  
                System.out.print("Please Enter Your Choice: ");
                int customerMenuInput = input.nextInt();
                if (customerMenuInput == 1)
                    customerCheckIn(customerIndex, input);
                else if (customerMenuInput == 2)
                    viewCustomerHistory(customerIndex, input);
                else if (customerMenuInput == 3)
                    viewCustomerStatus(customerIndex, input);
                else if (customerMenuInput == 4)
                    mainMenu();
                else 
                    throw new IllegalArgumentException("Input Error: Input Must Between 1 - 4 Only.");
            }
            catch(InputMismatchException ex){
                System.out.println("Input Error: Input Must Between 1 - 4 Only.");
                input.nextLine();
                System.out.println();
            }
            catch(IllegalArgumentException ex){
                System.out.println(ex.getMessage());
                input.nextLine();
                System.out.println();
            }
        } while (true);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void customerCheckIn(int customerIndex, Scanner input){
        String strIndex = Integer.toString(customerIndex);
        String customerFileName = "Customer.csv";
        String customerHistoryFileName = "CustomerHistory.csv"; 
        String shopFileName = "Shop.csv";
        int num = 1;
        try{
            BufferedReader checkCustomerFile = new BufferedReader(new FileReader(customerFileName));
            BufferedReader checkCustomerHistoryFile = new BufferedReader(new FileReader(customerHistoryFileName));
            BufferedReader checkShopFile = new BufferedReader(new FileReader(shopFileName));
            ArrayList<CustomerDetail> customer = readCustomerFromFile(customerFileName);
            ArrayList<Shop> shop = readShopFromFile(shopFileName);
            System.out.println();
            System.out.println("================= CUSTOMER CHECK IN =================");
            for(int i = 0; i < shop.size(); i++){
                System.out.println(String.format("%20s %20s", num++ + ". ",shop.get(i).getShopName()));
            }
            System.out.println(); 
            do{
                try{ 
                    System.out.print("Please Enter Your Choice: ");
                    int checkInInput = input.nextInt();
                    if (checkInInput > 0 && checkInInput <= shop.size()) {
                        if (checkCustomerHistoryFile.readLine() == null) {
                            ArrayList<CustomerHistory> history = new ArrayList<CustomerHistory>();
                            history.add(new CustomerHistory(strIndex, customer.get(customerIndex).getCustomerName(), currentDateTime(), shop.get(checkInInput-1).getShopName()));
                            saveCustomerHistoryToFile(history, customerHistoryFileName); // save customer history to csv file
                        }
                        else {
                            ArrayList<CustomerHistory> history = readCustomerHistoryFromFile("CustomerHistory.csv");
                            history.add(new CustomerHistory(strIndex, customer.get(customerIndex).getCustomerName(), currentDateTime(), shop.get(checkInInput-1).getShopName()));
                            saveCustomerHistoryToFile(history, customerHistoryFileName); // save customer history to csv file
                            for (int i = 0; i < customer.size(); i++) {
                                if (customer.get(i).getCustomerStatus().equals("Case")) {
                                    customerStatusIdentifier(customer.get(i).getCustomerName(), customerHistoryFileName, customerFileName);
                                    shopStatusIdentifier(customer.get(i).getCustomerName(), customerHistoryFileName, shopFileName);
                                }
                            }
                        }
                    }
                    else 
                        throw new IllegalArgumentException("Input Error: Input Must Between 1 - " + shop.size() + " Only.");

                    System.out.println("Check In Successfully.");
                    pressEnterToContinue();
                    customerMenu(customerIndex, input);
                    checkCustomerFile.close(); checkCustomerHistoryFile.close(); checkShopFile.close();
                }
                catch(InputMismatchException ex){
                    System.out.println("Input Error: Input Must Between 1 - " + shop.size() + " Only.");
                    input.nextLine();
                    System.out.println();
                }
                catch(IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                    input.nextLine();
                    System.out.println();
                }
                catch (ArrayIndexOutOfBoundsException ex){
                    System.out.println("Error: File Data Format Incorrect.");
                    System.exit(0);
                }
                catch (IOException ex){
                    System.out.println("Error: " + ex.getMessage());
                    System.exit(0);
                }
            } while(true);
        }
        catch (FileNotFoundException ex){
            System.out.println("Error: File Does Not Exists.");
            System.exit(0);
        }
        catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Error: File Data Format Incorrect.");
            System.exit(0);
        }
        catch (IOException ex){
            System.out.println("Error: " + ex.getMessage());
            System.exit(0);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static String currentDateTime() throws IOException{
        LocalDateTime myDateTime = LocalDateTime.now();
        DateTimeFormatter myFormatDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = myDateTime.format(myFormatDateTime);
        return formattedDateTime;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static ArrayList<CustomerHistory> readCustomerHistoryFromFile(String fileName) throws IOException {
        ArrayList<CustomerHistory> history = new ArrayList<CustomerHistory>();
        List<String> HistoryLines = Files.readAllLines(Paths.get(fileName));
        for (int i = 0; i < HistoryLines.size(); i++) {
            // split a line by comma
            String[] items = HistoryLines.get(i).split(",");
            history.add(new CustomerHistory(items[0], items[1], items[2], items[3]));
        }
        return history;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void saveCustomerHistoryToFile(ArrayList<CustomerHistory> history, String fileName) throws IOException{
        StringBuilder sb = new StringBuilder();
        FileWriter myFile = new FileWriter(fileName);
        for(int i = 0; i < history.size(); i++){
            sb.append(history.get(i).toCSVString() + "\n");
        }
        myFile.write(sb.toString());
        myFile.close();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void viewCustomerHistory(int customerIndex, Scanner input) {
        String strIndex = Integer.toString(customerIndex);
        String fileName = "CustomerHistory.csv";
        System.out.println();
        System.out.println("================ HISTORY ================");
        System.out.println();
        int num = 1;
        try{
            BufferedReader myFile = new BufferedReader(new FileReader(fileName));
            if (myFile.readLine() == null)  // empty file
                System.out.println("No History Available");
            else{
                int historyAvailability = checkAvailability(0, strIndex, fileName); //check whether the customer have history
                if (historyAvailability == -1)
                    System.out.println("No History Available");
                else{
                    System.out.println(String.format("%5s %20s", "No", "Date       Time      Shop"));
                    ArrayList<CustomerHistory> history = readCustomerHistoryFromFile(fileName);
                    for (int i = 0; i < history.size(); i++) {
                        if (history.get(i).getCustomerIndex().equals(strIndex))
                            System.out.println(String.format( "%5s %20s" , num++, history.get(i).getDateTime() + "  " + history.get(i).getShopName()));
                    }
                }
            }
            System.out.println();
            pressEnterToContinue();
            customerMenu(customerIndex, input);
            myFile.close();
        }
        catch (FileNotFoundException ex){
            System.out.println("Error: File Does Not Exists.");
            System.exit(0);
        }
        catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Error: File Data Format Incorrect.");
            System.exit(0);
        }
        catch (IOException ex){
            System.out.println("Error: " + ex.getMessage());
            System.exit(0);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void viewCustomerStatus(int customerIndex, Scanner input){
        String fileName = "Customer.csv";
        System.out.println();
        System.out.println("================ STATUS ================");
        System.out.println();
        try{
            BufferedReader myFile = new BufferedReader(new FileReader(fileName));   //check the file
            myFile.close();
            ArrayList<CustomerDetail> customer = readCustomerFromFile(fileName);
            System.out.println("Status: " + customer.get(customerIndex).getCustomerStatus());
            System.out.println();
            pressEnterToContinue();
            customerMenu(customerIndex, input);
            myFile.close();
        }
        catch (FileNotFoundException ex){
            System.out.println("Error: File Does Not Exists.");
            System.exit(0);
        }
        catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Error: File Data Format Incorrect.");
            System.exit(0);
        }
        catch (IOException ex){
            System.out.println("Error: " + ex.getMessage());
            System.exit(0);
        }
    }
    //////////////////////////////////////////////////////////// SHOP ////////////////////////////////////////////////////////////
    static void shopMainMenu(Scanner input){
        System.out.println("____________________________________________________________");
        System.out.println("|                                                          |");
        System.out.println("|          ================ SHOP ================          |");
        System.out.println("|                                                          |");
        System.out.println("|             REGISTER --------------------[1]             |");
        System.out.println("|             SIGN IN ---------------------[2]             |");
        System.out.println("|             EXIT ------------------------[3]             |");
        System.out.println("|                                                          |");
        System.out.println("|__________________________________________________________|"); 
        System.out.println(); 
        do {
            try{
                System.out.print("Please Enter Your Choice: ");
                int shopMainMenuInput = input.nextInt();
                if (shopMainMenuInput == 1)
                    shopRegistration(input);
                else if (shopMainMenuInput == 2)
                    shopSignIn(input);
                else if (shopMainMenuInput == 3)
                    mainMenu();
                else
                    throw new IllegalArgumentException("Input Error: Input Must Between 1 - 3 Only.");
            }
            catch(InputMismatchException ex){
                System.out.println("Input Error: Input Must Between 1 - 3 Only.");
                input.nextLine();
                System.out.println();
            }
            catch(IllegalArgumentException ex){
                System.out.println(ex.getMessage());
                input.nextLine();
                System.out.println();
            }
        } while(true); 
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void shopRegistration(Scanner input){
        String fileName = "Shop.csv";
        Shop s = new Shop();
        System.out.println();  
        System.out.println("============= Shop Registration ==============");
        do{
            try{
                System.out.println();
                System.out.print("Shop Name       : ");
                String shopName = input.nextLine();
                shopName = input.nextLine();
                BufferedReader myFile = new BufferedReader(new FileReader(fileName));
                if (myFile.readLine() == null) {              // check whether the file is empty.
                    System.out.print("Phone           : +60");
                    int phone = input.nextInt();
                    String shopPhone = Integer.toString(phone);
                    System.out.print("Manager Name    : ");
                    String managerName = input.nextLine();
                    managerName = input.nextLine(); 
                    ArrayList<Shop> shops = new ArrayList<Shop>();
                    shops.add(new Shop(shopName.toUpperCase(), shopPhone, managerName, s.getShopStatus()));
                    saveShopToFiles(shops, fileName);  // save data to csv file
                    System.out.println("Your Shop Account Has Been Created Successfully");
                    pressEnterToContinue();
                    shopMainMenu(input);
                }
                else{
                    System.out.print("Phone           : +60");
                    int phone = input.nextInt();
                    String shopPhone = Integer.toString(phone);
                    int shopNameAvailability = checkAvailability(0, shopName.toUpperCase(), fileName);  // check the existence of the shop name
                    int shopPhoneAvailability = checkAvailability(1, shopPhone, fileName); // check the existence of shop phone number
                    if (shopNameAvailability == -1 && shopPhoneAvailability == -1) {
                        System.out.print("Manager Name    : ");
                        String managerName = input.nextLine();
                        managerName = input.nextLine(); 
                        ArrayList<Shop> shops = readShopFromFile(fileName);
                        shops.add(new Shop(shopName.toUpperCase(), shopPhone, managerName, s.getShopStatus()));
                        saveShopToFiles(shops, fileName);                 // save data to csv file
                        System.out.println("Your Shop Account Has Been Created Successfully");
                        pressEnterToContinue();
                        shopMainMenu(input);
                    }
                    else 
                        throw new IllegalArgumentException("Input Error: This Shop Name or Phone Number Has Been Registered!");
                }
            }
            catch (FileNotFoundException ex){
                System.out.println("Error: File Does Not Exists.");
                
                System.exit(0);
            }
            catch (InputMismatchException ex){
                System.out.println ("Input Error: Invalid Phone Number." );
            }
            catch(IllegalArgumentException ex){
                System.out.println(ex.getMessage());
            }
            catch (ArrayIndexOutOfBoundsException ex){
                System.out.println("Error: File Data Format Incorrect.");
                System.exit(0);
            }
            catch (IOException ex){
                System.out.println("Error: " + ex.getMessage());
                System.exit(0);
            }
        } while(true);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static ArrayList<Shop> readShopFromFile(String fileName) throws IOException {
        ArrayList<Shop> shops = new ArrayList<Shop>();
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        for (int i = 0; i < lines.size(); i++) {
            // split a line by comma
            String[] items = lines.get(i).split(",");
            shops.add (new Shop(items[0], items[1], items[2], items[3]));
        }
        return shops;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void saveShopToFiles(ArrayList<Shop> shops, String fileName) throws IOException{
        StringBuilder sb = new StringBuilder();
        FileWriter myFile = new FileWriter(fileName);
        for(int i = 0; i < shops.size(); i++){
            sb.append(shops.get(i).toCSVString() + "\n");
        }
        myFile.write(sb.toString());
        myFile.close();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void shopSignIn(Scanner input){
        String fileName = "Shop.csv";
        System.out.println();
        System.out.println("=============== Shop Sign In ================");
        do{
            try{
                BufferedReader myFile = new BufferedReader(new FileReader(fileName));
                myFile.close();
                System.out.println();
                System.out.print("Shop Name    : ");
                String shopName = input.nextLine();
                shopName = input.nextLine();
                System.out.print("Phone        : +60");
                int shopPhone = input.nextInt();
                String strShopPhone = Integer.toString(shopPhone);
                int shopNameIndex = checkAvailability(0, shopName.toUpperCase(), fileName);
                int shopPhoneIndex = checkAvailability(1, strShopPhone, fileName);
                if (shopNameIndex == shopPhoneIndex){
                    System.out.println("Sign In Successfully");
                    pressEnterToContinue();
                    shopMenu(shopPhoneIndex, input);
                }
                else 
                    throw new IllegalArgumentException("Error: Incorrect Shop Name or Incorrect Phone Number!");
            }
            catch (FileNotFoundException ex){
                System.out.println("Error: File Does Not Exists.");
                
                System.exit(0);
            }
            catch (InputMismatchException ex){
                System.out.println("Input Error: Invalid Phone Number.");
            }
            catch (IllegalArgumentException ex){
                System.out.println(ex.getMessage());
            }
            catch (ArrayIndexOutOfBoundsException ex){
                System.out.println("Error: File Data Format Incorrect.");
                System.exit(0);
            }
            catch (IOException ex){
                System.out.println("Error: " + ex.getMessage());
                System.exit(0);
            }
        } while(true);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void shopMenu(int shopIndex, Scanner input){
        System.out.println("____________________________________________________________");
        System.out.println("|                                                          |");
        System.out.println("|           ================ SHOP ===============          |");
        System.out.println("|                                                          |");
        System.out.println("|             VIEW STATUS -----------------[1]             |");
        System.out.println("|             SIGN OUT --------------------[2]             |");
        System.out.println("|                                                          |");
        System.out.println("|__________________________________________________________|"); 
        System.out.println();   
        do{      
            try{  
                System.out.print("Please Enter Your Choice: ");
                int shopMenuInput = input.nextInt();
                if (shopMenuInput == 1)
                    viewShopStatus(shopIndex, input);
                else if (shopMenuInput == 2)
                    mainMenu();
                else 
                    throw new IllegalArgumentException("Input Error: Input Must Between 1 - 2 Only.");
            }
            catch(InputMismatchException ex){
                System.out.println("Input Error: Input Must Between 1 - 2 Only.");
                input.nextLine();
                System.out.println();
            }
            catch(IllegalArgumentException ex){
                System.out.println(ex.getMessage());
                input.nextLine();
                System.out.println();
            }
        } while (true);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void viewShopStatus(int shopIndex, Scanner input) {
        String fileName = "Shop.csv";
        try{
            BufferedReader myFile = new BufferedReader(new FileReader(fileName));
            System.out.println();
            System.out.println("================ STATUS ================");
            System.out.println();
            ArrayList<Shop> shops = readShopFromFile(fileName); 
            System.out.println("Status: " + shops.get(shopIndex).getShopStatus());
            System.out.println();
            pressEnterToContinue();
            shopMenu(shopIndex, input);
            myFile.close();
        }
        catch (FileNotFoundException ex){
            System.out.println("Error: " + fileName + " Does Not Exists.");
            input.nextLine(); // clear the wrong input
        }
        catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Error: File Data Format Incorrect.");
            System.exit(0);
        }
        catch (IOException ex){
            System.out.println("Error: " + ex.getMessage());
            System.exit(0);
        }
    }
    ////////////////////////////////////////////////////////// ADMIN /////////////////////////////////////////////////////////////////////
    static void adminMainMenu(Scanner input) {
        System.out.println("____________________________________________________________");
        System.out.println("|                                                          |");
        System.out.println("|      ======= ADMIN Covid-19 Tracing System =========     |");
        System.out.println("|                                                          |");
        System.out.println("|             Login -----------------------[1]             |");
        System.out.println("|             EXIT ------------------------[2]             |");
        System.out.println("|                                                          |");
        System.out.println("|__________________________________________________________|"); 
        System.out.println();
        do {
            try {
                System.out.print("Please Enter Your Choice: ");
                int MainMenuInput = input.nextInt();
                if (MainMenuInput == 1)
                    adminLogin(input);
                else if (MainMenuInput == 2)
                    mainMenu();
                else
                    throw new IllegalArgumentException("Input Error : Invalid input. Please select number between '1 and 2' only. Please try again.");
            }
            catch (InputMismatchException ex) {
                System.out.println("Input Error : Your input can only be integer.");
                input.nextLine();
                System.out.println();
            }
            catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                input.nextLine();
                System.out.println();
            }
        } while (true);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void adminLogin(Scanner input) {
        System.out.println();
        System.out.println(" =============== LOG IN =============== ");
        System.out.println();
        String id = "admin";
        String psd = "admin123";

        do {
            try{
                System.out.print("Username : ");
                String username = input.next();
                if (username.equals(id)) {
                    do{
                        try{
                            System.out.print("Password : ");
                            String password = input.next();
                            if ( password.equals (psd)) {
                                System.out.print("Login as Admin Success.");
                                adminMenu(input);
                            }
                            else 
                                throw new IllegalArgumentException("Invalid password. Please try again");
                        }
                        catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                            input.nextLine();
                            System.out.println();
                        }
                    }while (true);
                }
                else 
                    throw new IllegalArgumentException("Invalid username. Please try again");
            }
            catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                input.nextLine();
                System.out.println();
            } 
        } while (true);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void adminMenu(Scanner input) {
        System.out.println();
        System.out.println("____________________________________________________________");
        System.out.println("|                                                          |");
        System.out.println("|     ======= ADMIN Covid-19 Tracing System =========      |");
        System.out.println("|                                                          |");
        System.out.println("|             Master Visit History --------[1]             |");
        System.out.println("|             List of Customer ------------[2]             |");
        System.out.println("|             List of Shops ---------------[3]             |");
        System.out.println("|             Add 30 Random Visits --------[4]             |");
        System.out.println("|             Modify Customer Status ------[5]             |");
        System.out.println("|             Back to Main Menu -----------[6]             |");
        System.out.println("|                                                          |");
        System.out.println("|__________________________________________________________|"); 
        System.out.println();
        do {
            try {
                System.out.print("Please Enter Your Choice: ");
                int adminMenuInput = input.nextInt();
                if (adminMenuInput == 1)
                    masterVisitHistory(input);
                else if (adminMenuInput == 2)
                    listOfCustomer(input);
                else if (adminMenuInput == 3)
                    listOfShops(input);
                else if (adminMenuInput == 4)
                    randomVisits(input);
                else if (adminMenuInput == 5)
                    modifyCustomerStatus(input);
                else if (adminMenuInput == 6)
                    mainMenu();    
                else
                    throw new IllegalArgumentException("Input Error : Invalid input. Please select number between '1-6' only. Please try again. ");
            }
            catch (InputMismatchException ex) {
                System.out.println("Input Error : Your input can only be integer.");
                input.nextLine();
                System.out.println();
            }
            catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                input.nextLine();
                System.out.println();
            }
        } while (true);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void masterVisitHistory(Scanner input){
        String customerFileName = "Customer.csv";
        String customerHistoryFileName = "CustomerHistory.csv";
        System.out.println();
        System.out.println("============================================================================== Master Visit History ==========================================================================");
        System.out.println(); 
        try {
            BufferedReader checkCustomerFile = new BufferedReader(new FileReader(customerFileName));
            BufferedReader checkCustomerHistoryFile = new BufferedReader(new FileReader(customerHistoryFileName));
            checkCustomerFile.close(); checkCustomerHistoryFile.close();
            ArrayList<CustomerDetail> c = readCustomerFromFile(customerFileName);
            ArrayList<CustomerHistory> h = readCustomerHistoryFromFile(customerHistoryFileName);
            System.out.println(String.format("%5s %26s %20s %34s %35s %30s" , "No" , "Date", "Time", "Customer","Shop","Status"));
            for (int i = 0 ; i < h.size(); i++ ) {
                int intCustomerIndex = Integer.parseInt(h.get(i).getCustomerIndex()); 
                String splitDateAndTime = h.get(i).getDateTime();
                String[] dateAndTime = splitDateAndTime.split(" ");
                System.out.println(String.format( "%5s %26s %20s %34s %35s %30s", i+1, dateAndTime[0], dateAndTime[1], h.get(i).getCustomerName(), h.get(i).getShopName(), c.get(intCustomerIndex).getCustomerStatus()));
            }
            System.out.println("==============================================================================================================================================================================");
            pressEnterToContinue();
            adminMenu(input);
        }
        catch (FileNotFoundException ex){
            System.out.println("Error: File Does Not Exists.");
            System.exit(0);
        }
        catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Error: File Data Format Incorrect.");
            System.exit(0);
        }
        catch (IOException ex){
            System.out.println("Error: " + ex.getMessage());
            System.exit(0);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void listOfCustomer(Scanner input) {
        String fileName = "Customer.csv";
        System.out.println();
        System.out.println("============================================================= List Of Customer =============================================================");
        System.out.println();
        try{
            BufferedReader myFile = new BufferedReader(new FileReader(fileName));
            myFile.close();
            System.out.println(String.format("%5s %30s %30s %30s", "No" , "Name", "Phone", "Status"));
            ArrayList<CustomerDetail> c = readCustomerFromFile(fileName);
            for (int i = 0 ; i < c.size(); i++ ) {
            System.out.println(String.format( "%5s %30s %30s %30s", i+1, c.get(i).getCustomerName(),  c.get(i).getCustomerPhone(), c.get(i).getCustomerStatus()));
            }
            System.out.println("============================================================================================================================================");       
            pressEnterToContinue();
            adminMenu(input);
        }
        catch (FileNotFoundException ex){
            System.out.println("Error: File Does Not Exists.");
            System.exit(0);
        }
        catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Error: File Data Format Incorrect.");
            System.exit(0);
        }
        catch (IOException ex){
            System.out.println("Error: " + ex.getMessage());
            System.exit(0);
        }
    }    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void listOfShops(Scanner input) {
        String fileName = "Shop.csv";
        System.out.println();
        System.out.println("============================================================= List Of Shops =============================================================");
        System.out.println();
        try{
            BufferedReader myFile = new BufferedReader(new FileReader(fileName));
            myFile.close();
            System.out.println(String.format("%5s %30s %29s %31s %30s", "No" , "Shop Name", "Phone", "Manager", "Status"));
            ArrayList<Shop> s = readShopFromFile(fileName);
            for (int i = 0 ; i < s.size(); i++ ) {
            System.out.println(String.format("%5s %30s %29s %31s %30s", i+1, s.get(i).getShopName(), s.get(i).getPhone(), s.get(i).getManagerName(), s.get(i).getShopStatus()));
            }
            System.out.println("=========================================================================================================================================");
            pressEnterToContinue();
            adminMenu(input);
        }
        catch (FileNotFoundException ex){
            System.out.println("Error: File Does Not Exists.");
            System.exit(0);
        }
        catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Error: File Data Format Incorrect.");
            System.exit(0);
        }
        catch (IOException ex){
            System.out.println("Error: " + ex.getMessage());
            System.exit(0);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void randomVisits(Scanner input) {
        String customerFileName = "Customer.csv";
        String customerHistoryFileName = "CustomerHistory.csv";
        String shopFileName = "Shop.csv";
        try {
            BufferedReader checkCustomerFile = new BufferedReader(new FileReader(customerFileName));
            BufferedReader checkCustomerHistoryFile = new BufferedReader(new FileReader(customerHistoryFileName));
            BufferedReader checkShopFile = new BufferedReader(new FileReader(shopFileName));
            checkCustomerFile.close(); checkCustomerHistoryFile.close(); checkShopFile.close(); 
            ArrayList<CustomerDetail> c = readCustomerFromFile(customerFileName);
            ArrayList<CustomerHistory> h = readCustomerHistoryFromFile(customerHistoryFileName);
            ArrayList<Shop> shop = readShopFromFile(shopFileName);
            Random random = new Random();
            for (int i = 0; i < 30; i++){
                int randomCustomerIndex = random.nextInt(c.size());
                int randomShopIndex = random.nextInt(shop.size());
                int randomSeconds = random.nextInt(3600 * 24);
                LocalDateTime randomTime = LocalDateTime.now().minusSeconds(randomSeconds);
                DateTimeFormatter myFormatDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = randomTime.format(myFormatDateTime);
                String strRandomCustomerIndex = Integer.toString(randomCustomerIndex);
                h.add(new CustomerHistory(strRandomCustomerIndex, c.get(randomCustomerIndex).getCustomerName(), formattedDateTime, shop.get(randomShopIndex).getShopName()));
                saveCustomerHistoryToFile(h, customerHistoryFileName);
            }
            sortDateTime(customerHistoryFileName);
            System.out.println();
            System.out.println("Added 30 Random Visits Successfully.");
            pressEnterToContinue();
            adminMenu(input);
        }
        catch (FileNotFoundException ex){
            System.out.println("Error: File Does Not Exists.");
            System.exit(0);
        }
        catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Error: File Data Format Incorrect.");
            System.exit(0);
        }
        catch (IOException ex){
            System.out.println("Error: " + ex.getMessage());
            System.exit(0);
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void sortDateTime(String customerHistoryFileName) throws IOException{
        ArrayList<CustomerHistory> history = readCustomerHistoryFromFile(customerHistoryFileName);
        for (int i = 0; i < history.size(); i++) {
            for(int j = i+1; j < history.size(); j++) {
                if(history.get(i).getDateTime().compareTo(history.get(j).getDateTime()) > 0){
                    CustomerHistory temp = history.get(i);
                    history.set(i, history.get(j));      
                    history.set(j, temp);    
                    saveCustomerHistoryToFile(history, customerHistoryFileName);          
                }
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void modifyCustomerStatus(Scanner input){
        String customerFileName = "Customer.csv";
        String customerHistoryFileName = "CustomerHistory.csv";
        String shopFileName = "Shop.csv";
        System.out.println();
        System.out.println("============================================================= MODIFY STATUS =============================================================");
        System.out.println();
        do {
            try {
                BufferedReader checkCustomerFile = new BufferedReader(new FileReader(customerFileName));
                BufferedReader checkCustomerHistoryFile = new BufferedReader(new FileReader(customerHistoryFileName));
                BufferedReader checkShopFile = new BufferedReader(new FileReader(shopFileName));
                checkCustomerFile.close(); checkCustomerHistoryFile.close(); checkShopFile.close();
                ArrayList<CustomerDetail> c = readCustomerFromFile(customerFileName);
                System.out.println(String.format("%5s %30s %30s", "No", "Customer", "Status"));
                for (int i = 0 ; i < c.size(); i++ ) {
                    System.out.println(String.format( "%5s %30s %30s", i+1, c.get(i).getCustomerName(), c.get(i).getCustomerStatus()));
                }
                System.out.println();
                System.out.print("Please Enter the index of customer to modify their status: ");
                int modifyCustomerInput = input.nextInt();
                if ( modifyCustomerInput > 0 && modifyCustomerInput <= c.size()) {
                    System.out.println("This following customer: " + "[" + c.get(modifyCustomerInput-1).getCustomerName() + "]" + " has been flagged as case successfully.");
                    c.get(modifyCustomerInput-1).setCustomerStatus("Case");
                    saveCustomerToFiles(c, customerFileName);
                    customerStatusIdentifier(c.get(modifyCustomerInput-1).getCustomerName(), customerHistoryFileName, customerFileName);
                    shopStatusIdentifier(c.get(modifyCustomerInput-1).getCustomerName(), customerHistoryFileName, shopFileName);
                    System.out.println("=========================================================================================================================================");
                    pressEnterToContinue();
                    adminMenu(input);    
                }
                else 
                    throw new IllegalArgumentException("Input Error : Invalid input. Please select number between 1-" + c.size() + "  only. Please try again. ");
            } 
            catch (FileNotFoundException ex){
                System.out.println("Error: File Does Not Exists.");
                System.exit(0);
            }
            catch (InputMismatchException ex) {
                System.out.println("Input Error : Input can only be integer.");
                input.nextLine();
                System.out.println();
            } 
            catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                input.nextLine();
                System.out.println();
            }
           catch (ArrayIndexOutOfBoundsException ex){
                System.out.println("Error: File Data Format Incorrect.");
                System.exit(0);
            }
            catch (IOException ex){
                System.out.println("Error: " + ex.getMessage());
                System.exit(0);
            }
        } while (true);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void customerStatusIdentifier(String caseCustomerName, String customerHistoryFileName, String customerFileName) throws IOException{
        ArrayList<CustomerHistory> h = readCustomerHistoryFromFile(customerHistoryFileName);
        ArrayList<CustomerDetail> c = readCustomerFromFile(customerFileName);
        try {
            for (int i = 0; i < h.size(); i++){
                if (h.get(i).getCustomerName().equals(caseCustomerName)) {
                    for (int j = 0; j < h.size(); j++){
                        if (h.get(j).getShopName().equals(h.get(i).getShopName())) {
                            if (i < j){
                                Date startDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(h.get(i).getDateTime());
                                Date endDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(h.get(j).getDateTime());
                                long seconds = (endDate.getTime()-startDate.getTime()) / 1000;

                                if (seconds <= 3600){
                                    for (int x = 0; x < c.size(); x++){
                                        if (c.get(x).getCustomerName().equals(h.get(j).getCustomerName())) {
                                            if (!c.get(x).getCustomerStatus().equals("Case")){
                                                c.get(x).setCustomerStatus("Close");
                                                saveCustomerToFiles(c, customerFileName);
                                            }
                                        }
                                    }
                                }
                            }
                            if (i > j) {
                                Date startDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(h.get(j).getDateTime());
                                Date endDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(h.get(i).getDateTime());
                                long seconds = (endDate.getTime()-startDate.getTime()) / 1000;
                                if (seconds <= 3600){
                                    for (int x = 0; x < c.size(); x++){
                                        if (c.get(x).getCustomerName().equals(h.get(j).getCustomerName())) {
                                            if (!c.get(x).getCustomerStatus().equals("Case")){
                                                c.get(x).setCustomerStatus("Close");
                                                saveCustomerToFiles(c, customerFileName);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (ParseException ex){
            System.out.println("Error: A ParseException Was Caught.");
            
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void shopStatusIdentifier(String caseCustomerName, String customerHistoryFileName, String shopFileName) throws IOException{
        ArrayList<CustomerHistory> h = readCustomerHistoryFromFile(customerHistoryFileName);
        ArrayList<Shop> s = readShopFromFile(shopFileName);
        for (int i = 0; i < h.size(); i++){
            if (h.get(i).getCustomerName().equals(caseCustomerName)) {
                for (int j = 0; j < s.size(); j++){
                    if(h.get(i).getShopName().equals(s.get(j).getShopName())){
                        s.get(j).setShopStatus("Case");
                        saveShopToFiles(s, "Shop.csv");
                    }
                }
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    static void pressEnterToContinue() {
        Scanner input = new Scanner (System.in);
        System.out.print("Press Enter to Continue.");
        input.nextLine();  
        System.out.println();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
} 
