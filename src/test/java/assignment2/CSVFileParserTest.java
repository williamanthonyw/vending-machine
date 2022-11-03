package assignment2;

import assignment2.model.CSVFileParser;
import assignment2.model.CancellationReason;
import assignment2.model.CancelledTransaction;
import assignment2.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CSVFileParserTest {

    private CSVFileParser csvFileParser;

    private static String inventoryPath1 = "src/test/resources/test_inventory2.json";
    private static String testInventoryCSVPath = "src/test/resources/test_inventory1.csv"; 
    private static String testSellerTransactionCSVPath = "src/test/resources/seller_transaction1.csv";

    private static String testCashierTransactionCSVPath = "src/test/resources/cashier_transaction.csv";

    @Test
    public void updateAndGetCancelledTransaction(){

        csvFileParser = new CSVFileParser("", "", "", "src/test/resources/cancelledTransactions.csv","","");

        CancelledTransaction cancelledTransaction1 = new CancelledTransaction("test", CancellationReason.TIMEOUT, LocalDateTime.of(2022, 1, 12, 23, 11));
        CancelledTransaction cancelledTransaction2 = new CancelledTransaction("test2", CancellationReason.USER_CANCELLATION, LocalDateTime.of(2022, 12, 12, 23, 11));
        CancelledTransaction cancelledTransaction3 = new CancelledTransaction("test3", CancellationReason.CHANGE_NOT_AVAILABLE, LocalDateTime.of(2023, 12, 12, 23, 11));

        List<CancelledTransaction> cancelledTransactionList = Arrays.asList(new CancelledTransaction[] {cancelledTransaction1, cancelledTransaction2, cancelledTransaction3});

        csvFileParser.updateCancelledTransactions(cancelledTransactionList);

        List<CancelledTransaction> out = csvFileParser.getCancelledTransactions();

        assertEquals(out.size(), 3);

        CancelledTransaction out1 = out.get(0);
        assertEquals(out1.getUsername(), cancelledTransaction1.getUsername());
        assertEquals(out1.getCancellationReason(), cancelledTransaction1.getCancellationReason());
        assertEquals(out1.getTimeCancelled(), cancelledTransaction1.getTimeCancelled());

        CancelledTransaction out2 = out.get(1);
        assertEquals(out2.getUsername(), cancelledTransaction2.getUsername());
        assertEquals(out2.getCancellationReason(), cancelledTransaction2.getCancellationReason());
        assertEquals(out2.getTimeCancelled(), cancelledTransaction2.getTimeCancelled());

        CancelledTransaction out3 = out.get(2);
        assertEquals(out3.getUsername(), cancelledTransaction3.getUsername());
        assertEquals(out3.getCancellationReason(), cancelledTransaction3.getCancellationReason());
        assertEquals(out3.getTimeCancelled(), cancelledTransaction3.getTimeCancelled());

    }

    @Test
    public void getCancelledTransactionsEmpty(){

        csvFileParser = new CSVFileParser("", "", "", "","","");

        assertNotNull(csvFileParser.getCancelledTransactions());
        assertEquals(csvFileParser.getCancelledTransactions().size(), 0);

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
        
    }

    //test for reading and writing to seller transactions   
    @Test
    public void readAndWriteSellerTransactionTest(){
        JsonParser jp = new JsonParser(inventoryPath1, "src/test/resources/test_users3.json", "src/test/resources/InitialCash.json", "src/test/resources/credit_cards.json");
        CSVFileParser csvFileParser = new CSVFileParser(testInventoryCSVPath, testSellerTransactionCSVPath, "", "", "", "src/test/resources/cash_available1.java");
        List<Product> inventory = jp.getInventory();
        
        MainModel mainModel = new MainModel(jp, csvFileParser);

        mainModel.setUser(mainModel.getLoginModel().login("test1", "pw"));

        for (Product p: mainModel.getInventoryModel().getInventory()){
            mainModel.getUser().getCart().put(p, 2);
        }


        assertEquals(mainModel.getCsvFileParser().readInventoryFromFile().size() ,mainModel.getUser().getCart().size());

        int cartSize = mainModel.getUser().getCart().size();
        mainModel.checkout("card");

        List<List<String>> sellerTransactions = mainModel.getCsvFileParser().readSellerTransactions();

        assertEquals(sellerTransactions, mainModel.getSellerTransactionAsString());

        assertEquals(cartSize, sellerTransactions.size());

        for(List<String> s: sellerTransactions){
            assertEquals(2, Integer.parseInt(s.get(2)));
        }



        //reset
        List<List<String>> emptyList = new ArrayList<List<String>>();
        mainModel.getCsvFileParser().writeSellerTransactions(emptyList);
        jp.updateInventory(inventory);
    }

    //test for reading and writing to cashier transactions
    @Test
    public void readAndWriteCashierTransactionTest(){
        JsonParser jp = new JsonParser(inventoryPath1, "src/test/resources/test_users3.json", "src/test/resources/InitialCash.json", "src/test/resources/credit_cards.json");
        CSVFileParser csvFileParser = new CSVFileParser(testInventoryCSVPath, "", testCashierTransactionCSVPath, "", "", "src/test/resources/cash_available1.java");
        List<Product> inventory = jp.getInventory();
        
        MainModel mainModel = new MainModel(jp, csvFileParser);

        mainModel.setUser(mainModel.getLoginModel().login("test2", "pw"));

        for (Product p: mainModel.getInventoryModel().getInventory()){
            mainModel.getUser().getCart().put(p, 2);
        }


        assertEquals(mainModel.getCsvFileParser().readInventoryFromFile().size() ,mainModel.getUser().getCart().size());

        int cartSize = mainModel.getUser().getCart().size();
        mainModel.checkout("card");

        List<List<String>> cashierTransactions = mainModel.getCsvFileParser().readCashierTransactions();

        assertEquals(cashierTransactions, mainModel.getCashierTransactionAsString());

        assertEquals(1, cashierTransactions.size());

        for(List<String> s: cashierTransactions){
            assertEquals(5, s.size());
        }



        //reset
        List<List<String>> emptyList = new ArrayList<List<String>>();
        mainModel.getCsvFileParser().writeCashierTransactions(emptyList);
        jp.updateInventory(inventory);

    }





}
