package assignment2;

import assignment2.model.*;

import java.util.HashMap;
import java.util.List;

import javax.naming.InsufficientResourcesException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;

import static org.junit.jupiter.api.Assertions.*;



public class CashPaymentTest {

    //Test on initial amount of each notes and coins available
    @Test
    public void initialAmountCheck(){
        String initialPath = "src/main/resources/InitialCash.json";
        CashPaymentModel Test1 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test1.getCashList();

        for (Cash c: cashList){
            assertEquals(5, c.getAmount());
        }
    }

    //Test on values of each notes and coins
    @Test 
    public void cashValueCheck(){
        String initialPath = "src/main/resources/InitialCash.json";
        CashPaymentModel Test2 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test2.getCashList();

        for (Cash c: cashList){
            switch(c.getName()){
                case "5c":
                    assertEquals(0.05, c.getValue(), 0.01);  
                    break;
                case "10c":
                    assertEquals(0.10, c.getValue(), 0.01);  
                    break;
                case "20c":
                    assertEquals(0.20, c.getValue(), 0.01);  
                    break;
                case "50c":
                    assertEquals(0.50, c.getValue(), 0.01);  
                    break;
                case "$1":
                    assertEquals(1.00, c.getValue(), 0.01);  
                    break;
                case "$2":
                    assertEquals(2.00, c.getValue(), 0.01);  
                    break;
                case "$5":
                    assertEquals(5.00, c.getValue(), 0.01);  
                    break;
                case "$10":
                    assertEquals(10.00, c.getValue(), 0.01);  
                    break;
                case "$20":
                    assertEquals(20.00, c.getValue(), 0.01);  
                    break;
                case "$50":
                    assertEquals(50.00, c.getValue(), 0.01);  
                    break;
                case "$100":
                    assertEquals(100.00, c.getValue(), 0.01);  
                    break;
                
            }
        }

    }

    //Test on initial different notes and coins available: 11 different notes and coins
    @Test
    public void numberOfDifferentNotesandCoins(){
        String initialPath = "src/main/resources/InitialCash.json";
        CashPaymentModel Test3 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test3.getCashList();

        assertEquals(11, cashList.size());
    }

    //Test on payment where changes are in notes only
    @Test
    public void changeNotesOnly() throws InsufficientChangeException{
        String initialPath = "src/main/resources/test1cash.json";
        CashPaymentModel Test4 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test4.getCashList();
        double payment = 288.00;
        double price = 12.00;

        HashMap<String, Integer> change = Test4.calculateChange(payment, price);
        assertEquals(2, change.get("$100"));
        assertEquals(1, change.get("$50"));
        assertEquals(1, change.get("$20"));
        assertEquals(1, change.get("$5"));
        assertEquals(1, change.get("$1"));

        //reset json
        JsonParser jp = new JsonParser();
        jp.updateCash(cashList, initialPath);
    }

    //Test on payment where changes are in coins only
    @Test
    public void changeCoinsOnly() throws InsufficientChangeException{
        String initialPath = "src/main/resources/test2cash.json";
        CashPaymentModel Test5 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test5.getCashList();
        double payment = 0.50;
        double price = 0.15;

        HashMap<String, Integer> change = Test5.calculateChange(payment, price);
        assertEquals(1, change.get("20c"));
        assertEquals(1, change.get("10c"));
        assertEquals(1, change.get("5c"));

        //reset json
        JsonParser jp = new JsonParser();
        jp.updateCash(cashList, initialPath);

    }

    //Test on payment where the cash inventory is not sufficient
    @Test
    public void changeisNotSufficient() throws InsufficientChangeException{
        String initialPath = "src/main/resources/InitialCash.json";
        CashPaymentModel Test6 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test6.getCashList();
        double payment = 20000.00;
        double price = 12.00;

        assertThrows(InsufficientChangeException.class, () -> {Test6.calculateChange(payment, price);});
    }

    //Test on update cash inventory after a successful payment
    @Test
    public void updateChangeList() throws InsufficientChangeException{
        String initialPath = "src/main/resources/test3cash.json";
        CashPaymentModel Test7 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test7.getCashList();
        HashMap<String, Integer> cashMap = Test7.getCashMap(cashList);
        double payment = 139.00;
        double price = 122.45; //16.55

        HashMap<String, Integer> change = Test7.calculateChange(payment, price);
        
        for(Cash c: cashList){
            switch(c.getName()){
                case "$10":
                    assertEquals(cashMap.get("$10") - change.get("$10"), c.getAmount());
                case "$5":
                    assertEquals(cashMap.get("$5") - change.get("$5"), c.getAmount());
                case "$1":
                    assertEquals(cashMap.get("$1") - change.get("$1"), c.getAmount());
                case "50c":
                    assertEquals(cashMap.get("50c") - change.get("50c"), c.getAmount());
                case "5c":
                    assertEquals(cashMap.get("5c") - change.get("5c"), c.getAmount());
            }
        }

        //reset json
        JsonParser jp = new JsonParser();
        jp.updateCash(cashList, initialPath);
    }

    //Test on small change not sufficient
    @Test
    public void smallChangeInsufficient() throws InsufficientChangeException{
        String initialPath = "src/main/resources/test4cash.json";
        CashPaymentModel Test8 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test8.getCashList();

        double payment = 0.10;
        double price = 0.05;

        assertThrows(InsufficientChangeException.class, () -> {Test8.calculateChange(payment, price);});
        
    }

}