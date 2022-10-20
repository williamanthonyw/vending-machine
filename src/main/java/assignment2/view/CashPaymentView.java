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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class CashPaymentView implements View{
    private CashPaymentModel cashPaymentModel;
    private MainModel mainModel;

    private Scene scene;
    private BorderPane borderPane;

    private VBox mainBox;
    private ComboBox<String> cashList;

    private HBox cashLabel;
    private HBox paymentHBox;
    private HBox cashInserted;
    private HBox totalCartPrice;
    private HBox payOrCancel;

    private TextField quantityInput;

    private Button add;
    private Button pay;

    private HashMap<Double, Integer> payment;

    private Popup changePopup;
    private Popup notEnoughChange;
    private Popup insertMoreCash;
    private Popup cancelPopup;

    private Label insertedAmount;
    private Label totalCartPriceLBL;
   
    public CashPaymentView(MainModel mainModel){
        this.mainModel = mainModel;
        this.cashPaymentModel = this.mainModel.getCashPaymentModel();
    }

    @Override
    public List<Scene> getScenes(){
        return Arrays.asList(new Scene[] { scene });
    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN){
        mainBox.getChildren().add(0, menuBTN);
    }


    @Override
    public void setUp(){

        this.borderPane = new BorderPane();

        scene = new Scene(borderPane, 1000, 600);
        scene.getStylesheets().add("Style.css");

        this.mainBox = new VBox(30);
        BorderPane.setMargin(this.mainBox, new Insets(50, 50, 50, 50));
        borderPane.setCenter(this.mainBox);

        //title
        Label titleLBL = new Label("Checkout");
        titleLBL.setId("title");
        mainBox.getChildren().add(titleLBL);

        //mode: cash payment/card payment
//        paymentMode = new ChoiceBox<String>();
//        paymentMode.getItems().addAll("Cash", "Card");
//
//        //if card is selected. go to cardpaymentview.java
//        if (paymentMode.getValue().equals("Card")){
//
//        };

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

        // Total cash inserted
        cashInserted = new HBox();
        insertedAmount = new Label("Total Cash Inserted: ");
        cashInserted.getChildren().add(insertedAmount);
        mainBox.getChildren().add(cashInserted);

        //Total price
        totalCartPrice = new HBox();
        totalCartPriceLBL = new Label("Total Cart Price: $5");
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

        payment = new HashMap<Double, Integer>();
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


        //add button
        add.setOnAction((ActionEvent e) -> {

            try {
                switch(cashList.getValue()){
                    case "5c":
                        payment.put(0.05, Integer.parseInt(quantityInput.getText()));
                        break;
                    case "10c":
                        payment.put(0.10, Integer.parseInt(quantityInput.getText()));
                        break;
                    case "20c":
                        payment.put(0.20, Integer.parseInt(quantityInput.getText()));
                        break;
                    case "50c":
                        payment.put(0.50, Integer.parseInt(quantityInput.getText()));
                        break;
                    case "$1":
                        payment.put(1.00, Integer.parseInt(quantityInput.getText()));
                        break;
                    case "$2":
                        payment.put(2.00, Integer.parseInt(quantityInput.getText()));
                        break;
                    case "$5":
                        payment.put(5.00, Integer.parseInt(quantityInput.getText()));
                        break;
                    case "$10":
                        payment.put(10.00, Integer.parseInt(quantityInput.getText()));
                        break;
                    case "$20":
                        payment.put(20.00, Integer.parseInt(quantityInput.getText()));
                        break;
                    case "$50":
                        payment.put(50.00, Integer.parseInt(quantityInput.getText()));
                        break;
                    case "$100":
                        payment.put(100.00, Integer.parseInt(quantityInput.getText()));
                        break;
                }

                calculateCashUserInserted();


            } catch (NumberFormatException n) {
                System.out.println("Please input a valid number");
            }



        });

        //pay button
        pay.setOnAction((ActionEvent e) -> {
            setupCashPayment();
        });


    }

    public void calculateCashUserInserted(){

        double total = 0;

        for (double note : payment.keySet()){
            total += note * payment.get(note);
        }

        System.out.println(total);

        insertedAmount.setText("Total cash inserted: " +  Math.round(total*100.0)/100.0 );
    }

    public void setupCashPayment(){

        double totalPayment = this.cashPaymentModel.calculatePayment(payment);
        double totalPrice = 5.0;

        try{
            HashMap<String, Integer> change = this.cashPaymentModel.calculateChange(totalPayment, totalPrice, payment);
            changePopup = new Popup();
            Text thankYouText = new Text("Thank you for your purchase");

            //String formatting for change
            String changeFormat = String.format("Here is your change: %f", totalPayment-totalPrice);
            for (Map.Entry<String, Integer> c: change.entrySet()){
                String temp = String.format("%s: %d\n", c.getKey(), c.getValue());
                changeFormat = changeFormat.concat(temp);
            }
            Text changeText = new Text(changeFormat);
            Button back = new Button("Back");

            changePopup.getContent().addAll(thankYouText, changeText, back);

            back.setOnAction((ActionEvent e) -> {
                changePopup.hide();
            });
        }

        //change in vending machine is not enough
        catch(InsufficientChangeException e){
            notEnoughChange = new Popup();
            Text notEnoughText = new Text("No change available. Please insert different notes/coins or cancel your transaction");
            HBox cancelOrBack = new HBox();
            Button cancel = new Button("Cancel");
            Button back = new Button("Button");
            cancelOrBack.getChildren().addAll(back, cancel);
            notEnoughChange.getContent().addAll(notEnoughText, cancelOrBack);
        }


        //customer payment is not enough
        catch(PaymentNotEnoughException f){
            insertMoreCash = new Popup();
            Text insertLbl = new Text("Please insert more cash or cancel your transaction");
            HBox cancelOrBack = new HBox();
            Button cancel = new Button("Cancel");
            Button back = new Button("Button");
            cancelOrBack.getChildren().addAll(back, cancel);
            insertMoreCash.getContent().addAll(insertLbl, cancelOrBack);

            //back button
            back.setOnAction((ActionEvent e) -> {
                insertMoreCash.hide();
            });

            //cancel button
            cancel.setOnAction((ActionEvent e) -> {
                insertMoreCash.hide();
                //cancel
            });

        }

    }

    public void setUpCancelBTN(Button cancelBTN){
        payOrCancel.getChildren().add(cancelBTN);
    }





}
