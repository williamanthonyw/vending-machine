package assignment2.view;

import assignment2.model.CardPaymentModel;
import assignment2.model.JsonParser;
import assignment2.model.MainModel;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CardPaymentView implements View{
    private MainModel mainModel;
    private CardPaymentModel cardPaymentModel;
    private Scene scene;

    private HBox cardUserNameBox;
    private HBox cardNumberBox;

    private VBox mainBox;


    private BorderPane borderPane;
    public CardPaymentView(MainModel mainModel){
        this.mainModel = mainModel;
        this.cardPaymentModel = mainModel.getCardPaymentModel();
    }
    @Override
    public Scene getScene(){
        return scene;
    }

    @Override
    public void setUpMenuBTN(MenuButton menuBTN) {

    }

    @Override
    public void setUp(){
        this.borderPane = new BorderPane();

        scene = new Scene(borderPane, 1000, 600);
        scene.getStylesheets().add("Style.css");

        mainBox = new VBox(30);
        BorderPane.setMargin(mainBox,new Insets(200,0,0,300));
        borderPane.setCenter(mainBox);

        cardUserNameBox = new HBox(20);
        mainBox.getChildren().add(cardUserNameBox);


        cardNumberBox = new HBox(10);
        mainBox.getChildren().add(cardNumberBox);

        if(mainModel.getUser().getCardUser() != null){
            setUpSaveCardView();
        }else{
            setUpView();
        }
    }

    public void setUpView(){
        Label cardNameLBL = new Label("Cardholder name: ");
        TextField userNameTF = new TextField();
        cardUserNameBox.getChildren().addAll(cardNameLBL,userNameTF);


        Label cardNumberLBL = new Label("Credit card number: ");
        PasswordField cardNumberTF = new PasswordField();
        cardNumberTF.setSkin(new PasswordSkin(cardNumberTF));
        cardNumberBox.getChildren().addAll(cardNumberLBL,cardNumberTF);

        Button confirmBTN = new Button("Confirm");
        mainBox.getChildren().add(confirmBTN);

        confirmBTN.setOnMousePressed(event -> {
            cardPaymentModel.paymentProcess(userNameTF.getText(),cardNumberTF.getText());
        });
    }

    public void setUpSaveCardView(){
        Label cardNameLBL = new Label("Cardholder name: ");
        TextField userNameTF = new TextField(mainModel.getUser().getCardUser().getName());
        cardUserNameBox.getChildren().addAll(cardNameLBL,userNameTF);

        Label cardNumberLBL = new Label("Credit card number: ");
        PasswordField cardNumberTF = new PasswordField();
        cardNumberTF.setText(mainModel.getUser().getCardUser().getCardNumber());
        cardNumberBox.getChildren().addAll(cardNumberLBL,cardNumberTF);

        Button confirmBTN = new Button("Confirm");
        mainBox.getChildren().add(confirmBTN);
        confirmBTN.setOnMousePressed(event -> {
            cardPaymentModel.paymentProcess(userNameTF.getText(),cardNumberTF.getText());
        });
    }
}
