package assignment2.model;

import java.util.List;

public class CardPaymentModel {
    private final JsonParser jsonParser = new JsonParser();
    private List<CardUser> cardUserList;
    private MainModel mainModel;

    public CardPaymentModel(MainModel mainModel){
        this.mainModel = mainModel;
    }

    public void paymentProcess(String name, String number){
        cardUserList = jsonParser.getCardDetail("src/main/resources/credit_cards.json");

        for(CardUser user: cardUserList){
            if(user.getName().equals(name) && user.getCardNumber().equals(number)){
                mainModel.getUser().setCardUser(user);
//                System.out.println("payment success");
            }
        }
    }
}
