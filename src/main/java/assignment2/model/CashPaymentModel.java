package assignment2.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CashPaymentModel{
    private String cashPath;
    private List<Cash> cashList;
    // src/main/resources/Cash.json;
    
    public CashPaymentModel(String cp){
        this.cashPath = cp;
        Gson gson = new Gson();
        List<Cash> cash = new ArrayList<Cash>();
        
        //read from a json file into list of available notes/coins 
        try{
            Reader reader = new BufferedReader(new FileReader(this.cashPath));
            cash = gson.fromJson(reader, new TypeToken<List<Cash>>() {}.getType());

            if (cash == null){
                this.cashList = new ArrayList<Cash>();
            }

            else{
                this.cashList = cash;
            }
        }

        catch (Exception e){
            
        }

    }

    public List<Cash> getCashList(){
        return this.cashList;
    }

    public static void main(String[] args){
        
    }
}