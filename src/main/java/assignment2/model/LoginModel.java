package assignment2.model;

import java.util.HashMap;
import java.util.List;

public class LoginModel {

    private List<User> users;
    private User anonymousUser;
    private JsonParser jsonParser;
    private CSVFileParser csvFileParser;
    private MainModel mainModel;

    public LoginModel(List<User> users,JsonParser jsonParser, CSVFileParser csvFileParser){
        this.users = users;


        for (User user : users){

            if (user.getUsername().equals("anonymous")){
                this.anonymousUser = user;
            }

        }
        this.jsonParser = jsonParser;
        this.csvFileParser = csvFileParser;
    }

    public void setAnonymousUser(User user){
        this.anonymousUser = user;
    }

    public List<User> getUsers(){
        return users;
    }

    public User getAnonymousUser(){
        return anonymousUser;
    }

    public User login(String username, String password){


        for (User user : users){

            if (user.getUsername().equalsIgnoreCase(username)){

                if (user.getPassword().equals(password)){

                    // reset cart for user logged in
                    user.clearCart();

                    
                    return user;
                }
            }

        }

        return null;
    }

    public boolean addUser(User user){

        for(User user1: users){
            if(user1.getUsername().equalsIgnoreCase(user.getUsername())){
                return false;
            }
        }
        users.add(user);
        jsonParser.updateUsers(users);
        csvFileParser.updateUsers(users);
        return true;
    }

    public void removeUser(User user){
        this.users.remove(user);
        jsonParser.updateUsers(this.users);
        csvFileParser.updateUsers(this.users);
    }

}