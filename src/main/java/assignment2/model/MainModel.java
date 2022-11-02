package assignment2.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.google.gson.Gson;

public class MainModel {

    private LastFiveProductsModel lastFiveProductsModel;
    private CardPaymentModel cardPaymentModel;
    private LoginModel loginModel;
    private CashPaymentModel cashPaymentModel;
    private InventoryModel inventoryModel;


    private User user;
    private boolean isLoggedIn;

    private HashMap<Product, Integer> aggregatePurchases;
    private JsonParser jsonParser;

    private CSVFileParser csvFileParser;

    private List<CancelledTransaction> cancelledTransactions;
    private List<User> users;
    private List<List<String>> availableProducts;
    private List<List<String>> sellerTransactionString;
    private List<Transaction> sellerTransactions;

    private List<List<String>> cashierTransactionString;
    private List<Transaction> cashierTransactions;

    public MainModel(JsonParser jsonParser, CSVFileParser csvParser){

        this.jsonParser = jsonParser;
        this.csvFileParser = csvParser;

        this.loginModel = new LoginModel(jsonParser.getUsers(),this.getJsonParser(), csvFileParser);
        this.user = loginModel.getAnonymousUser();

        if (this.user != null){
            this.user.clearCart();
        }

        this.isLoggedIn = false;

        this.lastFiveProductsModel = new LastFiveProductsModel();
        this.cardPaymentModel = new CardPaymentModel(this, jsonParser );
        this.cashPaymentModel = new CashPaymentModel(jsonParser.getCash(), jsonParser,csvFileParser);
        this.inventoryModel = new InventoryModel(jsonParser.getInventory(), jsonParser, csvFileParser);

        this.sellerTransactions = new ArrayList<Transaction>();
        this.cashierTransactions = new ArrayList<Transaction>();

        this.sellerTransactionString = this.csvFileParser.readSellerTransactions();
        this.cashierTransactionString = this.csvFileParser.readCashierTransactions();

        

        this.cancelledTransactions = csvParser.getCancelledTransactions();
        this.aggregatePurchases = new HashMap<Product, Integer>();

        this.users = loginModel.getUsers(); ////////
        inventoryModel.initializeProductsToString(); ////
        availableProducts = this.inventoryModel.getInventoryAsString();

    }

    public LastFiveProductsModel getLastFiveProductsModel(){
        return this.lastFiveProductsModel;
    }

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public CardPaymentModel getCardPaymentModel(){return cardPaymentModel;}

    public CashPaymentModel getCashPaymentModel(){
        return this.cashPaymentModel;
    }
    public boolean isLoggedIn(){
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean logged){
        this.isLoggedIn = logged;
    }
    public List<Cash> getCash(){return cashPaymentModel.getCashList();}

    public User getUser(){
        return this.user;
    }

    public void logout(){
        this.user.clearCart();
        this.user = loginModel.getAnonymousUser();
        this.isLoggedIn = false;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Map<Product, Integer> getCart(){
        return user.getCart();
    }

    public Map<Product, Integer> getAggregatePurchases(){
        return this.aggregatePurchases;
    }

    public void cancelTransaction(CancellationReason cancellationReason, LocalDateTime timeCancelled){

        // clear cart (look at Katie's stuff)
        inventoryModel.putBack(user.getCart());
        user.clearCart();

        cancelledTransactions.add(new CancelledTransaction(user.getUsername(), cancellationReason, timeCancelled));
        System.out.println(cancellationReason);
        csvFileParser.updateCancelledTransactions(cancelledTransactions);

        // log user out
        logout();

    }

    public List<CancelledTransaction> getCancelledTransactions(){
        return cancelledTransactions;
    }

    public String getCancelledTransactionsAsString(){
        String out = "";

        for (CancelledTransaction c : cancelledTransactions){
            out += c.getUsername() + ", " + c.getCancellationReason().getReason() + ", " + c.getTimeCancelled().toString();
            out += "\n";
        }
        return out;
    }

    public String getTransactionsAsString(){
//        String out = "";
//
//        for (List<String> c : ){
//            out += c.getUsername() + ", " + c.getCancellationReason().getReason() + ", " + c.getTimeCancelled().toString();
//            out += "\n";
//        }
//        return out;
        return "please move your to string method to model @william";
    }

    public List<List<String>> getItemsSold(){  //below for displaying seller reports on owner dashboard
        return getCsvFileParser().readSellerTransactions();
    }

    public String getItemsSoldAsString(){
    
        String transTemp = "";
        List<List<String>> purchasedItems = getCsvFileParser().readSellerTransactions();

        for (List<String> s : purchasedItems){
            String temp2 = String.join(", ", s).stripTrailing();
            temp2 = temp2.concat("\n");
        
            transTemp = transTemp.concat(temp2);
        }
        return transTemp;
    }


    public List<List<String>> getAvailableProducts(){
        return this.availableProducts;
    }

    public String getAvailableProductsAsString(){
        //read inventory from file 
        //copypasted from sellerdashboardview - clean later
        String invTemp = "";

        if (availableProducts.size() == 0){
            invTemp = "No items available.";
        }

        else{
            for (List<String> s : availableProducts){
                String temp2 = String.join(", ", s).stripTrailing();
                temp2 = temp2.concat("\n");
                invTemp = invTemp.concat(temp2);
            }
        }
        return invTemp;
    }


    public String getUsersAsString(){  //for displaying users report
        String out = "";
        for (User user : users){
            out += user.getUsername() + ", " + user.getUserAccess() + "\n";
        }
        return out;
    }


    public boolean login(String username, String password){

        User attempt = loginModel.login(username, password);

        if (attempt == null){
            return false;
        } else {
            this.user = attempt;
            this.isLoggedIn = true;
            return true;
        }

        // need to update inventory file as well
    }

    public void addToCart(Product product, int quantity){
        user.addToCart(product, quantity);

        // update quantity in inventory
        inventoryModel.updateQuantity(product, quantity);

    }


    public void checkout(String paymentMethod){
        List<String> listOfProductsBought = new ArrayList<String>();
        

        // adds it to user's list of purchases
        for (Product product : user.getCart().keySet()){
            user.purchaseProduct(product, user.getCart().get(product));
            
            //add to seller's transaction view         
            this.sellerTransactions.add(new Transaction(product.getCode(), product.getName(), user.getCart().get(product)));
            System.out.println(this.sellerTransactions.get(0).getQuantitySold());
            
            listOfProductsBought.add(product.getName());
            
           
        }
        String productsString = String.join(", ", listOfProductsBought);
        //add to cashier's transaction view
        if (paymentMethod.equals("cash")){
            this.cashierTransactions.add(new Transaction(LocalDateTime.now(), productsString, this.cashPaymentModel.getMoneyPaid(), this.cashPaymentModel.getReturnedChange(), paymentMethod));
        }
        else{
           this.cashierTransactions.add(new Transaction(LocalDateTime.now(), productsString, 0, 0, paymentMethod));
        }

        user.clearCart();

        // update users file
        jsonParser.updateUsers(loginModel.getUsers());

        // update inventory file
        inventoryModel.updateInventory();

        //update seller transaction list string
        updateSellerTransactionString();
        System.out.println(this.sellerTransactionString);

        //update seller transaction list string
        updateCashierTransactionString();

        //write seller transactions to file
        csvFileParser.writeSellerTransactions(sellerTransactionString);

        //write cashier transactions to file
        csvFileParser.writeCashierTransactions(cashierTransactionString);

        logout();

    }

    public InventoryModel getInventoryModel(){
        return this.inventoryModel;
    }

    public double getCartPrice() {

        double sum = 0;

        for (Product p : user.getCart().keySet()){
            sum += p.getPrice() * user.getCart().get(p); // price * quantity
        }

        return sum;
    }

    public JsonParser getJsonParser(){
        return this.jsonParser;
    }

    public void updateSellerTransactionString(){
        //this.sellerTransactionString.clear();
        System.out.println(this.sellerTransactionString);

        for (Transaction t: this.sellerTransactions){
            boolean found = false;
            for (List<String> s: this.sellerTransactionString){
                if (t.getItemName().equals(s.get(1))){
                    found = true;
                    s.set(2, String.valueOf((Integer.parseInt(s.get(2)) + t.getQuantitySold())));
                    break;
                }
            }
            if (found == false){
                this.sellerTransactionString.add(List.of(String.valueOf(t.getItemCode()), t.getItemName(), String.valueOf(t.getQuantitySold())));
            }
            
        }
        this.sellerTransactions.clear();
    }

    public void updateCashierTransactionString(){

        for(Transaction t: this.cashierTransactions){
            this.cashierTransactionString.add(List.of(String.valueOf(t.getTransactionDate()), t.getItemName(), String.valueOf(t.getMoneyPaid()), String.valueOf(t.getReturnedChange()), t.getPaymentMethod()));
        }
        this.cashierTransactions.clear();
    }

    public List<List<String>> getSellerTransactionAsString(){
        return this.sellerTransactionString;
    }

    public List<List<String>> getCashierTransactionAsString(){
        return this.cashierTransactionString;
    }

    public CSVFileParser getCsvFileParser(){
        return this.csvFileParser;
    }

////    public UserManagementModel getUserManagementModel(){
//        return this.userManagementModel;
//    }



    public static void main(String[] args){

//          String originalInventoryPath = "src/test/resources/original_test_inventory.json";
//    String inventoryPath1 = "src/test/resources/test_inventory.json";
//    String inventoryPath2 = "src/test/resources/test_inventory3.json";
//    String inventoryPath3 = "src/test/resources/test_inventory4.json";


//  String testInventoryCSVPath = "src/test/resources/test_inventory.csv";
//    String testInventoryCSVPath2 = "src/test/resources/test_inventory2.csv";
//     String testSellerTransactionCSVPath = "src/test/resources/seller_transaction.csv";
//     String testCashierTransactionCSVPath = "src/test/resources/cashier_transaction.csv";
//         MainModel mainModel = new MainModel(inventoryPath2, "src/test/resources/test_users3.json", "src/test/resources/InitialCash.json", "src/test/resources/credit_cards.json", testInventoryCSVPath2, testSellerTransactionCSVPath, testCashierTransactionCSVPath);
//         InventoryModel inventoryModel = mainModel.getInventoryModel();
//         List<Product> defaultInventory = mainModel.getJsonParser().getInventory();
//         inventoryModel.initializeProductsToString();

//         // login user 1
//         mainModel.setUser(mainModel.getLoginModel().login("test1", "pw"));

//         //add 2 of each items to cart
//         for (Product p : inventoryModel.getInventory()){
//             mainModel.addToCart(p, 2);
//         }

//         int cartSize = mainModel.getUser().getCart().size();

//         //complete transaction and write to file 
//         mainModel.checkout("card");

//         List<List<String>> sellerTransactions = mainModel.getSellerTransactionAsString();
//         List<List<String>> cashierTransactions = mainModel.getCashierTransactionAsString();
        
//         System.out.println(sellerTransactions);
//         System.out.println(cashierTransactions);



        // assertEquals(cartSize, itemsPurchased.size());

        // //put into maps for both
        // HashMap<String, Integer> ip = new HashMap<String, Integer>();

        // for (int i=0; i<itemsPurchased.size(); i++){
        //     List<String> item = itemsPurchased.get(i);
        //     ip.put(item.get(1), Integer.parseInt(item.get(2)));
        // }

        // HashMap<String, Integer> ap = new HashMap<String, Integer>();

        // for (List<String> t: mainModel.getSellerTransactionAsString()){
        //     ap.put(t.get(1), Integer.parseInt(t.get(2)));
        // }

        // assertEquals(ip, ap);
    }
}
