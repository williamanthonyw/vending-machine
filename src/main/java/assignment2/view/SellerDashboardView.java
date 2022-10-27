package assignment2.view;

import assignment2.model.InventoryModel;
import assignment2.model.MainModel;
import assignment2.model.Purchase;
import assignment2.model.Product;
import assignment2.model.User;

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


public class SellerDashboardView implements View{

    private MainModel mainModel;
    private MainView mainView;
    private InventoryModel inventoryModel;
    private User user;

    private List<Purchase> purchasedItems;
    private List<Product> availableProducts;
    private static String productsPath = "src/main/resources/inventory.csv";
    private static String transactionsPath = "src/main/resources/transaction.csv";

    private Scene scene;
    private Stage stage;
    private VBox mainBox;
    private BorderPane borderPane;

    public SellerDashboardView(MainModel mainModel, MainView mainView){
        this.mainModel = mainModel;
        this.mainView = mainView;
        this.inventoryModel = this.mainModel.getInventoryModel();
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

        

        //list of available products
        availableProducts = this.inventoryModel.getInventory();

        //list of purchased items
        purchasedItems = this.user.getPurchases();

        //title 
        Label titleLBL = new Label("Seller Dashboard");
        titleLBL.setId("title");
        titleLBL.setAlignment(Pos.CENTER);
        mainBox.getChildren().add(titleLBL);

        //List of Purchased Items
        HBox transactionBox1 = new HBox();
        Label transactionLBL = new Label("List of Items Sold");
        
        
        HBox transactionBox2 = new HBox();
        TextArea transactionText = new TextArea();
        transactionText.setPrefHeight(600);
        transactionText.setPrefWidth(800);

        //read transactions done from file
        String transTemp = "";
        List<List<String>> transactions = this.mainModel.readPurchasesFromFile(transactionsPath);

        if (transactions.size() == 0){
            transTemp = "No transactions available.";
        }
        else{
            for (List<String> s : transactions){
                String temp2 = String.join(", ", s).stripTrailing();
                temp2 = temp2.concat("\n");
            
                transTemp = transTemp.concat(temp2);
            }
        }
        
        transactionText.setText(transTemp);

        transactionBox1.getChildren().add(transactionLBL);
        transactionBox2.getChildren().add(transactionText);

        mainBox.getChildren().addAll(transactionBox1, transactionBox2);

        //List of Available Items
        HBox inventoryBox1 = new HBox();
        Label inventoryLBL = new Label("List of Items Available");
        

        HBox inventoryBox2 = new HBox();
        TextArea inventoryText = new TextArea();
        inventoryText.setPrefHeight(600);
        inventoryText.setPrefWidth(800);

        //write inventory to file
        this.inventoryModel.writeInventoryToFile(productsPath);

        //read inventory from file 
        String invTemp = "";
        List<List<String>> inventoryItems = this.inventoryModel.readInventoryFromFile(productsPath);

        if (inventoryItems.size() == 0){
            invTemp = "No items available.";
        }

        else{
            for (List<String> s : inventoryItems){
                String temp2 = String.join(", ", s).stripTrailing();
                temp2 = temp2.concat("\n");
                invTemp = invTemp.concat(temp2);
            }
        }

        inventoryText.setText(invTemp);

        inventoryBox1.getChildren().add(inventoryLBL);
        inventoryBox2.getChildren().add(inventoryText);

        mainBox.getChildren().addAll(inventoryBox1, inventoryBox2);

        
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