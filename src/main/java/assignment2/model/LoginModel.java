package assignment2.model;

import java.util.List;

public class LoginModel {

    private List<User> users;
    private User anonymousUser;

    public LoginModel(List<User> users){
        this.users = users;

        for (User user : users){

            if (user.getUsername().equals("anonymous")){
                this.anonymousUser = user;
            }

        }
    }

    public List<User> getUsers(){
        return users;
    }



    public User getAnonymousUser(){
        return anonymousUser;
    }

    public User login(String username, String password){
        for (User user : users){

            if (user.getUsername().equals(username)){

                if (user.getPassword().equals(password)){
                    return user;
                }
            }

        }

        return null;
    }


}