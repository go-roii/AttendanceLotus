package attendanceLotus.controller.admin;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import attendanceLotus.model.admin.Teacher;
import attendanceLotus.util.CRUDOperation;
import attendanceLotus.util.ExtraFunction;

import java.net.URL;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class teachersController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private TableView<Teacher> tableView_teachers;

    @FXML
    private TableColumn<Teacher, String> tableColumn_fName;

    @FXML
    private TableColumn<Teacher, String> tableColumn_mName;

    @FXML
    private TableColumn<Teacher, String> tableColumn_lName;

    @FXML
    private TableColumn<Teacher, String> tableColumn_username;

    @FXML
    private TableColumn<Teacher, Boolean> tableColumn_action;

    @FXML
    private TextField textField_search;

    @FXML
    private Label label_addStudent;

    @FXML
    private TextField textField_fName;

    @FXML
    private TextField textField_mName;

    @FXML
    private TextField textField_lName;

    @FXML
    private TextField textField_username;

    @FXML
    private Button button_add;

    @FXML
    private Button button_update;

    @FXML
    private Button button_cancel;

    ObservableList<Teacher> teacherList = FXCollections.observableArrayList();
    FilteredList<Teacher> filteredData = new FilteredList(teacherList);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textField_search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(x -> x.getFName()
                        .concat(" " + x.getMName())
                        .concat(" " + x.getLName()).toLowerCase()
                        .contains(newValue.toLowerCase()))
        );

        // TABLE

        showTeachers();
        tableColumn_lName.setSortType(TableColumn.SortType.ASCENDING);
        tableView_teachers.getSortOrder().add(tableColumn_lName);

        // tableview keyboard functionality
        tableView_teachers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                label_addStudent.setText("Edit Teacher");

                Teacher selected = tableView_teachers.getSelectionModel().getSelectedItem();

                textField_fName.setText(selected.getFName());
                textField_mName.setText(selected.getMName());
                textField_lName.setText(selected.getLName());
                textField_username.setText(selected.getUsername());
            } else {
                label_addStudent.setText("Add Teacher");
                escapeTableView();
            }
        });

        // TEXT FIELD BUTTON BINDING

        // prompt text for generated username
        StringBinding sb = new StringBinding() {
            {
                super.bind(textField_fName.textProperty(),
                        textField_mName.textProperty(),
                        textField_lName.textProperty());
            }

            @Override
            public String computeValue() {
                String fName = textField_fName.getText().toLowerCase(),
                        mName = textField_mName.getText().toLowerCase(),
                        lName = textField_lName.getText().toLowerCase();

                if(fName.length() > 0 && mName.length() > 0 && lName.length() > 0) {
                    if(lName.contains(" "))
                        return "" + fName.charAt(0) + mName.charAt(0) + lName.substring(0, lName.indexOf(" "));
                    else
                        return "" + fName.charAt(0) + mName.charAt(0) + lName;
                }

                return "";
            }
        };

        textField_username.promptTextProperty().bind(sb);

        // button_add
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(textField_fName.textProperty(),
                        textField_mName.textProperty(),
                        textField_lName.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return textField_fName.getText().isEmpty() ||
                        textField_mName.getText().isEmpty() ||
                        textField_lName.getText().isEmpty();
            }
        };
        // button_cancel
        BooleanBinding bb2 = new BooleanBinding() {
            {
                super.bind(textField_fName.textProperty(),
                        textField_mName.textProperty(),
                        textField_lName.textProperty(),
                        textField_username.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return textField_fName.getText().isEmpty() &&
                        textField_mName.getText().isEmpty() &&
                        textField_lName.getText().isEmpty() &&
                        textField_username.getText().isEmpty();
            }
        };

        button_add.disableProperty().bind(bb);
        button_cancel.disableProperty().bind(bb2);

        ExtraFunction.hideButton(tableView_teachers, button_add);
        ExtraFunction.showButton(tableView_teachers, button_update);
    }



    // HELPER METHOD

    private String generateUsername() {
        String fName, mName, lName;
        String username;

        if(textField_username.getText().isEmpty()) {
            fName = "" + textField_fName.getText().toLowerCase().charAt(0);
            mName = "" + textField_mName.getText().toLowerCase().charAt(0);
            lName = "" + textField_lName.getText().toLowerCase();

            username = fName + mName + lName;

            if(lName.contains(" "))
                username = fName + mName + lName.toLowerCase().substring(0, lName.indexOf(" "));
        }
        else
            username = textField_username.getText();

        try {
            ResultSet rs = CRUDOperation.statement("SELECT COUNT(*) AS rowCount FROM users WHERE username LIKE '" + username + "%'");
            rs.next();
            int count = rs.getInt("rowCount");

            // add '1' at the end if no same username exists
            if (count == 0)
                username = username + "1";
                // increment number of last same username
            else
                username = username + (count + 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return username;
    }

    private void escapeTableView() {
        root.requestFocus();
        ExtraFunction.clearFields(textField_fName, textField_mName, textField_lName, textField_username);
    }



    // EVENT HANDLER

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == button_add)
            addRecord();
        else if (event.getSource() == button_update)
            updateRecord();
        else if (event.getSource() == button_cancel) {
            tableView_teachers.getSelectionModel().clearSelection();
            escapeTableView();
            return;
        }

        escapeTableView();
        teacherList.clear();
        showTeachers();
        tableView_teachers.sort();
    }

    @FXML
    private void tvTeacherKeyPressed(KeyEvent event) {
        KeyCode key = event.getCode();
        if (key == KeyCode.ESCAPE)
            tableView_teachers.getSelectionModel().clearSelection();
    }



    // TABLE

    private SortedList<Teacher> getTeachersList() {
        SortedList<Teacher> sortableData = new SortedList<>(filteredData);
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT * FROM teachers AS t " +
                    "INNER JOIN users AS u " +
                    "ON t.userID = u.userID");

            Teacher teacher;
            while (rs.next()) {
                teacher = new Teacher(rs.getInt("teacherID"), rs.getInt("userID"), rs.getString("fName"),
                        rs.getString("mName"), rs.getString("lName"), rs.getString("username"));
                teacherList.add(teacher);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sortableData;
    }

    private void showTeachers() {
        SortedList<Teacher> list = getTeachersList();

        tableColumn_fName.setCellValueFactory(new PropertyValueFactory<>("fName"));
        tableColumn_mName.setCellValueFactory(new PropertyValueFactory<>("mName"));
        tableColumn_lName.setCellValueFactory(new PropertyValueFactory<>("lName"));
        tableColumn_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumn_action.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tableColumn_action.setCellFactory(p -> new ButtonDeleteClass());

        tableView_teachers.setItems(list);
        list.comparatorProperty().bind(tableView_teachers.comparatorProperty());
    }

    private class ButtonDeleteClass extends TableCell<Teacher, Boolean> {
        final Button cellButton = new Button("Delete");

        ButtonDeleteClass() {
            cellButton.getStyleClass().add("button-destructive");
            cellButton.setOnAction(t -> {
                Teacher teacher = getTableView().getItems().get(getIndex());
                String teacherName = teacher.getFName() + " " + teacher.getMName() + " " + teacher.getLName();

                ButtonType buttonType_cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                ButtonType buttonType_delete = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);

                Alert alert_delete = new Alert(Alert.AlertType.NONE, "", buttonType_cancel, buttonType_delete);
                alert_delete.setTitle("");
                alert_delete.setHeaderText("Delete teacher?");
                alert_delete.getDialogPane().setContent(new Label(
                        "Are you sure you want to delete \"" + teacherName + "\"? \n" +
                                "You cannot undo this action."));
                alert_delete.initOwner(cellButton.getScene().getWindow());
                alert_delete.getDialogPane().getStyleClass().add("dialog");
                alert_delete.getDialogPane().getScene().getStylesheets().add("attendanceLotus/style.css");
                alert_delete.getDialogPane().lookupButton(buttonType_cancel).getStyleClass().add("button-cancel");
                alert_delete.getDialogPane().lookupButton(buttonType_delete).getStyleClass().add("button-destructive");

                Optional<ButtonType> result = alert_delete.showAndWait();
                if (result.orElse(buttonType_cancel) == buttonType_delete) {
                    deleteRecord(teacher.getUserID(), teacher.getTeacherID());
                    teacherList.clear();
                    showTeachers();
                    tableView_teachers.sort();
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty)
                setGraphic(cellButton);
            else
                setGraphic(null);
        }
    }



    // CRUD OPERATION

    private void addRecord() {
        String fName = textField_fName.getText(), mName = textField_mName.getText(), lName = textField_lName.getText();
        String teacherUsername = generateUsername();

        int userID = CRUDOperation.executeQuery("INSERT INTO users (username, password, userType) " +
                "VALUES ('" + teacherUsername + "', '1234' , 2)");
        CRUDOperation.executeQuery("INSERT INTO teachers (userID, fName, mName, lName) " +
                "VALUES (" + userID + "," + "'" + fName + "','" + mName + "','" + lName + "')");
    }

    private void updateRecord() {
        Teacher teacher = tableView_teachers.getSelectionModel().getSelectedItem();
        int userID = teacher.getUserID();
        int teacherID = teacher.getTeacherID();
        String fName = textField_fName.getText(), mName = textField_mName.getText(), lName = textField_lName.getText(),
        username = textField_username.getText();

        CRUDOperation.executeQuery("UPDATE teachers SET fName = '" + fName + "', mName = '" + mName + "'," +
                "lName = '" + lName + "' " +
                "WHERE teacherID = " + teacherID);
        CRUDOperation.executeQuery("UPDATE users SET username = '" + username + "' " +
                "WHERE userID = " + userID);
    }

    private void deleteRecord(int userID, int teacherID) {
        CRUDOperation.executeQuery("DELETE FROM users " +
                "WHERE userID = " + userID);
        CRUDOperation.executeQuery("DELETE FROM teachers " +
                "WHERE userID = " + userID);
        CRUDOperation.executeQuery("UPDATE courses_classes_subjects SET teacherID = NULL " +
                "WHERE teacherID = " + teacherID);
    }

}
