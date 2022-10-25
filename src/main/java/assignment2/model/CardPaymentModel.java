package assignment2.model;
import java.util.List;
public class CardPaymentModel {
    private final JsonParser jsonParser = new JsonParser();
    private List<CardUser> cardUserList;
    private MainModel mainModel;
    public CardPaymentModel(MainModel mainModel){
        this.mainModel = mainModel;
    }


    public boolean paymentProcess(String name, String number,boolean saveDetail){
        cardUserList = jsonParser.getCardDetail("src/main/resources/credit_cards.json");

//        //can't get CardNumber using gson
//        System.out.println(cardUserList.get(1).getName());
//
//

        for(CardUser user: cardUserList){
            if(user.getName().equals(name) && user.getCardNumber().equals(number)){
                if(saveDetail){
                    mainModel.getUser().setCardUser(user);
                }
                System.out.println(mainModel.getUser().getCardUser());
                return true;
            }
        }

        return false;
    }
}
