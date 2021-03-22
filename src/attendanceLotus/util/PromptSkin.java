package attendanceLotus.util;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionModel;

public class PromptSkin<T> extends ComboBoxListViewSkin<T> {

    private static final PseudoClass PROMPT = PseudoClass.getPseudoClass("prompt");
    private static final PseudoClass FILLED = PseudoClass.getPseudoClass("filled");

    public PromptSkin(final ComboBox<T> comboBox) {
        super(comboBox);

        ChangeListener<Number> selectionIndexChangeListener = (observable, oldIndex, newIndex) -> {
            getNode().pseudoClassStateChanged(PROMPT, newIndex.intValue() < 0);
            getNode().pseudoClassStateChanged(FILLED, newIndex.intValue() >= 0);
        };

        ChangeListener<SelectionModel> modelChangeListener = (observable, oldSelectionModel, newSelectionModel) -> {
            if (oldSelectionModel != null)
                oldSelectionModel.selectedIndexProperty().removeListener(selectionIndexChangeListener);

            if (newSelectionModel != null) {
                newSelectionModel.selectedIndexProperty().addListener(selectionIndexChangeListener);
                selectionIndexChangeListener.changed(null, null, newSelectionModel.getSelectedIndex());
            }
            else
                selectionIndexChangeListener.changed(null, null, -1);
        };

        comboBox.selectionModelProperty().addListener(modelChangeListener);
        modelChangeListener.changed(null, null, comboBox.getSelectionModel());
    }

}