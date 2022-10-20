package assignment2.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;

import java.util.List;

public interface View {

    public List<Scene> getScenes();

    public void setUp();

    public void setUpMenuBTN(MenuButton menuBTN);

    public void setUpCancelBTN(Button cancelBTN);

}
