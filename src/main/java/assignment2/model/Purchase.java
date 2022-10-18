package assignment2.model;
import java.time.LocalDateTime;

public class Purchase {

    private double price;
    private int quantity;
    private String item;
    private LocalDateTime datePurchased;

    public Purchase(String item, double price, int quantity, LocalDateTime datePurchased) {
        this.price = price;
        this.quantity = quantity;
        this.item = item;
        this.datePurchased = datePurchased;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public LocalDateTime getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(LocalDateTime datePurchased) {
        this.datePurchased = datePurchased;
    }

    public String toString(){
        return item;
    }






}