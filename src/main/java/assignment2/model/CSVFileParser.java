package assignment2.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class CSVFileParser {

    private JsonParser jp;
    private String inventoryCSV;
    private String transactionCSV;
    private String cancelledTransactionsCSV;
    private String usersCSV;

    public CSVFileParser(String inventoryCSV, String transactionCSV, String cancelledTransactionsCSV, String usersCSV){
        this.inventoryCSV = inventoryCSV;
        this.transactionCSV = transactionCSV;
        this.cancelledTransactionsCSV = cancelledTransactionsCSV;
        this.usersCSV = usersCSV;
    }

    public CSVFileParser(String inventoryCSV){
        this.inventoryCSV = inventoryCSV;
        this.transactionCSV = "";
    }

    public List<CancelledTransaction> getCancelledTransactions(){
        List<CancelledTransaction> cancelledTransactions = new ArrayList<CancelledTransaction>();

        File file = new File(cancelledTransactionsCSV);

        try{
            CSVReader reader = new CSVReader(new FileReader(file));

            String[] row;

            while((row = reader.readNext()) != null){
                cancelledTransactions.add(new CancelledTransaction(row[0], CancellationReason.getCancellationReason(row[1]), LocalDateTime.parse(row[2])));
            }

            reader.close();
        }

        catch(IOException e){

        } catch(CsvValidationException c){

        }

        return cancelledTransactions;
    }

    public void updateCancelledTransactions(List<CancelledTransaction> cancelledTransactions){

        File file = new File(cancelledTransactionsCSV);

        try{
            List<String[]> rows = new ArrayList<String[]>();

            FileWriter fileWriter = new FileWriter(file);
            CSVWriter writer = new CSVWriter(fileWriter);

            for (CancelledTransaction c: cancelledTransactions){
                rows.add(new String[] {c.getUsername(), c.getCancellationReason().getReason(), c.getTimeCancelled().toString()});
            }

            writer.writeAll(rows);
            writer.close();

        }
        catch(IOException e){

        }
    }



    public void writePurchasesToFile(HashMap<Product, Integer> itemsPurchased){
        File file = new File(transactionCSV);

        try{
            List<String[]> items = new ArrayList<String[]>();

            FileWriter fileWriter = new FileWriter(file);
            CSVWriter writer = new CSVWriter(fileWriter);

            for (Product p: itemsPurchased.keySet()){
                items.add(new String[] {String.valueOf(p.getCode()), p.getName(), String.valueOf(itemsPurchased.get(p))});
            }
            writer.writeAll(items);
            writer.close();

        }
        catch(IOException e){

        }
    }

    
    public List<List<String>> readInventoryFromFile(){
        List<List<String>> items = new ArrayList<List<String>>();
        File file = new File(this.inventoryCSV);

        String[] item;

        try{
            CSVReader reader = new CSVReader(new FileReader(file));

            while((item = reader.readNext()) != null){
                items.add(Arrays.asList(item));
            }

            reader.close();
        }
        catch (IOException e){

        }
        catch(CsvValidationException c){

        }
        return items;
    }


    public void writeInventoryToFile(List<List<String>> items){
        File file = new File(this.inventoryCSV);
        
        try{
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            List<String[]> writeProducts = new ArrayList<String[]>();

            //convert List<List<String>> to List<String[]>
            for (List<String> s: items){
                writeProducts.add(new String[] {s.get(0), s.get(1), s.get(2), s.get(3), s.get(4)});
            }
           
            writer.writeAll(writeProducts);
            writer.close();
        }

        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void updateUsers(List<User> users){
        File f = new File(this.usersCSV);
        try{
            CSVWriter writer = new CSVWriter(new FileWriter(f));
            List<String[]> writeUsers = new ArrayList<String[]>();

            for (User s: users){

                writeUsers.add(new String[] {s.getUsername(), s.getUserAccess().toString()});
            }
           
            writer.writeAll(writeUsers);
            writer.close();

        }
        catch(IOException e){

        }
    }
}
