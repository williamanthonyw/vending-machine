package assignment2.view;

import assignment2.model.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.text.TextAlignment;

import java.util.*;


public class OwnerDashboardView implements View{

    private MainModel mainModel;
    private MainView mainView;
    private User user;

    private List<Purchase> purchasedItems;
    private List<Product> availableProducts;

    private Scene scene;
    private Stage stage;
    private VBox mainBox;
    private VBox usersBox;
    private VBox cancelledTransactionsBox;
    private BorderPane borderPane;

    public OwnerDashboardView(MainModel mainModel, MainView mainView){
        this.mainModel = mainModel;
        this.mainView = mainView;
        this.user = this.mainModel.getUser();
    }


    @Override
    public List<Scene> getScenes(){
        return Arrays.asList(new Scene[] { scene });
    }

    @Override
    public void setUp(){

        this.stage = new Stage();
        this.borderPane = new BorderPane();

        this.mainBox = new VBox(30);
        BorderPane.setMargin(this.mainBox, new Insets(50, 50, 50, 50));
        this.borderPane.setCenter(this.mainBox);



        this.scene = new Scene(this.borderPane, 1000, 1000);
        this.scene.getStylesheets().add("Style.css");

        //title
        Label titleLBL = new Label("Owner Dashboard");
        titleLBL.setId("title");
        titleLBL.setAlignment(Pos.CENTER);
        mainBox.getChildren().add(titleLBL);

        //List of Purchased Items
        usersBox = new VBox(20);
        Label usersLBL = new Label("List of Users");
        TextArea userTA = new TextArea();
        userTA.setEditable(false);

        cancelledTransactionsBox = new VBox(20);
        setUpCancelledTransactionsBox();

        mainBox.getChildren().addAll(usersBox, cancelledTransactionsBox);


    }

    public void setUpCancelledTransactionsBox(){

        Label cancelledTransactionsLBL = new Label("List of Cancelled Transactions");
        cancelledTransactionsLBL.setAlignment(Pos.CENTER);
        TextArea cancelledTransactionsTA = new TextArea();
        cancelledTransactionsTA.setMinHeight(200);
        cancelledTransactionsTA.setMinWidth(900);
        cancelledTransactionsTA.setEditable(false);

        cancelledTransactionsBox.getChildren().addAll(cancelledTransactionsLBL, cancelledTransactionsTA);

        String result = "";

        if (mainModel.getCancelledTransactions().size() == 0){
            result = "No cancelled transactions available.";
        }
        else{
            result = mainModel.getCancelledTransactionsAsString();
        }

        cancelledTransactionsTA.setText(result);

    }

    public void setUpUsersBox(){

    }



    @Override
    public void setUpMenuBTN(MenuButton menuBTN){
        mainBox.getChildren().add(0, menuBTN);
    }

    @Override
    public void setUpCancelBTN(Button cancelBTN){

    }

    @Override
    public void refresh(){

    }


}
