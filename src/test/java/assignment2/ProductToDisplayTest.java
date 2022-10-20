package assignment2;

import assignment2.model.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;

import javax.accessibility.AccessibleAttributeSequence;

public class ProductToDisplayTest {  //the product obj with relevant fields used to populate table

    private ProductToDisplay productToDisplay;

    @BeforeEach
    public void beforeTests(){
        this.productToDisplay = new ProductToDisplay(new Product("Coca cola", 4.50));

    }

    @Test
    public void nameCorrectTest(){
        assertEquals("Coca cola", productToDisplay.getName());

        productToDisplay.setName("Pepsi");
        assertEquals("Pepsi", productToDisplay.getName());
    }

    @Test
    public void codeCorrectTest(){
    
        productToDisplay.setCode(11);
        assertEquals(11, productToDisplay.getCode());
    }

    @Test
    public void categoryCorrectTest(){
        productToDisplay.setCategory("drinks");
        assertEquals("drinks", productToDisplay.getCategory());
    }

    @Test
    public void priceCorrectTest(){
        assertEquals(4.50, productToDisplay.getPrice());

        productToDisplay.setPrice(4.28);
        assertEquals(4.28, productToDisplay.getPrice());
    }

    @Test
    public void quantityCorrectTest(){
        productToDisplay.setQuantity(4);
        assertEquals(4, productToDisplay.getQuantity());
    }
}
