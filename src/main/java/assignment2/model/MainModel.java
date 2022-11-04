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

    public void cancelTransaction(CancellationReason cancellationReason, LocalDateTime timeCancelled){

        // clear cart (look at Katie's stuff)
        inventoryModel.putBack(user.getCart());
        user.clearCart();

        cancelledTransactions.add(new CancelledTransaction(user.getUsername(), cancellationReason, timeCancelled));
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

    public String getAvailableChangeAsString(){
        String result = "";
        List<List<String>> cashString = cashPaymentModel.getCashString();

        for(List<String> s: cashString){
            String temp = String.join(", ",s).stripTrailing();
            temp = temp.concat("\n");
            result = result.concat(temp);
        }
        return result;
        
    }

    public String getTransactionsAsString(){
         //read transactions done from file
         String transTemp = "";
 
        for (List<String> s : cashierTransactionString){
            String temp2 = String.join(", ", s).stripTrailing();
            temp2 = temp2.concat("\n");
        
            transTemp = transTemp.concat(temp2);
        }
        return transTemp;
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

    public String getCashierTransactionsAsString(){
        String transTemp = "";
        List<List<String>> cashierTransactions = getCsvFileParser().readCashierTransactions();
 
         if (cashierTransactions.size() == 0){
             transTemp = "No transactions available.";
         }
         else{
             for (List<String> s : cashierTransactions){
                 String temp2 = String.join(", ", s).stripTrailing();
                 temp2 = temp2.concat("\n");
             
                 transTemp = transTemp.concat(temp2);
             }
         }
        return transTemp;
    }


    public List<List<String>> getAvailableProducts(){
        return this.availableProducts;
    }

    public String getAvailableProductsAsString(){

        availableProducts = this.inventoryModel.getInventoryAsString();
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
        
        double total = 0;
        // adds it to user's list of purchases
        for (Product product : user.getCart().keySet()){
            user.purchaseProduct(product, user.getCart().get(product));
            total += product.getPrice() * user.getCart().get(product);
            
            //add to seller's transaction view         
            this.sellerTransactions.add(new Transaction(product.getCode(), product.getName(), user.getCart().get(product)));
            
            listOfProductsBought.add(product.getName());
            
           
        }
        String productsString = String.join(", ", listOfProductsBought);
        //add to cashier's transaction view
        if (paymentMethod.equals("cash")){
            this.cashierTransactions.add(new Transaction(LocalDateTime.now(), productsString, this.cashPaymentModel.getMoneyPaid(), this.cashPaymentModel.getReturnedChange(), paymentMethod));
        }
        else{
           this.cashierTransactions.add(new Transaction(LocalDateTime.now(), productsString, total, 0, paymentMethod));
        }

        user.clearCart();

        // update users file
        jsonParser.updateUsers(loginModel.getUsers());

        // update inventory file
        inventoryModel.updateInventory();

        //update seller transaction list string
        updateSellerTransactionString();

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

}
