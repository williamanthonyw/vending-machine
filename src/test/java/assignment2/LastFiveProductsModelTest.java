package assignment2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import assignment2.model.*;

import java.util.List;

public class LastFiveProductsModelTest {

    private User user;
    private LastFiveProductsModel lastFiveProductsModel;

    @BeforeEach
    public void setup(){

        this.user = new User("test", "pw");

        this.lastFiveProductsModel = new LastFiveProductsModel();

    }

    @Test
    public void getLastFiveProductsBoughtByUserTestEmpty(){

        // GIVEN user with no purchases

        assertEquals(lastFiveProductsModel.getLastFiveProductsBoughtByUser(user).size(), 0);

    }

    @Test
    public void getLastFiveProductsBoughtByUserTestLessThanFivePurchases(){

        Product product1 = new Product("milk", 28);
        Product product2 = new Product("cheese", 28);
        Product product3 = new Product("milk", 28);


        user.purchaseProduct(product1, 2);
        user.purchaseProduct(product2, 2);
        user.purchaseProduct(product3, 2);

        // GIVEN user with 3 purchases
        List<Purchase> actual = lastFiveProductsModel.getLastFiveProductsBoughtByUser(user);
        assertEquals(lastFiveProductsModel.getLastFiveProductsBoughtByUser(user).size(), 2);

        // should contain only last five products bought
        assertEquals(actual.get(0).getItem(), product3.getName());
        assertEquals(actual.get(1).getItem(), product2.getName());

    }

    @Test
    public void getLastFiveProductsBoughtByUserTestExactlyFivePurchasesWithRepeats(){

        Product product1 = new Product("milk1", 28);
        Product product2 = new Product("milk2", 28);
        Product product3 = new Product("milk1", 28);
        Product product4 = new Product("milk4", 28);
        Product product5 = new Product("milk4", 28);

        user.purchaseProduct(product1, 2);
        user.purchaseProduct(product2, 2);
        user.purchaseProduct(product3, 2);
        user.purchaseProduct(product4, 2);
        user.purchaseProduct(product5, 2);

        // GIVEN user with 5 purchases
        List<Purchase> actual = lastFiveProductsModel.getLastFiveProductsBoughtByUser(user);
        assertEquals(actual.size(), 3);

        assertEquals(actual.get(0).getItem(), product5.getName());
        assertEquals(actual.get(1).getItem(), product1.getName());
        assertEquals(actual.get(2).getItem(), product2.getName());
    }

    @Test
    public void getLastFiveProductsBoughtByUserTestExactlyFivePurchasesWithoutRepeats(){

        Product product1 = new Product("milk1", 28);
        Product product2 = new Product("milk2", 28);
        Product product3 = new Product("milk3", 28);
        Product product4 = new Product("milk4", 28);
        Product product5 = new Product("milk5", 28);

        user.purchaseProduct(product1, 2);
        user.purchaseProduct(product2, 2);
        user.purchaseProduct(product3, 2);
        user.purchaseProduct(product4, 2);
        user.purchaseProduct(product5, 2);

        // GIVEN user with 5 purchases
        List<Purchase> actual = lastFiveProductsModel.getLastFiveProductsBoughtByUser(user);
        assertEquals(actual.size(), 5);

        assertEquals(actual.get(0).getItem(), product5.getName());
        assertEquals(actual.get(1).getItem(), product4.getName());
        assertEquals(actual.get(2).getItem(), product3.getName());
        assertEquals(actual.get(3).getItem(), product2.getName());
        assertEquals(actual.get(4).getItem(), product1.getName());
    }


    @Test
    public void getLastFiveProductsBoughtByUserTestMoreThanFivePurchases(){

        Product product1 = new Product("honey", 28);
        Product product2 = new Product("milk", 28);
        Product product3 = new Product("cheese", 28);
        Product product4 = new Product("milk", 28);
        Product product5 = new Product("skittles", 28);
        Product product6 = new Product("water", 28);
        Product product7 = new Product("candy", 28);
        Product product8 = new Product("water", 28);
        Product product9 = new Product("melon", 28);
        Product product10 = new Product("water", 28);

        user.purchaseProduct(product1, 2);
        user.purchaseProduct(product2, 2);
        user.purchaseProduct(product3, 2);
        user.purchaseProduct(product4, 2);
        user.purchaseProduct(product5, 2);
        user.purchaseProduct(product6, 2);
        user.purchaseProduct(product7, 2);
        user.purchaseProduct(product8, 2);
        user.purchaseProduct(product9, 2);
        user.purchaseProduct(product10, 2);

        // GIVEN user with 10 purchases
        List<Purchase> actual = lastFiveProductsModel.getLastFiveProductsBoughtByUser(user);
        assertEquals(actual.size(), 5);

        // should contain only last five products bought
        assertEquals(actual.get(0).getItem(), product10.getName());
        assertEquals(actual.get(1).getItem(), product9.getName());
        assertEquals(actual.get(2).getItem(), product7.getName());
        assertEquals(actual.get(3).getItem(), product5.getName());
        assertEquals(actual.get(4).getItem(), product4.getName());


    }



}