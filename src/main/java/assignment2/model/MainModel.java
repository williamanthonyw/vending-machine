package assignment2.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class MainModel {

    private LastFiveProductsModel lastFiveProductsModel;
    private CardPaymentModel cardPaymentModel;
    private LoginModel loginModel;
    private CashPaymentModel cashPaymentModel;
    private ProductOptionsModel productOptionsModel;

    private User user;
    private boolean isLoggedIn;

    private JsonParser jsonParser = new JsonParser();

    public MainModel(){
        this.lastFiveProductsModel = new LastFiveProductsModel();
        this.cardPaymentModel = new CardPaymentModel(this);

        this.productOptionsModel = new ProductOptionsModel();   ////////

        this.loginModel = new LoginModel(jsonParser.getUsers("src/test/resources/test_users.json"));
//                "src/main/resources/users.json"));

        this.user = loginModel.getAnonymousUser();
        this.isLoggedIn = false;

        this.cashPaymentModel = new CashPaymentModel("src/test/resources/initialCash.json");

        purchaseProduct(new Product("milk", 28), 2);

        // for now logging in
//        this.user = loginModel.login("Kylie", "password");
//        this.isLoggedIn = true;
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
        this.isLoggedIn = false;
    }

    public void cancelTransaction(){

        // clear cart (look at Katie's stuff)

        // log user out
        logout();

    }

    public void purchaseProduct(Product product, int quantity){

        // handle payment stuff

        // adds it to user's list of purchases
        user.purchaseProduct(product, quantity);

        // update users file
        jsonParser.updateUsers(loginModel.getUsers(), "src/main/resources/users.json");

        // need to update inventory file as well



    }

    public ProductOptionsModel getProductOptionsModel(){
        return this.productOptionsModel;
    }

}
