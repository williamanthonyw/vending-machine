package assignment2;

import assignment2.model.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

public class MainModelTest {
    private MainModel mainModel;

    @BeforeEach
    public void beforeTests(){
        this.mainModel = new MainModel();
    }

    @Test
    public void getLastFiveProductsModelTest(){
        LastFiveProductsModel lastFiveProductsModel = mainModel.getLastFiveProductsModel();
        assertNotNull(lastFiveProductsModel);
    }

    @Test
    public void getLoginModelTest(){
        LoginModel loginModel = mainModel.getLoginModel();
        assertNotNull(loginModel);
    }
    
    @Test
    public void getCashPaymentModelTest(){
        CashPaymentModel cashPaymentModel = mainModel.getCashPaymentModel();
        assertNotNull(cashPaymentModel);
    }
    
    @Test
    public void getCardPaymentModelTest(){
        CardPaymentModel cardPaymentModel = mainModel.getCardPaymentModel();
        assertNotNull(cardPaymentModel);
    }
    
    @Test
    public void getProductOptionsModelTest(){
        ProductOptionsModel productOptionsModel = mainModel.getProductOptionsModel();
        assertNotNull(productOptionsModel);
    }

    @Test
    public void loginStatusCorrectTest(){
        
        String anonName = mainModel.getUser().getUsername();
        assertEquals("anonymous", anonName);

        assertFalse(mainModel.isLoggedIn());  //not logged in by default

    }

    @Test
    public void setIsLoggedInTest(){
        mainModel.setIsLoggedIn(true);
        assertTrue(mainModel.isLoggedIn());
    }

    // @Test
    // public void logoutTest(){

    // }

    @Test
    public void cancelTransactionTest(){
        mainModel.setIsLoggedIn(true);
        assertTrue(mainModel.isLoggedIn());

        mainModel.cancelTransaction();
        assertFalse(mainModel.isLoggedIn());

    }

    @Test
    public void purchaseProductTest(){

        mainModel.purchaseProduct(new Product("Coca cola", 4.50), 3);
        // need to test purchase been added to users.json
    }


    
}
