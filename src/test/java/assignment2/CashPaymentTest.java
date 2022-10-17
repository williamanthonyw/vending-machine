package assignment2;

import assignment2.model.*;
import assignment2.model.InsufficientChangeException;

import java.util.HashMap;
import java.util.List;

import javax.naming.InsufficientResourcesException;

import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;

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

    //Test on payment where changes are in notes
    @Test
    public void changeNotesOnly() throws InsufficientChangeException{
        String initialPath = "src/main/resources/InitialCash.json";
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
    
    }

    @Test
    public void changeCoinsOnly() throws InsufficientChangeException{
        String initialPath = "src/main/resources/InitialCash.json";
        CashPaymentModel Test5 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test5.getCashList();
        double payment = 0.50;
        double price = 0.15;

        HashMap<String, Integer> change = Test5.calculateChange(payment, price);
        assertEquals(1, change.get("20c"));
        assertEquals(1, change.get("10c"));
        assertEquals(1, change.get("5c"));
    }


}