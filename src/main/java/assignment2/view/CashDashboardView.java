package assignment2.view;

import assignment2.model.Cash;
import assignment2.model.CashPaymentModel;
import assignment2.model.MainModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;

public class CashDashboardView implements View{
    private CashPaymentModel cashPaymentModel;
    private MainModel mainModel;
    private MainView mainView;
    private Scene scene;
    private VBox mainBox;
    private BorderPane borderPane;
    private List<Cash> cashList;
    private List<List<String>> cashString;

    public CashDashboardView(MainModel mainModel, MainView mainView){
        this.mainModel = mainModel;
        this.mainView = mainView;
        this.cashPaymentModel = mainModel.getCashPaymentModel();
    }

    @Override
    public List<Scene> getScenes() {
        return Arrays.asList(new Scene[]{scene});
    }

    @Override
    public void setUp() {
        borderPane = new BorderPane();
        scene = new Scene(borderPane,1000,600);
        scene.getStylesheets().add("Style.css");

        mainBox = new VBox(30);
        BorderPane.setMargin(mainBox, new Insets(50,50,50,50));
        borderPane.setCenter(mainBox);

        cashList = this.cashPaymentModel.getCashList();
        Label titleLBL = new Label("Cash Dashboard");
        titleLBL.setId("title");
        mainBox.getChildren().add(titleLBL);

//        HBox cashBox = new HBox();
        TextArea cashText = new TextArea();
        cashText.setMinHeight(250);
        cashText.setMinWidth(200);
        String result = "";
        cashString = cashPaymentModel.getCashString();

//        cashBox.setAlignment(Pos.CENTER);
        mainBox.getChildren().addAll(cashText);

        if(cashList.size() == 0){
            result = "No cash available";
        }else{
            for(List<String> s: cashString){
                String temp = String.join(", ",s).stripTrailing();
                temp = temp.concat("\n");
                result = result.concat(temp);
            }
        }
        cashText.setText(result);
    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN) {
        mainBox.getChildren().add(0,menuBTN);
    }

    @Override
    public void setUpCancelBTN(Button cancelBTN) {

    }

    @Override
    public void refresh() {

    }
}
