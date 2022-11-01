package assignment2;

import assignment2.model.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

public class MainModelTest {
    private MainModel mainModel;
    


    @BeforeEach
    public void beforeTests(){
        this.mainModel = new MainModel(
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );
        mainModel.getInventoryModel().initializeProductsToString();
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
    public void getInventoryModelTest(){
       InventoryModel inventoryModel = mainModel.getInventoryModel();
        assertNotNull(inventoryModel);
    }

    @Test
    public void loginStatusCorrectTest(){

        User anon = new User("anonymous", "pw");
        mainModel.getLoginModel().setAnonymousUser(anon);
        mainModel.setUser(anon);
        
        String anonName = mainModel.getUser().getUsername();
        assertEquals("anonymous", anonName);

        assertFalse(mainModel.isLoggedIn());  //not logged in by default

    }

    @Test
    public void setIsLoggedInTest(){
        mainModel.setIsLoggedIn(true);
        assertTrue(mainModel.isLoggedIn());
    }

    @Test
    public void cancelTransactionTest(){

        User user = new User("test", "pw");
        mainModel.setUser(user);

        User anon = new User("anonymous", "pw");
        mainModel.getLoginModel().setAnonymousUser(anon);

        mainModel.setIsLoggedIn(true);
        assertTrue(mainModel.isLoggedIn());

        mainModel.cancelTransaction();
        assertFalse(mainModel.isLoggedIn());

    }

    @Test
    public void checkoutTest(){

        this.mainModel = new MainModel(
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );

        mainModel.getInventoryModel().initializeProductsToString();

        User user = new User("test", "pw");
        mainModel.setUser(user);

        User anon = new User("anonymous", "pw");

        mainModel.getLoginModel().setAnonymousUser(anon);

        Map<Product, Integer> cart = new LinkedHashMap<Product, Integer>();

        Product cocaCola = new Product("Coca Cola", 2 );
        Product skittles = new Product("Skittles", 2 );

        cart.put(cocaCola, 2);
        cart.put(skittles, 2);

        user.setCart(cart);

        mainModel.checkout("card");

        assertEquals(user.getPurchases().size(), 2 );
        assertEquals(mainModel.getCart().size(), 0);

        // should log them out
        assertEquals(mainModel.isLoggedIn(), false);


    }

    @Test
    public void addToCartTest(){


        User user = new User("test", "pw");
        mainModel.setUser(user);

        Product cocaCola = new Product("Coca Cola", 2 );

        mainModel.getInventoryModel().addProduct(cocaCola,
                                        "Coca Cola",
                                        "drinks",
                                                121,
                                                12, 3);

        mainModel.addToCart(cocaCola,2);

        assertEquals(user.getCart().size(),1);
        assertEquals(user.getCart().get(cocaCola), 2);

        assertTrue(mainModel.getInventoryModel().getInventory().contains(cocaCola));
        assertEquals(cocaCola.getQuantity(), 1); // added 3 removed 2
    }


    @Test
    public void getCartPriceTest(){

        User user = new User("test", "pw");
        mainModel.setUser(user);

        Product cocaCola = new Product("Coca Cola", 3 );
        Product skittles = new Product("Skittles", 2.3 );

        HashMap<Product, Integer> cart = new LinkedHashMap<Product, Integer>();

        cart.put(cocaCola,2);
        cart.put(skittles, 1);

        user.setCart(cart);

        assertEquals(mainModel.getCartPrice(), 8.3);
    }

    @Test
    public void loginFailTest(){

//        User anon = new User("anonymous", "pw");
//        mainModel.getLoginModel().setAnonymousUser(anon);

        assertFalse(mainModel.login("doesn't exist", "pw"));

        // should not be logged in stay at anon
        assertFalse(mainModel.isLoggedIn());
        assertEquals(mainModel.getUser(), mainModel.getLoginModel().getAnonymousUser());

    }

    @Test
    public void loginSuccessTest(){

        User testUser = new User("test", "pw");

        mainModel.getLoginModel().addUser(testUser);

        assertTrue(mainModel.login("test", "pw"));

        // should be logged in
        assertTrue(mainModel.isLoggedIn());
        assertEquals(mainModel.getUser(),testUser);

    }











}
