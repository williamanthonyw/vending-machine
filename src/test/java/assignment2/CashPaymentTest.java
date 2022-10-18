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
    JsonParser jp = new JsonParser();
    private List<Cash> defaultCash = jp.getCash("src/main/resources/InitialCash.json");

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

        HashMap<Double, Integer> cash = new HashMap<Double, Integer>();
        cash.put(100.0, 2);
        cash.put(50.0, 1);
        cash.put(20.0, 1);
        cash.put(10.0, 1);
        cash.put(5.0, 1);
        cash.put(2.0, 1);
        cash.put(1.0, 1);

        double payment = Test4.calculatePayment(cash);
        double price = 12.00;

        HashMap<String, Integer> change = Test4.calculateChange(payment, price, cash);
        assertEquals(2, change.get("$100"));
        assertEquals(1, change.get("$50"));
        assertEquals(1, change.get("$20"));
        assertEquals(1, change.get("$5"));
        assertEquals(1, change.get("$1"));

        //reset json
        JsonParser jp = new JsonParser();
        jp.updateCash(defaultCash, initialPath);
    }

    //Test on payment where changes are in coins only
    @Test
    public void changeCoinsOnly() throws InsufficientChangeException{
        String initialPath = "src/main/resources/test2cash.json";
        CashPaymentModel Test5 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test5.getCashList();

        HashMap<Double, Integer> cash = new HashMap<Double, Integer>();
        cash.put(0.20, 2);
        cash.put(0.10, 1);

        double payment = Test5.calculatePayment(cash);
        double price = 0.15;

        HashMap<String, Integer> change = Test5.calculateChange(payment, price, cash);
        assertEquals(1, change.get("20c"));
        assertEquals(1, change.get("10c"));
        assertEquals(1, change.get("5c"));

        //reset json
        JsonParser jp = new JsonParser();
        jp.updateCash(defaultCash, initialPath);

    }

    // //Test on payment where the cash inventory is not sufficient
    @Test
    public void changeisNotSufficient() throws InsufficientChangeException{
        String initialPath = "src/main/resources/testnotenough.json";
        CashPaymentModel Test6 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test6.getCashList();

        HashMap<Double, Integer> cash = new HashMap<Double, Integer>();
        cash.put(100.0, 200);
        
        double payment = Test6.calculatePayment(cash);
        double price = 12.00;

        assertThrows(InsufficientChangeException.class, () -> {Test6.calculateChange(payment, price, cash);});
    }

    // //Test on update cash inventory after a successful payment
    // @Test
    public void updateChangeList() throws InsufficientChangeException{
        String initialPath = "src/main/resources/test3cash.json";
        CashPaymentModel Test7 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test7.getCashList();
        

        HashMap<Double, Integer> cash = new HashMap<Double, Integer>();
        cash.put(100.0, 1);
        cash.put(20.0, 1);
        cash.put(10.0, 1);
        cash.put(5.0, 1);
        cash.put(2.0, 2);
        
        double payment = Test7.calculatePayment(cash);
        double price = 122.45; //16.55
        HashMap<String, Integer> cashMap = Test7.getCashMap(cashList);
        HashMap<String, Integer> change = Test7.calculateChange(payment, price, cash);
       

        for(Cash c: cashList){
            assertEquals(cashMap.get(c.getName()) + cash.get(c.getValue())- change.get(c.getName()), c.getAmount());
        }

        //reset json
        JsonParser jp = new JsonParser();
        jp.updateCash(defaultCash, initialPath);
    }

    // //Test on small change not sufficient
    // @Test
    public void smallChangeInsufficient() throws InsufficientChangeException{
        String initialPath = "src/main/resources/test4cash.json";
        CashPaymentModel Test8 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test8.getCashList();

        HashMap<Double, Integer> cash = new HashMap<Double, Integer>();
        cash.put(0.10, 1);

        double payment = Test8.calculatePayment(cash);
        double price = 0.05;

        assertThrows(InsufficientChangeException.class, () -> {Test8.calculateChange(payment, price, cash);});
        
    }

    // //Test on calculating the total payment given a hashmap of each coins and notes
    // @Test
    public void calculateTotalPayment(){
        String initialPath = "src/main/resources/cash.json";
        CashPaymentModel Test9 = new CashPaymentModel(initialPath);
        
        HashMap<Double, Integer> cash = new HashMap<Double, Integer>();
        cash.put(0.05, 2);
        cash.put(0.10, 3);  
        cash.put(0.20, 10);
        cash.put(0.50, 6);
        cash.put(1.00, 2);
        cash.put(2.00, 7);
        cash.put(5.00, 3);
        cash.put(10.00, 4);
        cash.put(20.00, 1);
        cash.put(50.00, 2);
        cash.put(100.00, 3);

        double payment = Test9.calculatePayment(cash);
        assertEquals(496.4, payment, 0);
    }

    // //Test on calculating change less than 5 cents
    @Test 
    public void smallChangeRound() throws InsufficientChangeException{
        String initialPath = "src/main/resources/test5cash.json";

        //rounding up
        CashPaymentModel Test10 = new CashPaymentModel(initialPath);

        HashMap<Double, Integer> cash1 = new HashMap<Double, Integer>();
        cash1.put(0.05, 2);

        double payment1 = Test10.calculatePayment(cash1);
        double price1 = 0.06;

        HashMap<String, Integer> change1 = Test10.calculateChange(payment1, price1, cash1);
        assertEquals(1, change1.get("5c"));

        //reset json
        JsonParser jp = new JsonParser();
        jp.updateCash(defaultCash, initialPath);


        //rounding down
        CashPaymentModel Test11 = new CashPaymentModel(initialPath);

        HashMap<Double, Integer> cash2 = new HashMap<Double, Integer>();
        cash2.put(0.05, 2);

        double payment2 = Test11.calculatePayment(cash2);
        double price2 = 0.08;

        HashMap<String, Integer> change2 = Test11.calculateChange(payment2, price2, cash2);
        assertEquals(0, change2.get("5c"));

        //reset json
        jp.updateCash(defaultCash, initialPath);
    }

    //Test on multiple payments
    @Test
    public void multiplePayments() throws InsufficientChangeException{
        String initialPath = "src/main/resources/test6cash.json";
        CashPaymentModel Test12 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test12.getCashList();
       

        HashMap<Double, Integer> payment1 = new HashMap<Double, Integer>();
        payment1.put(0.05, 10);
        payment1.put(0.10, 8);  
        payment1.put(0.20, 8);
        payment1.put(0.50, 5);
        payment1.put(1.00, 7);
        payment1.put(2.00, 6);
        payment1.put(5.00, 4);
        payment1.put(10.00, 1);
        payment1.put(20.00, 3);
        payment1.put(50.00, 1);
        payment1.put(100.00, 4);

        double totalPayment1 = Test12.calculatePayment(payment1);
        double price1 = 368.44;
        HashMap<String, Integer> cashMap1 = Test12.getCashMap(cashList);
        HashMap<String, Integer> change1 = Test12.calculateChange(totalPayment1, price1, payment1);
        

        for (Cash c: cashList){
            assertEquals(cashMap1.get(c.getName()) + payment1.get(c.getValue())- change1.get(c.getName()), c.getAmount());
        }

        HashMap<Double, Integer> payment2 = new HashMap<Double, Integer>();
        payment2.put(0.05, 10);
        payment2.put(0.10, 2);  
        payment2.put(0.20, 1);
        payment2.put(0.50, 3);
        payment2.put(1.00, 8);
        payment2.put(2.00, 5);
        payment2.put(5.00, 9);
        payment2.put(10.00, 3);
        payment2.put(20.00, 4);
        payment2.put(50.00, 3);
        payment2.put(100.00, 2);

        double totalPayment2 = Test12.calculatePayment(payment2);
        double price2 = 223.79;
        HashMap<String, Integer> cashMap2 = Test12.getCashMap(cashList);
        HashMap<String, Integer> change2 = Test12.calculateChange(totalPayment2, price2, payment2);
        

        for (Cash c: cashList){
            assertEquals(cashMap2.get(c.getName()) + payment2.get(c.getValue())- change2.get(c.getName()), c.getAmount());
        }

        //reset json
        JsonParser jp = new JsonParser();
        jp.updateCash(defaultCash, initialPath);

    }
}