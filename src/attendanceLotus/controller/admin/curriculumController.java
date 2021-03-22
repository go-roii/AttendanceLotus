package attendanceLotus.controller.admin;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import attendanceLotus.model.admin.Class;
import attendanceLotus.model.admin.Course;
import attendanceLotus.model.admin.Subject;
import attendanceLotus.util.CRUDOperation;
import attendanceLotus.util.ExtraFunction;

import java.net.URL;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class curriculumController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private TabPane tabPane_curriculum;

    @FXML
    private VBox vBox_class;

    @FXML
    private VBox vBox_subject;

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
    private Label placeholder_class;

    @FXML
    private Label placeholder_subject;

    @FXML
    private ComboBox<Course> comboBox_courses;

    @FXML
    private ComboBox<Class> comboBox_classes;

    @FXML
    private ComboBox<Subject> comboBox_subjects;

    @FXML
    private Button button_addCourse;

    @FXML
    private Button button_addClass;

    @FXML
    private Button button_addSubject;

    private enum Table {
        COURSE,
        CLASS,
        SUBJECT
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // disable button when combobox doesn't have any selection
        ExtraFunction.disableButton(comboBox_courses, button_addCourse);
        ExtraFunction.disableButton(comboBox_classes, button_addClass);
        ExtraFunction.disableButton(comboBox_subjects, button_addSubject);

        // show courses list
        showCourses();
        // show combobox_course items
        comboBox_courses.setItems(getOutsideCourseList());

        // vBox class and subject no selection bg color
        vBox_class.setStyle("-fx-background-color: #F8F8F8");
        vBox_subject.setStyle("-fx-background-color: #F8F8F8");

        // table view selection listener changes
        tableView_courses.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                vBox_class.setStyle("");
                comboBox_classes.setDisable(false);
            }
            else {
                vBox_class.setStyle("-fx-background-color: #F8F8F8");
                vBox_subject.setStyle("-fx-background-color: #F8F8F8");
                placeholder_class.setText("No course selected");
                placeholder_subject.setText("No class selected");
                comboBox_classes.setDisable(true);
            }
        });
        tableView_classes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                vBox_subject.setStyle("");
                comboBox_subjects.setDisable(false);
            }
            else {
                vBox_subject.setStyle("-fx-background-color: #F8F8F8");
                placeholder_subject.setText("No subject selected");
                comboBox_subjects.setDisable(true);
            }
        });

//        vBox_class.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
//            if (event.getCode() == KeyCode.ESCAPE)
//                System.out.println("F1 pressed");
//            event.consume();
//        });
    }



    // HELPER METHOD

    @FXML
    private void selectTabList() {
        tabPane_curriculum.setId("tab-pane-list");

        // update table views and comboboxes after any changes in masterlist
        if(curriculumMasterlistController.update) {
            tableView_courses.getItems().clear();
            tableView_courses.getItems().addAll(getCourseList());
            tableView_classes.getItems().clear();
            tableView_subjects.getItems().clear();
            comboBox_courses.getSelectionModel().clearSelection();
            comboBox_courses.setItems(getOutsideCourseList());

            curriculumMasterlistController.update = false;
        }
    }

    @FXML
    private void selectTabMasterlist() {
        tabPane_curriculum.setId("masterlist");
    }

    private void refreshTable(Table table) {
        switch(table) {
            case COURSE:
                tableView_courses.getItems().clear();
                tableView_courses.getItems().addAll(getCourseList());
                tableView_classes.getItems().clear();
                tableView_subjects.getItems().clear();
                comboBox_courses.getSelectionModel().clearSelection();
                comboBox_courses.setItems(getOutsideCourseList());
                break;
            case CLASS:
                int courseID = tableView_courses.getSelectionModel().getSelectedItem().getCourseID();

                tableView_classes.getItems().clear();
                tableView_classes.getItems().addAll(getCurrClassList(courseID));
                comboBox_classes.getSelectionModel().clearSelection();
                comboBox_classes.setItems(getOutsideCurrClassList(courseID));
                break;
            case SUBJECT:
                int ccl_ID = tableView_classes.getSelectionModel().getSelectedItem().getCcl_ID();

                tableView_subjects.getItems().clear();
                tableView_subjects.getItems().addAll(getSubjectList(ccl_ID));
                comboBox_subjects.getSelectionModel().clearSelection();
                comboBox_subjects.setItems(getOutsideSubjectList(ccl_ID));
                break;
            default:
                System.out.println("No such table");
        }
    }

    private void escapeTableView(TableView tableView, ComboBox comboBox) {
        root.requestFocus();
        tableView.getSelectionModel().clearSelection();
        comboBox.getSelectionModel().clearSelection();
    }



    // MOUSE CLICKED

    @FXML
    private void tvCourseMouseClicked() {
        try {
            Course course = tableView_courses.getSelectionModel().getSelectedItem();
            int courseID = course.getCourseID();

            showClasses(courseID);
            comboBox_classes.setItems(getOutsideCurrClassList(courseID));

            tableView_subjects.getItems().clear();
            comboBox_subjects.getSelectionModel().clearSelection();
        } catch(NullPointerException ex) {
            root.requestFocus();
        }
    }

    @FXML
    private void tvClassMouseClicked() {
        try {
            int ccl_ID = tableView_classes.getSelectionModel().getSelectedItem().getCcl_ID();

            showSubjects(ccl_ID);
            comboBox_subjects.setItems(getOutsideSubjectList(ccl_ID));
        } catch(NullPointerException ex) {
            root.requestFocus();
        }
    }



    // KEY PRESSED

    @FXML
    private void tvCourseKeyPressed(KeyEvent event) {
        KeyCode key = event.getCode();
        if(key == KeyCode.ESCAPE) {
            escapeTableView(tableView_courses, comboBox_courses);
            tableView_classes.getItems().clear();
        }
    }

    @FXML
    private void tvClassKeyPressed(KeyEvent event) {
        KeyCode key = event.getCode();
        if (key == KeyCode.ESCAPE) {
            escapeTableView(tableView_classes, comboBox_classes);
            tableView_subjects.getItems().clear();
        }
    }

    @FXML
    private void tvSubjectKeyPressed(KeyEvent event) {
        KeyCode key = event.getCode();
        if (key == KeyCode.ESCAPE)
            escapeTableView(tableView_subjects, comboBox_subjects);
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
                if(course.getIsHidden() == 0)
                    courseList.add(course);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return courseList;
    }

    private ObservableList<Class> getCurrClassList(int courseID) {
        ObservableList<Class> currClassList = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT ccl.ccl_ID, cl.classID, CONCAT(cl.year, '-', cl.block) AS yearBlock " +
                    "FROM classes AS cl " +
                    "INNER JOIN courses_classes AS ccl ON ccl.classID = cl.classID " +
                    "INNER JOIN courses AS c ON c.courseID = ccl.courseID " +
                    "WHERE ccl.courseID = " + courseID);

            Class currClass;
            while(rs.next()) {
                currClass = new Class(rs.getInt("ccl_ID"), rs.getInt("classID"),
                        rs.getString("yearBlock"));
                currClassList.add(currClass);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String courseName = tableView_courses.getSelectionModel().getSelectedItem().getCourseName();
        if(currClassList.isEmpty()) {
            placeholder_class.setText("No class in " + courseName);
            placeholder_subject.setText("No subject in table");
        }
        else
            placeholder_subject.setText("No subject selected");

        return currClassList;
    }

    private ObservableList<Subject> getSubjectList(int ccl_ID) {
        ObservableList<Subject> subjectList = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT s.* FROM subjects AS s " +
                    "INNER JOIN courses_classes_subjects AS cls ON cls.subjectID = s.subjectID " +
                    "WHERE cls.ccl_ID = " + ccl_ID);

            Subject subject;
            while(rs.next()) {
                subject = new Subject(rs.getInt("subjectID"), rs.getString("subjectName"));
                subjectList.add(subject);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String yearBlock = tableView_classes.getSelectionModel().getSelectedItem().getYearBlock();
        if(subjectList.isEmpty())
            placeholder_subject.setText("No subject in " + yearBlock);

        return subjectList;
    }

    private void showCourses() {
        ObservableList<Course> list = getCourseList();

        tableColumn_course.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tableColumn_actionCourse.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tableColumn_actionCourse.setCellFactory(p -> new ButtonRemoveCourse());
        tableView_courses.setItems(list);
    }

    private void showClasses(int courseID) {
        ObservableList<Class> list = getCurrClassList(courseID);

        tableColumn_class.setCellValueFactory(new PropertyValueFactory<>("yearBlock"));
        tableColumn_actionClass.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tableColumn_actionClass.setCellFactory(p -> new ButtonRemoveClass());
        tableView_classes.setItems(list);
    }

    private void showSubjects(int ccl_ID) {
        ObservableList<Subject> list = getSubjectList(ccl_ID);

        tableColumn_subject.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        tableColumn_actionSubject.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tableColumn_actionSubject.setCellFactory(p -> new ButtonRemoveSubject());
        tableView_subjects.setItems(list);
    }

    // combobox_courses items
    private ObservableList<Course> getOutsideCourseList() {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT * FROM courses");

            Course course;
            while(rs.next()) {
                course = new Course(rs.getInt("courseID"), rs.getString("courseName"),
                        rs.getInt("isHidden"));
                if(course.getIsHidden() == 1)
                    courseList.add(course);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return courseList;
    }

    // combobox_classes items
    private ObservableList<Class> getOutsideCurrClassList(int courseID) {
        ObservableList<Class> currClassList = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT ccl.ccl_ID, cl.classID, CONCAT(cl.year, '-', cl.block) AS yearBlock " +
                    "FROM courses_classes AS ccl " +
                    "RIGHT JOIN classes AS cl ON ccl.classID = cl.classID " +
                    "AND ccl.courseID = " + courseID + " " +
                    "WHERE ccl.classID IS NULL");

            Class currClass;
            while(rs.next()) {
                currClass = new Class(rs.getInt("ccl_ID"), rs.getInt("classID"),
                        rs.getString("yearBlock"));
                currClassList.add(currClass);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currClassList;
    }

    // combobox_subjects items
    private ObservableList<Subject> getOutsideSubjectList(int ccl_ID) {
        ObservableList<Subject> subjectList = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT s.* FROM courses_classes_subjects AS ccls " +
                    "RIGHT JOIN subjects AS s ON ccls.subjectID = s.subjectID " +
                    "AND ccls.ccl_ID = " + ccl_ID + " " +
                    "WHERE ccls.ccl_ID IS NULL");

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



    // TABLE CELL REMOVE BUTTON

    private class ButtonRemoveCourse extends TableCell<Course, Boolean> {
        final Button cellButton = new Button("Remove");

        ButtonRemoveCourse() {
            cellButton.getStyleClass().add("button-destructive");
            cellButton.setOnAction(t -> {
                Course course = getTableView().getItems().get(getIndex());
                String courseName = course.getCourseName();

                Alert alert_remove = removeDialog(
                        "Remove course?",
                        "This will only remove \"" + courseName + "\" from the list. To delete it, switch to \"Masterlist\"");
                alert_remove.initOwner(cellButton.getScene().getWindow());

                Optional<ButtonType> result = alert_remove.showAndWait();
                if(result.orElse(alert_remove.getButtonTypes().get(0)) == alert_remove.getButtonTypes().get(1))
                    removeCourseRecord(course.getCourseID());
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

    private class ButtonRemoveClass extends TableCell<Class, Boolean> {
        final Button cellButton = new Button("Remove");

        ButtonRemoveClass() {
            cellButton.getStyleClass().add("button-destructive");
            cellButton.setOnAction(t -> {
                Class currClass = getTableView().getItems().get(getIndex());
                String courseName = tableView_courses.getSelectionModel().getSelectedItem().getCourseName();
                String yearBlock = currClass.getYearBlock();
                int courseID = tableView_courses.getSelectionModel().getSelectedItem().getCourseID();
                int classID = currClass.getClassID();

                Alert alert_remove = removeDialog(
                        "Remove class?",
                        "You will permanently lose all \"" + courseName + " " + yearBlock + "\" students' attendance records. \n" +
                                "You cannot undo this action.");
                alert_remove.initOwner(cellButton.getScene().getWindow());

                Optional<ButtonType> result = alert_remove.showAndWait();
                if(result.orElse(alert_remove.getButtonTypes().get(0)) == alert_remove.getButtonTypes().get(1))
                    removeClassFromCourseRecord(courseID, classID);
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

    private class ButtonRemoveSubject extends TableCell<Subject, Boolean> {
        final Button cellButton = new Button("Remove");

        ButtonRemoveSubject() {
            cellButton.getStyleClass().add("button-destructive");
            cellButton.setOnAction(t -> {
                Subject subject = getTableView().getItems().get(getIndex());
                String courseName = tableView_courses.getSelectionModel().getSelectedItem().getCourseName();
                String yearBlock = tableView_classes.getSelectionModel().getSelectedItem().getYearBlock();
                String subjectName = subject.getSubjectName();
                int ccl_ID = tableView_classes.getSelectionModel().getSelectedItem().getCcl_ID();
                int subjectID = subject.getSubjectID();

                Alert alert_remove = removeDialog(
                        "Remove subject?",
                        "You will permanently lose all \"" + courseName + " " + yearBlock + " " + subjectName +
                                "\" students' attendance records. \n" +
                                "You cannot undo this action.");
                alert_remove.initOwner(cellButton.getScene().getWindow());

                Optional<ButtonType> result = alert_remove.showAndWait();
                if(result.orElse(alert_remove.getButtonTypes().get(0)) == alert_remove.getButtonTypes().get(1))
                    removeSubjectFromClassRecord(ccl_ID, subjectID);
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

    private Alert removeDialog(String header, String content) {
        ButtonType buttonType_cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonType_remove = new ButtonType("Remove", ButtonBar.ButtonData.OK_DONE);

        Alert alert_remove = new Alert(Alert.AlertType.NONE, "", buttonType_cancel, buttonType_remove);
        alert_remove.setTitle("");
        alert_remove.setHeaderText(header);
        alert_remove.getDialogPane().setContent(new Label(content));
        alert_remove.getDialogPane().getStyleClass().add("dialog");
        alert_remove.getDialogPane().getStylesheets().add("attendanceLotus/style.css");
        alert_remove.getDialogPane().lookupButton(buttonType_cancel).getStyleClass().add("button-cancel");
        alert_remove.getDialogPane().lookupButton(buttonType_remove).getStyleClass().add("button-destructive");

        return alert_remove;
    }



    // CRUD OPERATION

    // add record
    @FXML
    private void addCourseRecord() {
        int courseID = comboBox_courses.getSelectionModel().getSelectedItem().getCourseID();

        CRUDOperation.executeQuery("UPDATE courses SET isHidden = 0 " +
                "WHERE courseID = " + courseID);
        refreshTable(Table.COURSE);
    }

    @FXML
    private void addClassToCourseRecord() {
        int courseID = tableView_courses.getSelectionModel().getSelectedItem().getCourseID();
        int classID = comboBox_classes.getSelectionModel().getSelectedItem().getClassID();

        CRUDOperation.executeQuery("INSERT INTO courses_classes (courseID, classID) " +
                "VALUES(" + courseID + ", " + classID + ")");
        refreshTable(Table.CLASS);
    }

    @FXML
    private void addSubjectToClassRecord() {
        int ccl_ID = tableView_classes.getSelectionModel().getSelectedItem().getCcl_ID();
        int subjectID = comboBox_subjects.getSelectionModel().getSelectedItem().getSubjectID();

        CRUDOperation.executeQuery("INSERT INTO courses_classes_subjects (ccl_ID, subjectID) " +
                "VALUES (" + ccl_ID + ", " + subjectID + ")");
        refreshTable(Table.SUBJECT);
    }

    // delete record
    private void removeCourseRecord(int courseID) {
        CRUDOperation.executeQuery("UPDATE courses SET isHidden = 1 " +
                "WHERE courseID = " + courseID);
        refreshTable(Table.COURSE);
    }

    private void removeClassFromCourseRecord(int courseID, int classID) {
        CRUDOperation.executeQuery("DELETE FROM courses_classes " +
                "WHERE courseID = " + courseID + " AND " + "classID = " + classID);
        refreshTable(Table.CLASS);
    }

    private void removeSubjectFromClassRecord(int ccl_ID, int subjectID) {
        CRUDOperation.executeQuery("DELETE FROM courses_classes_subjects " +
                "WHERE ccl_ID = " + ccl_ID + " AND subjectID = " + subjectID);
        refreshTable(Table.SUBJECT);
    }

}