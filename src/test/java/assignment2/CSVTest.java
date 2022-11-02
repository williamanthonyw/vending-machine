package assignment2;

import assignment2.model.*;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class CSVTest {
    private static String originalInventoryPath = "src/test/resources/original_test_inventory.json";
    private static String inventoryPath1 = "src/test/resources/test_inventory2.json";
    private static String inventoryPath2 = "src/test/resources/test_inventory3.json";
    private static String inventoryPath3 = "src/test/resources/test_inventory4.json";


    private static String testInventoryCSVPath = "src/test/resources/test_inventory1.csv";
    private static String testInventoryCSVPath2 = "src/test/resources/test_inventory2.csv";
    private static String testSellerTransactionCSVPath = "src/test/resources/seller_transaction1.csv";
    private static String testSellerTransactionCSVPath2 = "src/test/resources/seller_transaction2.csv";
    private static String testCashierTransactionCSVPath = "src/test/resources/cashier_transaction.csv";

    JsonParser origJP = new JsonParser(originalInventoryPath, inventoryPath3, inventoryPath2, inventoryPath1);
    List<Product> defaultInventory = origJP.getInventory();
    
    public void writeToDefault(JsonParser jp){
        jp.updateInventory(defaultInventory);
    }

    //test for reading and writing inventory to CSV File
    @Test
    public void readAndWriteInventoryTest(){
        JsonParser jp = new JsonParser(inventoryPath1, "src/test/resources/test_users3.json", "src/test/resources/InitialCash.json", "src/test/resources/credit_cards.json");
        List<Product> inventory = jp.getInventory();
        CSVFileParser csvFileParser = new CSVFileParser(testInventoryCSVPath, testSellerTransactionCSVPath);
        InventoryModel inventoryModel = new InventoryModel(inventory, jp, csvFileParser);
        inventoryModel.initializeProductsToString();
        
        //read from file
        List<List<String>> inventoryRead = inventoryModel.getInventoryAsString();

        //check for equivalent length
        assertEquals(inventoryModel.getInventory().size(), inventoryRead.size());

        //check if the content is equivalent

        for (int i=0; i < inventoryModel.getInventory().size(); i++){
            List<String> productString = inventoryRead.get(i);
            assertEquals(5, productString.size());
            
            //name
            assertEquals(inventoryModel.getInventory().get(i).getName(), productString.get(0));

            //code
            assertEquals(String.valueOf(inventoryModel.getInventory().get(i).getCode()), productString.get(1));

            //category
            assertEquals(inventoryModel.getInventory().get(i).getCategory(), productString.get(2));

            //price
            assertEquals(String.valueOf(inventoryModel.getInventory().get(i).getPrice()), productString.get(3));

            //quantity
            assertEquals(String.valueOf(inventoryModel.getInventory().get(i).getQuantity()), productString.get(4));

        }

        writeToDefault(jp);
        
    }

    //test for reading and writing seller transactions to CSV file
    @Test
    public void readAndWriteSellerTransactionTest(){
        JsonParser jp2 = new JsonParser(inventoryPath2, "src/test/resources/test_users3.json", "src/test/resources/InitialCash.json", "src/test/resources/credit_cards.json");
        CSVFileParser cv2 = new CSVFileParser(testInventoryCSVPath2, testSellerTransactionCSVPath, testCashierTransactionCSVPath, "");
        MainModel mainModel = new MainModel(jp2, cv2);
        InventoryModel inventoryModel = mainModel.getInventoryModel();
        inventoryModel.initializeProductsToString();
        List<Product> defaultInventory = mainModel.getJsonParser().getInventory();

        // login user 1
        mainModel.setUser(mainModel.getLoginModel().login("test1", "pw"));

        //add 2 of each items to cart
        for (Product p : inventoryModel.getInventory()){
            mainModel.addToCart(p, 2);
        }

        int cartSize = mainModel.getUser().getCart().size();
        int itemsBefore = mainModel.getSellerTransactionAsString().size();

        //complete transaction and write to file 
        mainModel.checkout("card");

        List<List<String>> itemsPurchased = mainModel.getSellerTransactionAsString();

        assertEquals(itemsBefore + cartSize, itemsPurchased.size());

        //put into maps for both
        HashMap<String, Integer> ip = new HashMap<String, Integer>();

        for (int i=0; i<itemsPurchased.size(); i++){
            List<String> item = itemsPurchased.get(i);
            ip.put(item.get(1), Integer.parseInt(item.get(2)));
        }

        HashMap<String, Integer> ap = new HashMap<String, Integer>();

        for (List<String> t: mainModel.getSellerTransactionAsString()){
            ap.put(t.get(1), Integer.parseInt(t.get(2)));
        }

        assertEquals(ip, ap);

        writeToDefault(mainModel.getJsonParser());

    }

    //multiple transactions seller
    @Test
    public void multipleSellerTransactionsTest(){

        JsonParser jp3 = new JsonParser(inventoryPath3, "src/test/resources/test_users3.json", "src/test/resources/InitialCash.json", "src/test/resources/credit_cards.json");
        CSVFileParser cv3 = new CSVFileParser(testInventoryCSVPath, testSellerTransactionCSVPath2, testCashierTransactionCSVPath, "");

        MainModel mainModel = new MainModel(jp3, cv3);
        InventoryModel inventoryModel = mainModel.getInventoryModel();
        inventoryModel.initializeProductsToString();

        //login user 1
        mainModel.setUser(mainModel.getLoginModel().login("test1", "pw"));

        //add 1 of each items to cart -> User 1
        for (Product p : inventoryModel.getInventory()){
            mainModel.addToCart(p, 1);
        }

        int cartSizeUser1 = mainModel.getUser().getCart().size();


        //complete transaction and write to file 
        mainModel.checkout("card");

        List<List<String>> itemsPurchasedUser1 = mainModel.getSellerTransactionAsString();
        
        assertEquals(cartSizeUser1, itemsPurchasedUser1.size());

        //put into maps for both
        HashMap<String, Integer> ip1 = new HashMap<String, Integer>();

        for (int i=0; i<itemsPurchasedUser1.size(); i++){
            List<String> item = itemsPurchasedUser1.get(i);
            ip1.put(item.get(1), Integer.parseInt(item.get(2)));
        }

        HashMap<String, Integer> ap1 = new HashMap<String, Integer>();

        for (List<String> t: mainModel.getSellerTransactionAsString()){
            ap1.put(t.get(1), Integer.parseInt(t.get(2)));
        }

        assertEquals(ip1, ap1);

        //login user 2
        mainModel.setUser(mainModel.getLoginModel().login("test2", "pw"));

        //add 2 of each items to cart -> User 2
        for (Product p : inventoryModel.getInventory()){
            mainModel.addToCart(p, 2);
        }

        int cartSizeUser2 = mainModel.getUser().getCart().size();

        mainModel.checkout("card");

        List<List<String>> itemsPurchasedUser2 = mainModel.getSellerTransactionAsString();

        assertEquals(cartSizeUser2 + cartSizeUser1, itemsPurchasedUser2.size());

        //put into maps for both
        HashMap<String, Integer> ip2 = new HashMap<String, Integer>();

        for (int i=0; i<itemsPurchasedUser2.size(); i++){
            List<String> item = itemsPurchasedUser2.get(i);
            ip2.put(item.get(1), Integer.parseInt(item.get(2)));
        }

        HashMap<String, Integer> ap2 = new HashMap<String, Integer>();

        for (List<String> t: mainModel.getSellerTransactionAsString()){
            ap2.put(t.get(1), Integer.parseInt(t.get(2)));
        }

        assertEquals(ip2, ap2);

        //login user 3
        mainModel.setUser(mainModel.getLoginModel().login("test3", "pw"));

        //add 3 of each items to cart -> User 3
        for (Product p : inventoryModel.getInventory()){
            mainModel.addToCart(p, 3);
        }

        int cartSizeUser3 = mainModel.getUser().getCart().size();

        mainModel.checkout("card");

        List<List<String>> itemsPurchasedUser3 = mainModel.getSellerTransactionAsString();

        assertEquals(cartSizeUser3 + cartSizeUser2 + cartSizeUser1, itemsPurchasedUser3.size());

        //put into maps for both
        HashMap<String, Integer> ip3 = new HashMap<String, Integer>();

        for (int i=0; i<itemsPurchasedUser3.size(); i++){
            List<String> item = itemsPurchasedUser3.get(i);
            ip3.put(item.get(1), Integer.parseInt(item.get(2)));
        }

        HashMap<String, Integer> ap3 = new HashMap<String, Integer>();

        for (List<String> t: mainModel.getSellerTransactionAsString()){
            ap3.put(t.get(1), Integer.parseInt(t.get(2)));
        }

        assertEquals(ip3, ap3);

        writeToDefault(mainModel.getJsonParser());
    }


}
