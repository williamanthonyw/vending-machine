package assignment2.model;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Map;

public class User {

    private String username;
    private String password;
    private List<Purchase> purchases;
    private Map<Product, Integer> cart;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.purchases = new ArrayList<Purchase>();
        this.cart = new HashMap<Product, Integer>();
    }

    public void purchaseProduct(Product product, int quantity){
        Purchase purchase = new Purchase(product.getName(), product.getPrice(), quantity, LocalDateTime.now());
        this.purchases.add(purchase);

        // need to write to file
    }

    public List<Purchase> getPurchases(){
        return purchases;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public Map<Product, Integer> getCart() {
        return cart;
    }

    public void addToCart(Product product, int quantity){
        if (cart.get(product) == null){
            cart.put(product, quantity);
        } else {
            cart.put(product, cart.get(product) + quantity);
        }
    }

    public void setCart(Map<Product, Integer> cart){
        this.cart = cart;
    }

    public void clearCart(){
       this.cart = new HashMap<Product, Integer>();
    }






}