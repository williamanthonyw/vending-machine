package assignment2.view;

import assignment2.model.CancellationReason;
import assignment2.model.MainModel;
import assignment2.model.Product;
import assignment2.model.UserAccess;
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

import java.time.LocalDateTime;
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
        // goToSellerInventoryView();
        goToOwnerDashboardView();
//        goToProductOptionsView();

        stage.show();
    }

    public void goToOwnerDashboardView(){
        goToView(new OwnerDashboardView(mainModel, this));
    }

    private void goToSellerInventoryView() {
        goToView(new SellerInventoryView(this.mainModel, this));
    }

    public void goToSellerDashboardView(){
        goToView(new SellerDashboardView(this.mainModel, this));
    }

    public void goToView(View view){
        view.setUp();
        stage.setScene(view.getScenes().get(0));
        view.setUpCancelBTN(cancelBTN);
        setUpMenu();
        view.setUpMenuBTN(menuBTN);

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
        view.setUpCancelBTN(cancelBTN);
    }

    public void goToProductOptionsView(){
        goToView(new ProductOptionsView(mainModel, stage, this));
    }

    public void goToModifyCashView(){goToView(new ModifyCashView(mainModel,stage,this));}

    public void goToLoginView(){
        goToView(new LoginView(mainModel, this));
    }

    public void goToCardPaymentView(ProductOptionsView productOptionsView){
        goToView(new CardPaymentView(mainModel, this, productOptionsView));
    }

    public void goToCashPaymentView(ProductOptionsView productOptionsView){ goToView(new CashPaymentView(mainModel, this, productOptionsView)); }

    public void setUpCancelOnTimeOut(){

        Duration delay = Duration.seconds(120);

        this.timer = new PauseTransition(delay);

        timer.setOnFinished(evt -> {

            Platform.runLater(()-> {
                cancelPopup();
            });

            mainModel.cancelTransaction(CancellationReason.TIMEOUT, LocalDateTime.now());

            timer.playFromStart();
        });


        timer.play();



    }

    public void cancelPopup(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cancel Transaction");
        alert.setHeaderText("Your transaction has been cancelled");
        if (mainModel.isLoggedIn()){
            alert.setContentText("Your cart has been cleared and you have been logged out.");
        } else {
            alert.setContentText("Your cart has been cleared.");
        }

        alert.showAndWait();

        // back to default page
        goToProductOptionsView();
    }

    public void setUpCancelBTN(){

        this.cancelBTN = new Button("Cancel");

        cancelBTN.setOnAction((ActionEvent e) -> {
            cancelPopup();
            mainModel.cancelTransaction(CancellationReason.USER_CANCELLATION, LocalDateTime.now());
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

        MenuItem userManagementBTN = new MenuItem("Manage Users");
        userManagementBTN.setOnAction((ActionEvent e) -> {
            goToUserManagementView();
        });

        MenuItem sellerInventoryBTN = new MenuItem("Manage Inventory");
        sellerInventoryBTN.setOnAction((ActionEvent e) -> {
            goToSellerInventoryView();
        });

        MenuItem sellerDashboardBTN = new MenuItem("Manage reports");
        sellerDashboardBTN.setOnAction((ActionEvent e) -> {
            goToSellerDashboardView();
        });

        MenuItem modifyCashBTN = new MenuItem("Modify Cash");
        modifyCashBTN.setOnAction((ActionEvent e) -> {
            goToModifyCashView();
        });

        MenuItem ownerDashboardBTN = new MenuItem("Owner Dashboard");
        ownerDashboardBTN.setOnAction((ActionEvent e) -> {
            goToModifyCashView();
        });

        menuBTN.getItems().addAll(productOptionsBTN);

        if (mainModel.isLoggedIn()){
            menuBTN.getItems().addAll(logoutBTN);


            if (mainModel.getUser().getUserAccess().equals(UserAccess.OWNER)){
                menuBTN.getItems().addAll(userManagementBTN);
                menuBTN.getItems().addAll(sellerInventoryBTN);
                menuBTN.getItems().add(modifyCashBTN);
                menuBTN.getItems().addAll(ownerDashboardBTN);
            }
            if (mainModel.getUser().getUserAccess().equals(UserAccess.SELLER)){
                menuBTN.getItems().addAll(sellerInventoryBTN);
                menuBTN.getItems().addAll(sellerDashboardBTN);
            }
            if (mainModel.getUser().getUserAccess().equals(UserAccess.CASHIER)){
                menuBTN.getItems().add(modifyCashBTN);
            }

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

    public void goToUserManagementView(){
        goToView(new UserManagementView(mainModel, stage, this));
    }
}


