package assignment2.model;


public class Cash{
    private String name;
    private double value;
    private int amount;

    // Cash object: name, value in Dollars, amount left in stock

    public Cash(String name, double value, int amount){
        this.name = name;
        this.value = value;
        this.amount = amount;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public double getValue(){
        return this.value;
    }

    public void setValue(double newValue){
        this.value = newValue;
    }

    public int getAmount(){
        return this.amount;
    }

    public void setAmount(int newAmount){
        this.amount = newAmount;
    }

}