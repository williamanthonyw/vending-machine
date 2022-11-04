package assignment2.view;

import assignment2.model.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
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

    private Button usersBTN;
    private Button cancelledTransactionsBTN;
    private Button availableItemsBTN;
    private Button itemsSoldBTN;
    private Button availableChangeBTN;
    private Button summaryTransactionsBTN;

    private Button closeBTN;

    private Scene usersScene;
    private Scene cancelledTransactionsScene;
    private Scene availableItemsScene;
    private Scene itemsSoldScene;
    private Scene availableChangeScene;
    private Scene summaryTransactionsScene;

    private BorderPane usersBorderPane;
    private BorderPane cancelledTransactionsBorderPane;
    private BorderPane availableItemsBorderPane;
    private BorderPane itemsSoldBorderPane;
    private BorderPane availableChangeBorderPane;
    private BorderPane summaryTransactionsBorderPane;


    public OwnerDashboardView(MainModel mainModel, MainView mainView){
        this.mainModel = mainModel;
        this.mainView = mainView;
        this.user = this.mainModel.getUser();
    }


    @Override
    public List<Scene> getScenes(){
        return Arrays.asList(new Scene[] { scene, usersScene, cancelledTransactionsScene, 
            availableItemsScene, itemsSoldScene, availableChangeScene, summaryTransactionsScene });
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

        this.usersBorderPane = new BorderPane();
        this.cancelledTransactionsBorderPane = new BorderPane();
        this.availableItemsBorderPane = new BorderPane();
        this.itemsSoldBorderPane = new BorderPane();
        this.availableChangeBorderPane = new BorderPane();
        this.summaryTransactionsBorderPane = new BorderPane();

        this.usersScene = new Scene(usersBorderPane, 600, 600);
        this.cancelledTransactionsScene = new Scene(cancelledTransactionsBorderPane, 600, 600);
        this.availableItemsScene = new Scene(availableItemsBorderPane, 600, 600);
        this.itemsSoldScene = new Scene(itemsSoldBorderPane, 600, 600);
        this.availableChangeScene = new Scene(availableChangeBorderPane, 600, 600);
        this.summaryTransactionsScene = new Scene(summaryTransactionsBorderPane, 600, 600);

        //title
        Label titleLBL = new Label("Owner Dashboard");
        titleLBL.setId("title");
        titleLBL.setAlignment(Pos.CENTER);
        mainBox.getChildren().add(titleLBL);

        this.usersBTN = new Button("View List of Usernames and Roles");
        this.cancelledTransactionsBTN = new Button("View Summary of Cancelled Transactions");
        this.availableItemsBTN = new Button("View List of Current Available Items");
        this.itemsSoldBTN = new Button("View Summary of Items Sold");
        this.availableChangeBTN = new Button("View List of Available Change");
        this.summaryTransactionsBTN = new Button("View Summary of Transactions");


        mainBox.getChildren().addAll(usersBTN, cancelledTransactionsBTN, availableItemsBTN, 
            itemsSoldBTN, availableChangeBTN, summaryTransactionsBTN);
        
        setUpButtons();

    }

    public void setUpButtons(){
        usersBTN.setOnAction((e) -> {
            setUpUsersPopup();
            
        });
        cancelledTransactionsBTN.setOnAction((e) -> {
            setUpCancelledTransactionsPopup();
            
        });
        availableItemsBTN.setOnAction((e) -> {
            setUpAvailableItemsPopup();
            
        });
        itemsSoldBTN.setOnAction((e) -> {
            setUpItemsSoldPopup();
            
        });
        availableChangeBTN.setOnAction((e) -> {
            setUpAvailableChangePopup();
            
        });
        summaryTransactionsBTN.setOnAction((e) -> {
            setUpSummaryTransactionsPopup();
            
        });
    }

    public void setUpCloseBTN(Stage stage, VBox mainBox){
        closeBTN = new Button("Close");
        mainBox.getChildren().addAll(closeBTN);

        closeBTN.setOnAction((e) -> {
            stage.hide();
        
        });
    }

    public void setUpUsersPopup(){
        usersScene.getStylesheets().add("Style.css");
        Stage usersStage = new Stage();
        usersStage.setScene(usersScene);
        usersStage.initModality(Modality.APPLICATION_MODAL);
        usersStage.initOwner(this.stage);
        usersStage.show();

        VBox mainBox = new VBox(60);
        usersBorderPane.setMargin(mainBox, new Insets(70, 70, 70, 70));
        usersBorderPane.setCenter(mainBox);

        Label usersLBL = new Label("List of Usernames and Role");
        usersLBL.setAlignment(Pos.CENTER);
        TextArea userTA = new TextArea();
        userTA.setEditable(false);
        userTA.setMinHeight(300);
        userTA.setMinWidth(500);
        mainBox.getChildren().addAll(usersLBL, userTA);

        String result = mainModel.getUsersAsString();
        userTA.setText(result);

        setUpCloseBTN(usersStage, mainBox);
    }

    public void setUpCancelledTransactionsPopup(){
        cancelledTransactionsScene.getStylesheets().add("Style.css");
        Stage cancelledTransactionsStage = new Stage();
        cancelledTransactionsStage.setScene(cancelledTransactionsScene);
        cancelledTransactionsStage.initModality(Modality.APPLICATION_MODAL);
        cancelledTransactionsStage.initOwner(this.stage);
        cancelledTransactionsStage.show();

        VBox mainBox = new VBox(60);
        cancelledTransactionsBorderPane.setMargin(mainBox, new Insets(70, 70, 70, 70));
        cancelledTransactionsBorderPane.setCenter(mainBox);

        Label cancelledTransactionsLBL = new Label("List of Cancelled Transactions");
        cancelledTransactionsLBL.setAlignment(Pos.CENTER);
        TextArea cancelledTransactionsTA = new TextArea();
        cancelledTransactionsTA.setWrapText(true);
        cancelledTransactionsTA.setMinHeight(300);
        cancelledTransactionsTA.setMinWidth(500);
        cancelledTransactionsTA.setEditable(false);
        mainBox.getChildren().addAll(cancelledTransactionsLBL, cancelledTransactionsTA);

        String result = "";
        if (mainModel.getCancelledTransactions().size() == 0){
            result = "No cancelled transactions available.";
        }
        else{
            result = mainModel.getCancelledTransactionsAsString();
        }

        cancelledTransactionsTA.setText(result);

        setUpCloseBTN(cancelledTransactionsStage, mainBox);
    }

    public void setUpAvailableItemsPopup(){
        availableItemsScene.getStylesheets().add("Style.css");
        Stage availableItemsStage = new Stage();
        availableItemsStage.setScene(availableItemsScene);
        availableItemsStage.initModality(Modality.APPLICATION_MODAL);
        availableItemsStage.initOwner(this.stage);
        availableItemsStage.show();

        VBox mainBox = new VBox(60);
        availableItemsBorderPane.setMargin(mainBox, new Insets(70, 70, 70, 70));
        availableItemsBorderPane.setCenter(mainBox);

        Label availableItemsLBL = new Label("List of Current Available Items");
        availableItemsLBL.setAlignment(Pos.CENTER);

        TextArea availableItemsTA = new TextArea();
        availableItemsTA.setWrapText(true);
        availableItemsTA.setMinHeight(300);
        availableItemsTA.setMinWidth(500);
        availableItemsTA.setEditable(false);
        
        mainBox.getChildren().addAll(availableItemsLBL, availableItemsTA);

        String result = "";
        if (mainModel.getAvailableProducts().size() == 0){
            result = "No available products.";
        }
        else{
            result = mainModel.getAvailableProductsAsString();
        }
        availableItemsTA.setText(result);
        setUpCloseBTN(availableItemsStage, mainBox);
        System.out.println(mainBox.getChildren());
    }

    public void setUpItemsSoldPopup(){  
        itemsSoldScene.getStylesheets().add("Style.css");
        Stage itemsSoldStage = new Stage();
        itemsSoldStage.setScene(itemsSoldScene);
        itemsSoldStage.initModality(Modality.APPLICATION_MODAL);
        itemsSoldStage.initOwner(this.stage);
        itemsSoldStage.show();

        VBox mainBox = new VBox(60);
        itemsSoldBorderPane.setMargin(mainBox, new Insets(70, 70, 70, 70));
        itemsSoldBorderPane.setCenter(mainBox);

        Label itemsSoldLBL = new Label("Summary of Items Sold");
        itemsSoldLBL.setAlignment(Pos.CENTER);
        TextArea itemsSoldTA = new TextArea();
        itemsSoldTA.setWrapText(true);
        itemsSoldTA.setMinHeight(300);
        itemsSoldTA.setMinWidth(500);
        itemsSoldTA.setEditable(false);
        mainBox.getChildren().addAll(itemsSoldLBL, itemsSoldTA);

        String result = "";
        if (mainModel.getItemsSold().size() == 0){
            result = "No transactions available.";
        }
        else{
            result = mainModel.getItemsSoldAsString();
        }
        itemsSoldTA.setText(result);


        setUpCloseBTN(itemsSoldStage, mainBox);
    }

    public void setUpAvailableChangePopup(){  //////////////to do/////////////////
        availableChangeScene.getStylesheets().add("Style.css");
        Stage availableChangeStage = new Stage();
        availableChangeStage.setScene(availableChangeScene);
        availableChangeStage.initModality(Modality.APPLICATION_MODAL);
        availableChangeStage.initOwner(this.stage);
        availableChangeStage.show();

        VBox mainBox = new VBox(60);
        availableChangeBorderPane.setMargin(mainBox, new Insets(70, 70, 70, 70));
        availableChangeBorderPane.setCenter(mainBox);

        Label availableChangeLBL = new Label("List of Available Change");
        availableChangeLBL.setAlignment(Pos.CENTER);
        TextArea availableChangeTA = new TextArea();
        availableChangeTA.setWrapText(true);
        availableChangeTA.setMinHeight(300);
        availableChangeTA.setMinWidth(500);
        availableChangeTA.setEditable(false);
        mainBox.getChildren().addAll(availableChangeLBL, availableChangeTA);

        String result = "";
        if (mainModel.getCash().size() == 0){
            result = "No change available.";
        }
        else{
            result = mainModel.getAvailableChangeAsString();
        }
        availableChangeTA.setText(result);

        setUpCloseBTN(availableChangeStage, mainBox);
    }

    public void setUpSummaryTransactionsPopup(){
        summaryTransactionsScene.getStylesheets().add("Style.css");
        Stage summaryTransactionsStage = new Stage();
        summaryTransactionsStage.setScene(summaryTransactionsScene);
        summaryTransactionsStage.initModality(Modality.APPLICATION_MODAL);
        summaryTransactionsStage.initOwner(this.stage);
        summaryTransactionsStage.show();

        VBox mainBox = new VBox(60);
        summaryTransactionsBorderPane.setMargin(mainBox, new Insets(70, 70, 70, 70));
        summaryTransactionsBorderPane.setCenter(mainBox);

        Label summaryTransactionsLBL = new Label("Summary of Transactions");
        summaryTransactionsLBL.setAlignment(Pos.CENTER);
        TextArea summaryTransactionsTA = new TextArea();
        summaryTransactionsTA.setWrapText(true);
        summaryTransactionsTA.setMinHeight(300);
        summaryTransactionsTA.setMinWidth(500);
        summaryTransactionsTA.setEditable(false);
        mainBox.getChildren().addAll(summaryTransactionsLBL, summaryTransactionsTA);

        List<List<String>> cashierTransactionString = this.mainModel.getCsvFileParser().readCashierTransactions();

        String result = "";
        if (cashierTransactionString.size() == 0){
            result = "No transactions available.";
        }
        else{
            result = mainModel.getTransactionsAsString();
        }
        summaryTransactionsTA.setText(result);

        setUpCloseBTN(summaryTransactionsStage, mainBox);
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
