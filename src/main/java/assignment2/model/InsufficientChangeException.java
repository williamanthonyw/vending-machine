package assignment2.model;

public class InsufficientChangeException extends Exception{
    public InsufficientChangeException(String str){
        super(str);
    }
}