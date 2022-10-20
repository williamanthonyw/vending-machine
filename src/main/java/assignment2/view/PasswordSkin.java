package assignment2.view;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TextFieldSkin;


//https://stackoverflow.com/questions/35688835/bullet-to-asterisk-in-a-passwordfield
public class PasswordSkin extends TextFieldSkin {
    public PasswordSkin(TextField textField){
        super(textField);
    }
    @Override
    protected String maskText(String txt) {
        if (getSkinnable() instanceof PasswordField) {
            int n = txt.length();
            StringBuilder passwordBuilder = new StringBuilder(n);
            for (int i = 0; i < n; i++)
                passwordBuilder.append("*");

            return passwordBuilder.toString();
        } else {
            return txt;
        }
    }
}
