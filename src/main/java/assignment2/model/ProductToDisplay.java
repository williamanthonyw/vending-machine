package assignment2.model;

import javafx.scene.control.*;
// import assignment2.view.ProductOptionsView;
public class ProductToDisplay {
    
    private String name;
    private int code;
    private String category;
    private double price;
    private int quantity;

    //might be set to null by gson fromjson method(?)- check
    // private int quantityInBasket;
    // private Button addButton;

    public ProductToDisplay(Product product){
        this.name = product.getName();
        this.price = product.getPrice();
        //this.quantityAvailable = ...  
        //this.quantityInBasket = ...
        // this.addButton = new Button("ADD");
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getCode(){
        return this.code;
    }

    public void setCode(int code){
        this.code = code;
    }

    public String getCategory(){
        return this.category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public double getPrice(){
        return this.price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

}
