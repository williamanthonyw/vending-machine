package assignment2.model;

import java.util.List;

public class LastFiveProductsModel {

    public List<Purchase> getLastFiveProductsBoughtByUser(User user){

        int size = user.getPurchases().size();

        if (size <= 5){
            // get all the  purchases
            return user.getPurchases();
        } else {

            // get the last 5 purchases from list
            return user.getPurchases().subList(size - 5, size);
        }

    }
}