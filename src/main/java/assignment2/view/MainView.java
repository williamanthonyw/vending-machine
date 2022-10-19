package assignment2.view;

import assignment2.model.MainModel;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainView {

    private MainModel mainModel;
    private Stage stage;
    private MenuButton menuBTN;

    public MainView(MainModel mainModel){
        this.mainModel = mainModel;

        setUpMenu();
    }

    public void setUp(Stage stage){
        this.stage = stage;
        goToCardPaymentView();
        stage.show();
    }

    public void goToView(View view){
        view.setUp();
        stage.setScene(view.getScene());
        view.setUpMenuBTN(menuBTN);
    }

    public void goToLastFiveProductsView(){
        goToView(new LastFiveProductsView(mainModel));
    }

    public void goToLoginView(){
        goToView(new LoginView(mainModel));
    }

    public void goToCardPaymentView(){
        goToView(new CardPaymentView(mainModel));
    }

    public void setUpMenu() {

        this.menuBTN = new MenuButton();

        ImageView menuIV = new ImageView(new Image("white-menu.png"));
        menuIV.setFitHeight(20.0);
        menuIV.setFitWidth(20.0);
        menuBTN.setGraphic(menuIV);
        menuBTN.setOnMouseEntered((e) -> {
            menuBTN.fire();
        });

        MenuItem loginBTN = new MenuItem("Login");
        loginBTN.setOnAction((ActionEvent e) -> {
            goToLoginView();
        });

        menuBTN.getItems().addAll(loginBTN);


    }
}


