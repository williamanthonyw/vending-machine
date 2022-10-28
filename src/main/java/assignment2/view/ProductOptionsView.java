package assignment2.view;
//from last5productsview
import assignment2.model.Purchase;
import assignment2.model.MainModel;
import assignment2.model.LastFiveProductsModel;
import java.time.LocalDateTime;

import assignment2.model.InventoryModel;
import assignment2.model.Product;
import assignment2.model.Product;
import assignment2.model.MainModel;
import assignment2.model.InventoryModel;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.binding.*;
import javafx.util.*;

import java.util.*;

import java.io.*;

public class ProductOptionsView implements View{

    //
    private TableView<Purchase> productsTable;
    private LastFiveProductsModel lastFiveProductsModel;

    private Scene scene;
    private MainModel mainModel;
    private InventoryModel inventoryModel;
    private VBox mainBox;
    private VBox productOptionsBox;
    private Scene popupScene;
    private BorderPane popupBorderPane;

    private BorderPane borderPane;
    private Stage stage;

    private TableView<Product> drinksTable;
    private TableView<Product> chocolatesTable;
    private TableView<Product> chipsTable;
    private TableView<Product> candiesTable;
    private Button checkoutBTN;
    private Button cartBTN;
    ComboBox<String> selectCategory;
    ComboBox<Integer> selectQuantity;

    private MainView mainView;

    private HBox headerBox;  //menubtn + last5products
    private HBox buttonBox;
    private VBox lastFiveProductsBox;
    private VBox topBox;

    public ProductOptionsView(MainModel mainModel, Stage stage, MainView mainView){
        this.mainModel = mainModel;
        this.inventoryModel = mainModel.getInventoryModel();
        this.stage = stage;
        this.mainView = mainView;
        ///
        this.lastFiveProductsModel = mainModel.getLastFiveProductsModel();
        
        headerBox = new HBox(200);
        headerBox.setMaxHeight(400);
        lastFiveProductsBox = new VBox(10);
        topBox = new VBox(20);
    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN) {
        topBox.getChildren().add(0, menuBTN);
        headerBox.getChildren().addAll(topBox, lastFiveProductsBox);

        mainBox.getChildren().add(0, headerBox);
    }

    @Override
    public void setUp() {
        this.borderPane = new BorderPane();

        scene = new Scene(borderPane, 1000, 600);
        scene.getStylesheets().add("Style.css");

        mainBox = new VBox(0);
        BorderPane.setMargin(mainBox, new Insets(50, 50, 50, 50));
        borderPane.setCenter(mainBox);

        Label lastFiveProductsTitleLBL;

        if (mainModel.isLoggedIn()){
            lastFiveProductsTitleLBL = new Label("Last Five Products Bought By You");
        } else {
            lastFiveProductsTitleLBL = new Label("Last Five Products Bought By Anonymous Users");
        }
        
        productsTable = new TableView<Purchase>();

        lastFiveProductsBox.getChildren().addAll(lastFiveProductsTitleLBL, productsTable);
        setUpProductsTable();

        showProducts();
        ////

        Label titleLBL = new Label("All Products");
        titleLBL.setId("title");
        topBox.getChildren().add(titleLBL);

        buttonBox = new HBox(10);
        topBox.getChildren().add(buttonBox);

        cartBTN = new Button("View Cart");
        buttonBox.getChildren().add(cartBTN);


        checkoutBTN = new Button("Checkout");
        checkoutBTN.setMaxHeight(10);
        buttonBox.getChildren().add(checkoutBTN);
        checkoutBTN.setOnAction((e) -> {
            setUpCheckoutButton();
        });

        Label selectCategoryLBL = new Label("Select category");
        // selectCategoryLBL.setID("align");
        selectCategory = new ComboBox<String>();
        selectCategory.getItems().addAll(inventoryModel.getCategories());
        selectCategory.setValue("drinks");  // drinks by default

        HBox categorySelectionBox = new HBox(10);
        categorySelectionBox.getChildren().addAll(selectCategoryLBL, selectCategory);
        topBox.getChildren().add(categorySelectionBox);

        productOptionsBox = new VBox(10);
        productOptionsBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.getChildren().add(productOptionsBox);

        setUpDrinksTable();  //show drinks table as default

        setUpSelectCategory();

        this.popupBorderPane = new BorderPane();
        popupScene = new Scene(popupBorderPane, 500, 300);

        setupCart();
    }


    public void setUpDrinksTable(){

        Label drinksLBL = new Label("Drinks");
        drinksLBL.setId("title");
        productOptionsBox.getChildren().add(drinksLBL);

        drinksTable = new TableView<Product>();
        drinksTable.setPlaceholder(new Label("No drinks available"));
        drinksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //

        TableColumn<Product, String> nameColumn = new TableColumn<Product, String>("Item");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        drinksTable.getColumns().add(nameColumn);

        TableColumn<Product, Double> priceColumn = new TableColumn<Product, Double>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        drinksTable.getColumns().add(priceColumn);

        TableColumn<Product, Integer> quantityColumn = new TableColumn<Product, Integer>("Quantity Available");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
        drinksTable.getColumns().add(quantityColumn);

        TableColumn addBTNColumn = new TableColumn("Action");
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<Product, String>, TableCell<Product, String>> cellFactory
                = //
                new Callback<TableColumn<Product, String>, TableCell<Product, String>>() {
            @Override
            public TableCell call(final TableColumn<Product, String> param) {
                final TableCell<Product, String> cell = new TableCell<Product, String>() {

                    final Button btn = new Button("Add");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                // add to cart
                                setUpPopupScreen(getTableView().getItems().get(getIndex()), drinksTable);
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        addBTNColumn.setCellFactory(cellFactory);

        populateTable(drinksTable, "drinks");

        productOptionsBox.getChildren().add(drinksTable);
        drinksTable.getColumns().add(addBTNColumn);

        //
        // drinksTable.prefHeightProperty().bind(drinksTable.fixedCellSizeProperty().multiply(Bindings.size(drinksTable.getItems()).add(1.01)));
        // drinksTable.minHeightProperty().bind(drinksTable.prefHeightProperty());
        // drinksTable.maxHeightProperty().bind(drinksTable.prefHeightProperty());

    }

    public void setUpChocolatesTable(){
        Label chocolatesLBL = new Label("Chocolates");
        chocolatesLBL.setId("title");
        productOptionsBox.getChildren().add(chocolatesLBL);

        chocolatesTable = new TableView<Product>();
        chocolatesTable.setPlaceholder(new Label("No chocolates available"));
        chocolatesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> nameColumn = new TableColumn<Product, String>("Item");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        chocolatesTable.getColumns().add(nameColumn);

        TableColumn<Product, Double> priceColumn = new TableColumn<Product, Double>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        chocolatesTable.getColumns().add(priceColumn);

        TableColumn<Product, Integer> quantityColumn = new TableColumn<Product, Integer>("Quantity Available");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
        chocolatesTable.getColumns().add(quantityColumn);

        TableColumn addBTNColumn = new TableColumn("Action");
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<Product, String>, TableCell<Product, String>> cellFactory
                = //
                new Callback<TableColumn<Product, String>, TableCell<Product, String>>() {
            @Override
            public TableCell call(final TableColumn<Product, String> param) {
                final TableCell<Product, String> cell = new TableCell<Product, String>() {

                    final Button btn = new Button("Add");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                // add to cart
                                setUpPopupScreen(getTableView().getItems().get(getIndex()), chocolatesTable);
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        addBTNColumn.setCellFactory(cellFactory);

        populateTable(chocolatesTable, "chocolates");

        productOptionsBox.getChildren().add(chocolatesTable);
        chocolatesTable.getColumns().add(addBTNColumn);
    }

    public void setUpChipsTable(){
        Label chipsLBL = new Label("Chips");
        chipsLBL.setId("title");
        productOptionsBox.getChildren().add(chipsLBL);

        chipsTable = new TableView<Product>();
        chipsTable.setPlaceholder(new Label("No chips available"));
        chipsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> nameColumn = new TableColumn<Product, String>("Item");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        chipsTable.getColumns().add(nameColumn);

        TableColumn<Product, Double> priceColumn = new TableColumn<Product, Double>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        chipsTable.getColumns().add(priceColumn);

        TableColumn<Product, Integer> quantityColumn = new TableColumn<Product, Integer>("Quantity Available");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
        chipsTable.getColumns().add(quantityColumn);

        TableColumn addBTNColumn = new TableColumn("Action");
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<Product, String>, TableCell<Product, String>> cellFactory
                = //
                new Callback<TableColumn<Product, String>, TableCell<Product, String>>() {
            @Override
            public TableCell call(final TableColumn<Product, String> param) {
                final TableCell<Product, String> cell = new TableCell<Product, String>() {

                    final Button btn = new Button("Add");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                // add to cart
                                setUpPopupScreen(getTableView().getItems().get(getIndex()), chipsTable);
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        addBTNColumn.setCellFactory(cellFactory);

        populateTable(chipsTable, "chips");

        productOptionsBox.getChildren().add(chipsTable);
        chipsTable.getColumns().add(addBTNColumn);
    }

    public void setUpCandiesTable(){
        Label candiesLBL = new Label("Candies");
        candiesLBL.setId("title");
        productOptionsBox.getChildren().add(candiesLBL);


        candiesTable = new TableView<Product>();
        candiesTable.setPlaceholder(new Label("No candies available"));
        candiesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> nameColumn = new TableColumn<Product, String>("Item");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        candiesTable.getColumns().add(nameColumn);

        TableColumn<Product, Double> priceColumn = new TableColumn<Product, Double>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        candiesTable.getColumns().add(priceColumn);

        TableColumn<Product, Integer> quantityColumn = new TableColumn<Product, Integer>("Quantity Available");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantity"));
        candiesTable.getColumns().add(quantityColumn);

        TableColumn addBTNColumn = new TableColumn("Action");
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<Product, String>, TableCell<Product, String>> cellFactory
                = //
                new Callback<TableColumn<Product, String>, TableCell<Product, String>>() {
            @Override
            public TableCell call(final TableColumn<Product, String> param) {
                final TableCell<Product, String> cell = new TableCell<Product, String>() {

                    final Button btn = new Button("Add");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                // add to cart
                                setUpPopupScreen(getTableView().getItems().get(getIndex()), candiesTable);
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        addBTNColumn.setCellFactory(cellFactory);

        populateTable(candiesTable, "candies");

        productOptionsBox.getChildren().add(candiesTable);
        candiesTable.getColumns().add(addBTNColumn);
    }

    public void populateTable(TableView table, String category){

        List<Product> products = inventoryModel.getInventory();

        table.getItems().clear();

        if (products.size() == 0){
            table.setPlaceholder(new Label("No products available"));
        }

        for (Product product: products){
            if (product.getCategory().equals(category)){
                table.getItems().add(product);

            }
        }
    }

    public void setUpSelectCategory(){
        selectCategory.setOnAction((e) -> {
            String selectedCategory = selectCategory.getValue();

            productOptionsBox.getChildren().clear();  //clear existing table
            
            if (selectedCategory.equals("drinks")){
                setUpDrinksTable();
            }
            if (selectedCategory.equals("chocolates")){
                setUpChocolatesTable();
            }
            if (selectedCategory.equals("chips")){
                setUpChipsTable();
            }
            if (selectedCategory.equals("candies")){
                setUpCandiesTable();
            }
        });
    }

    public void setUpPopupScreen(Product product, TableView<Product> table){   //todo: popup screen doesnt close after timeout - mouse presses not registered + window doesnt close

        popupScene.getStylesheets().add("Style.css");

        Stage popupStage = new Stage();

        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(this.stage);
        popupStage.show();

        VBox popupMainBox = new VBox(60);
        popupBorderPane.setMargin(popupMainBox, new Insets(50, 50, 50, 50));
        popupBorderPane.setCenter(popupMainBox);
        popupMainBox.getChildren().add(new Label("Select Quantity to purchase"));
        HBox selectionBox = new HBox(80);
        popupMainBox.getChildren().add(selectionBox);

        ArrayList<Integer> quantities = new ArrayList<>();
        for (int i = 1; i <= product.getQuantity(); i++){
            quantities.add(i);
        }
        selectQuantity = new ComboBox<Integer>();
        selectQuantity.getItems().addAll(quantities);
        selectQuantity.setValue(1);  // 1 by default
        Button addToCartBTN = new Button("Add to cart");
        selectionBox.getChildren().addAll(selectQuantity, addToCartBTN);

        addToCartBTN.setOnAction((e) -> {
            int selectedQuantity = selectQuantity.getValue();
            popupStage.hide();


            ///// add the product obj + selected quantity to cart
            mainModel.addToCart(product, selectedQuantity);

            table.refresh();


        });
    }

    public void setUpCheckoutButton(){

        popupScene.getStylesheets().add("Style.css");

        Stage popupStage = new Stage();

        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(this.stage);
        popupStage.show();

        VBox popupMainBox = new VBox(60);
        popupBorderPane.setMargin(popupMainBox, new Insets(50, 50, 50, 50));
        popupBorderPane.setCenter(popupMainBox);

        if (mainModel.getCart().size() == 0){
            popupMainBox.getChildren().add(new Label("Your cart is empty.\nPlease add to your cart before checking out."));
        } else {


            popupMainBox.getChildren().add(new Label("Select payment type"));
            HBox selectionBox = new HBox(80);
            popupMainBox.getChildren().add(selectionBox);

            List<String> paymentTypes = Arrays.asList(new String[] { "Cash", "Card"});

            ComboBox<String> selectPaymentType = new ComboBox<String>();
            selectPaymentType.getItems().addAll(paymentTypes);
            selectPaymentType.setValue("Cash");  // 1 by default
            Button continueToCheckoutBTN = new Button("Continue");
            selectionBox.getChildren().addAll(selectPaymentType, continueToCheckoutBTN);

            continueToCheckoutBTN.setOnAction((e) -> {

                String paymentTypeValue = selectPaymentType.getValue();

                if (paymentTypeValue.equals("Cash")){
                    mainView.goToCashPaymentView(this);
                } else {
                    mainView.goToCardPaymentView(this);
                }

                popupStage.hide();

                ///// add the product obj + selected quantity to cart
            });


        }



    }

    @Override
    public void setUpCancelBTN(Button cancelBTN){   ///////////////
        cancelBTN.setMaxHeight(10);
        buttonBox.getChildren().add(cancelBTN);
    }

    @Override
    public List<Scene> getScenes(){
        return Arrays.asList(new Scene[] { scene, popupScene });
    }


    public void setupCart(){

        cartBTN.setOnAction((ActionEvent e) -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Your cart");
            alert.setHeaderText("Your Cart");


            StringBuilder cart = new StringBuilder();

            if (mainModel.getCart().size() == 0){
                cart = new StringBuilder("Your cart is empty");
            } else {
                for (Product p : mainModel.getCart().keySet()){
                    cart.append(p.getName()).append(" ").append(mainModel.getCart().get(p)).append("\n");
                }
            }


            alert.setContentText(cart.toString());
            alert.showAndWait();
        });

    }

    @Override
    public void refresh(){


    }

    //////
    public void setUpProductsTable() {

        productsTable.setPlaceholder(new Label("You have bought 0 products"));
        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Purchase, String> itemColumn  = new TableColumn<Purchase, String>("Item");
        itemColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("Item"));
        productsTable.getColumns().add(itemColumn);

    }
    public void showProducts(){

        List<Purchase> purchases = lastFiveProductsModel.getLastFiveProductsBoughtByUser(mainModel.getUser());

        // reset table
        productsTable.getItems().clear();

        if (purchases.size() == 0){
            productsTable.setPlaceholder(new Label("You have bought 0 products"));
        }

        for (int i = 0; i < purchases.size(); i++){
            productsTable.getItems().add(purchases.get(i));
        }

    }



}
