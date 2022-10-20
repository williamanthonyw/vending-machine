package assignment2.model;

import java.util.*;

public class ProductOptionsModel {

    private List<Product> productsToDisplay;
    
    // public ProductOptionsModel(List<ProductToDisplay> productsToDisplay){
        
    //     this.productsToDisplay = productsToDisplay;
    // }

    public List<Product> getProductsToDisplay(){
        JsonParser jparser = new JsonParser();
        productsToDisplay = jparser.getProductsToDisplay("src/main/resources/Inventory.json");
        
        return productsToDisplay;
    }

    public void updateInventory(){   /////////// after purchase/changes
        ;
    }

    public ArrayList<String> getCategories(){
        ArrayList<String> categories = new ArrayList<>();
        List<Product> allProducts = getProductsToDisplay();

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

    }
}
