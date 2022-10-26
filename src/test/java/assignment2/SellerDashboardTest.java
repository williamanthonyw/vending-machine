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

    //default
    private List<Product> defaultInventory = JsonParser.getInventory(inventoryPath);

    @AfterEach
    public void writeToDefault(){
        JsonParser.updateInventory(defaultInventory, inventoryPath);
    }

    //test for reading and writing inventory to CSV File
    @Test
    public void ReadAndWriteInventoryTest(){
        InventoryModel inventoryModel = new InventoryModel(inventoryPath);
        List<Product> inventory = inventoryModel.getInventory();

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
    public void ReadAndWriteTransactionTest(){
        InventoryModel inventoryModel = new InventoryModel(inventoryPath);
        List<Product> inventory = inventoryModel.getInventory();

        MainModel mainModel = new MainModel();

        //add 2 of each items to cart
        for (Product p : inventory){
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


}
