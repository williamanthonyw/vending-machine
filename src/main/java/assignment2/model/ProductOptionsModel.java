package assignment2.model;

import java.util.*;

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
}
