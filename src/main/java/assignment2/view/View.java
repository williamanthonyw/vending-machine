package assignment2.view;

import javafx.scene.Scene;
import javafx.scene.control.MenuButton;

public interface View {

    public Scene getScene();

    public void setUp();

    public void setUpMenuBTN(MenuButton menuBTN);

}
