package assignment2.view;

import assignment2.model.MainModel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.BorderPane;

import java.util.Arrays;
import java.util.List;

public class CardPaymentView implements View{
    private MainModel mainModel;
    private Scene scene;


    private BorderPane borderPane;
    public CardPaymentView(MainModel mainModel){
        this.mainModel = mainModel;
        //CardPaymentModel
    }
    @Override
    public List<Scene> getScenes(){
        return Arrays.asList(new Scene[] { scene });
    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN) {

    }

    @Override
    public void setUpCancelBTN(Button cancelBTN){
//        mainBox.getChildren().add(cancelBTN);
    }

    @Override
    public void setUp(){}
}
