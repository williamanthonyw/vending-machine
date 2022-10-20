package assignment2.view;

import assignment2.model.Purchase;
import assignment2.model.MainModel;
import assignment2.model.LastFiveProductsModel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LastFiveProductsView implements View{

    private LastFiveProductsModel lastFiveProductsModel;
    private MainModel mainModel;
    private Scene scene;

    private VBox mainBox;

    private TableView<Purchase> productsTable;

    private VBox summaryBox;

    private BorderPane borderPane;

    public LastFiveProductsView(MainModel mainModel){
        this.lastFiveProductsModel = mainModel.getLastFiveProductsModel();
        this.mainModel = mainModel;
    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN){
        mainBox.getChildren().add(0, menuBTN);
    }

    public void setUpCancelBTN(Button cancelBTN){
        mainBox.getChildren().add(cancelBTN);
    }

    @Override
    public void setUp(){

        this.borderPane = new BorderPane();

        scene = new Scene(borderPane, 1000, 600);
        scene.getStylesheets().add("Style.css");

        mainBox = new VBox(30);
        BorderPane.setMargin(mainBox, new Insets(50, 50, 50, 50));
        borderPane.setCenter(mainBox);

        Label titleLBL;

        if (mainModel.isLoggedIn()){
            titleLBL = new Label("Last Five Products Bought By You");
        } else {
            titleLBL = new Label("Last Five Products Bought By Anonymous Users");
        }

        titleLBL.setId("title");
        mainBox.getChildren().add(titleLBL);

        productsTable = new TableView<Purchase>();

        mainBox.getChildren().addAll(productsTable);
        setUpProductsTable();

        showProducts();

    }

    public void setUpProductsTable() {

        productsTable.setPlaceholder(new Label("You have bought 0 products"));
        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Purchase, String> itemColumn  = new TableColumn<Purchase, String>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("Item"));
        productsTable.getColumns().add(itemColumn);

        TableColumn<Purchase, Double> priceColumn = new TableColumn<Purchase, Double>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Double>("price"));
        productsTable.getColumns().add(priceColumn);

        TableColumn<Purchase, Integer> quantityColumn = new TableColumn<Purchase, Integer>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Purchase,Integer>("quantity"));
        productsTable.getColumns().add(quantityColumn);

        TableColumn<Purchase, LocalDateTime> datePurchasedColumn = new TableColumn<Purchase, LocalDateTime>("Date Purchased");
        datePurchasedColumn.setCellValueFactory(new PropertyValueFactory<Purchase,LocalDateTime>("datePurchased"));
        productsTable.getColumns().add(datePurchasedColumn);



    }
    public void showProducts(){

        List<Purchase> purchases = lastFiveProductsModel.getLastFiveProductsBoughtByUser(mainModel.getUser());

        // reset table
        productsTable.getItems().clear();

        if (purchases.size() == 0){
            productsTable.setPlaceholder(new Label("You have bought 0 products"));
        }

        // add them in reverse order, so most recent appear first
        for (int i = purchases.size() - 1; i >= 0; i--){
            productsTable.getItems().add(purchases.get(i));
        }

    }

    @Override
    public List<Scene> getScenes(){
        return Arrays.asList(new Scene[] { scene });
    }





}
