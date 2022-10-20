package assignment2.model;

import java.util.*;

public class ProductOptionsModel {

    // private List<ProductToDisplay> productsToDisplay;
    
    // public ProductOptionsModel(List<ProductToDisplay> productsToDisplay){
        
    //     this.productsToDisplay = productsToDisplay;
    // }

    public List<ProductToDisplay> getProductsToDisplay(){
        JsonParser jparser = new JsonParser();
        List<ProductToDisplay> productsToDisplay = jparser.getProductsToDisplay("src/main/resources/Inventory.json");
        
        return productsToDisplay;
    }

    public ArrayList<String> getCategories(){
        ArrayList<String> categories = new ArrayList<>();
        List<ProductToDisplay> allProducts = getProductsToDisplay();

        for (ProductToDisplay product : allProducts){
            if (! categories.contains(product.getCategory())){
                categories.add(product.getCategory());
            }
        }
        return categories;
    }
}
