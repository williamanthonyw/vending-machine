package assignment2;

import assignment2.model.*;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVParser;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class InventoryModelTest {
    private InventoryModel inventoryModel;
    private JsonParser jsonParser;
    private CSVFileParser csvFileParser;

    @BeforeEach
    public void beforeTests(){

        this.csvFileParser = new CSVFileParser("src/test/resources/inventory.csv","src/test/resources/transaction.csv");

        this.jsonParser = new JsonParser("src/test/resources/InventoryTest1.json",
                "",
                "",
                ""
        );

        List<Product> inventory = jsonParser.getInventory();


        // so we don't write to anything
        this.jsonParser = new JsonParser("",
                "",
                "",
                ""
        );

        this.inventoryModel = new InventoryModel(inventory, jsonParser, csvFileParser);
        this.inventoryModel.initializeProductsToString();

    }

    @Test
    public void getInventoryTest(){
        List<Product> products = inventoryModel.getInventory();
        assertEquals(16, products.size());
        assertEquals(3.95, products.get(0).getPrice());
        assertEquals(7, products.get(0).getQuantity());
        assertEquals("Coca Cola", products.get(2).getName());

    }

    @Test
    public void getCategoriesTest(){
        List<String> categories = inventoryModel.getCategories();
        assertEquals(4, categories.size());
        assertEquals("drinks", categories.get(0));
        assertEquals("candies", categories.get(3));

    }

    @Test
    public void getInventoryByCategoryTest(){
        List<Product> drinks = inventoryModel.getInventory("drinks");
        assertEquals(5, drinks.size());

        List<String> expectedNames = new ArrayList<String>();
        expectedNames.add("Mineral Water");
        expectedNames.add("Sprite");
        expectedNames.add("Coca Cola");
        expectedNames.add("Pepsi");
        expectedNames.add("Juice");

        for (Product drink: drinks){
            if (!expectedNames.contains(drink.getName())){
                fail();
            } else {
                expectedNames.remove(drink.getName());
            }
        }


    }

    @Test
    public void getInventoryByCategoryNoneInCategoryTest(){
        List<Product> health = inventoryModel.getInventory("health");
        assertEquals(0, health.size());
    }

    @Test
    public void getAllProductNamesTest(){
        List<String> allProductNamesExpected = Arrays.asList(new String[] {
                "Mineral Water",
                "Sprite",
                "Coca Cola",
                "Pepsi",
                "Juice",
                "Mars",
                "M&M",
                "Bounty",
                "Snickers",
                "Smiths",
                "Pringles",
                "Kettle",
                "Thins",
                "Mentos",
                "Sour Patch",
                "Skittles"
        });

        assertEquals(allProductNamesExpected, inventoryModel.getAllProductNames());
    }

    @Test
    public void updateQuantityTest(){

        Product testProduct = new Product("name", 5);
        testProduct.setQuantity(20);

        inventoryModel.updateQuantity(testProduct, 2);

        assertEquals(testProduct.getQuantity(), 18);

    }

    @Test
    public void getCodeRangeTest(){

        int[] actual = inventoryModel.getCodeRange("drinks");

        assertEquals(actual.length, 2);
        assertEquals(actual[0], 100);
        assertEquals(actual[1], 199);

    }

    @Test
    public void setProductDetailsValidTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);

        Product testProduct = new Product("name", 5);

        UpdateProductState actual =  inventoryModelTest.setProductDetails(testProduct, "Coca Cola", "drinks", 128, 20, 12);

        assertEquals(actual, UpdateProductState.SUCCESS);
        assertEquals(testProduct.getName(), "Coca Cola");
        assertEquals(testProduct.getCategory(), "drinks");
        assertEquals(testProduct.getCode(), 128);
        assertEquals(testProduct.getPrice(), 20);
        assertEquals(testProduct.getQuantity(), 12);

    }

    @Test
    public void setProductDetailsWrongCategoryTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);

        Product testProduct = new Product("name", 5);
        testProduct.setCategory("drinks");
        testProduct.setCode(102);
        testProduct.setQuantity(2);

        // Coca cole doesn't belong in candies
        UpdateProductState actual =  inventoryModelTest.setProductDetails(testProduct, "Coca Cola", "candies", 128, 20, 12);

        assertEquals(actual, UpdateProductState.CATEGORY_ERROR);

        // shouldn't be updated
        assertEquals(testProduct.getName(), "name");
        assertEquals(testProduct.getCategory(), "drinks");
        assertEquals(testProduct.getCode(), 102);
        assertEquals(testProduct.getPrice(), 5);
        assertEquals(testProduct.getQuantity(), 2);

    }

    @Test
    public void setProductDetailsWrongCodeAboveTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);

        Product testProduct = new Product("name", 5);
        testProduct.setCategory("drinks");
        testProduct.setCode(102);
        testProduct.setQuantity(2);

        // drinks should be in 100-199
        UpdateProductState actual =  inventoryModelTest.setProductDetails(testProduct, "Coca Cola", "drinks", 200, 20, 12);

        assertEquals(actual, UpdateProductState.CODE_RANGE_ERROR);

        // shouldn't be updated
        assertEquals(testProduct.getName(), "name");
        assertEquals(testProduct.getCategory(), "drinks");
        assertEquals(testProduct.getCode(), 102);
        assertEquals(testProduct.getPrice(), 5);
        assertEquals(testProduct.getQuantity(), 2);

    }

    @Test
    public void setProductDetailsWrongCodeBelowTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);

        Product testProduct = new Product("name", 5);
        testProduct.setCategory("drinks");
        testProduct.setCode(102);
        testProduct.setQuantity(2);

        // drinks should be in 100-199
        UpdateProductState actual =  inventoryModelTest.setProductDetails(testProduct, "Coca Cola", "drinks", 99, 20, 12);

        assertEquals(actual, UpdateProductState.CODE_RANGE_ERROR);

        // shouldn't be updated
        assertEquals(testProduct.getName(), "name");
        assertEquals(testProduct.getCategory(), "drinks");
        assertEquals(testProduct.getCode(), 102);
        assertEquals(testProduct.getPrice(), 5);
        assertEquals(testProduct.getQuantity(), 2);

    }

    @Test
    public void setProductDetailsDuplicateProductTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);

        Product testProduct = new Product("name", 5);
        testProduct.setCategory("drinks");
        testProduct.setCode(102);
        testProduct.setQuantity(2);

        // Mineral water already exists
        UpdateProductState actual =  inventoryModelTest.setProductDetails(testProduct, "Mineral Water", "drinks", 122, 20, 12);

        assertEquals(actual, UpdateProductState.DUPLICATE_PRODUCT_ERROR);

        // shouldn't be updated
        assertEquals(testProduct.getName(), "name");
        assertEquals(testProduct.getCategory(), "drinks");
        assertEquals(testProduct.getCode(), 102);
        assertEquals(testProduct.getPrice(), 5);
        assertEquals(testProduct.getQuantity(), 2);

    }

    @Test
    public void setProductDetailsDuplicateCodeTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);

        Product testProduct = new Product("name", 5);
        testProduct.setCategory("drinks");
        testProduct.setCode(102);
        testProduct.setQuantity(2);

        // Sprite is 112
        UpdateProductState actual =  inventoryModelTest.setProductDetails(testProduct, "Coca Cola", "drinks", 112, 20, 12);

        assertEquals(actual, UpdateProductState.CODE_DUPLICATE_ERROR);

        // shouldn't be updated
        assertEquals(testProduct.getName(), "name");
        assertEquals(testProduct.getCategory(), "drinks");
        assertEquals(testProduct.getCode(), 102);
        assertEquals(testProduct.getPrice(), 5);
        assertEquals(testProduct.getQuantity(), 2);

    }

    @Test
    public void setProductDetailsPriceBelowZeroTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);

        Product testProduct = new Product("name", 5);
        testProduct.setCategory("drinks");
        testProduct.setCode(102);
        testProduct.setQuantity(2);

        // Price must be at least $0
        UpdateProductState actual =  inventoryModelTest.setProductDetails(testProduct, "Coca Cola", "drinks", 122, -1, 12);

        assertEquals(actual, UpdateProductState.PRICE_ERROR);

        // shouldn't be updated
        assertEquals(testProduct.getName(), "name");
        assertEquals(testProduct.getCategory(), "drinks");
        assertEquals(testProduct.getCode(), 102);
        assertEquals(testProduct.getPrice(), 5);
        assertEquals(testProduct.getQuantity(), 2);

    }

    @Test
    public void setProductDetailsQuantityBelowZeroTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);

        Product testProduct = new Product("name", 5);
        testProduct.setCategory("drinks");
        testProduct.setPrice(5);
        testProduct.setCode(102);
        testProduct.setQuantity(2);

        // Quantity should be 0-15
        UpdateProductState actual =  inventoryModelTest.setProductDetails(testProduct, "Coca Cola", "drinks", 122, 12, -1);

        assertEquals(actual, UpdateProductState.QUANTITY_ERROR);

        // shouldn't be updated
        assertEquals(testProduct.getName(), "name");
        assertEquals(testProduct.getCategory(), "drinks");
        assertEquals(testProduct.getCode(), 102);
        assertEquals(testProduct.getPrice(), 5);
        assertEquals(testProduct.getQuantity(), 2);

    }

    @Test
    public void setProductDetailsQuantityAboveFifteenTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);

        Product testProduct = new Product("name", 5);
        testProduct.setCategory("drinks");
        testProduct.setCode(102);
        testProduct.setQuantity(2);

        // Quantity should be 0-15
        UpdateProductState actual =  inventoryModelTest.setProductDetails(testProduct, "Coca Cola", "drinks", 122, 12, 16);

        assertEquals(actual, UpdateProductState.QUANTITY_ERROR);

        // shouldn't be updated
        assertEquals(testProduct.getName(), "name");
        assertEquals(testProduct.getCategory(), "drinks");
        assertEquals(testProduct.getCode(), 102);
        assertEquals(testProduct.getPrice(), 5);
        assertEquals(testProduct.getQuantity(), 2);

    }

    @Test
    public void addProductSuccessfulTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);
        inventoryModelTest.initializeProductsToString();

        Product testProduct = new Product("name", 5);
        testProduct.setCategory("drinks");
        testProduct.setCode(102);
        testProduct.setQuantity(2);

        UpdateProductState actual =  inventoryModelTest.addProduct(testProduct, "Coca Cola", "drinks", 122, 12, 12);

        assertEquals(actual, UpdateProductState.SUCCESS);
        assertTrue(inventoryModelTest.getInventory().contains(testProduct));

    }

    @Test
    public void addProductUnsuccessfulTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);
        inventoryModelTest.initializeProductsToString();

        Product testProduct = new Product("name", 5);
        testProduct.setCategory("drinks");
        testProduct.setCode(102);
        testProduct.setQuantity(2);

        UpdateProductState actual =  inventoryModelTest.addProduct(testProduct, "Coca Cola", "drinks", 122, 12, -1);

        assertEquals(actual, UpdateProductState.QUANTITY_ERROR);

        // shouldn't be added to inventory
        assertFalse(inventoryModelTest.getInventory().contains(testProduct));

    }

    @Test
    public void updateProductSuccessfulTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);
        inventoryModelTest.initializeProductsToString();

        Product product = inventoryModelTest.getInventory().get(0);

        UpdateProductState actual =  inventoryModelTest.updateProduct(product, "Coca Cola", "drinks", 122, 12, 12);

        assertEquals(actual, UpdateProductState.SUCCESS);
        assertTrue(inventoryModelTest.getInventory().contains(product));

    }

    @Test
    public void updateProductUnsuccessfulTest(){

        JsonParser temp = new JsonParser(                "src/test/resources/test_inventory.json",
                "",
                "",
                ""
        );

        InventoryModel inventoryModelTest = new InventoryModel(temp.getInventory(), jsonParser, csvFileParser);
        inventoryModelTest.initializeProductsToString();
        
        Product product = inventoryModelTest.getInventory().get(0);

        UpdateProductState actual =  inventoryModelTest.updateProduct(product, "Coca Cola", "drinks", 122, 12, -12);

        assertEquals(actual, UpdateProductState.QUANTITY_ERROR);
        // PRODUCT remains
        assertTrue(inventoryModelTest.getInventory().contains(product));

    }

    @Test
    public void putBackTest(){

        Map<Product, Integer> cart = new LinkedHashMap<Product, Integer>();

        Product cocaCola = inventoryModel.getInventory().get(2);
        Product skittles = inventoryModel.getInventory().get(inventoryModel.getInventory().size() - 1);

        cart.put(cocaCola, 2);
        cart.put(skittles, 2);

        inventoryModel.putBack(cart);

        assertEquals(cocaCola.getName(), "Coca Cola");
        assertEquals(skittles.getName(), "Skittles");
        assertEquals(cocaCola.getQuantity(), 9);
        assertEquals(skittles.getQuantity(), 9);

    }


    @Test
    public void deleteProductTest(){

        Map<Product, Integer> cart = new LinkedHashMap<Product, Integer>();

        Product cocaCola = inventoryModel.getInventory().get(2);

        inventoryModel.deleteProduct(cocaCola);

        assertFalse(inventoryModel.getInventory().contains(cocaCola));


    }







}
