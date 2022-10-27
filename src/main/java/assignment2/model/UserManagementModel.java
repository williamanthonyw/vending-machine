package assignment2.model;
import java.util.*;

public class UserManagementModel {
    private List<User> users;
    private JsonParser jsonParser;

    public UserManagementModel(List<User> users){
        this.users = users;
    }
    

    public List<User> getUsers(){
        return this.users;
    }


}
