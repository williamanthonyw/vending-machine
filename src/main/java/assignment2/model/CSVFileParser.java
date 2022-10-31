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
    private String transactionCSV;

    public CSVFileParser(String inventoryCSV, String transactionCSV){
        this.inventoryCSV = inventoryCSV;
        this.transactionCSV = transactionCSV;
    }

    public CSVFileParser(String inventoryCSV){
        this.inventoryCSV = inventoryCSV;
        this.transactionCSV = "";
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
}
