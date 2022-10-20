package assignment2.model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class User {

    private String username;
    private String password;
    private List<Purchase> purchases;
    private CardUser cardUser;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.purchases = new ArrayList<Purchase>();
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


    public void setCardUser(CardUser cardUser){
        this.cardUser = cardUser;
    }
    public CardUser getCardUser() {
        return cardUser;
    }
}