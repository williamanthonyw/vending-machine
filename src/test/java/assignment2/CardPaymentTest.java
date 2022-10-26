package assignment2;

import assignment2.model.CardPaymentModel;
import assignment2.model.CardUser;
import assignment2.model.MainModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardPaymentTest {
    private CardPaymentModel cardPaymentModel;
    private MainModel mainModel;

    @BeforeEach
    public void setUp(){
        mainModel = new MainModel();
        cardPaymentModel = new CardPaymentModel(mainModel);
        CardUser cardUser = new CardUser("Allen","12345");
    }
    // @Test
    // public void paymentProcessTest(){
    //     cardPaymentModel.paymentProcess("Kasey","60146");
    // }
}
