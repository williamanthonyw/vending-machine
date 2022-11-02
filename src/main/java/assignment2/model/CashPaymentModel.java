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
    private CSVFileParser csvFileParser;
    private List<List<String>> cashString;
    private double moneyPaid;
    private double returnedChange;

    public CashPaymentModel(List<Cash> cashList, JsonParser jsonParser,CSVFileParser csvFileParser){

        this.jsonParser = jsonParser;
        this.cashList = cashList;
        this.moneyPaid = 0;
        this.returnedChange = 0;
        this.csvFileParser = csvFileParser;
        this.cashString = this.csvFileParser.readCashFromFile();
    }

    public List<Cash> getCashList(){
        return this.cashList;
    }

    public double getMoneyPaid(){
        return this.moneyPaid;
    }

    public double getReturnedChange(){
        return this.returnedChange;
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
        this.moneyPaid = totalPayment;

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
    public void updateCash(int newAmount, Cash cash) {
        cash.setAmount(newAmount);
        for(Cash c : cashList){
            System.out.println(c.getAmount());
        }
        jsonParser.updateCash(cashList);
        csvFileParser.updateCash(cashList);
        updateCashString();
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

        totalChange(totalChange);

        return totalChange;
    }

    public void totalChange(Map<String, Integer> change){

        double total = 0;

        for (String s: change.keySet()){
            switch(s){
                case "5c":
                    total += 0.05 * change.get(s);
                    break;
                case "10c":
                    total += 0.10  * change.get(s);
                    break;
                case "20c":
                    total += 0.20  * change.get(s);
                    break;
                case "50c":
                    total += 0.50  * change.get(s);
                    break;
                case "$1":
                    total += 1.00  * change.get(s);
                    break;
                case "$2":
                    total += 2.00  * change.get(s);
                    break;
                case "$5":
                    total += 5.00  * change.get(s);
                    break;
                case "$10":
                    total += 10.00  * change.get(s);
                    break;
                case "$20":
                    total += 20.00  * change.get(s);
                    break;
                case "$50":
                    total += 50.00  * change.get(s);
                    break;
                case "$100":
                    total += 100.00 * change.get(s);
                    break;
            }
        }
        System.out.println(total);

        this.returnedChange = Math.round(total*100.0)/100.0;
    }

    public void updateCashString(){
        this.cashString.clear();

        for(Cash c: cashList){
            List<String> stringList = List.of(c.getName(),Integer.toString(c.getAmount()));
            this.cashString.add(stringList);
        }
    }

    public List<List<String>> getCashString(){
        return cashString;
    }

    public static void main(String[] args){

    }


}