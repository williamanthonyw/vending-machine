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

public class UpdateProductView extends ProductView{

    private Product product;

    public UpdateProductView(MainModel mainModel,  MainView mainView, SellerInventoryView sellerInventoryView, Product product){
        super(mainModel, mainView, sellerInventoryView);
        this.product = product;
        System.out.println(product);
    }


    @Override
    public void setUp(){

        super.setUp();
        titleLBL.setText("Update " + product.getName() + " (" + product.getCategory() + ") - " + product.getCode());
        super.setUpForm(product);

    }





}
