package assignment2.model;

import java.time.LocalDateTime;
import java.util.List;

public class Transaction {
    
    private int itemCode;
    private String itemName;
    private int quantitySold;

    private LocalDateTime transactionDate;
    private double moneyPaid;
    private double returnedChange;
    private String paymentMethod;


    //for seller report
    public Transaction(int itemCode, String itemName, int quantitySold){
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantitySold = quantitySold;
    }

    // for cashier report
    public Transaction(LocalDateTime transactionDate, String itemName, double moneyPaid, double returnedChange, String paymentMethod){
        this.transactionDate = transactionDate;
        this.itemName = itemName;
        this.moneyPaid = moneyPaid;
        this.returnedChange = returnedChange;
        this.paymentMethod = paymentMethod;
    }
    
    public int getItemCode(){
        return this.itemCode;
    }

    public String getItemName(){
        return this.itemName;
    }

    public int getQuantitySold(){
        return this.quantitySold;
    }

    public LocalDateTime getTransactionDate(){
        return this.transactionDate;
    }

    public double getMoneyPaid(){
        return this.moneyPaid;
    }

    public double getReturnedChange(){
        return this.returnedChange;
    }

    public String getPaymentMethod(){
        return this.paymentMethod;
    }
    
}
