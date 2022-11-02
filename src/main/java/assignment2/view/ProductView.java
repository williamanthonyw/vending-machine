package assignment2.view;

import assignment2.model.InventoryModel;
import assignment2.model.MainModel;
import assignment2.model.Product;
import assignment2.model.UpdateProductState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class ProductView implements View {

    private SellerInventoryView sellerInventoryView;

    private Scene scene;

    private VBox mainBox;

    private BorderPane borderPane;
    protected InventoryModel inventoryModel;
    private MainView mainView;
    protected Label titleLBL;

    public ProductView(MainModel mainModel,  MainView mainView, SellerInventoryView sellerInventoryView){
        this.inventoryModel = mainModel.getInventoryModel();
        this.sellerInventoryView = sellerInventoryView;
        this.mainView = mainView;
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

        char arrowSymbol = '\u2190';
        Button backBTN = new Button(arrowSymbol + "");
        backBTN.setOnAction((e) -> {
            mainView.returnToView(sellerInventoryView);
        });
        mainBox.getChildren().add(backBTN);

        this.titleLBL = new Label();
        titleLBL.setId("title");
        mainBox.getChildren().add(titleLBL);


    }

    public void setUpForm(Product product) {

        VBox formBox = new VBox(20);
        mainBox.getChildren().add(formBox);

        VBox nameBox = new VBox(5);
        Label nameLBL = new Label("Name");
        nameLBL.setId("categoryLBL");
        ComboBox<String> nameCB = new ComboBox<String>();
        nameCB.getItems().addAll(inventoryModel.getAllProductNames());
        nameCB.setValue(product.getName());
        nameBox.getChildren().addAll(nameLBL, nameCB);

        VBox categoryBox = new VBox(5);
        Label categoryLBL = new Label("Category");
        categoryLBL.setId("categoryLBL");
        ComboBox<String> categoryCB = new ComboBox<String>();
        categoryCB.getItems().addAll(inventoryModel.getCategories());
        categoryCB.setValue(product.getCategory());
        categoryBox.getChildren().addAll(categoryLBL, categoryCB);

        VBox itemCodeBox = new VBox(5);
        Label itemCodeLBL = new Label("Item Code");
        itemCodeLBL.setId("categoryLBL");
        TextField itemCodeTF = new TextField(product.getCode() + "");
        itemCodeBox.getChildren().addAll(itemCodeLBL, itemCodeTF);

        VBox priceBox = new VBox(5);
        Label priceLBL = new Label("Price");
        priceLBL.setId("categoryLBL");
        TextField priceTF = new TextField(product.getPrice() + "");
        priceBox.getChildren().addAll(priceLBL, priceTF);

        VBox quantityBox = new VBox(5);
        Label quantityLBL = new Label("Quantity");
        quantityLBL.setId("categoryLBL");
        TextField quantityTF = new TextField(product.getQuantity() + "");
        quantityBox.getChildren().addAll(quantityLBL, quantityTF);


        Button savechangesBTN = new Button("Save Changes");

        savechangesBTN.setOnAction((e) -> {

            if (itemCodeTF.getText() == null){
                showErrorMsg("Please enter an item code.");
                return;
            } else if (quantityTF.getText() == null){
                showErrorMsg("Please enter a quantity.");
                return;
            } else if (priceTF.getText() == null){
                showErrorMsg("Please enter a price.");
                return;
            }

            Integer itemCode = null;
            Integer quantity = null;
            Double price = null;

            try {
                itemCode = Integer.parseInt(itemCodeTF.getText());
            } catch (NumberFormatException n){
                showErrorMsg("Please enter a valid item code.");
            }

            try {
                quantity = Integer.parseInt(quantityTF.getText());
            } catch (NumberFormatException n){
                showErrorMsg("Please enter a valid quantity.");
            }

            try {
                price = Double.parseDouble(priceTF.getText());
            } catch (NumberFormatException n){
                showErrorMsg("Please enter a valid price.");
            }

            if (itemCode != null &&
                    quantity != null &&
                    price != null){

                UpdateProductState result = getResult(product, nameCB.getValue(), categoryCB.getValue(), itemCode, price, quantity);

                switch (result){
                    case SUCCESS:
                        mainView.returnToView(sellerInventoryView);
                        break;
                    case DUPLICATE_PRODUCT_ERROR:
                        showErrorMsg("Product with name in category already exists.");
                        break;
                    case CATEGORY_ERROR:
                        showErrorMsg(nameCB.getValue() + " does not belong in " + categoryCB.getValue());
                        break;
                    case CODE_RANGE_ERROR:
                        int[] codeRange = inventoryModel.getCodeRange(categoryCB.getValue());
                        showErrorMsg("Products of category " + categoryCB.getValue() + " belong in the range " + codeRange[0] + "-" + codeRange[1]);
                        break;
                    case CODE_DUPLICATE_ERROR:
                        showErrorMsg("Please select a different item code, as a product already exists with this item code");
                        break;
                    case PRICE_ERROR:
                        showErrorMsg("Price must not be at least $0");
                        break;
                    case QUANTITY_ERROR:
                        showErrorMsg("Quantity must be between 0 and 15");
                        break;


                }



            }



        });


        formBox.getChildren().addAll(nameBox, categoryBox, itemCodeBox, priceBox, quantityBox, savechangesBTN);

    }

    public UpdateProductState getResult(Product product,
                                        String name,
                                        String category,
                                        int itemCode,
                                        double price,
                                        int quantity){

        UpdateProductState result = inventoryModel.updateProduct(product, name, category, itemCode, price, quantity);

        return result;

    }

    public void showErrorMsg(String errorMsg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Your changes have not been saved as you have entered invalid details");
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }

    @Override
    public List<Scene> getScenes() {
        return Arrays.asList(new Scene[] { scene });
    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN){

    }

    @Override
    public void setUpCancelBTN(Button cancelBTN){

    }

    @Override
    public void refresh(){

    }
}
