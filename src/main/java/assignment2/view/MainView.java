package assignment2.view;

import assignment2.model.MainModel;
import assignment2.model.Product;
import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainView {

    private MainModel mainModel;
    private Stage stage;
    private MenuButton menuBTN;
    private Button cancelBTN;
    private Transition timer;


    public MainView(MainModel mainModel){
        this.mainModel = mainModel;

        setUpCancelBTN();
        setUpCancelOnTimeOut();
    }

    public void setUp(Stage stage){
        this.stage = stage;
        //goToSellerInventoryView();
        goToModifyCashView();

        stage.show();
    }

    private void goToSellerInventoryView() {
        goToView(new SellerInventoryView(this.mainModel, this));
    }

    public void goToView(View view){
        view.setUp();
        stage.setScene(view.getScenes().get(0));
        setUpMenu();
        view.setUpMenuBTN(menuBTN);
        view.setUpCancelBTN(cancelBTN);

        // restart timer on key press
        for (Scene scene : view.getScenes()){
            scene.addEventFilter(InputEvent.ANY, evt -> {

                if (evt.getEventType().getName().equals("MOUSE_PRESSED") ||
                        evt.getEventType().getName().equals("KEY_PRESSED")){
                    timer.playFromStart();
                }
            }
            );
        }

    }

    public void returnToView(View view){
        stage.setScene(view.getScenes().get(0));
        view.refresh();
    }

    public void goToProductOptionsView(){
        goToView(new ProductOptionsView(mainModel, stage, this));
    }

    public void goToModifyCashView(){goToView(new ModifyCashView(mainModel,stage,this));}

    public void goToLoginView(){
        goToView(new LoginView(mainModel, this));
    }

    public void goToCardPaymentView(){
        goToView(new CardPaymentView(mainModel, this));
    }

    public void goToCashPaymentView(){ goToView(new CashPaymentView(mainModel, this)); }

    public void setUpCancelOnTimeOut(){

        Duration delay = Duration.seconds(120);

        this.timer = new PauseTransition(delay);

        timer.setOnFinished(evt -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cancel Transaction");
            alert.setHeaderText("Your transaction has been cancelled");
            if (mainModel.isLoggedIn()){
                alert.setContentText("Your cart has been cleared and you have been logged out.");
            } else {
                alert.setContentText("Your cart has been cleared.");
            }

            mainModel.cancelTransaction();

            Platform.runLater(()-> {
                alert.showAndWait();
            });

            // back to default page
            goToProductOptionsView();

            timer.playFromStart();
        });


        timer.play();



    }

    public void setUpCancelBTN(){

        this.cancelBTN = new Button("Cancel");

        cancelBTN.setOnAction((ActionEvent e) -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cancel Transaction");
            alert.setHeaderText("Your transaction has been cancelled");
            if (mainModel.isLoggedIn()){
                alert.setContentText("Your cart has been cleared and you have been logged out.");
            } else {
                alert.setContentText("Your cart has been cleared.");
            }

            mainModel.cancelTransaction();

            alert.showAndWait();

            // back to default page
            goToProductOptionsView();
        });

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

        // MenuItem homeBTN = new MenuItem("Home");
        // homeBTN.setOnAction((ActionEvent e) -> {
        //     goToLastFiveProductsView();
        // });

        MenuItem loginBTN = new MenuItem("Login");
        loginBTN.setOnAction((ActionEvent e) -> {
            goToLoginView();
        });

        MenuItem logoutBTN = new MenuItem("Logout");

        logoutBTN.setOnAction((ActionEvent e) -> {
            if (mainModel.isLoggedIn()){
                mainModel.logout();
                goToProductOptionsView();
            } else {

                Alert notLoggedin = new Alert(Alert.AlertType.ERROR);
                notLoggedin.setHeaderText("You were not logged in");
                notLoggedin.showAndWait();

            }

        });


        MenuItem productOptionsBTN = new MenuItem("Products");
        productOptionsBTN.setOnAction((ActionEvent e) -> {
            goToProductOptionsView();
        });

        menuBTN.getItems().addAll(productOptionsBTN);

        if (mainModel.isLoggedIn()){
            menuBTN.getItems().addAll(logoutBTN);
        } else {
            menuBTN.getItems().addAll(loginBTN);
        }


    }

    public void goToUpdateProductView(SellerInventoryView sellerInventoryView, Product product) {
        goToView(new UpdateProductView(mainModel, this, sellerInventoryView, product));
    }

    public void gotoAddProductView(SellerInventoryView sellerInventoryView) {
        goToView(new AddProductView(mainModel, this, sellerInventoryView));
    }
}


