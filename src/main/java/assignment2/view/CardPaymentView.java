
package assignment2.view;
import assignment2.model.*;
import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

public class CardPaymentView implements View {
    private MainModel mainModel;
    private CardPaymentModel cardPaymentModel;
    private Scene scene;
    private HBox cardUserNameBox;
    private HBox cardNumberBox;
    private VBox mainBox;
    private BorderPane borderPane;

    private MainView mainView;

    private ProductOptionsView productOptionsView;

    public CardPaymentView(MainModel mainModel, MainView mainView, ProductOptionsView productOptionsView) {
        this.mainModel = mainModel;
        this.mainView = mainView;
        this.cardPaymentModel = mainModel.getCardPaymentModel();
        this.productOptionsView = productOptionsView;
    }

    @Override
    public List<Scene> getScenes() {
        return Arrays.asList(new Scene[]{scene});
    }

    public void setUpMenuBTN(MenuButton menuBTN) {
        mainBox.getChildren().add(0, menuBTN);
    }

    public void setUpCancelBTN(Button cancelBTN) {
        mainBox.getChildren().add(cancelBTN);
    }

    @Override
    public void refresh(){

    }

    @Override
    public void setUp() {
        this.borderPane = new BorderPane();
        scene = new Scene(borderPane, 1000, 600);
        scene.getStylesheets().add("Style.css");

        mainBox = new VBox(30);
        BorderPane.setMargin(mainBox, new Insets(200, 0, 0, 300));
        BorderPane.setMargin(mainBox, new Insets(50, 50, 50, 50));
        borderPane.setCenter(mainBox);

        char arrowSymbol = '\u2190';
        Button backBTN = new Button(arrowSymbol + "");
        backBTN.setOnAction((e) -> {
            mainView.returnToView(productOptionsView);
        });
        mainBox.getChildren().add(backBTN);

        Label titleLBL = new Label("Checkout");
        titleLBL.setId("title");
        mainBox.getChildren().add(titleLBL);

        cardUserNameBox = new HBox(20);
        mainBox.getChildren().add(cardUserNameBox);

        cardNumberBox = new HBox(10);
        mainBox.getChildren().add(cardNumberBox);
//        if (mainModel.isLoggedIn()) {
//
//        } else {
//
//        }
        setUpView();
    }

    public void setUpView() {
        Label cardNameLBL = new Label("Cardholder name: ");
        TextField userNameTF = new TextField();
        Label cardNumberLBL = new Label("Credit card number: ");

        PasswordField cardNumberTF = new PasswordField();

        if(mainModel.getUser().getCardUser() != null && mainModel.isLoggedIn()){
            System.out.println("hello" + mainModel.getUser().getCardUser().getName());
            userNameTF.setText(mainModel.getUser().getCardUser().getName());
            cardNumberTF.setText(mainModel.getUser().getCardUser().getCardNumber());
        }

        cardUserNameBox.getChildren().addAll(cardNameLBL, userNameTF);

        cardNumberTF.setSkin(new PasswordSkin(cardNumberTF));
        cardNumberBox.getChildren().addAll(cardNumberLBL, cardNumberTF);

        HBox totalCartPrice = new HBox();
        Label totalCartPriceLBL = new Label("Total Cart Price: " + mainModel.getCartPrice());
        totalCartPrice.getChildren().add(totalCartPriceLBL);
        mainBox.getChildren().add(totalCartPrice);


        Button confirmBTN = new Button("Checkout");
        mainBox.getChildren().add(confirmBTN);

        confirmBTN.setOnMousePressed(event -> {

            CardUser response = cardPaymentModel.paymentProcess(userNameTF.getText(), cardNumberTF.getText());

            if (response != null) {
                Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
                successAlert.setHeaderText("Thank you for your purchase");

                if (mainModel.isLoggedIn() && mainModel.getUser().getCardUser() == null){

                    successAlert.setContentText("Do you wish to save your card details?");
                    ((Button) successAlert.getDialogPane().lookupButton(ButtonType.OK)).setOnAction(e -> {
                        cardPaymentModel.saveCard(response);
                    });

                    ((Button) successAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Save Card");

                    ((Button) successAlert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No");

                } else {
                    ((Button) successAlert.getDialogPane().lookupButton(ButtonType.CANCEL)).setVisible(false);
                }

                successAlert.showAndWait();

                mainModel.checkout("card");
                mainView.goToProductOptionsView();

            } else {

                Alert errorAlert = new Alert(Alert.AlertType.CONFIRMATION);
                errorAlert.setHeaderText("Invalid credit card details. Please enter valid details.");

                ((Button) errorAlert.getDialogPane().lookupButton(ButtonType.CANCEL)).setOnAction((ActionEvent e) -> {
                    mainView.cancelPopup();
                    mainModel.cancelTransaction(CancellationReason.USER_CANCELLATION, LocalDateTime.now());
                });

                errorAlert.showAndWait();

            }
        });
    }

}