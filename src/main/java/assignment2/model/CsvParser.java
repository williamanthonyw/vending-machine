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

public class CsvParser {

    private String cancelledTransactionsCSV;
    private String transactionCSV;

    public CsvParser(String transactionCSV,  String cancelledTransactionsCSV){
        this.transactionCSV = transactionCSV;
        this.cancelledTransactionsCSV = cancelledTransactionsCSV;
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

    public List<List<String>> readPurchasesFromFile(String filename){
        List<List<String>> items = new ArrayList<List<String>>();
        File file = new File(filename);
        String[] item;

        try{
            CSVReader reader = new CSVReader(new FileReader(file));

            while((item = reader.readNext()) != null){
                items.add(Arrays.asList(item));
            }

            reader.close();
        }

        catch(IOException e){
        }
        catch(CsvValidationException c){
        }

        return items;
    }

}
