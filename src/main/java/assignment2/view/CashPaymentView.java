package assignment2.view;

import assignment2.model.MainModel;
import assignment2.model.PaymentNotEnoughException;
import assignment2.model.CashPaymentModel;
import assignment2.model.InsufficientChangeException;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class CashPaymentView implements View{
    private CashPaymentModel cashPaymentModel;
    private MainModel mainModel;

    private Stage stage;
    private Scene scene;
    private Scene popupScene;
    private BorderPane borderPane;
    private BorderPane popupBorderpane;

    private VBox mainBox;
    private VBox popupBox;
    private ComboBox<String> cashList;

    private HBox cashLabel;
    private HBox paymentHBox;
    private HBox cashInserted;
    private HBox totalCartPrice;
    private HBox payOrCancel;

    private TextField quantityInput;

    private Button add;
    private Button pay;
    private Button cancel;

    private HashMap<Double, Integer> payment = new HashMap<Double, Integer>();


    private Alert changePopup;
    private Alert notEnoughChange;
    private Alert insertMoreCash;

    private Label insertedAmount;
    private Label totalCartPriceLBL;
    private MainView mainView;

    private double total;

    public CashPaymentView(MainModel mainModel, MainView mainView){
        this.mainModel = mainModel;
        this.cashPaymentModel = this.mainModel.getCashPaymentModel();
        this.mainView = mainView;
    }

    @Override
    public List<Scene> getScenes(){
        return Arrays.asList(new Scene[] { scene });
    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN){
        mainBox.getChildren().add(0, menuBTN);
    }


    // @Override
    // public void setupPopupTimeoutHandler(MainView mainView){

    // }

    @Override
    public void setUp(){

        stage = new Stage();

        this.borderPane = new BorderPane();
        scene = new Scene(borderPane, 1000, 600);
        scene.getStylesheets().add("Style.css");

        this.mainBox = new VBox(30);
        BorderPane.setMargin(this.mainBox, new Insets(50, 50, 50, 50));
        borderPane.setCenter(this.mainBox);

        this.popupBorderpane = new BorderPane();
        popupScene = new Scene(popupBorderpane);

        //title
        Label titleLBL = new Label("Checkout");
        titleLBL.setId("title");
        mainBox.getChildren().add(titleLBL);

        cashLabel = new HBox();
        Label cashLbl = new Label("Cash");
        cashLabel.getChildren().add(cashLbl);

        mainBox.getChildren().add(cashLabel);
        
        //dropdown of notes and coins + quantity + add button
        paymentHBox = new HBox();
        cashList = new ComboBox<String>();
        cashList.getItems().addAll("5c", "10c", "20c", "50c", "$1", "$2", "$5", "$10", "$20", "$50", "$100");

        quantityInput = new TextField("Quantity");
        add = new Button("Add");

        paymentHBox.getChildren().addAll(cashList, quantityInput, add);
        mainBox.getChildren().add(paymentHBox);

        payment.put(0.05, 0);
        payment.put(0.10, 0);
        payment.put(0.20, 0);
        payment.put(0.50, 0);
        payment.put(1.0, 0);
        payment.put(2.0, 0);
        payment.put(5.0, 0);
        payment.put(10.0, 0);
        payment.put(20.0, 0);
        payment.put(50.0, 0);
        payment.put(100.0, 0);

        // Total cash inserted
        cashInserted = new HBox();
        insertedAmount = new Label("Total Cash Inserted: $");
        cashInserted.getChildren().add(insertedAmount);
        mainBox.getChildren().add(cashInserted);

        //Total price
        totalCartPrice = new HBox();
        totalCartPriceLBL = new Label("Total Cart Price: " + mainModel.getCartPrice());
        totalCartPrice.getChildren().add(totalCartPriceLBL);
        mainBox.getChildren().add(totalCartPrice);

        //Pay and Cancel button
        payOrCancel = new HBox();
        pay = new Button("Pay");
        payOrCancel.getChildren().addAll(pay);
        mainBox.getChildren().add(payOrCancel);

        //listener for quantity input
        quantityInput.textProperty().addListener((a, Old, New) -> {
            if (New.equals("-")){
                //error label
            }
        });

        setUpAddButton();


        pay.setOnAction((ActionEvent e) -> {
            setupCashPayment();
        });

    }

    @Override
    public void refresh(){

    }

    public void calculateCashUserInserted(){

        total = 0;

        for (double note : payment.keySet()){
            total += note * payment.get(note);
        }

        insertedAmount.setText("Total Cash inserted: $" +  Math.round(total*100.0)/100.0 );
    }

    public void setUpAddButton(){

        //add button
        add.setOnAction((ActionEvent e) -> {

            if (cashList.getValue() == null){
                Alert notLoggedin = new Alert(Alert.AlertType.ERROR);
                notLoggedin.setHeaderText("Please select a cash quantity.");
                notLoggedin.showAndWait();
                return;
            }

            try {

                if (Integer.parseInt(quantityInput.getText()) <= 0){
                    Alert notLoggedin = new Alert(Alert.AlertType.ERROR);
                    notLoggedin.setHeaderText("Invalid quantity. Quantities must be more than 0.");
                    notLoggedin.showAndWait();
                    return;
                }

                switch(cashList.getValue()){
                    case "5c":
                        payment.put(0.05, Integer.parseInt(quantityInput.getText()) + payment.get(0.05));
                        break;
                    case "10c":
                        payment.put(0.10, Integer.parseInt(quantityInput.getText()) + payment.get(0.10));
                        break;
                    case "20c":
                        payment.put(0.20, Integer.parseInt(quantityInput.getText()) + payment.get(0.20));
                        break;
                    case "50c":
                        payment.put(0.50, Integer.parseInt(quantityInput.getText()) + payment.get(0.50));
                        break;
                    case "$1":
                        payment.put(1.00, Integer.parseInt(quantityInput.getText()) + payment.get(1.0));
                        break;
                    case "$2":
                        payment.put(2.00, Integer.parseInt(quantityInput.getText()) +  payment.get(2.00));
                        break;
                    case "$5":
                        payment.put(5.00, Integer.parseInt(quantityInput.getText()) + payment.get(5.00) );
                        break;
                    case "$10":
                        payment.put(10.00, Integer.parseInt(quantityInput.getText()) + payment.get(10.00));
                        break;
                    case "$20":
                        payment.put(20.00, Integer.parseInt(quantityInput.getText()) + payment.get(20.0));
                        break;
                    case "$50":
                        payment.put(50.00, Integer.parseInt(quantityInput.getText()) + payment.get(50.0));
                        break;
                    case "$100":
                        payment.put(100.00, Integer.parseInt(quantityInput.getText()));
                        break;
                }

                calculateCashUserInserted();


            } catch (NumberFormatException n) {
                Alert notLoggedin = new Alert(Alert.AlertType.ERROR);
                notLoggedin.setHeaderText("Invalid quantity. Please insert a valid integer.");
                notLoggedin.showAndWait();
            }



        });

        //pay button
        pay.setOnAction((ActionEvent e) -> {
            setupCashPayment();

        });

    }


    public void setupCashPayment(){

        double totalPayment = this.cashPaymentModel.calculatePayment(payment);
        double totalPrice = mainModel.getCartPrice();

        try{

            HashMap<String, Integer> change = this.cashPaymentModel.calculateChange(totalPayment, totalPrice, payment);

            changePopup = new Alert(Alert.AlertType.INFORMATION);
            changePopup.setHeaderText("Thank you for your purchase");

            //String formatting for change
            String changeFormat = String.format("Here is your change: $%.2f\n", Math.round(((totalPayment-totalPrice)*100.0))/100.0);

            for (Map.Entry<String, Integer> c: change.entrySet()){
                String temp = String.format("%s: %d\n", c.getKey(), c.getValue());
                changeFormat = changeFormat.concat(temp);
            }
            Label changeText = new Label(changeFormat);


            changePopup.setContentText(changeFormat);
            changePopup.showAndWait();

            mainModel.checkout();
            mainView.goToLastFiveProductsView();
        }

        //change in vending machine is not enough
       catch(InsufficientChangeException e){
            notEnoughChange = new Alert(Alert.AlertType.ERROR);


            notEnoughChange.setHeaderText("No change available. Please insert different notes/coins or cancel your transaction");

            ((Button) notEnoughChange.getDialogPane().lookupButton(ButtonType.OK)).setText("Back");

            notEnoughChange.showAndWait();
        } catch(PaymentNotEnoughException f){

            insertMoreCash = new Alert(Alert.AlertType.ERROR);
            insertMoreCash.setHeaderText("Please insert more cash or cancel your transaction");
//            //cancel button
//            ((Button) insertMoreCash.getDialogPane().lookupButton(ButtonType.OK)).setOnAction((ActionEvent e) -> {
//                insertMoreCash.hide();
//                //cancel
//            });

            ((Button) insertMoreCash.getDialogPane().lookupButton(ButtonType.OK)).setText("Back");

            insertMoreCash.showAndWait();


        }

    }

    @Override
    public void setUpCancelBTN(Button cancelBTN){
        payOrCancel.getChildren().add(cancelBTN);
    }





}
