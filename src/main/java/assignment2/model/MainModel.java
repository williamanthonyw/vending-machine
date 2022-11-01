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
    private CsvParser csvParser;

    private List<CancelledTransaction> cancelledTransactions;


    public MainModel(JsonParser jsonParser, CsvParser csvParser){

        this.jsonParser = jsonParser;
        this.csvParser = csvParser;

        this.loginModel = new LoginModel(jsonParser.getUsers(),this.getJsonParser());
        this.user = loginModel.getAnonymousUser();

        if (this.user != null){
            this.user.clearCart();
        }

        this.isLoggedIn = false;

        this.lastFiveProductsModel = new LastFiveProductsModel();
        this.cardPaymentModel = new CardPaymentModel(this, jsonParser );
        this.cashPaymentModel = new CashPaymentModel(jsonParser.getCash(), jsonParser);
        this.inventoryModel = new InventoryModel(jsonParser.getInventory(), jsonParser);

        this.aggregatePurchases = new HashMap<Product, Integer>();

        this.cancelledTransactions = csvParser.getCancelledTransactions();

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
        csvParser.updateCancelledTransactions(cancelledTransactions);

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

    public void checkout(){

        // adds it to user's list of purchases
        for (Product product : user.getCart().keySet()){
            user.purchaseProduct(product, user.getCart().get(product));

            if (aggregatePurchases.get(product) == null){
                this.aggregatePurchases.put(product, 0);
            }

            this.aggregatePurchases.put(product, user.getCart().get(product) + this.aggregatePurchases.get(product));

        }

        user.clearCart();

        // update users file
        jsonParser.updateUsers(loginModel.getUsers());

        // update inventory file
        inventoryModel.updateInventory();

        //write purchases to file
        csvParser.writePurchasesToFile(this.aggregatePurchases);

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

////    public UserManagementModel getUserManagementModel(){
//        return this.userManagementModel;
//    }
}
