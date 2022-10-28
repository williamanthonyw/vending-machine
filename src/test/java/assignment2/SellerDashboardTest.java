package assignment2;

import assignment2.model.*;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class SellerDashboardTest {
    private static String testInventoryCSVPath = "src/test/resources/test_inventory.csv";
    private static String inventoryPath = "src/test/resources/test_inventory.json";

    private static String testTransactionCSVPath = "src/test/resources/transaction.csv";

    JsonParser jp = new JsonParser(inventoryPath, "src/test/resources/test_users3.json", "src/test/resources/InitialCash.json", "src/test/resources/credit_cards.json");

    //default
    private List<Product> defaultInventory = jp.getInventory();

    @AfterEach
    public void writeToDefault(){
        jp.updateInventory(defaultInventory);
    }

    //test for reading and writing inventory to CSV File
    @Test
    public void readAndWriteInventoryTest(){
        List<Product> inventory = jp.getInventory();
        InventoryModel inventoryModel = new InventoryModel(inventory, jp);
        

        //write to csv file
        inventoryModel.writeInventoryToFile(testInventoryCSVPath);

        //read from file
        List<List<String>> inventoryRead = inventoryModel.readInventoryFromFile(testInventoryCSVPath);

        //check for equivalent length
        assertEquals(inventory.size(), inventoryRead.size());

        //check if the content is equivalent

        for (int i=0; i < inventory.size(); i++){
            List<String> productString = inventoryRead.get(i);
            assertEquals(5, productString.size());
            
            //name
            assertEquals(inventory.get(i).getName(), productString.get(0));

            //code
            assertEquals(String.valueOf(inventory.get(i).getCode()), productString.get(1));

            //category
            assertEquals(inventory.get(i).getCategory(), productString.get(2));

            //price
            assertEquals(String.valueOf(inventory.get(i).getPrice()), productString.get(3));

            //quantity
            assertEquals(String.valueOf(inventory.get(i).getQuantity()), productString.get(4));

        }
        
    }

    //test for reading and writing transactions to CSV file
    @Test
    public void readAndWriteTransactionTest(){
        List<Product> inventory = jp.getInventory();
        InventoryModel inventoryModel = new InventoryModel(inventory, jp);

        MainModel mainModel = new MainModel(inventoryPath, "src/test/resources/test_users3.json", "src/test/resources/InitialCash.json", "src/test/resources/credit_cards.json", "src/test/resources/transaction.csv");

        // login user 1
        mainModel.setUser(mainModel.getLoginModel().login("test1", "pw"));

        //add 2 of each items to cart
        for (Product p : inventoryModel.getInventory()){
            mainModel.addToCart(p, 2);
        }

        int cartSize = mainModel.getUser().getCart().size();

        //complete transaction and write to file 
        mainModel.checkout();

        List<List<String>> itemsPurchased = mainModel.readPurchasesFromFile(testTransactionCSVPath);

        assertEquals(cartSize, itemsPurchased.size());

        //put into maps for both
        HashMap<String, Integer> ip = new HashMap<String, Integer>();

        for (int i=0; i<itemsPurchased.size(); i++){
            List<String> item = itemsPurchased.get(i);
            ip.put(item.get(1), Integer.parseInt(item.get(2)));
        }

        HashMap<String, Integer> ap = new HashMap<String, Integer>();

        for (Product p: mainModel.getAggregatePurchases().keySet()){
            ap.put(p.getName(), mainModel.getAggregatePurchases().get(p));
        }

        assertEquals(ip, ap);

    }

    //multiple transactions
    @Test
    public void multipleTransactionsTest(){
        List<Product> inventory = jp.getInventory();
        InventoryModel inventoryModel = new InventoryModel(inventory, jp);

        MainModel mainModel = new MainModel(inventoryPath, "src/test/resources/test_users3.json", "src/test/resources/InitialCash.json", "src/test/resources/credit_cards.json", "src/test/resources/transaction.csv");

        //login user 1
        mainModel.setUser(mainModel.getLoginModel().login("test1", "pw"));

        //add 2 of each items to cart -> User 1
        for (Product p : inventoryModel.getInventory()){
            mainModel.addToCart(p, 2);
        }

        int cartSizeUser1 = mainModel.getUser().getCart().size();


        //complete transaction and write to file 
        mainModel.checkout();

        List<List<String>> itemsPurchasedUser1 = mainModel.readPurchasesFromFile(testTransactionCSVPath);
        
        assertEquals(cartSizeUser1, itemsPurchasedUser1.size());

        //put into maps for both
        HashMap<String, Integer> ip1 = new HashMap<String, Integer>();

        for (int i=0; i<itemsPurchasedUser1.size(); i++){
            List<String> item = itemsPurchasedUser1.get(i);
            ip1.put(item.get(1), Integer.parseInt(item.get(2)));
        }

        HashMap<String, Integer> ap1 = new HashMap<String, Integer>();

        for (Product p: mainModel.getAggregatePurchases().keySet()){
            ap1.put(p.getName(), mainModel.getAggregatePurchases().get(p));
        }

        assertEquals(ip1, ap1);

        //login user 2
        mainModel.setUser(mainModel.getLoginModel().login("test2", "pw"));

        //add 3 of each items to cart -> User 2
        for (Product p : inventory){
            mainModel.addToCart(p, 3);
        }

        int cartSizeUser2 = mainModel.getUser().getCart().size();

        mainModel.checkout();

        List<List<String>> itemsPurchasedUser2 = mainModel.readPurchasesFromFile(testTransactionCSVPath);

        assertEquals(cartSizeUser2, itemsPurchasedUser2.size());

        //put into maps for both
        HashMap<String, Integer> ip2 = new HashMap<String, Integer>();

        for (int i=0; i<itemsPurchasedUser2.size(); i++){
            List<String> item = itemsPurchasedUser2.get(i);
            ip2.put(item.get(1), Integer.parseInt(item.get(2)));
        }

        HashMap<String, Integer> ap2 = new HashMap<String, Integer>();

        for (Product p: mainModel.getAggregatePurchases().keySet()){
            ap2.put(p.getName(), mainModel.getAggregatePurchases().get(p));
        }

        assertEquals(ip2, ap2);

        //login user 3
        mainModel.setUser(mainModel.getLoginModel().login("test3", "pw"));

        //add 5 of each items to cart -> User 3
        for (Product p : inventory){
            mainModel.addToCart(p, 5);
        }

        int cartSizeUser3 = mainModel.getUser().getCart().size();

        mainModel.checkout();

        List<List<String>> itemsPurchasedUser3 = mainModel.readPurchasesFromFile(testTransactionCSVPath);

        assertEquals(cartSizeUser3, itemsPurchasedUser3.size());

        //put into maps for both
        HashMap<String, Integer> ip3 = new HashMap<String, Integer>();

        for (int i=0; i<itemsPurchasedUser3.size(); i++){
            List<String> item = itemsPurchasedUser3.get(i);
            ip3.put(item.get(1), Integer.parseInt(item.get(2)));
        }

        HashMap<String, Integer> ap3 = new HashMap<String, Integer>();

        for (Product p: mainModel.getAggregatePurchases().keySet()){
            ap3.put(p.getName(), mainModel.getAggregatePurchases().get(p));
        }

        assertEquals(ip3, ap3);
    }


}
