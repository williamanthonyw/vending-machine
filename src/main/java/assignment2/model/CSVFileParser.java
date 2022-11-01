package assignment2.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class CSVFileParser {

    private JsonParser jp;
    private String inventoryCSV;
    private String sellerTransactionCSV;

    private String cashierTransactionCSV;

    public CSVFileParser(String inventoryCSV, String sellerTransactionCSV, String cashierTransactionCSV){
        this.inventoryCSV = inventoryCSV;
        this.sellerTransactionCSV = sellerTransactionCSV;
        this.cashierTransactionCSV = cashierTransactionCSV;
    
    }

    public CSVFileParser(String inventoryCSV, String sellerTransactionCSV){
        this.inventoryCSV = inventoryCSV;
        this.sellerTransactionCSV = sellerTransactionCSV;
        this.cashierTransactionCSV = "";

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

    public List<List<String>> readSellerTransactions(){
        List<List<String>> transactions = new ArrayList<List<String>>();
        File file = new File(this.sellerTransactionCSV);

        String[] transaction;

        try{
            CSVReader reader = new CSVReader(new FileReader(file));

            while ((transaction = reader.readNext()) != null){
                transactions.add(Arrays.asList(transaction));
            }

            reader.close();
        }

        catch (IOException e){

        }
        catch (CsvValidationException f){

        }

        return transactions;
    }

    public List<List<String>> readCashierTransactions(){
        List<List<String>> transactions = new ArrayList<List<String>>();
        File file = new File(this.cashierTransactionCSV);

        String[] transaction;

        try{
            CSVReader reader = new CSVReader(new FileReader(file));

            while ((transaction = reader.readNext()) != null){
                transactions.add(Arrays.asList(transaction));
            }

            reader.close();
        }

        catch (IOException e){

        }
        catch (CsvValidationException f){

        }

        return transactions;
    }

    public void writeSellerTransactions(List<List<String>> transactions){
        File file = new File(this.sellerTransactionCSV);

        try{
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            List<String[]> writeTransactions = new ArrayList<String[]>();

            for (List<String> s: transactions){
                writeTransactions.add(new String[] {s.get(0), s.get(1), s.get(2)});
            }

            writer.writeAll(writeTransactions);
            writer.close();
        }

        catch(IOException e){

        }
    }

    public void writeCashierTransactions(List<List<String>> transactions){
        File file = new File(this.cashierTransactionCSV);

        try{
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            List<String[]> writeTransactions = new ArrayList<String[]>();

            for (List<String> s: transactions){
                writeTransactions.add(new String[] {s.get(0), s.get(1), s.get(2), s.get(3), s.get(4)});
            }

            writer.writeAll(writeTransactions);
            writer.close();
        }

        catch(IOException e){

        }
    }

}
