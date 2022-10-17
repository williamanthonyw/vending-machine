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

    public HashMap<String, Integer> calculateChange(double payment, double price) throws InsufficientChangeException{
        HashMap<String, Integer> totalChange = new HashMap<String, Integer>();
        double change = payment - price; 

        //calculate total amount of cash available
        double totalCash = 0;
        for (Cash c: this.cashList){
            totalCash += (c.getValue() * c.getAmount());
        }

        if (totalCash < change){
            throw new InsufficientChangeException("Not enough change");
        }
        // reverse sorted order from highest value to lowest value
        List<Cash> reverseCashList = this.cashList;
        Collections.reverse(reverseCashList);

        //calculate change given paid amount -> from notes with highest value
        for (Cash c: reverseCashList){
            int counter = 0;
            while (c.getAmount() != 0 && change != 0 && c.getValue() <= change){
                if (change < 0.05){
                    break;
                }
                change -= c.getValue();
                change = Math.round(change*100.0)/100.0;
                c.setAmount(c.getAmount() - 1);
                System.out.println(change);

                counter++;

            }
            totalChange.put(c.getName(), counter);

        }

        if (change != 0){
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
        CashPaymentModel c = new CashPaymentModel("src/main/resources/InitialCash.json");
        System.out.println(c.cashList);
        double payment = 139.00;
        double price = 122.45; //16.55
        try{
            HashMap<String, Integer> change = c.calculateChange(payment, price);
            System.out.println(change);
        }
        catch (InsufficientChangeException e){
            e.printStackTrace();
        }
        
        System.out.println(c.getCashMap(c.cashList));
        
    }
}