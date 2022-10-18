package assignment2.view;

import assignment2.model.MainModel;
import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.geometry.Pos;
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
//    private Timer timer;

    private Transition timer;

//    private Handler timeoutHandler;


    public MainView(MainModel mainModel){
        this.mainModel = mainModel;

        setUpMenu();
        setUpCancelBTN();
        setUpCancelOnTimeOut();
    }

    public void setUp(Stage stage){
        this.stage = stage;
        goToLastFiveProductsView();
        // goToProductOptionsView();    ///////////

        stage.show();
    }

    public void goToView(View view){
        view.setUp();
        stage.setScene(view.getScene());
        view.setUpMenuBTN(menuBTN);
        view.setUpCancelBTN(cancelBTN);

        // restart timer on key press
        view.getScene().addEventFilter(InputEvent.ANY, evt -> {

            if (evt.getEventType().getName().equals("MOUSE_PRESSED") ||
                evt.getEventType().getName().equals("KEY_PRESSED")){
                timer.playFromStart();
                System.out.println("Restart timer" + evt.getEventType());
            }
        }
        );
    }

    public void goToLastFiveProductsView(){
        goToView(new LastFiveProductsView(mainModel));
    }

    /////
    public void goToProductOptionsView(){
        goToView(new ProductOptionsView(mainModel, stage));
    }
    
    public void goToLoginView(){
        goToView(new LoginView(mainModel));
    }


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
            goToLastFiveProductsView();

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
            goToLastFiveProductsView();
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

        MenuItem loginBTN = new MenuItem("Login");
        loginBTN.setOnAction((ActionEvent e) -> {
            goToLoginView();
        });
        MenuItem productOptionsBTN = new MenuItem("Products");
        productOptionsBTN.setOnAction((ActionEvent e) -> {
            goToProductOptionsView();
        });

        menuBTN.getItems().addAll(loginBTN, productOptionsBTN);


    }
}


