package attendanceLotus.controller.admin;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import attendanceLotus.model.admin.Course;
import attendanceLotus.model.admin.Student;
import attendanceLotus.util.CRUDOperation;
import attendanceLotus.util.ExtraFunction;

import java.net.URL;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class studentsController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private TableView<Student> tableView_students;

    @FXML
    private TableColumn<Student, String> tableColumn_fName;

    @FXML
    private TableColumn<Student, String> tableColumn_mName;

    @FXML
    private TableColumn<Student, String> tableColumn_lName;

    @FXML
    private TableColumn<Student, String> tableColumn_courseName;

    @FXML
    private TableColumn<Student, String> tableColumn_studentID;

    @FXML
    private TableColumn<Student, Boolean> tableColumn_action;

    @FXML
    private TextField textField_search;

    @FXML
    private Label label_addStudent;

    @FXML
    private Label label_fName;

    @FXML
    private Label label_mName;

    @FXML
    private Label label_lName;

    @FXML
    private Label label_course;

    @FXML
    private Label label_studentID;

    @FXML
    private TextField textField_fName;

    @FXML
    private TextField textField_mName;

    @FXML
    private TextField textField_lName;

    @FXML
    private ComboBox<Course> comboBox_courses;

    @FXML
    private TextField textField_studentID;

    @FXML
    private Button button_add;

    @FXML
    private Button button_update;

    @FXML
    private Button button_cancel;

    ObservableList<Student> studentList = FXCollections.observableArrayList();
    FilteredList<Student> filteredData = new FilteredList(studentList);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textField_search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(x -> x.getFName()
                        .concat(" " + x.getMName())
                        .concat(" " + x.getLName())
                        .concat(" " + x.getCourseName())
                        .concat(" " + x.getStudentID()).toLowerCase()
                        .contains(newValue.toLowerCase()))
        );

        comboBox_courses.setItems(getCourseList());

        showStudents();
        tableColumn_studentID.setSortType(TableColumn.SortType.DESCENDING);
        tableView_students.getSortOrder().add(tableColumn_studentID);

        // tableview keyboard functionality
        tableView_students.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                label_addStudent.setText("Edit Student");

                Student selected = tableView_students.getSelectionModel().getSelectedItem();

                textField_fName.setText(selected.getFName());
                textField_mName.setText(selected.getMName());
                textField_lName.setText(selected.getLName());
                for(Course course: comboBox_courses.getItems())
                    if(selected.getCourseID() == course.getCourseID())
                        comboBox_courses.getSelectionModel().select(course);
                textField_studentID.setText(selected.getStudentID());
            }
            else {
                label_addStudent.setText("Add Student");
                escapeTableView(true);
            }
        });

        setStudentIDTextField();

        // TEXT FIELD BINDING

        // button_add
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(textField_fName.textProperty(),
                        textField_mName.textProperty(),
                        textField_lName.textProperty(),
                        comboBox_courses.valueProperty(),
                        textField_studentID.textProperty());
            }

            @Override
            protected boolean computeValue() {
                boolean fieldEmpty = textField_fName.getText().isEmpty() ||
                        textField_mName.getText().isEmpty() ||
                        textField_lName.getText().isEmpty() ||
                        comboBox_courses.getSelectionModel().isEmpty() ||
                        textField_studentID.getText().isEmpty();

                if(tableView_students.getSelectionModel().isEmpty()) {
                    return fieldEmpty;
                }
                else {
                    Student student = tableView_students.getSelectionModel().getSelectedItem();

                    return fieldEmpty || textField_studentID.getText().equals(student.getStudentID());
                }
            }
        };
        // button_cancel
        BooleanBinding bb2 = new BooleanBinding() {
            {
                super.bind(textField_fName.textProperty(),
                        textField_mName.textProperty(),
                        textField_lName.textProperty(),
                        comboBox_courses.valueProperty(),
                        textField_studentID.textProperty());
            }

            @Override
            protected boolean computeValue() {
                return textField_fName.getText().isEmpty() &&
                        textField_mName.getText().isEmpty() &&
                        textField_lName.getText().isEmpty() &&
                        comboBox_courses.getSelectionModel().isEmpty() &&
                        textField_studentID.getText().isEmpty();
            }
        };

        button_add.disableProperty().bind(bb);
        button_cancel.disableProperty().bind(bb2);

        button_add.visibleProperty().bind(Bindings.isEmpty(tableView_students.getSelectionModel().getSelectedItems()));
        button_add.managedProperty().bind(Bindings.isEmpty(tableView_students.getSelectionModel().getSelectedItems()));
        button_update.visibleProperty().bind(Bindings.isEmpty(tableView_students.getSelectionModel().getSelectedItems()).not());
        button_update.managedProperty().bind(Bindings.isEmpty(tableView_students.getSelectionModel().getSelectedItems()).not());
    }



    // HELPER METHOD

    // increment student id to avoid replicate error
    private void setStudentIDTextField() {
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT studentID FROM students " +
                    "ORDER BY studentID DESC");

            if(rs.first())
                if(rs.getString("studentID") != null) {
                    // implementation 1
//                    String studentID = rs.getString("studentID");
//                    char lastChar = studentID.charAt(studentID.length() - 1);
//                    String newStudentID = studentID.substring(0, studentID.length() - 1) + (++lastChar);

                    // implementation 2: failed
//                    String studentID = rs.getString("studentID");
//                    String newStudentID = "";
//                    for(int i = studentID.length() - 1; i >= 0; i--) {
//                        if(studentID.charAt(i) >= 0 && studentID.charAt(i) <= 9)
//                            newStudentID = newStudentID.concat(studentID.charAt(i) + "");
//                        else if(studentID.charAt(i) >= 'a' && studentID.charAt(i) <= 'z')
//                            System.out.println(studentID.charAt(i));
//                    }

                    // implementation 3
                    String studentID = rs.getString("studentID");
                    String newStudentID = (Long.parseLong(studentID) + 1) + "";

                    textField_studentID.setText(newStudentID);
                    textField_studentID.pseudoClassStateChanged(PseudoClass.getPseudoClass("filled"), true);
                }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void escapeTableView(boolean setStudID) {
        root.requestFocus();
        ExtraFunction.clearFields(textField_fName, textField_mName, textField_lName, textField_studentID);
        ExtraFunction.clearFields(comboBox_courses);

        if(setStudID)
            setStudentIDTextField();
    }



    // EVENT HANDLER

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == button_add)
            addRecord();
        else if(event.getSource() == button_update)
            updateRecord();
        else if(event.getSource() == button_cancel) {
            tableView_students.getSelectionModel().clearSelection();
            escapeTableView(false);
            return;
        }

        escapeTableView(true);
        studentList.clear();
        showStudents();
        tableView_students.sort();
    }

    @FXML
    private void tvStudentKeyPressed(KeyEvent event) {
        KeyCode key = event.getCode();
        if (key == KeyCode.ESCAPE)
            tableView_students.getSelectionModel().clearSelection();
    }



    // TABLE

    public SortedList<Student> getStudentList() {
        SortedList<Student> sortableData = new SortedList<>(filteredData);
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT * FROM students AS s " +
                    "INNER JOIN courses AS c ON s.courseID = c.courseID " +
                    "INNER JOIN users AS u ON s.userID = u.userID");

            Student student;
            while(rs.next()) {
                student = new Student(rs.getInt("studID"), rs.getInt("userID"), rs.getString("fName"),
                        rs.getString("mName"), rs.getString("lName"), rs.getInt("courseID"),
                        rs.getString("studentID"), rs.getString("courseName"));
                studentList.add(student);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sortableData;
    }

    public void showStudents() {
        SortedList<Student> list = getStudentList();

        tableColumn_fName.setCellValueFactory(new PropertyValueFactory<>("fName"));
        tableColumn_mName.setCellValueFactory(new PropertyValueFactory<>("mName"));
        tableColumn_lName.setCellValueFactory(new PropertyValueFactory<>("lName"));
        tableColumn_courseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tableColumn_studentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        tableColumn_action.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tableColumn_action.setCellFactory(p -> new ButtonDeleteClass());

        tableView_students.setItems(list);
        list.comparatorProperty().bind(tableView_students.comparatorProperty());
    }

    // combobox_course items
    public ObservableList<Course> getCourseList() {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT * FROM courses");

            Course course;
            while(rs.next()) {
                course = new Course(rs.getInt("courseID"), rs.getString("courseName"),
                        rs.getInt("isHidden"));
                    courseList.add(course);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return courseList;
    }

    private class ButtonDeleteClass extends TableCell<Student, Boolean> {
        final Button cellButton = new Button("Delete");

        ButtonDeleteClass() {
            cellButton.getStyleClass().add("button-destructive");
            cellButton.setOnAction(t -> {
                Student student = getTableView().getItems().get(getIndex());
                String studName = student.getFName() + " " + student.getMName() + " " + student.getLName();

                ButtonType buttonType_cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                ButtonType buttonType_delete = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);

                Alert alert_delete = new Alert(Alert.AlertType.NONE, "", buttonType_cancel, buttonType_delete);
                alert_delete.setTitle("");
                alert_delete.setHeaderText("Delete student?");
                alert_delete.getDialogPane().setContent(new Label(
                        "You will permanently lose \"" + studName + "'s\" attendance records. \n" +
                                "You cannot undo this action."));
                alert_delete.initOwner(cellButton.getScene().getWindow());
                alert_delete.getDialogPane().getStyleClass().add("dialog");
                alert_delete.getDialogPane().getScene().getStylesheets().add("attendanceLotus/style.css");
                alert_delete.getDialogPane().lookupButton(buttonType_cancel).getStyleClass().add("button-cancel");
                alert_delete.getDialogPane().lookupButton(buttonType_delete).getStyleClass().add("button-destructive");

                Optional<ButtonType> result = alert_delete.showAndWait();
                if(result.orElse(buttonType_cancel) == buttonType_delete) {
                    deleteRecord(student.getUserID());
                    studentList.clear();
                    showStudents();
                    tableView_students.sort();
                    setStudentIDTextField();
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty)
                setGraphic(cellButton);
            else
                setGraphic(null);
        }
    }



    // CRUD OPERATION

    private void addRecord() {
        String fName = textField_fName.getText(), mName = textField_mName.getText(), lName = textField_lName.getText(),
                studentID = textField_studentID.getText();
        int courseID = comboBox_courses.getSelectionModel().getSelectedItem().getCourseID();

        int userID = CRUDOperation.executeQuery("INSERT INTO users (username, password, userType) " +
                "VALUES ('" + studentID + "', '1234' , 3)");

        CRUDOperation.executeQuery("INSERT INTO students (userID, fName, mName, lName, courseID, studentID) " +
                "VALUES (" + userID + "," + "'" + fName + "','" + mName + "','" + lName + "'," +
                courseID + ",'" + studentID + "')");
    }

    private void updateRecord() {
        // update username on users too
        Student student = tableView_students.getSelectionModel().getSelectedItem();
        int studID = student.getStudID();
        int userID = student.getUserID();
        String fName = textField_fName.getText(), mName = textField_mName.getText(), lName = textField_lName.getText(),
                studentID = textField_studentID.getText();
        int courseID = comboBox_courses.getSelectionModel().getSelectedItem().getCourseID();

        CRUDOperation.executeQuery("UPDATE students SET fName = '" + fName + "', mName = '" + mName + "'," +
                "lName = '" + lName + "', courseID = " + courseID + ", studentID = '" + studentID + "' " +
                "WHERE studID = " + studID);
        CRUDOperation.executeQuery("UPDATE users SET username = '" + studentID + "' " +
                "WHERE userID = " + userID);
    }

    private void deleteRecord(int userID) {
        CRUDOperation.executeQuery("DELETE FROM users " +
                "WHERE userID = " + userID);
        CRUDOperation.executeQuery("DELETE FROM students " +
                "WHERE userID = " + userID);
    }

}
