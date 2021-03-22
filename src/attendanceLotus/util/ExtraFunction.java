package attendanceLotus.util;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ExtraFunction {

    // clears field such as text box and combobox
    public static void clearFields(TextField... textFields) {
        for(TextField textField : textFields)
            textField.setText("");
    }

    public static void clearFields(ComboBox... comboBoxes) {
        for(ComboBox comboBox : comboBoxes)
            comboBox.getSelectionModel().clearSelection();
    }

    // disables button when field is empty
    public static void disableButton(TextField textField, Button button) {
    BooleanBinding bb = new BooleanBinding() {
        { super.bind(textField.textProperty()); }

        @Override
        protected boolean computeValue() {
            return (textField.getText().isEmpty());
        }
    };

    button.disableProperty().bind(bb);
    }

    public static void disableButton(ComboBox comboBox, Button button) {
        BooleanBinding bb = new BooleanBinding() {
            { super.bind(comboBox.valueProperty()); }

            @Override
            protected boolean computeValue() {
                return (comboBox.getSelectionModel().isEmpty());
            }
        };

            button.disableProperty().bind(bb);
    }

    // disables button when selection is empty
    public static void disableButton(TableView tableView, Button button) {
        BooleanBinding bb = new BooleanBinding() {
            { super.bind(tableView.getSelectionModel().selectedItemProperty()); }

            @Override
            protected boolean computeValue() {
                return (tableView.getSelectionModel().isEmpty());
            }
        };

        button.disableProperty().bind(bb);
    }

    // hides button when table view selection is empty
    public static void hideButton(TableView tableView, Button button_add) {
        button_add.visibleProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));
        button_add.managedProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));
    }

    // shows hidden button when there is table view selection
    public static void showButton(TableView tableView, Button... buttons) {
        for(Button button : buttons) {
            button.visibleProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()).not());
            button.managedProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()).not());
        }
    }

}
