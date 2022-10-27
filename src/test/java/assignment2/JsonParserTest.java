package assignment2;

import assignment2.model.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;

public class JsonParserTest {


    private JsonParser jsonParser;

    @BeforeEach
    public void setup(){

        jsonParser = new JsonParser();

    }


    @Test
    public void getUsersFileDoesNotExist(){


        List<User> actual = jsonParser.getUsers("not exist");

        assertNotNull(actual);
        assertEquals(actual.size(), 0);

    }

    @Test
    public void getUsersEmptyFile(){


        List<User> actual = jsonParser.getUsers("src/test/resources/empty_users.json");

        assertNotNull(actual);
        assertEquals(actual.size(), 0);

    }

    @Test
    public void getUsersValidFile(){

        List<User> actual = jsonParser.getUsers("src/test/resources/test_users.json");

        assertNotNull(actual);
        assertEquals(actual.size(), 2);

    }

    @Test
    public void updateUsersEmptyList(){

        List<User> users = new ArrayList<User>();
        String filename = "src/test/resources/test_users2.json";

        jsonParser.updateUsers(users, filename);

         try {
             String file = Files.readString(Path.of(filename));
             assertEquals(file, "[]");
         } catch (IOException e){
             fail();
         }

    }

    @Test
    public void updateUsersValidListNoPurchases(){

        List<User> users = new ArrayList<User>();
        users.add(new User("test1", "pw"));
        users.add(new User("test2", "pw"));
        users.add(new User("test3", "pw"));

        String filename = "src/test/resources/test_users3.json";

        jsonParser.updateUsers(users, filename);

        try {
            String file = Files.readString(Path.of(filename));
            assertEquals(file, "[{\"username\":\"test1\",\"password\":\"pw\",\"purchases\":[],\"cart\":{}},{\"username\":\"test2\",\"password\":\"pw\",\"purchases\":[],\"cart\":{}},{\"username\":\"test3\",\"password\":\"pw\",\"purchases\":[],\"cart\":{}}]");
        } catch (IOException e){
            fail();
        }

    }

    @Test
    public void updateUsersValidListWithPurchases(){

        List<User> users = new ArrayList<User>();
        users.add(new User("test1", "pw"));
        users.add(new User("test2", "pw"));
        users.add(new User("test3", "pw"));

        Purchase purchase1 = new Purchase("milk", 2, 3, LocalDateTime.of(2022, 2, 2, 2, 2));
        Purchase purchase2 = new Purchase("milk", 2, 3, LocalDateTime.of(2023, 3, 3, 3, 3));
        Purchase purchase3 = new Purchase("milk", 2, 3, LocalDateTime.of(2024, 4, 4, 4, 4));

        List<Purchase> purchaseList1 = new ArrayList<Purchase>();
        purchaseList1.add(purchase1);
        List<Purchase> purchaseList2 = new ArrayList<Purchase>();
        purchaseList2.add(purchase2);
        List<Purchase> purchaseList3 = new ArrayList<Purchase>();
        purchaseList3.add(purchase3);

        users.get(0).setPurchases(purchaseList1);
        users.get(1).setPurchases(purchaseList2);
        users.get(2).setPurchases(purchaseList3);


        String filename = "src/test/resources/test_users2.json";

        jsonParser.updateUsers(users, filename);

        try {
            String file = Files.readString(Path.of(filename));
            assertEquals(file, "[{\"username\":\"test1\",\"password\":\"pw\",\"purchases\":[{\"price\":2.0,\"quantity\":3,\"item\":\"milk\",\"datePurchased\":{\"date\":{\"year\":2022,\"month\":2,\"day\":2},\"time\":{\"hour\":2,\"minute\":2,\"second\":0,\"nano\":0}}}],\"cart\":{}},{\"username\":\"test2\",\"password\":\"pw\",\"purchases\":[{\"price\":2.0,\"quantity\":3,\"item\":\"milk\",\"datePurchased\":{\"date\":{\"year\":2023,\"month\":3,\"day\":3},\"time\":{\"hour\":3,\"minute\":3,\"second\":0,\"nano\":0}}}],\"cart\":{}},{\"username\":\"test3\",\"password\":\"pw\",\"purchases\":[{\"price\":2.0,\"quantity\":3,\"item\":\"milk\",\"datePurchased\":{\"date\":{\"year\":2024,\"month\":4,\"day\":4},\"time\":{\"hour\":4,\"minute\":4,\"second\":0,\"nano\":0}}}],\"cart\":{}}]");
        } catch (IOException e){
            fail();
        }

    }
}