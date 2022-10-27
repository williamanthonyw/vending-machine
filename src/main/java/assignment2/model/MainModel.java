package assignment2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class MainModel {

    private LastFiveProductsModel lastFiveProductsModel;
    private CardPaymentModel cardPaymentModel;
    private LoginModel loginModel;
    private CashPaymentModel cashPaymentModel;
    private InventoryModel inventoryModel;
    private UserManagementModel userManagementModel;

    private String inventoryFile;
    private String usersFile;
    private String initialCashFile;
    private String cardFile;

    private User user;
    private boolean isLoggedIn;

    private JsonParser jsonParser;

    public MainModel(String inventoryFile, String usersFile, String initialCashFile, String cardFile){

        this.jsonParser = new JsonParser(inventoryFile, usersFile, initialCashFile, cardFile);

        this.loginModel = new LoginModel(jsonParser.getUsers());
        this.user = loginModel.getAnonymousUser();

        if (this.user != null){
            this.user.clearCart();
        }

        this.isLoggedIn = false;

        this.inventoryFile = inventoryFile;
        this.usersFile = usersFile;
        this.initialCashFile = initialCashFile;
        this.cardFile = cardFile;

        this.lastFiveProductsModel = new LastFiveProductsModel();
        this.cardPaymentModel = new CardPaymentModel(this, jsonParser );
        this.cashPaymentModel = new CashPaymentModel(jsonParser.getCash(), jsonParser);
        this.inventoryModel = new InventoryModel(jsonParser.getInventory(), jsonParser);
        this.userManagementModel = new UserManagementModel(jsonParser.getUsers(), jsonParser);

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

    public User getUser(){
        return this.user;
    }

    public void logout(){
        this.user = loginModel.getAnonymousUser();
        this.user.clearCart();
        this.isLoggedIn = false;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Map<Product, Integer> getCart(){
        return user.getCart();
    }

    public void cancelTransaction(){

        // clear cart (look at Katie's stuff)
        inventoryModel.putBack(user.getCart());
        user.clearCart();


        // log user out
        logout();

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
        }

        user.clearCart();

        // update users file
        jsonParser.updateUsers(loginModel.getUsers());

        // update inventory file
        inventoryModel.updateInventory();

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

    public UserManagementModel getUserManagementModel(){
        return this.userManagementModel;
    }
}
