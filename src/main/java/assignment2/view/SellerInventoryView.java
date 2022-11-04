package assignment2.view;

import assignment2.model.*;
import com.sun.tools.javac.Main;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SellerInventoryView implements View{

    private InventoryModel inventoryModel;
    private MainModel mainModel;
    private Scene scene;

    private VBox mainBox;
    private MainView mainView;

    private TableView<Product> productsTable;

    private VBox summaryBox;
    private HBox topBox;
    private String currentCategory;

    private BorderPane borderPane;
    private ComboBox<String> selectCategory;

    public SellerInventoryView(MainModel mainModel, MainView mainView){
        this.inventoryModel = mainModel.getInventoryModel(); 
        this.mainModel = mainModel;
        this.mainView = mainView;

    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN){
        mainBox.getChildren().add(0, menuBTN);
    }

    @Override
    public void setUpCancelBTN(Button cancelBTN){

    }

    public void showProducts(){

        List<Product> products = inventoryModel.getInventory(currentCategory);

        // reset table
        productsTable.getItems().clear();

        if (products.size() == 0){
            productsTable.setPlaceholder(new Label("You have bought 0 products"));
        }

        productsTable.getItems().addAll(products);

    }


    @Override
    public List<Scene> getScenes() {
        return Arrays.asList(new Scene[] { scene });
    }

    @Override
    public void refresh(){
        showProducts();
    }

    @Override
    public void setUp(){
        this.inventoryModel.initializeProductsToString();
        this.borderPane = new BorderPane();

        scene = new Scene(borderPane, 1000, 600);
        scene.getStylesheets().add("Style.css");

        mainBox = new VBox(30);
        BorderPane.setMargin(mainBox, new Insets(50, 50, 50, 50));
        borderPane.setCenter(mainBox);

        Label titleLBL = new Label("Your Products");
        titleLBL.setId("title");
        mainBox.getChildren().add(titleLBL);

        this.topBox = new HBox(440);
        mainBox.getChildren().add(topBox);

        setUpSelectCategory();

        Button addBTN = new Button("Add Product");
        addBTN.setOnAction((e) -> {
            mainView.gotoAddProductView(this);
        });
        topBox.getChildren().add(addBTN);

        Label selectLBL = new Label("Please select on a product to edit its details.");
        selectLBL.setId("categoryLBL");
        mainBox.getChildren().add(selectLBL);



        productsTable = new TableView<Product>();
        mainBox.getChildren().addAll(productsTable);
        setUpProductsTable();

        this.currentCategory = "drinks";
        showProducts();


    }


    public void setUpSelectCategory(){

        HBox selectCategoryBox = new HBox(20);
        Label selectCategoryLBL = new Label("Select category");
        selectCategory = new ComboBox<String>();
        selectCategory.getItems().addAll(inventoryModel.getCategories());
        selectCategory.setValue("drinks");  // drinks by default
        selectCategoryBox.getChildren().addAll(selectCategoryLBL, selectCategory);

        selectCategory.setOnAction((e) -> {
            this.currentCategory = selectCategory.getValue();
            showProducts();
        });


        topBox.getChildren().add(selectCategoryBox);

    }

    public void setUpProductsTable() {

        productsTable.setPlaceholder(new Label("You have 0 products"));
        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Product, String> nameColumn  = new TableColumn<Product, String>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productsTable.getColumns().add(nameColumn);

        TableColumn<Product, Double> categoryColumn = new TableColumn<Product, Double>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("category"));
        productsTable.getColumns().add(categoryColumn);

        TableColumn<Product, Double> codeColumn = new TableColumn<Product, Double>("Code");
        codeColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("code"));
        productsTable.getColumns().add(codeColumn);

        TableColumn<Product, Double> priceColumn = new TableColumn<Product, Double>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        productsTable.getColumns().add(priceColumn);

        TableColumn<Product, Integer> quantityColumn = new TableColumn<Product, Integer>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Product,Integer>("quantity"));
        productsTable.getColumns().add(quantityColumn);

        TableColumn<Product, String> deleteColumn = new TableColumn<Product, String>("Delete Product");
        deleteColumn.setCellValueFactory(new PropertyValueFactory<>(""));

        deleteColumn.setCellFactory(new Callback<TableColumn<Product, String>, TableCell<Product, String>>() {
                                        @Override
                                        public TableCell call(final TableColumn<Product, String> param) {
                                            final TableCell<Product, String> cell = new TableCell<Product, String>() {

                                                final Button btn = new Button("Delete");

                                                @Override
                                                public void updateItem(String item, boolean empty) {
                                                    super.updateItem(item, empty);
                                                    setText(null);
                                                    if (!empty) {
                                                        btn.setOnAction(event -> {
                                                            Product product = getTableView().getItems().get(getIndex());
                                                            inventoryModel.deleteProduct(product);

                                                            showProducts();
                                                        });
                                                        setGraphic(btn);
                                                    } else {
                                                        setGraphic(null);
                                                    }
                                                }
                                            };
                                            return cell;
                                        }
                                    });

        productsTable.getColumns().add(deleteColumn);

        productsTable.setOnMouseClicked((e) -> {

            if (productsTable.getSelectionModel().getSelectedItem() != null){
                mainView.goToUpdateProductView(this, productsTable.getSelectionModel().getSelectedItem());
            }

        });


    }


}
