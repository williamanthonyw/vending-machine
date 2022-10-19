package assignment2.model;

public class CardUser{
    private String name;
    private String cardNumber;
    public CardUser(String name, String cardNumber){
        this.name = name;
        this.cardNumber = cardNumber;
    }
    public String getName(){
        return name;
    }
    public String getCardNumber() {
        return cardNumber;
    }

}
