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

    //default
    JsonParser jp = new JsonParser();
    private List<Product> defaultInventory = jp.getInventory(inventoryPath);

    @AfterEach
    public void writeToDefault(){
        jp.updateInventory(defaultInventory, inventoryPath);
    }

    //test for reading and writing inventory to CSV File
    @Test
    public void ReadAndWriteInventoryTest(){
        InventoryModel inventoryModel = new InventoryModel(inventoryPath);
        List<Product> inventory = inventoryModel.getInventory();

        //write to csv file
        inventoryModel.writeInventoryToFile(testInventoryCSVPath);

        //read from file
        List<String[]> inventoryRead = inventoryModel.readInventoryFromFile(testInventoryCSVPath);

        //check for equivalent length
        // assertEquals(inventory.size(), inventoryRead.size());
        System.out.println(inventory.size());
        System.out.println(inventoryRead.size());

        //check if the content is equivalent

        for (int i=0; i < inventory.size(); i++){
            String[] productString = inventoryRead.get(i);
            assertEquals(5, productString.length);
            
            //name
            assertEquals(inventory.get(i).getName(), productString[0]);

            //code
            assertEquals(String.valueOf(inventory.get(i).getCode()), productString[1]);

            //category
            assertEquals(inventory.get(i).getCategory(), productString[2]);

            //price
            assertEquals(String.valueOf(inventory.get(i).getPrice()), productString[3]);

            //quantity
            assertEquals(String.valueOf(inventory.get(i).getQuantity()), productString[4]);

        }
        


    }
}
