package assignment2;

import assignment2.model.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;

public class InventoryModelTest {
    private InventoryModel inventoryModel;

    @BeforeEach
    public void beforeTests(){
        this.inventoryModel= new InventoryModel("src/main/resources/InventoryTest1.json");

    }

    @Test
    public void getProductsToDisplayTest(){
        List<Product> products = inventoryModel.getInventory();
        assertEquals(16, products.size());
        assertEquals(3.95, products.get(0).getPrice());
        assertEquals(7, products.get(0).getQuantity());
        assertEquals("Coca cola", products.get(2).getName());

    }

    @Test
    public void getCategoriesTest(){
        List<String> categories = inventoryModel.getCategories();
        assertEquals(4, categories.size());
        assertEquals("drinks", categories.get(0));
        assertEquals("candies", categories.get(3));

    }
}
