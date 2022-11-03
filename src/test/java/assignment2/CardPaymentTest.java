package assignment2;

import assignment2.model.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardPaymentTest {
    private CardPaymentModel cardPaymentModel;
    private MainModel mainModel;
    private JsonParser jsonParser;
    private CSVFileParser csvFileParser;
    private CardUser InvalidCardUser;
    private CardUser ValidCardUser;

    @BeforeEach
    public void setUp(){
        this.jsonParser = new JsonParser("","src/test/resources/test_users.json","","src/test/resources/credit_cards.json");
        this.csvFileParser = new CSVFileParser("","","","","","");


        this.mainModel = new MainModel(this.jsonParser,this.csvFileParser);
        cardPaymentModel = new CardPaymentModel(mainModel, jsonParser);
        InvalidCardUser = new CardUser("Allen","12345");
        ValidCardUser = new CardUser("Charles","40691");
    }
    @Test
    public void paymentProcessTest(){
        assertNull(cardPaymentModel.paymentProcess("Allen","12345"));
        assertNotEquals(ValidCardUser,cardPaymentModel.paymentProcess("Charles",ValidCardUser.getCardNumber()));
    }

    @Test
    public void saveCardTest(){
        cardPaymentModel.saveCard(ValidCardUser);
    }
}
