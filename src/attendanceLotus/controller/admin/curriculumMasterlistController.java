package attendanceLotus.controller.admin;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import attendanceLotus.model.admin.Course;
import attendanceLotus.model.admin.Class;
import attendanceLotus.model.admin.Subject;
import attendanceLotus.util.CRUDOperation;
import attendanceLotus.util.ExtraFunction;

import java.net.URL;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class curriculumMasterlistController implements Initializable {

    @FXML
    private SplitPane root;

    @FXML
    private TableView<Course> tableView_courses;

    @FXML
    private TableColumn<Course, String> tableColumn_course;

    @FXML
    private TableColumn<Course, Boolean> tableColumn_actionCourse;

    @FXML
    private TableView<Class> tableView_classes;

    @FXML
    private TableColumn<Class, String> tableColumn_class;

    @FXML
    private TableColumn<Class, Boolean> tableColumn_actionClass;

    @FXML
    private TableView<Subject> tableView_subjects;

    @FXML
    private TableColumn<Subject, String> tableColumn_subject;

    @FXML
    private TableColumn<Subject, Boolean> tableColumn_actionSubject;

    @FXML
    private TextField textField_courseName;

    @FXML
    private Spinner<Integer> spinner_year;

    @FXML
    private Spinner<Character> spinner_block;

    @FXML
    private TextField textField_subjectName;

    @FXML
    private Button button_addCourse;

    @FXML
    private Button button_addClass;

    @FXML
    private Button button_addSubject;

    @FXML
    private Button button_updateCourse;

    @FXML
    private Button button_updateClass;

    @FXML
    private Button button_updateSubject;

    static boolean update = false;

    private ObservableList<Character> blocks = FXCollections.observableArrayList();

    private SpinnerValueFactory valueFactory =
            new SpinnerValueFactory.ListSpinnerValueFactory(blocks);

    private enum Table {
        COURSE,
        CLASS,
        SUBJECT
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ExtraFunction.disableButton(textField_courseName, button_addCourse);
        ExtraFunction.disableButton(textField_subjectName, button_addSubject);

        ExtraFunction.hideButton(tableView_courses, button_addCourse);
        ExtraFunction.showButton(tableView_courses, button_updateCourse);
        ExtraFunction.hideButton(tableView_classes, button_addClass);
        ExtraFunction.showButton(tableView_classes, button_updateClass);
        ExtraFunction.hideButton(tableView_subjects, button_addSubject);
        ExtraFunction.showButton(tableView_subjects, button_updateSubject);

        for(char c = 'A'; c <= 'Z'; ++c)
            blocks.add(c);
        spinner_block.setValueFactory(valueFactory);

        // table view selection listener changes
        tableView_courses.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                String courseName = tableView_courses.getSelectionModel().getSelectedItem().getCourseName();
                textField_courseName.setText(courseName);
            }
            else
                escapeTableView(textField_courseName);
        });
        tableView_classes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null)
                setSpinnerValue();
            else {
                spinner_year.getValueFactory().setValue(1);
                spinner_block.getValueFactory().setValue('A');
                root.requestFocus();
            }
        });
        tableView_subjects.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                String subjectName = tableView_subjects.getSelectionModel().getSelectedItem().getSubjectName();
                textField_subjectName.setText(subjectName);
            }
            else
                escapeTableView(textField_subjectName);
        });


        showCourses();
        showCurrClasses();
        showSubjects();
    }


    // HELPER METHOD

    private void setSpinnerValue() {
        Class currClass = tableView_classes.getSelectionModel().getSelectedItem();

        spinner_year.getValueFactory().setValue(currClass.getYear());
        spinner_block.getValueFactory().setValue(currClass.getBlock());
    }

    private void escapeTableView(TextField textField) {
        root.requestFocus();
        textField.setText("");
    }

    private void refreshTable(Table table) {
        switch(table) {
            case COURSE:
                tableView_courses.getItems().clear();
                tableView_courses.getItems().addAll(getCourseList());
                break;
            case CLASS:
                tableView_classes.getItems().clear();
                tableView_classes.getItems().addAll(getCurrClassList());
                break;
            case SUBJECT:
                tableView_subjects.getItems().clear();
                tableView_subjects.getItems().addAll(getSubjectList());
                break;
            default:
                System.out.println("No such table");
        }

            update = true;
    }



    // ON ACTION

    @FXML
    private void btnCourseOnAction(ActionEvent event) {
        if(event.getSource() == button_addCourse)
            addCourseRecord();
        else if(event.getSource() == button_updateCourse)
            updateCourseRecord();

        textField_courseName.setText("");
    }

    @FXML
    private void btnClassOnAction(ActionEvent event) {
        if(event.getSource() == button_addClass)
            addCurrClassRecord();
        else if(event.getSource() == button_updateClass)
            updateClassRecord();
    }

    @FXML
    private void btnSubjectOnAction(ActionEvent event) {
        if(event.getSource() == button_addSubject)
            addSubjectRecord();
        else if(event.getSource() == button_updateSubject)
            updateSubjectRecord();

        textField_subjectName.setText("");
    }



    // MOUSE CLICKED

    @FXML
    private void tvCourseMouseClicked() {
        try {
            String courseName = tableView_courses.getSelectionModel().getSelectedItem().getCourseName();
            textField_courseName.setText(courseName);
        } catch(NullPointerException ex) {
            root.requestFocus();
        }
    }

    @FXML
    private void tvClassMouseClicked() {
        try {
            setSpinnerValue();
        } catch(NullPointerException ex) {
            root.requestFocus();
        }
    }

    @FXML
    private void tvSubjectMouseClicked() {
        try {
            String subjectName = tableView_subjects.getSelectionModel().getSelectedItem().getSubjectName();
            textField_subjectName.setText(subjectName);
        } catch(NullPointerException ex) {
            root.requestFocus();
        }
    }



    // KEY PRESSED

    @FXML
    private void tvCourseKeyPressed(KeyEvent event) {
        KeyCode key = event.getCode();
        if(key == KeyCode.ESCAPE)
            tableView_courses.getSelectionModel().clearSelection();
    }

    @FXML
    private void tvClassKeyPressed(KeyEvent event) {
        KeyCode key = event.getCode();
        if(key == KeyCode.ESCAPE)
            tableView_classes.getSelectionModel().clearSelection();
    }

    @FXML
    private void tvSubjectKeyPressed(KeyEvent event) {
        KeyCode key = event.getCode();
        if(key == KeyCode.ESCAPE)
            tableView_subjects.getSelectionModel().clearSelection();
    }



    // OBSERVABLE LIST

    private ObservableList<Course> getCourseList() {
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

    private ObservableList<Class> getCurrClassList() {
        ObservableList<Class> currClassList = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT cl.*, CONCAT(cl.year, '-', cl.block) AS yearBlock FROM classes AS cl");

            Class currClass;
            while(rs.next()) {
                currClass = new Class(rs.getInt("classID"), rs.getInt("year"),
                        rs.getString("block").charAt(0), rs.getString("yearBlock"));
                currClassList.add(currClass);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currClassList;
    }

    private ObservableList<Subject> getSubjectList() {
        ObservableList<Subject> subjectList = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT * FROM subjects");

            Subject subject;
            while(rs.next()) {
                subject = new Subject(rs.getInt("subjectID"), rs.getString("subjectName"));
                subjectList.add(subject);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return subjectList;
    }

    private void showCourses() {
        ObservableList<Course> courseList = getCourseList();

        tableColumn_course.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tableColumn_actionCourse.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tableColumn_actionCourse.setCellFactory(p -> new ButtonDeleteCourse());
        tableView_courses.setItems(courseList);
    }

    private void showCurrClasses() {
        ObservableList<Class> currClassList = getCurrClassList();

        tableColumn_class.setCellValueFactory(new PropertyValueFactory<>("yearBlock"));
        tableColumn_actionClass.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tableColumn_actionClass.setCellFactory(p -> new ButtonDeleteClass());
        tableView_classes.setItems(currClassList);
    }

    private void showSubjects() {
        ObservableList<Subject> subjectList = getSubjectList();

        tableColumn_subject.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        tableColumn_actionSubject.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tableColumn_actionSubject.setCellFactory(p -> new ButtonDeleteSubject());
        tableView_subjects.setItems(subjectList);
    }



    // TABLE CELL DELETE BUTTON

    private class ButtonDeleteCourse extends TableCell<Course, Boolean> {
        final Button cellButton = new Button("Delete");

        ButtonDeleteCourse() {
            cellButton.getStyleClass().add("button-destructive");
            cellButton.setOnAction(t -> {
                Course course = getTableView().getItems().get(getIndex());
                String courseName = course.getCourseName();

                Alert alert_delete = deleteDialog(
                        "Delete course?",
                        "You will permanently lose all \"" + courseName + "\" students' attendance records. \n" +
                                "You cannot undo this action.");
                alert_delete.initOwner(cellButton.getScene().getWindow());

                Optional<ButtonType> result = alert_delete.showAndWait();
                if(result.orElse(alert_delete.getButtonTypes().get(0)) == alert_delete.getButtonTypes().get(1))
                    removeCourseRecord(getTableView().getItems().get(getIndex()).getCourseID());
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

    private class ButtonDeleteClass extends TableCell<Class, Boolean> {
        final Button cellButton = new Button("Delete");

        ButtonDeleteClass() {
            cellButton.getStyleClass().add("button-destructive");
            cellButton.setOnAction(t -> {
                Class currClass = getTableView().getItems().get(getIndex());
                String yearBlock = currClass.getYearBlock();

                Alert alert_delete = deleteDialog(
                        "Delete class?",
                        "You will permanently lose all \"" + yearBlock + "\" students' attendance records. \n" +
                                "You cannot undo this action.");
                alert_delete.initOwner(cellButton.getScene().getWindow());

                Optional<ButtonType> result = alert_delete.showAndWait();
                if(result.orElse(alert_delete.getButtonTypes().get(0)) == alert_delete.getButtonTypes().get(1))
                    removeCurrClassRecord(currClass.getClassID());
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

    private class ButtonDeleteSubject extends TableCell<Subject, Boolean> {
        final Button cellButton = new Button("Delete");

        ButtonDeleteSubject() {
            cellButton.getStyleClass().add("button-destructive");
            cellButton.setOnAction(t -> {
                Subject subject = getTableView().getItems().get(getIndex());
                String subjectName = subject.getSubjectName();

                Alert alert_delete = deleteDialog(
                        "Delete subject?",
                        "You will permanently lose all \"" + subjectName + "\" students' attendance records. \n" +
                                "You cannot undo this action.");
                alert_delete.initOwner(cellButton.getScene().getWindow());

                Optional<ButtonType> result = alert_delete.showAndWait();
                if(result.orElse(alert_delete.getButtonTypes().get(0)) == alert_delete.getButtonTypes().get(1))
                    removeSubjectRecord(subject.getSubjectID());
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

    private Alert deleteDialog(String header, String content) {
        ButtonType buttonType_cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonType_delete = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);

        Alert alert_delete = new Alert(Alert.AlertType.NONE, "", buttonType_cancel, buttonType_delete);
        alert_delete.setTitle("");
        alert_delete.setHeaderText(header);
        alert_delete.getDialogPane().setContent(new Label(content));
        alert_delete.getDialogPane().getStyleClass().add("dialog");
        alert_delete.getDialogPane().getStylesheets().add("attendanceLotus/style.css");
        alert_delete.getDialogPane().lookupButton(buttonType_cancel).getStyleClass().add("button-cancel");
        alert_delete.getDialogPane().lookupButton(buttonType_delete).getStyleClass().add("button-destructive");

        return alert_delete;
    }



    // CRUD OPERATION

    // add record
    private void addCourseRecord() {
        String courseName = textField_courseName.getText();

        CRUDOperation.executeQuery("INSERT INTO courses (courseName, isHidden) " +
                "VALUES ('" + courseName + "', 1)");
        refreshTable(Table.COURSE);
    }

    private void addCurrClassRecord() {
        int year = spinner_year.getValue();
        char block = spinner_block.getValue();

        CRUDOperation.executeQuery("INSERT INTO classes (year, block) " +
                "VALUES (" + year + ", '" + block + "')");
        refreshTable(Table.CLASS);
    }

    private void addSubjectRecord() {
        String subjectName = textField_subjectName.getText();
        CRUDOperation.executeQuery("INSERT INTO subjects (subjectName) " +
                "VALUES ('" + subjectName + "')");
        refreshTable(Table.SUBJECT);
    }

    // delete record
    private void removeCourseRecord(int courseID) {
        CRUDOperation.executeQuery("DELETE FROM courses " +
                "WHERE courseID = " + courseID);
        refreshTable(Table.COURSE);
    }

    private void removeCurrClassRecord(int classID) {
        CRUDOperation.executeQuery("DELETE FROM classes " +
                "WHERE classID = " + classID);
        refreshTable(Table.CLASS);
    }

    private void removeSubjectRecord(int subjectID) {
        CRUDOperation.executeQuery("DELETE FROM subjects " +
                "WHERE subjectID = " + subjectID);
        refreshTable(Table.SUBJECT);
    }

    // update record
    private void updateCourseRecord() {
        String courseName = textField_courseName.getText();
        int courseID = tableView_courses.getSelectionModel().getSelectedItem().getCourseID();

        CRUDOperation.executeQuery("UPDATE courses SET courseName = '" + courseName + "' " +
                "WHERE courseID = " + courseID);
        refreshTable(Table.COURSE);
    }

    private void updateClassRecord() {
        int year = spinner_year.getValue();
        char block = spinner_block.getValue();
        int classID = tableView_classes.getSelectionModel().getSelectedItem().getClassID();

        CRUDOperation.executeQuery("UPDATE classes SET year = " + year + ", block = '" + block + "' " +
                "WHERE classID = " + classID);
        refreshTable(Table.CLASS);
    }

    private void updateSubjectRecord() {
        String subjectName = textField_subjectName.getText();
        int subjectID = tableView_subjects.getSelectionModel().getSelectedItem().getSubjectID();

        CRUDOperation.executeQuery("UPDATE subjects SET subjectName = '" + subjectName + "' " +
                "WHERE subjectID = " + subjectID);
        refreshTable(Table.SUBJECT);
    }

}
