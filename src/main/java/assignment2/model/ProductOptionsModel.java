package assignment2.model;

import java.util.*;

public class ProductOptionsModel {

    private List<ProductToDisplay> productsToDisplay;
    
    // public ProductOptionsModel(List<ProductToDisplay> productsToDisplay){
        
    //     this.productsToDisplay = productsToDisplay;
    // }

    public List<ProductToDisplay> getProductsToDisplay(String filename){
        JsonParser jparser = new JsonParser();
        productsToDisplay = jparser.getProductsToDisplay(filename);
        
        return productsToDisplay;
    }

    public void updateInventory(){   /////////// after purchase/changes
        ;
    }

    public ArrayList<String> getCategories(String filename){
        ArrayList<String> categories = new ArrayList<>();
        List<ProductToDisplay> allProducts = getProductsToDisplay(filename);

        for (ProductToDisplay product : allProducts){
            if (! categories.contains(product.getCategory())){
                categories.add(product.getCategory());
            }
        }
        return categories;
    }
}
