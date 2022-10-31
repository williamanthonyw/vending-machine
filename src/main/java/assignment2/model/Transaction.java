package assignment2.model;

import java.time.LocalDateTime;

public class Transaction {
    
    private String itemCode;
    private String itemName;
    private int quantitySold;

    private LocalDateTime transactionDate;
    private double moneyPaid;
    private double returnedChange;
    private String paymentMethod;

    //for seller report
    public Transaction(String itemCode, String itemName, int quantitySold){
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

    
}
