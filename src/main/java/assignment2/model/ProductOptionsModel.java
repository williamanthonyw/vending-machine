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
}
