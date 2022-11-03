package assignment2;

import static org.junit.jupiter.api.Assertions.*;

import assignment2.model.CSVFileParser;
import assignment2.model.JsonParser;
import assignment2.model.LoginModel;
import assignment2.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class LoginTest {
    private LoginModel loginModel;
    private List<User> userList;
    private User user1;
    private User anonymousUser;

    @BeforeEach
    public void setup(){
        user1 = new User("Allen","1234");
        userList = new ArrayList<User>();
        userList.add(user1);
        anonymousUser = new User("anonymous","");
        userList.add(anonymousUser);

        JsonParser jsonParser = new JsonParser("", "", "", "");
        CSVFileParser cvs = new CSVFileParser("", "", "", "", "src/test/resources/users.csv", "");
        loginModel = new LoginModel(userList,  jsonParser, cvs);
    }

    @Test
    public void getUsersTest(){
        List<User> userTest = loginModel.getUsers();
        assertEquals(userList,userTest);
    }

    @Test
    public void getAnonymousUser(){
        User userTest = loginModel.getAnonymousUser();
        assertEquals(anonymousUser,userTest);
    }

    @Test
    public void loginTest(){
        User userTest = loginModel.login("Allen","1234");
        assertEquals(userTest,user1);
    }

    @Test
    public void addUserTest(){
        User user2 = new User("Allen1","4567");
        assertTrue(loginModel.addUser(user2));
        assertFalse(loginModel.addUser(user2));
    }
}
