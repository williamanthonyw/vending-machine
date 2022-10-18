package assignment2.view;

import assignment2.model.MainModel;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.BorderPane;

public class CardPaymentView implements View{
    private MainModel mainModel;
    private Scene scene;


    private BorderPane borderPane;
    public CardPaymentView(MainModel mainModel){
        this.mainModel = mainModel;
        //CardPaymentModel
    }
    @Override
    public Scene getScene(){
        return scene;
    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN) {

    }

    @Override
    public void setUp(){}
}
