package assignment2.model;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class InventoryModel {

    private List<Product> inventory;
    private List<String> categories;
    private Map<String, List<String>> productNamesByCategory;
    private List<String> allProductNames;
    private String filename;
    private JsonParser jsonParser;
    
    public InventoryModel(List<Product> inventory, JsonParser jsonParser){
        this.inventory = inventory;
        this.jsonParser = jsonParser;
        this.categories = Arrays.asList(new String[] {"drinks", "chocolates", "chips", "candies"});

        productNamesByCategory = new LinkedHashMap<String, List<String>>();
        productNamesByCategory.put("drinks",  new ArrayList<String>(Arrays.asList(new String[] {"Mineral Water", "Sprite", "Coca Cola", "Pepsi", "Juice"})));
        productNamesByCategory.put("chocolates",  new ArrayList<String>(Arrays.asList(new String[] {"Mars", "M&M", "Bounty", "Snickers"})));
        productNamesByCategory.put("chips", new ArrayList<String>(Arrays.asList(new String[] {"Smiths", "Pringles", "Kettle", "Thins"})));
        productNamesByCategory.put("candies", new ArrayList<String>(Arrays.asList(new String[] {"Mentos", "Sour Patch", "Skittles"})));

        this.allProductNames = new ArrayList<String>();

       productNamesByCategory.keySet().forEach((String category) -> {
           allProductNames.addAll(productNamesByCategory.get(category));
       });


     }

    public List<Product> getInventory(){
        return inventory;
    }

    public List<String> getAllProductNames(){
        return allProductNames;
    }

    public List<Product> getInventory(String category){

        List<Product> products = new ArrayList<Product>();

        for (Product p : inventory){
            if (p.getCategory().equals(category)){
                products.add(p);
            }
        }

        return products;
    }

    public void updateInventory(){   /////////// after purchase/changes

        // write back to inventory
        jsonParser.updateInventory(inventory);

    }

    public List<String> getCategories(){
        return categories;
    }

    public void updateQuantity(Product product, int numBought){
       product.setQuantity(product.getQuantity() - numBought);

       if (product.getQuantity() <= 0){
           inventory.remove(product);
       }

    }

    public int[] getCodeRange(String category){
       int correctCode = (categories.indexOf(category) + 1) * 100;

       return new int[]{correctCode, correctCode + 99};
    }

    public void putBack(Map<Product, Integer> cart){

        for (Product p : cart.keySet()){
            p.setQuantity(p.getQuantity() + cart.get(p));
        }

        updateInventory();

    }

    public UpdateProductState addProduct(Product product,
                                            String name,
                                            String category,
                                            int code,
                                            double price,
                                            int quantity){

        UpdateProductState result = setProductDetails(product, name, category, code, price, quantity);

        if (result == UpdateProductState.SUCCESS){
            inventory.add(product);

            updateInventory();
        }

        return result;

    }

    public UpdateProductState updateProduct(Product product,
                                                String name,
                                                String category,
                                                int code,
                                                double price,
                                                int quantity){

        UpdateProductState result = setProductDetails(product, name, category, code, price, quantity);

        if (result == UpdateProductState.SUCCESS){
            updateInventory();
        }

        return result;

    }

    public UpdateProductState setProductDetails(Product product,
                              String name,
                              String category,
                              int code,
                              double price,
                              int quantity){


        if (!productNamesByCategory.get(category).contains(name)){
            return UpdateProductState.CATEGORY_ERROR;
        }

        int[] codeRange = getCodeRange(category);

        // code should be in the range of that category
        if (code < codeRange[0] || code > codeRange[1]){
            return UpdateProductState.CODE_RANGE_ERROR;
        }

        for (Product p : inventory){

            if (p != product){

                // no duplicate products allowed
                if (p.getName().equals(name) &&
                    p.getCategory().equals(category)) {
                    return UpdateProductState.DUPLICATE_PRODUCT_ERROR;
                }

                // no duplicate codes allowed
                if (p.getCode() == code){
                    return UpdateProductState.CODE_DUPLICATE_ERROR;
                }
            }

        }

        if (price < 0){
            return UpdateProductState.PRICE_ERROR;
        }

        if (quantity > 15 || quantity < 0){
            return UpdateProductState.QUANTITY_ERROR;
        }


        product.setName(name);
        product.setCategory(category);
        product.setCode(code);
        product.setPrice(price);
        product.setQuantity(quantity);


        return UpdateProductState.SUCCESS;

    }

    public void deleteProduct(Product product) {
        inventory.remove(product);

        updateInventory();
    }

    public List<List<String>> readInventoryFromFile(String filename){
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

            for (Product p : this.inventory){
                items.add(new String[] {p.getName(),String.valueOf(p.getCode()), p.getCategory(),  String.valueOf(p.getPrice()), String.valueOf(p.getQuantity())});
            }



            writer.writeAll(items);

            writer.close();
        }

        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
    }
}
