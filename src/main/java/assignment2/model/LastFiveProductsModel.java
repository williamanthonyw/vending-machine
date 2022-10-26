package assignment2.model;

import java.util.ArrayList;
import java.util.List;

public class LastFiveProductsModel {

    public static boolean containsProductName(String name, List<Purchase> purchases){

        for (Purchase p : purchases){
            if (p.getItem().equals(name)){
                return true;
            }
        }

        return false;
    }

    public List<Purchase> getLastFiveProductsBoughtByUser(User user){

        int size = user.getPurchases().size();

        List<Purchase> lastFiveProducts = new ArrayList<Purchase>();

        for (int i = size - 1; i >= 0; i--){

            if (lastFiveProducts.size() >= 5){
                break;
            }

            Purchase current = user.getPurchases().get(i);

            if (!containsProductName(current.getItem(), lastFiveProducts)){
                lastFiveProducts.add(current);
            }

        }

        return lastFiveProducts;


    }
}