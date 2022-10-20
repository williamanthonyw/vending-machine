package assignment2.view;

// import assignment2.model.Product;
import assignment2.model.ProductToDisplay; 
import assignment2.model.MainModel;
import assignment2.model.ProductOptionsModel;
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

import javax.swing.*;
import java.util.*;

import java.io.*;

public class ProductOptionsView implements View{
    private Scene scene;
    private MainModel mainModel;
    private ProductOptionsModel productOptionsModel;
    private VBox mainBox;
    private VBox productOptionsBox;
    private Scene popupScene;
    private BorderPane popupBorderPane;

    private BorderPane borderPane;
    private Stage stage;

    private TableView<ProductToDisplay> drinksTable; 
    private TableView<ProductToDisplay> chocolatesTable; 
    private TableView<ProductToDisplay> chipsTable;
    private TableView<ProductToDisplay> candiesTable;
    private Button checkoutBTN;
    private Button selectCategoryBTN;
    ComboBox<String> selectCategory;

    private MainView mainView;

    private HBox buttonBox;

    public ProductOptionsView(MainModel mainModel, Stage stage, MainView mainView){
        this.mainModel = mainModel;
        this.productOptionsModel = mainModel.getProductOptionsModel();
        this.stage = stage;
        this.mainView = mainView;

    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN) {
        mainBox.getChildren().add(0, menuBTN);
    }

    @Override
    public void setUp() {
        this.borderPane = new BorderPane();

        scene = new Scene(borderPane, 1000, 600);
        scene.getStylesheets().add("Style.css");

        mainBox = new VBox(20);
        BorderPane.setMargin(mainBox, new Insets(50, 50, 50, 50));
        borderPane.setCenter(mainBox);

        HBox topBox = new HBox(560);
        mainBox.getChildren().add(topBox);

        Label titleLBL = new Label("All Products");
        titleLBL.setId("title");
        topBox.getChildren().add(titleLBL);

        buttonBox = new HBox(5);
        topBox.getChildren().add(buttonBox);

        checkoutBTN = new Button("CHECKOUT");
        checkoutBTN.setMaxHeight(10);
        buttonBox.getChildren().add(checkoutBTN);

        Label selectCategoryLBL = new Label("Select category");
        // selectCategoryLBL.setID("align");
        selectCategory = new ComboBox<String>();
        selectCategory.getItems().addAll(productOptionsModel.getCategories());
        selectCategory.setValue("drinks");  // drinks by default

        selectCategoryBTN = new Button("View Products");

        HBox categorySelectionBox = new HBox(5);
        categorySelectionBox.getChildren().addAll(selectCategoryLBL, selectCategory, selectCategoryBTN);
        mainBox.getChildren().add(categorySelectionBox);

        productOptionsBox = new VBox(10);
        productOptionsBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.getChildren().add(productOptionsBox);

        setUpDrinksTable();  //show drinks table as default

        setUpSelectCategoryBTN();

        this.popupBorderPane = new BorderPane();
        popupScene = new Scene(popupBorderPane, 500, 300);

        setUpCheckoutButton();
    }


    public void setUpDrinksTable(){

        Label drinksLBL = new Label("Drinks");
        drinksLBL.setId("title");
        productOptionsBox.getChildren().add(drinksLBL);

        drinksTable = new TableView<ProductToDisplay>();
        drinksTable.setPlaceholder(new Label("No drinks available"));
        drinksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //

        TableColumn<ProductToDisplay, String> nameColumn = new TableColumn<ProductToDisplay, String>("Item");
        nameColumn.setCellValueFactory(new PropertyValueFactory<ProductToDisplay, String>("name"));
        drinksTable.getColumns().add(nameColumn);

        TableColumn<ProductToDisplay, Double> priceColumn = new TableColumn<ProductToDisplay, Double>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<ProductToDisplay, Double>("price"));
        drinksTable.getColumns().add(priceColumn);

        TableColumn<ProductToDisplay, Integer> quantityColumn = new TableColumn<ProductToDisplay, Integer>("Quantity Available");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<ProductToDisplay, Integer>("quantity"));
        drinksTable.getColumns().add(quantityColumn);

        TableColumn addBTNColumn = new TableColumn("Action");
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<ProductToDisplay, String>, TableCell<ProductToDisplay, String>> cellFactory
                = //
                new Callback<TableColumn<ProductToDisplay, String>, TableCell<ProductToDisplay, String>>() {
            @Override
            public TableCell call(final TableColumn<ProductToDisplay, String> param) {
                final TableCell<ProductToDisplay, String> cell = new TableCell<ProductToDisplay, String>() {

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
                                setUpPopupScreen(getTableView().getItems().get(getIndex()));
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

        chocolatesTable = new TableView<ProductToDisplay>();
        chocolatesTable.setPlaceholder(new Label("No chocolates available"));
        chocolatesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ProductToDisplay, String> nameColumn = new TableColumn<ProductToDisplay, String>("Item");
        nameColumn.setCellValueFactory(new PropertyValueFactory<ProductToDisplay, String>("name"));
        chocolatesTable.getColumns().add(nameColumn);

        TableColumn<ProductToDisplay, Double> priceColumn = new TableColumn<ProductToDisplay, Double>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<ProductToDisplay, Double>("price"));
        chocolatesTable.getColumns().add(priceColumn);

        TableColumn<ProductToDisplay, Integer> quantityColumn = new TableColumn<ProductToDisplay, Integer>("Quantity Available");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<ProductToDisplay, Integer>("quantity"));
        chocolatesTable.getColumns().add(quantityColumn);

        TableColumn addBTNColumn = new TableColumn("Action");
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<ProductToDisplay, String>, TableCell<ProductToDisplay, String>> cellFactory
                = //
                new Callback<TableColumn<ProductToDisplay, String>, TableCell<ProductToDisplay, String>>() {
            @Override
            public TableCell call(final TableColumn<ProductToDisplay, String> param) {
                final TableCell<ProductToDisplay, String> cell = new TableCell<ProductToDisplay, String>() {

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
                                setUpPopupScreen(getTableView().getItems().get(getIndex()));
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


        chipsTable = new TableView<ProductToDisplay>();
        chipsTable.setPlaceholder(new Label("No chips available"));
        chipsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ProductToDisplay, String> nameColumn = new TableColumn<ProductToDisplay, String>("Item");
        nameColumn.setCellValueFactory(new PropertyValueFactory<ProductToDisplay, String>("name"));
        chipsTable.getColumns().add(nameColumn);

        TableColumn<ProductToDisplay, Double> priceColumn = new TableColumn<ProductToDisplay, Double>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<ProductToDisplay, Double>("price"));
        chipsTable.getColumns().add(priceColumn);

        TableColumn<ProductToDisplay, Integer> quantityColumn = new TableColumn<ProductToDisplay, Integer>("Quantity Available");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<ProductToDisplay, Integer>("quantity"));
        chipsTable.getColumns().add(quantityColumn);

        TableColumn addBTNColumn = new TableColumn("Action");
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<ProductToDisplay, String>, TableCell<ProductToDisplay, String>> cellFactory
                = //
                new Callback<TableColumn<ProductToDisplay, String>, TableCell<ProductToDisplay, String>>() {
            @Override
            public TableCell call(final TableColumn<ProductToDisplay, String> param) {
                final TableCell<ProductToDisplay, String> cell = new TableCell<ProductToDisplay, String>() {

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
                                setUpPopupScreen(getTableView().getItems().get(getIndex()));
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


        candiesTable = new TableView<ProductToDisplay>();
        candiesTable.setPlaceholder(new Label("No candies available"));
        candiesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ProductToDisplay, String> nameColumn = new TableColumn<ProductToDisplay, String>("Item");
        nameColumn.setCellValueFactory(new PropertyValueFactory<ProductToDisplay, String>("name"));
        candiesTable.getColumns().add(nameColumn);

        TableColumn<ProductToDisplay, Double> priceColumn = new TableColumn<ProductToDisplay, Double>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<ProductToDisplay, Double>("price"));
        candiesTable.getColumns().add(priceColumn);

        TableColumn<ProductToDisplay, Integer> quantityColumn = new TableColumn<ProductToDisplay, Integer>("Quantity Available");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<ProductToDisplay, Integer>("quantity"));
        candiesTable.getColumns().add(quantityColumn);

        TableColumn addBTNColumn = new TableColumn("Action");
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<ProductToDisplay, String>, TableCell<ProductToDisplay, String>> cellFactory
                = //
                new Callback<TableColumn<ProductToDisplay, String>, TableCell<ProductToDisplay, String>>() {
            @Override
            public TableCell call(final TableColumn<ProductToDisplay, String> param) {
                final TableCell<ProductToDisplay, String> cell = new TableCell<ProductToDisplay, String>() {

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
                                setUpPopupScreen(getTableView().getItems().get(getIndex()));
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
        List<ProductToDisplay> products = productOptionsModel.getProductsToDisplay();

        table.getItems().clear();

        if (products.size() == 0){
            table.setPlaceholder(new Label("No products available"));
        }

        for (ProductToDisplay product: products){
            if (product.getCategory().equals(category)){
                table.getItems().add(product);

            }
        }
    }

    public void setUpSelectCategoryBTN(){
        selectCategoryBTN.setOnAction((e) -> {
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

    public void setUpPopupScreen(ProductToDisplay product){   //todo: popup screen doesnt close after timeout - mouse presses not registered + window doesnt close

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
        ComboBox<Integer> selectQuantity = new ComboBox<Integer>();
        selectQuantity.getItems().addAll(quantities);
        selectQuantity.setValue(1);  // 1 by default
        Button addToCartBTN = new Button("Add to cart");
        selectionBox.getChildren().addAll(selectQuantity, addToCartBTN);

        addToCartBTN.setOnAction((e) -> {
            int selectedQuantity = selectQuantity.getValue();
            popupStage.hide();

            ///// add the product obj + selected quantity to cart
        });
    }

    public void setUpCheckoutButton(){

        popupScene.getStylesheets().add("Style.css");

        Stage popupStage = new Stage();

        checkoutBTN.setOnAction((e) -> {
            popupStage.show();
        });


        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(this.stage);

        VBox popupMainBox = new VBox(60);
        popupBorderPane.setMargin(popupMainBox, new Insets(50, 50, 50, 50));
        popupBorderPane.setCenter(popupMainBox);
        popupMainBox.getChildren().add(new Label("Select payment type"));
        HBox selectionBox = new HBox(80);
        popupMainBox.getChildren().add(selectionBox);

        List<String> paymentTypes = Arrays.asList(new String[] { "Cash", "Card"});

        ComboBox<String> selectPaymentType = new ComboBox<String>();
        selectPaymentType.getItems().addAll(paymentTypes);
        selectPaymentType.setValue("Cash");  // 1 by default
        Button checkoutBTN = new Button("Continue to Checkout");
        selectionBox.getChildren().addAll(selectPaymentType, checkoutBTN);

        checkoutBTN.setOnAction((e) -> {
           String paymentTypeValue = selectPaymentType.getValue();

           System.out.println(paymentTypeValue);

           if (paymentTypeValue.equals("Cash")){
               mainView.goToCashPaymentView();
           } else {
               mainView.goToCardPaymentView();
           }

            popupStage.hide();

            ///// add the product obj + selected quantity to cart
        });

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

}
