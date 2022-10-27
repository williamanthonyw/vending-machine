package assignment2.view;

import assignment2.model.LoginModel;
//import assignment2.model.UserManagementModel;
import assignment2.model.MainModel;
import assignment2.model.User;
import assignment2.model.UserAccess;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.*;
import java.util.*;


public class UserManagementView implements View {

    private MainView mainView;
    private LoginModel userManagementModel;
    private MainModel mainModel;

    private TableView usersTable;
    private Scene scene;
    private VBox mainBox;
    private BorderPane borderPane;
    private Stage stage;

    private Scene popupScene;
    private BorderPane popupBorderPane;

    private ComboBox selectNewAccess;
    private Button saveNewAccessBTN;
    private Button removeUserBTN;

    public UserManagementView(MainModel mainModel, Stage stage, MainView mainView){
        this.mainModel = mainModel;
        this.stage = stage;
        this.mainView = mainView;
        this.userManagementModel = mainModel.getLoginModel();

    }


	@Override
	public List<Scene> getScenes() {
        return Arrays.asList(new Scene[] { scene, popupScene });
    }

	@Override
	public void setUp() {
        this.borderPane = new BorderPane();

        scene = new Scene(borderPane, 1000, 600);
        scene.getStylesheets().add("Style.css");

        mainBox = new VBox(50);
        BorderPane.setMargin(mainBox, new Insets(50, 50, 50, 50));
        borderPane.setCenter(mainBox);

        Label userManagementTitleLBL = new Label("Manage Users");
        userManagementTitleLBL.setId("title");

        usersTable = new TableView<User>();
        mainBox.getChildren().addAll(userManagementTitleLBL, usersTable);

        setUpUsersTable();

        this.popupBorderPane = new BorderPane();
        popupScene = new Scene(popupBorderPane, 500, 300);

        
	}


    public void setUpUsersTable(){
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<User, String> nameColumn = new TableColumn<User, String>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        usersTable.getColumns().add(nameColumn);

        TableColumn<User, UserAccess> roleColumn = new TableColumn<User, UserAccess>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<User, UserAccess>("userAccess"));
        usersTable.getColumns().add(roleColumn);

        TableColumn changeAccessBTNColumn = new TableColumn("Change User Access");
        changeAccessBTNColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory
                = //
                new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell call(final TableColumn<User, String> param) {
                final TableCell<User, String> cell = new TableCell<User, String>() {

                    final Button btn = new Button("Change Access");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {

                                setUpPopup(getTableView().getItems().get(getIndex()), usersTable);
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        changeAccessBTNColumn.setCellFactory(cellFactory);

        TableColumn removeUserBTNColumn = new TableColumn("Remove User");
        removeUserBTNColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactory2
                = //
                new Callback<TableColumn<User, String>, TableCell<User, String>>() {
            @Override
            public TableCell call(final TableColumn<User, String> param) {
                final TableCell<User, String> cell = new TableCell<User, String>() {

                    final Button removeBTN = new Button("Remove");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            removeBTN.setOnAction(event -> {

                                // setUpPopup(getTableView().getItems().get(getIndex()), usersTable);
                                userManagementModel.removeUser(getTableView().getItems().get(getIndex()));
                                refresh();
                            });
                            setGraphic(removeBTN);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        removeUserBTNColumn.setCellFactory(cellFactory2);

        populateTable(usersTable);

        usersTable.getColumns().addAll(changeAccessBTNColumn, removeUserBTNColumn);

    }

    public void populateTable(TableView<User> table){

        List<User> users = userManagementModel.getUsers();

        table.getItems().clear();

        if (users.size() == 0){
            table.setPlaceholder(new Label("No users"));
        }

        for (User user: users){
            table.getItems().add(user);
        }
    }


    public void setUpPopup(User user, TableView<User> table){
        popupScene.getStylesheets().add("Style.css");

        Stage popupStage = new Stage();

        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(this.stage);
        popupStage.show();

        VBox popupMainBox = new VBox(60);
        popupBorderPane.setMargin(popupMainBox, new Insets(50, 50, 50, 50));
        popupBorderPane.setCenter(popupMainBox);
        popupMainBox.getChildren().add(new Label("Select Access Type for " + user.getUsername()));
        HBox selectionBox = new HBox(80);
        popupMainBox.getChildren().add(selectionBox);

        ArrayList<UserAccess> userAccessTypes = new ArrayList<UserAccess>(Arrays.asList(UserAccess.values()));

        selectNewAccess = new ComboBox<UserAccess>();
        selectNewAccess.getItems().addAll(userAccessTypes);
        selectNewAccess.setValue(user.getUserAccess());

        saveNewAccessBTN = new Button("Save changes");
        selectionBox.getChildren().addAll(selectNewAccess, saveNewAccessBTN);

        saveNewAccessBTN.setOnAction((e) -> {
            UserAccess selectedNewAccess = (UserAccess) selectNewAccess.getValue();
            popupStage.hide();

            //logsout if the current user's access is changed - checkk (mainmodel user doesnt seem to be updated after login?)
            if (mainModel.getUser().equals(user) && !selectedNewAccess.equals(user.getUserAccess())){
                mainModel.logout();
            }

            user.setUserAccess(selectedNewAccess);

            mainModel.getJsonParser().updateUsers(userManagementModel.getUsers());

            table.refresh();

            
        });
    }


	@Override
	public void setUpMenuBTN(MenuButton menuBTN) {

        mainBox.getChildren().add(0, menuBTN);	

	}

	@Override
	public void setUpCancelBTN(Button cancelBTN) {

        cancelBTN.setMaxHeight(10);
        mainBox.getChildren().add(cancelBTN);		
	}


	@Override
	public void refresh() {  //clear and repopulate table with updated users
        populateTable(this.usersTable);
	}
    
}
