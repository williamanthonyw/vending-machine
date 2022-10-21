package assignment2.view;

import assignment2.model.LoginModel;
import assignment2.model.MainModel;
import assignment2.model.User;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoginView implements View{
    private MainModel mainModel;
    private LoginModel loginModel;
    private MainView mainView;
    private Scene scene;
    private BorderPane root;

    private VBox mainBox;
    private VBox loginBox;
    private VBox registerBox;

    public LoginView(MainModel mainModel, MainView mainView){
        this.mainModel = mainModel;
        this.loginModel = mainModel.getLoginModel();
        this.mainView = mainView;
    }

    @Override
    public List<Scene> getScenes(){
        return Arrays.asList(new Scene[] { scene });
    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN) {
        mainBox.getChildren().add(0, menuBTN);
    }

    @Override
    public void setUp(){
        root = new BorderPane();
        scene = new Scene(root, 1000,600);
        scene.getStylesheets().add("Style.css");

        root = new BorderPane();
        scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add("Style.css");

        mainBox = new VBox(30);
        BorderPane.setMargin(mainBox, new Insets(50, 50, 50, 50));
        root.setCenter(mainBox);

        loginBox = new VBox(20);
        root.setLeft(loginBox);
        BorderPane.setMargin(loginBox,new Insets(0,0,0,200));
        loginBox.setAlignment(Pos.CENTER);
        loginBox.getStylesheets().add("vbox");

        setUpLogin();

        registerBox = new VBox(20);
        root.setRight(registerBox);
        BorderPane.setMargin(registerBox, new Insets(0, 200, 0, 0));
        registerBox.setAlignment(Pos.CENTER);
        registerBox.getStylesheets().add("vbox");

        setRegisterBox();
    }

    public void setUpLogin(){

        Label loginTitle = new Label("Login");
        loginBox.getChildren().add(loginTitle);

        Label userNameLBL = new Label("Username: ");
        TextField userNameTF = new TextField();
        loginBox.getChildren().addAll(userNameLBL, userNameTF);

        Label passwordLBL = new Label("Password: ");
        PasswordField passwordTF = new PasswordField();
        passwordTF.setSkin(new PasswordSkin(passwordTF));
        loginBox.getChildren().addAll(passwordLBL, passwordTF);

        Region space = new Region();
        loginBox.getChildren().add(space);

        Button signInBTN = new Button("Sign in");
        loginBox.getChildren().add(signInBTN);

        Label signInErrorLBL = new Label("");
        loginBox.getChildren().add(signInErrorLBL);

        signInBTN.setOnAction((ActionEvent e)->{

            if (mainModel.login(userNameTF.getText(),passwordTF.getText())){
                //View update?
                mainView.goToLastFiveProductsView();
            } else {

                signInErrorLBL.setText("Incorrect username or password");
            }

        });

    };

    public void setRegisterBox(){
        Label registerTitle = new Label("Register");
        registerBox.getChildren().add(registerTitle);

        Label newUserNameLBL = new Label("Username: ");
        TextField newUserNameTF = new TextField();
        registerBox.getChildren().addAll(newUserNameLBL, newUserNameTF);

        Label newPasswordLBL = new Label("Password: ");
        PasswordField newPasswordTF = new PasswordField();
        newPasswordTF.setSkin(new PasswordSkin(newPasswordTF));
        registerBox.getChildren().addAll(newPasswordLBL, newPasswordTF);

        Region space = new Region();
        registerBox.getChildren().add(space);

        Button registerBTN = new Button("Register");
        registerBox.getChildren().add(registerBTN);

        Label registerErrorLBL = new Label("");
        registerBox.getChildren().add(registerErrorLBL);

        registerBTN.setOnAction((ActionEvent e)->{
            User user = new User(newUserNameTF.getText(),newPasswordTF.getText());
            if(loginModel.addUser(user)){

                mainModel.login(newUserNameTF.getText(),newPasswordTF.getText());

                mainView.goToLastFiveProductsView();
            }else{
                registerErrorLBL.setText("Account with username already exists!");
            }
        });
    }

    @Override
    public void setUpCancelBTN(Button cancelBTN){
//        mainBox.getChildren().add(cancelBTN);
    }

}
