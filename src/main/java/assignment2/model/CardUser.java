package assignment2.model;

public class CardUser{
    private String name;
    private String number;
    public CardUser(String name, String number){
        this.name = name;
        this.number = number;
    }
    public String getName(){
        return name;
    }
    public String getCardNumber() {
        return number;
    }

}
