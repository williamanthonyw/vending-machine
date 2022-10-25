package assignment2.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;


public class ProductOptionsModel {

    private List<Product> productsToDisplay;
    
    // public ProductOptionsModel(List<ProductToDisplay> productsToDisplay){
        
    //     this.productsToDisplay = productsToDisplay;
    // }


    public List<Product> getProductsToDisplay(String filename){
        JsonParser jparser = new JsonParser();
        productsToDisplay = jparser.getProductsToDisplay(filename);
        
        return productsToDisplay;
    }

    public void updateInventory(){   /////////// after purchase/changes

        // write back to inventory
        JsonParser jparser = new JsonParser();
        jparser.updateInventory(productsToDisplay, "src/main/resources/Inventory.json" );

    }

    public ArrayList<String> getCategories(String filename){
        ArrayList<String> categories = new ArrayList<>();
        List<Product> allProducts = getProductsToDisplay(filename);

        for (Product product : allProducts){
            if (! categories.contains(product.getCategory())){
                categories.add(product.getCategory());
            }
        }
        return categories;
    }

    public void updateQuantity(Product product, int numBought){
       product.setQuantity(product.getQuantity() - numBought);

    }

    public void putBack(Map<Product, Integer> cart){

        for (Product p : cart.keySet()){
            p.setQuantity(p.getQuantity() + cart.get(p));
        }

        updateInventory();

    }

    public List<String[]> readInventoryFromFile(String filename){
        List<String[]> items = new ArrayList<String[]>();
        File file = new File(filename);

        try{
            CSVReader reader = new CSVReader(new FileReader(file));

            while(reader.readNext() != null){
                String[] item = (reader.readNext());
                items.add(item);
            }

            reader.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch(CsvValidationException c){
            c.printStackTrace();
        }
        return items;
    }

    public void writeInventoryToFile(String filename){
        File file = new File(filename);
        
        try{
            CSVWriter writer = new CSVWriter(new FileWriter(file));

            List<String[]> items = new ArrayList<String[]>();

            for (Product p : this.productsToDisplay){
                items.add(new String[] {String.valueOf(p.getCode()), p.getCategory(), p.getName(), String.valueOf(p.getPrice()), String.valueOf(p.getQuantity())});
            }

            writer.writeAll(items);

            writer.close();
        }

        catch(IOException e){
            e.printStackTrace();
        }
    }
}
