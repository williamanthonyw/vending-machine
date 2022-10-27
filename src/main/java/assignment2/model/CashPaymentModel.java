package assignment2.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.lang.Math.*;


public class CashPaymentModel{

    private JsonParser jsonParser;
    private List<Cash> cashList;
    private double currentChange;
    
    public CashPaymentModel(List<Cash> cashList, JsonParser jsonParser){

        this.jsonParser = jsonParser;
        this.cashList = cashList;
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

    public void addPaymentToMachine(HashMap<Double, Integer> cashPayment){
        for (Cash c: cashList){
            if (cashPayment.get(c.getValue()) == null){
                cashPayment.put(c.getValue(),0);
            }
            //System.out.println(cashPayment.get(c.getValue()));
            c.setAmount(c.getAmount() + cashPayment.get(c.getValue()));
        }
    }

    public Map<String, Integer> calculateChange(double payment, double price, HashMap<Double, Integer> cashPayment) throws InsufficientChangeException, PaymentNotEnoughException{
        
        LinkedHashMap<String, Integer> totalChange = new LinkedHashMap<String, Integer>();

        //initialize total change
        totalChange.put("5c", 0);
        totalChange.put("10c", 0);
        totalChange.put("20c", 0);
        totalChange.put("50c", 0);
        totalChange.put("$1", 0);
        totalChange.put("$2", 0);
        totalChange.put("$5", 0);
        totalChange.put("$10", 0);
        totalChange.put("$20", 0);
        totalChange.put("$50", 0);
        totalChange.put("$100", 0);
        System.out.println(totalChange);

        if (payment < price){
            throw new PaymentNotEnoughException("Please add more cash or cancel");
        }
        double change = payment - price;

        System.out.println(cashList);

        //add payment cash to machine
        addPaymentToMachine(cashPayment);

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
            //totalChange.put(c.getName(), counter);
            totalChange.replace(c.getName(), totalChange.get(c.getName()), counter);
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

        jsonParser.updateCash(this.cashList);



        System.out.println(totalChange);

        return totalChange;
    }

    public static void main(String[] args){

    }


}