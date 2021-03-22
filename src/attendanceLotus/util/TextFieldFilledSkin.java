package attendanceLotus.util;

import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.css.PseudoClass;
import javafx.scene.control.TextField;

public class TextFieldFilledSkin extends TextFieldSkin {

    private static final PseudoClass FILLED = PseudoClass.getPseudoClass("filled");

    public TextFieldFilledSkin(final TextField textField) {
        super(textField);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            textField.pseudoClassStateChanged(FILLED, !newValue.isEmpty());
        });
    }
}