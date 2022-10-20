package assignment2.model;

public class PaymentNotEnoughException extends Exception{
    public PaymentNotEnoughException(String msg){
        super(msg);
    }
}
