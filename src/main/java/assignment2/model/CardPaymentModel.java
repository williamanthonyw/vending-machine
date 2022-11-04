package assignment2.model;
import java.util.List;
public class CardPaymentModel {

    private JsonParser jsonParser;
    private List<CardUser> cardUserList;
    private MainModel mainModel;
    private String cardFile;

    public CardPaymentModel(MainModel mainModel, JsonParser jsonParser){
        this.mainModel = mainModel;
        this.jsonParser = jsonParser;

        this.cardUserList = jsonParser.getCardDetail();

    }

    public void saveCard(CardUser cardUser){
        mainModel.getUser().setCardUser(cardUser);
        jsonParser.updateUsers(mainModel.getLoginModel().getUsers());
    }

    public CardUser paymentProcess(String name, String number){

        for(CardUser user: cardUserList){
            if(user.getName().equals(name) && user.getCardNumber().equals(number)){
                return user;
            }
        }

        return null;
    }
}
