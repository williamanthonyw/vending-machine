package assignment2.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    private String inventoryFile;
    private String usersFile;
    private String initialCashFile;
    private String cardFile;

    private User user;
    private boolean isLoggedIn;

    private HashMap<Product, Integer> aggregatePurchases;
    private JsonParser jsonParser;

    public MainModel(String inventoryFile, String usersFile, String initialCashFile, String cardFile){

        this.jsonParser = new JsonParser(inventoryFile, usersFile, initialCashFile, cardFile);

        this.loginModel = new LoginModel(jsonParser.getUsers());
        this.user = loginModel.getAnonymousUser();

        if (this.user != null){
            this.user.clearCart();
        }

        this.isLoggedIn = false;


        this.aggregatePurchases = new HashMap<Product, Integer>();
        this.inventoryFile = inventoryFile;
        this.usersFile = usersFile;
        this.initialCashFile = initialCashFile;
        this.cardFile = cardFile;

        this.lastFiveProductsModel = new LastFiveProductsModel();
        this.cardPaymentModel = new CardPaymentModel(this, jsonParser );
        this.cashPaymentModel = new CashPaymentModel(jsonParser.getCash(), jsonParser);
        this.inventoryModel = new InventoryModel(jsonParser.getInventory(), jsonParser);

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

    public List<List<String>> readPurchasesFromFile(String filename){
        List<List<String>> items = new ArrayList<List<String>>();
        File file = new File(filename);
        String[] item;

        try{
            CSVReader reader = new CSVReader(new FileReader(file));

            while((item = reader.readNext()) != null){
                items.add(Arrays.asList(item));
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
        writePurchasesToFile(this.aggregatePurchases, "src/test/resources/transaction.csv");

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

    public static void main(String[] args){
    // String testInventoryCSVPath = "src/test/resources/test_inventory.csv";
    // String inventoryPath = "src/test/resources/test_inventory.json";

    // String testTransactionCSVPath = "src/test/resources/transaction.csv";


    // JsonParser jp = new JsonParser(inventoryPath, "src/test/resources/test_users3.json", "src/test/resources/InitialCash.json", "src/test/resources/credit_cards.json");
    // List<Product> defaultInventory = jp.getInventory();

    // InventoryModel im = new InventoryModel(jp.getInventory(), jp);
    // MainModel mm = new MainModel(inventoryPath, "src/test/resources/test_users3.json", "src/test/resources/InitialCash.json", "src/test/resources/credit_cards.json");

    // // for (Product p: im.getInventory()){
    // //     System.out.println(p.getCategory() + p.getCode() + p.getName() + p.getPrice() + p.getQuantity());
    // // }
    // // mm.setUser(mm.getLoginModel().login("test1", "pw"));
    // // System.out.println(mm.getUser());
    
    // // for (Product p: im.getInventory()){
    // //     mm.addToCart(p, 2);
    // // }

    // // mm.checkout();

    // // String transTemp = "";
    // // List<List<String>> transactions = mm.readPurchasesFromFile(testTransactionCSVPath);

    // // System.out.println(transactions);

    // //     for (List<String> s : transactions){
    // //             String temp2 = String.join(", ", s);
    // //             temp2 = temp2.concat("\n");
        
    // //             transTemp = transTemp.concat(temp2);
    // //     }
    // // System.out.println(transTemp);

    // im.writeInventoryToFile(testInventoryCSVPath);

    //     //read inventory from file 
    // String invTemp = "";
    // List<List<String>> inventoryItems = im.readInventoryFromFile(testInventoryCSVPath);

    // for (List<String> s : inventoryItems){
    //     String temp2 = String.join(", ", s).stripTrailing();
    //     temp2 = temp2.concat("\n");
    //     invTemp = invTemp.concat(temp2);
    // }

    // System.out.println(invTemp);
    // jp.updateInventory(defaultInventory);
    }
}
