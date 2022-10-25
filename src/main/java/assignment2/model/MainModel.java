package assignment2.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
    private ProductOptionsModel productOptionsModel;

    private User user;
    private boolean isLoggedIn;

    private JsonParser jsonParser = new JsonParser();
    private HashMap<Product, Integer> aggregatePurchases;

    public MainModel(){
        this.lastFiveProductsModel = new LastFiveProductsModel();
        this.cardPaymentModel = new CardPaymentModel(this);

        this.productOptionsModel = new ProductOptionsModel();   ////////

        this.loginModel = new LoginModel(jsonParser.getUsers("src/main/resources/users.json"));

        System.out.println(loginModel.getUsers());

        this.user = loginModel.getAnonymousUser();
        this.user.clearCart();
        this.isLoggedIn = false;

        this.cashPaymentModel = new CashPaymentModel("src/main/resources/cash.json");

        this.aggregatePurchases = new HashMap<Product, Integer>();

//        purchaseProduct(new Product("milk", 28), 2);

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
        this.user.clearCart();
        this.isLoggedIn = false;
    }

    public Map<Product, Integer> getCart(){
        return user.getCart();
    }

    public Map<Product, Integer> getAggregatePurchases(){
        return this.aggregatePurchases;
    }

    public void cancelTransaction(){

        // clear cart (look at Katie's stuff)
        productOptionsModel.putBack(user.getCart());
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
        productOptionsModel.updateQuantity(product, quantity);

        System.out.println(user.getCart());
    }

    public void writePurchasesToFile(HashMap<Product, Integer> itemsPurchased, String filename){
        File file = new File(filename);

        try{
            List<String[]> items = new ArrayList<String[]>();
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            
            for (Product p: itemsPurchased.keySet()){
                items.add(new String[] {String.valueOf(p.getCode()), p.getName(), String.valueOf(itemsPurchased.get(p))});
            }
            writer.writeAll(items);
            writer.close();

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public List<String[]> readPurchasesFromFile(String filename){
        List<String[]> items = new ArrayList<String[]>();
        File file = new File(filename);

        try{
            CSVReader reader = new CSVReader(new FileReader(file));

            while(reader.readNext() != null){
                String[] item = reader.readNext();
                items.add(item);
            }

            reader.close();
        }

        catch(IOException e){
            e.printStackTrace();
        }
        catch(CsvValidationException c){
            c.printStackTrace();
        }

        return items;
    }


    public void checkout(){

        // adds it to user's list of purchases
        for (Product product : user.getCart().keySet()){
            user.purchaseProduct(product, user.getCart().get(product));
            this.aggregatePurchases.put(product, user.getCart().get(product) + aggregatePurchases.get(product));
        }

        user.clearCart();

        // update users file
        jsonParser.updateUsers(loginModel.getUsers(), "src/main/resources/users.json");

        // update inventory file
        productOptionsModel.updateInventory();

        //write purchases to file
        writePurchasesToFile(this.aggregatePurchases, "src/main/resources/transaction.csv");

        logout();

    }

    public ProductOptionsModel getProductOptionsModel(){
        return this.productOptionsModel;
    }

    public double getCartPrice() {

        double sum = 0;

        for (Product p : user.getCart().keySet()){
            sum += p.getPrice() * user.getCart().get(p); // price * quantity
        }

        return sum;
    }
}
