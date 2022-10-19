package assignment2.view;

// import assignment2.model.Product;
import assignment2.model.ProductToDisplay; 
import assignment2.model.MainModel;
import assignment2.model.ProductOptionsModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.beans.binding.*;
import javafx.util.*;

import java.util.*;

import java.io.*;

public class ProductOptionsView implements View{
    private Scene scene;
    private MainModel mainModel;
    private ProductOptionsModel productOptionsModel;
    private VBox mainBox;
    private VBox productOptionsBox;

    private BorderPane borderPane;

    private TableView<ProductToDisplay> drinksTable; 
    private TableView<ProductToDisplay> chocolatesTable; 
    private TableView<ProductToDisplay> chipsTable;
    private TableView<ProductToDisplay> candiesTable;
    private Button checkoutBTN;
    private Button cancelBTN;
    private Button selectCategoryBTN;
    ComboBox<String> selectCategory;

    public ProductOptionsView(MainModel mainModel){
        this.mainModel = mainModel;
        this.productOptionsModel = mainModel.getProductOptionsModel();

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

        HBox buttonBox = new HBox(5);
        topBox.getChildren().add(buttonBox);

        checkoutBTN = new Button("CHECKOUT");
        checkoutBTN.setMaxHeight(10);
        buttonBox.getChildren().add(checkoutBTN);

        cancelBTN = new Button("Cancel");
        cancelBTN.setMaxHeight(10);
        buttonBox.getChildren().add(cancelBTN);

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
        //////////////////////////////////////////////////////////////////////////////
        
        // ScrollPane scrollPane = new ScrollPane();
        // scrollPane.setContent(productOptionsBox);
        // mainBox.getChildren().add(scrollPane);
        // scrollPane.setFitToWidth(true);

        // Label drinksLBL = new Label("Drinks");
        // drinksLBL.setId("title");
        // productOptionsBox.getChildren().add(drinksLBL);
        // setUpDrinksTable();

        // Label chocolatesLBL = new Label("Chocolates");
        // chocolatesLBL.setId("title");
        // productOptionsBox.getChildren().add(chocolatesLBL);
        // setUpChocolatesTable();

        // Label chipsLBL = new Label("Chips");
        // chipsLBL.setId("title");
        // productOptionsBox.getChildren().add(chipsLBL);
        // setUpChipsTable();

        // Label candiesLBL = new Label("Candies");
        // candiesLBL.setId("title");
        // productOptionsBox.getChildren().add(candiesLBL);
        // setUpCandiesTable();
        
        setUpCheckoutButton();
        setUpCancelButton();
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
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
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
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
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
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
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
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
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

    public void setUpCheckoutButton(){

    }

    public void setUpCancelButton(){

    }

    @Override
    public Scene getScene(){
        return scene;
    }

}
