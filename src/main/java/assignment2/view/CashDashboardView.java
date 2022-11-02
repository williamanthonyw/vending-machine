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
    private List<List<String>> cashierTransactionString;

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

        HBox cashBox1 = new HBox();
        Label cashLBL = new Label("List of available change");
        cashBox1.getChildren().add(cashLBL);

        HBox cashBox2 = new HBox();
        TextArea cashText = new TextArea();
        cashText.setMinHeight(100);
        cashText.setMinWidth(900);
        cashText.setEditable(false);

        String result = "";
        cashString = cashPaymentModel.getCashString();

//        cashBox.setAlignment(Pos.CENTER);
        cashBox2.getChildren().add(cashText);

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

        mainBox.getChildren().addAll(cashBox1, cashBox2);

         //List of Purchased Items
         cashierTransactionString = this.mainModel.getCsvFileParser().readCashierTransactions();

         HBox transactionBox1 = new HBox();
         Label transactionLBL = new Label("List of transactions");
         
         
         HBox transactionBox2 = new HBox();
         TextArea transactionText = new TextArea();
         transactionText.setMinHeight(100);
         transactionText.setMinWidth(900);
         transactionText.setEditable(false);
 
         //read transactions done from file
         String transTemp = "";
 
         if (cashierTransactionString.size() == 0){
             transTemp = "No transactions available.";
         }
         else{
             for (List<String> s : cashierTransactionString){
                 String temp2 = String.join(", ", s).stripTrailing();
                 temp2 = temp2.concat("\n");
             
                 transTemp = transTemp.concat(temp2);
             }
         }
         
         transactionText.setText(transTemp);
 
         transactionBox1.getChildren().add(transactionLBL);
         transactionBox2.getChildren().add(transactionText);
 
         mainBox.getChildren().addAll(transactionBox1, transactionBox2);


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
