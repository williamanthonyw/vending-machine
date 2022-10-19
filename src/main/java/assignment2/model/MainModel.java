package assignment2.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class MainModel {

    private LastFiveProductsModel lastFiveProductsModel;
    private LoginModel loginModel;
    private ProductOptionsModel productOptionsModel;  ////

    private User user;
    private boolean isLoggedIn;

    private JsonParser jsonParser = new JsonParser();

    public MainModel(){
        this.lastFiveProductsModel = new LastFiveProductsModel();
        this.productOptionsModel = new ProductOptionsModel();   ////////

        this.loginModel = new LoginModel(jsonParser.getUsers("src/main/resources/users.json"));

        this.user = loginModel.getAnonymousUser();
        this.isLoggedIn = false;


        purchaseProduct(new Product("milk", 28), 2);

        // for now logging in
//        this.user = loginModel.login("Kylie", "password");
//        this.isLoggedIn = true;
    }

    public LastFiveProductsModel getLastFiveProductsModel(){
        return this.lastFiveProductsModel;
    }

    public boolean isLoggedIn(){
        return isLoggedIn;
    }

    public User getUser(){
        return this.user;
    }

    public void purchaseProduct(Product product, int quantity){

        // handle payment stuff

        // adds it to user's list of purchases
        user.purchaseProduct(product, quantity);

        // update users file
        jsonParser.updateUsers(loginModel.getUsers(), "src/main/resources/users.json");

        // need to update inventory file as well   //////////TODO



    }

    public ProductOptionsModel getProductOptionsModel(){
        return this.productOptionsModel;
    }

}
