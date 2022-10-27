package assignment2.view;

import assignment2.model.Cash;
import assignment2.model.CashPaymentModel;
import assignment2.model.MainModel;
import assignment2.model.Product;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModifyCashView implements View {
    private MainModel mainModel;
    private MainView mainView;
    private VBox mainBox;
    private BorderPane borderPane;
    private Scene scene;
    private TableView<Cash> cashTable;
    private List<Cash> cashList;
    private BorderPane popUpBP;
    private Scene popupScene;
    private Stage stage;
    private CashPaymentModel cashPaymentModel;

    public ModifyCashView(MainModel mainModel,Stage stage, MainView mainView){
        this.mainModel = mainModel;
        this.mainView = mainView;
        this.stage = stage;
        this.cashPaymentModel = this.mainModel.getCashPaymentModel();
    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN){
        //If isCashier
        mainBox.getChildren().add(0,menuBTN);
    }

    @Override
    public void setUp(){
        this.borderPane = new BorderPane();
        this.popUpBP = new BorderPane();
        popupScene = new Scene(popUpBP,500,300);

        scene = new Scene(borderPane,1000,600);
        scene.getStylesheets().add("Style.css");

        mainBox = new VBox(0);
        BorderPane.setMargin(mainBox,new Insets(50,50,50,50));
        borderPane.setCenter(mainBox);

        Label titleLBL = new Label("Modify Cash");
        titleLBL.setId("title");
        mainBox.getChildren().add(titleLBL);

        cashTable = new TableView<Cash>();

        mainBox.getChildren().add(cashTable);

        setUpCashTable();
        showCashAmount();
    }
    public void setUpCashTable(){
        cashTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Cash, String> nameColumn = new TableColumn<Cash, String>("name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<Cash,String>("name"));

        TableColumn<Cash, Integer> amountColumn = new TableColumn<Cash, Integer>("amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<Cash,Integer>("amount"));

        TableColumn addBTNColumn = new TableColumn("Modify");
        addBTNColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<Cash, String>, TableCell<Cash, String>> cellFactory
                = //
                new Callback<TableColumn<Cash, String>, TableCell<Cash, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Cash, String> param) {
                        final TableCell<Cash, String> cell = new TableCell<Cash, String>() {

                            final Button btn = new Button("Modify");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        // add to cart
                                        System.out.println("hello");
                                        setUpPopupScreen(getTableView().getItems().get(getIndex()), cashTable);
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
        cashTable.getColumns().addAll(nameColumn,amountColumn,addBTNColumn);
    }
    public void showCashAmount(){
        cashList = mainModel.getCash();
        cashTable.getItems().clear();

        for(int i = 0; i<cashList.size();i++){
            cashTable.getItems().add(cashList.get(i));
        }
    }
    public void setUpPopupScreen(Cash cash, TableView<Cash> table){
        popupScene.getStylesheets().add("Style.css");

        Stage popupStage = new Stage();

        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(this.stage);
        popupStage.show();

        VBox popupMainBox = new VBox(60);
        popUpBP.setMargin(popupMainBox,new Insets(50,50,50,50));
        popUpBP.setCenter(popupMainBox);
        popupMainBox.getChildren().add(new Label("Enter the cash amount"));

        HBox enterBox = new HBox(80);
        popupMainBox.getChildren().add(enterBox);

        TextField inputAmount = new TextField();
        Button changeBTN = new Button("Change Amount");
        enterBox.getChildren().addAll(inputAmount,changeBTN);

        changeBTN.setOnAction((e)->{
            try {
                int newCashAmount = Integer.parseInt(inputAmount.getText());
                if(newCashAmount < 0){
                    Alert notLoggedin = new Alert(Alert.AlertType.ERROR);
                    notLoggedin.setHeaderText("Invalid quantity. Please enter an integer greater than 0.");
                    notLoggedin.showAndWait();
                    return;
                }
                cashPaymentModel.updateCash(newCashAmount, cash);
                popupStage.hide();
            }catch (NumberFormatException n) {
                Alert notLoggedin = new Alert(Alert.AlertType.ERROR);
                notLoggedin.setHeaderText("Invalid quantity. Please insert a valid integer.");
                notLoggedin.showAndWait();
            }

            cashTable.refresh();
        });
    }

    @Override
    public void refresh(){}
    @Override
    public List<Scene> getScenes(){
        return Arrays.asList(new Scene[] { scene });
    }
    @Override
    public void setUpCancelBTN(Button cancelBTN){}
}
