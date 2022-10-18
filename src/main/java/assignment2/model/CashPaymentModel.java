package assignment2.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.lang.Math.*;


public class CashPaymentModel{
    private String cashPath;
    private List<Cash> cashList;
    // src/main/resources/Cash.json;
    
    public CashPaymentModel(String cp){
        this.cashPath = cp;
        JsonParser jp = new JsonParser();
        
        
        //read from a json file into list of available notes/coins 
        List<Cash> cash = jp.getCash(this.cashPath);
        this.cashList = cash;
    }

    public List<Cash> getCashList(){
        return this.cashList;
    }

    public HashMap<String, Integer> getCashMap(List<Cash> cashList){
        HashMap<String, Integer> cashMap = new HashMap<String, Integer>();
        
        for (Cash c: cashList){
            cashMap.put(c.getName(), c.getAmount());
        }

        return cashMap;
    }

    public double calculatePayment(HashMap<Double, Integer> cashPayment){
        double totalPayment = 0;
        
        for (HashMap.Entry<Double, Integer> map: cashPayment.entrySet()){
            totalPayment += map.getKey() * map.getValue();
        }

        return totalPayment;
    }

    public void addPaymentToMachine(HashMap<Double, Integer> cashPayment, List<Cash> cashList){
        for (Cash c: cashList){
            if (cashPayment.get(c.getValue()) == null){
                cashPayment.put(c.getValue(),0);
            }
            //System.out.println(cashPayment.get(c.getValue()));
            c.setAmount(c.getAmount() + cashPayment.get(c.getValue()));
        }
    }

    public HashMap<String, Integer> calculateChange(double payment, double price, HashMap<Double, Integer> cashPayment) throws InsufficientChangeException{
        HashMap<String, Integer> totalChange = new HashMap<String, Integer>();
        double change = payment - price; 

        

        //add payment cash to machine
        addPaymentToMachine(cashPayment, this.cashList);

        //calculate total amount of cash available
        double totalCash = 0;
        for (Cash c: this.cashList){
            totalCash += (c.getValue() * c.getAmount());
        }

        if (totalCash < change){
            throw new InsufficientChangeException("Not enough cash in vending machine");
        }

        // reverse sorted order from highest value to lowest value
        List<Cash> reverseCashList = this.cashList;
        Collections.reverse(reverseCashList);

        //calculate change given paid amount -> from notes with highest value
        for (Cash c: reverseCashList){
            int counter = 0;
            
            while (c.getAmount() != 0 && change != 0 && c.getValue() <= change){
                change -= c.getValue();
                change = Math.round(change*100.0)/100.0;
                c.setAmount(c.getAmount() - 1);

                counter++;

            }
            totalChange.put(c.getName(), counter);
            System.out.println(change);
        }

        int fiveCentsChange = totalChange.get("5c");

        //rounding change less than 5 cents
        HashMap<String, Integer> cashMap = this.getCashMap(reverseCashList);
        if (change < 0.05){
            change = Math.round(change*20.0)/20.0;

            if (change == 0.05){
                if (cashMap.get("5c") >= 1){
                    for (Cash c: reverseCashList){
                        if (c.getName().equals("5c")){
                            c.setAmount(c.getAmount()-1);
                            totalChange.replace("5c", fiveCentsChange, fiveCentsChange+1);
                            change -= 0.05;
                            // System.out.println(change);
                            break;
                        }
                    }
                }
                else{
                    throw new InsufficientChangeException("Not enough change");
                }
            }
        }
        // System.out.println(change);
        if (change > 0){
            throw new InsufficientChangeException("Not enough change");
        }


        //update change amount with reversing the reversed list
        Collections.reverse(reverseCashList);
        this.cashList = reverseCashList;
        JsonParser jp = new JsonParser();
        jp.updateCash(this.cashList, this.cashPath);

        return totalChange;
    }

    public static void main(String[] args){
        String initialPath = "src/main/resources/test6cash.json";
        CashPaymentModel Test12 = new CashPaymentModel(initialPath);
        List<Cash> cashList = Test12.getCashList();
        HashMap<String, Integer> cashMap1 = Test12.getCashMap(cashList);
        // cashMap1.forEach((k,v) ->{
        //     System.out.printf("key: %s, value: %d%n", k, v);
        // }

        // );
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
        try{
            
            HashMap<String, Integer> change1 = Test12.calculateChange(totalPayment1, price1, payment1);
            
            System.out.println(change1);
            for (Cash c: Test12.getCashList()){
                
                System.out.println(cashMap1.get(c.getName()) + payment1.get(c.getValue()) - change1.get(c.getName()) == c.getAmount());
                // System.out.println("After: " + c.getAmount());
            }

            
        }
        catch (InsufficientChangeException e){
            e.printStackTrace();
        }
        //reset json
        JsonParser jp = new JsonParser();
        jp.updateCash(jp.getCash("src/main/resources/richcash.json"), initialPath);
        
        
        
    }
}