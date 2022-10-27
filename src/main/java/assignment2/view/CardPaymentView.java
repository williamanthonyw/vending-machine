
package assignment2.view;
import assignment2.model.CardPaymentModel;
import assignment2.model.JsonParser;
import assignment2.model.MainModel;
import com.sun.tools.javac.Main;
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
        cardUserNameBox.getChildren().addAll(cardNameLBL, userNameTF);
        Label cardNumberLBL = new Label("Credit card number: ");

        PasswordField cardNumberTF = new PasswordField();
        if(mainModel.getUser().getCardUser() != null && mainModel.isLoggedIn()){
            userNameTF.setText(mainModel.getUser().getCardUser().getName());
            cardNumberTF.setText(mainModel.getUser().getCardUser().getCardNumber());
        }
        cardNumberTF.setSkin(new PasswordSkin(cardNumberTF));
        cardNumberBox.getChildren().addAll(cardNumberLBL, cardNumberTF);

        HBox totalCartPrice = new HBox();
        Label totalCartPriceLBL = new Label("Total Cart Price: " + mainModel.getCartPrice());
        totalCartPrice.getChildren().add(totalCartPriceLBL);
        mainBox.getChildren().add(totalCartPrice);

        CheckBox checkBox = new CheckBox("Save Card Detail");
        checkBox.setTextFill(Color.WHITE);
        mainBox.getChildren().add(checkBox);

        Button confirmBTN = new Button("Checkout");
        mainBox.getChildren().add(confirmBTN);

        confirmBTN.setOnMousePressed(event -> {
            if (cardPaymentModel.paymentProcess(userNameTF.getText(), cardNumberTF.getText(),checkBox.isSelected())) {

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setHeaderText("Thank you for your purchase");

//                if (mainModel.isLoggedIn()){
//                    ((Button) successAlert.getDialogPane().lookupButton(ButtonType.OK)).setOnAction(e -> {
//
//                    });
//
//                    ((Button) successAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Save Card");
//
//                    ((Button) successAlert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("No save card");
//
//                }

                successAlert.showAndWait();

                mainModel.checkout();
                mainView.goToProductOptionsView();

            } else {

                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Invalid credit card details. Please enter valid details.");
                errorAlert.showAndWait();

            }
        });
    }

}