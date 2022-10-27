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

    public boolean paymentProcess(String name, String number,boolean saveDetail){

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
