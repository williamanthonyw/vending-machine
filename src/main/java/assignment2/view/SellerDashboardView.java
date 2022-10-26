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
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

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
        Label transactionLBL = new Label("List of Items Sold");
        TextArea transactionText = new TextArea();

        //read transactions done from file
        String transTemp = "";
        List<List<String>> transactions = this.mainModel.readPurchasesFromFile(transactionsPath);

        for (List<String> s : transactions){
            transTemp = transTemp.concat(s.toString());
        }
        transactionText.setText(transTemp);

        //List of Available Items
        Label inventoryLBL = new Label("List of Items Available");
        TextArea inventoryText = new TextArea();

        //write inventory to file
        this.inventoryModel.writeInventoryToFile(productsPath);

        //read inventory from file 
        String invTemp = "";
        List<List<String>> inventoryItems = this.inventoryModel.readInventoryFromFile(productsPath);

        for (List<String> s : inventoryItems){
            invTemp = invTemp.concat(s.toString());
        }
 
        inventoryText.setText(invTemp);

         

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