package attendanceLotus.util;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.css.PseudoClass;
import javafx.scene.control.DatePicker;

public class DatePickerShownSkin extends DatePickerSkin {

    private static final PseudoClass SHOWN = PseudoClass.getPseudoClass("shown");

    public DatePickerShownSkin(final DatePicker datePicker) {
        super(datePicker);

        datePicker.showingProperty().addListener((observable, oldValue, newValue) -> {
            datePicker.pseudoClassStateChanged(SHOWN, newValue);
        });

    }
}