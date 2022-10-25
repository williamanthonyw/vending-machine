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

public class AddProductView extends ProductView{

    private Product product;


    public AddProductView(MainModel mainModel,  MainView mainView, SellerInventoryView sellerInventoryView){
        super(mainModel, mainView, sellerInventoryView);
        this.product = new Product();
    }


    @Override
    public void setUp(){

        super.setUp();
        titleLBL.setText("Add New Product");
        super.setUpForm(product);

    }
    
    @Override
    public UpdateProductState getResult(Product product,
                                        String name,
                                        String category,
                                        int itemCode,
                                        double price,
                                        int quantity){
        return inventoryModel.addProduct(product, name, category, itemCode, price, quantity);
    }





}
