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

    public List<User> getUsers(String filename){

        try {

            Reader reader = new BufferedReader(new FileReader(filename));

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

    public void updateUsers(List<User> users, String filename){

        try (FileWriter file = new FileWriter(filename)) {

            file.write(gson.toJson(users));
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }


    }
    ///////////////////
    public List<Product> getProductsToDisplay(String filename){
        try {
            Reader reader = new BufferedReader(new FileReader(filename));

            // Convert json file to a list of products to display
            List<Product> productsToDisplay = gson.fromJson(reader, new TypeToken<List<Product>>() {}.getType());

            return productsToDisplay;

        } catch (IOException e) {
            return new ArrayList<Product>();
        }
    }

    // + updateInventory 

    public List<Cash> getCash(String filename){
        try{
            Reader reader = new BufferedReader(new FileReader(filename));

            List<Cash> cashList = gson.fromJson(reader, new TypeToken<List<Cash>>() {}.getType());
            // System.out.println("hello");

            return cashList;
        }

        catch (IOException e){
            return new ArrayList<Cash>();
        }
    }

    public void updateCash(List<Cash> cashList, String filename){
        
        try(FileWriter file = new FileWriter(filename)){
            file.write(gson.toJson(cashList));
            file.flush();
        }
        
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateInventory(List<Product> productList, String filename){

        try(FileWriter file = new FileWriter(filename)){

            if (productList != null){
                file.write(gson.toJson(productList));
                file.flush();
            }


        }

        catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<CardUser> getCardDetail(String filename) {
        try {

            Reader reader = new BufferedReader(new FileReader(filename));

            // Convert json file to a list of exchange rates
            List<CardUser> cardUsers = gson.fromJson(reader, new TypeToken<List<CardUser>>() {}.getType());
            return cardUsers;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<CardUser>();
    }
}

