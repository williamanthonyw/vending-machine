package assignment2.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Scanner;

public class JsonParser {

    static Gson gson = new Gson();

    private String inventoryFile;
    private String usersFile;
    private String initialCashFile;
    private String cardFile;

    public JsonParser(String inventoryFile, String usersFile, String initialCashFile, String cardFile){
        this.inventoryFile = inventoryFile;
        this.usersFile = usersFile;
        this.initialCashFile = initialCashFile;
        this.cardFile = cardFile;
    }

    public List<User> getUsers(){

        try {

            Reader reader = new BufferedReader(new FileReader(usersFile));

            // Convert json file to a list of exchange rates
            List<User> users = gson.fromJson(reader, new TypeToken<List<User>>() {}.getType());

            if (users == null){
                return new ArrayList<User>();
            }

            return users;

        } catch (IOException e) {
            return new ArrayList<User>();
        }

    }

    public void updateUsers(List<User> users){

        try (FileWriter file = new FileWriter(usersFile)) {

            file.write(gson.toJson(users));
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }


    }
    ///////////////////
    public List<Product> getInventory(){
        try {
            Reader reader = new BufferedReader(new FileReader(inventoryFile));

            // Convert json file to a list of products
            List<Product> inventory = gson.fromJson(reader, new TypeToken<List<Product>>() {}.getType());

            if (inventory == null){
                return new ArrayList<Product>();
            }

            return inventory;

        } catch (IOException e) {
            return new ArrayList<Product>();
        }
    }

    // + updateInventory 

    public List<Cash> getCash(){
        try{
            Reader reader = new BufferedReader(new FileReader(initialCashFile));

            List<Cash> cashList = gson.fromJson(reader, new TypeToken<List<Cash>>() {}.getType());
            // System.out.println("hello");

            if (cashList == null){
                return new ArrayList<Cash>();
            }

            return cashList;
        }

        catch (IOException e){
            return new ArrayList<Cash>();
        }
    }

    public void updateCash(List<Cash> cashList){
        
        try(FileWriter file = new FileWriter(initialCashFile)){
            file.write(gson.toJson(cashList));
            file.flush();
        }
        
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateInventory(List<Product> productList){

        try(FileWriter file = new FileWriter(inventoryFile)){

            if (productList != null){
                file.write(gson.toJson(productList));
                file.flush();
            }


        }

        catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<CardUser> getCardDetail() {
        try {

            Reader reader = new BufferedReader(new FileReader(cardFile));

            // Convert json file to a list of exchange rates
            List<CardUser> cardUsers = gson.fromJson(reader, new TypeToken<List<CardUser>>() {}.getType());
            return cardUsers;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<CardUser>();
    }
}

