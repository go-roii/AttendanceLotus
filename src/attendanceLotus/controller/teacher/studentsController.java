package attendanceLotus.controller.teacher;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import attendanceLotus.model.teacher.Curriculum;
import attendanceLotus.model.teacher.Student;
import attendanceLotus.util.CRUDOperation;

import java.net.URL;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class studentsController implements Initializable {

    @FXML
    private ComboBox<Curriculum> comboBox_curriculum;

    @FXML
    private TableView<Student> tableView_students;

    @FXML
    private TableColumn<Student, String> tableColumn_fName;

    @FXML
    private TableColumn<Student, String> tableColumn_mName;

    @FXML
    private TableColumn<Student, String> tableColumn_lName;

    @FXML
    private TableColumn<Student, Boolean> tableColumn_actionStudent;

    @FXML
    private TableView<Student> tableView_studentsML;

    @FXML
    private TableColumn<Student, String> tableColumn_fNameML;

    @FXML
    private TableColumn<Student, String> tableColumn_mNameML;

    @FXML
    private TableColumn<Student, String> tableColumn_lNameML;

    @FXML
    private TableColumn<Student, Boolean> tableColumn_actionStudentML;

    @FXML
    private TextField textField_search;

    ObservableList<Student> studentList = FXCollections.observableArrayList();
    ObservableList<Student> studentMasterlist = FXCollections.observableArrayList();
    FilteredList<Student> filteredData = new FilteredList(studentList);
    FilteredList<Student> filteredData2 = new FilteredList(studentMasterlist);

    private final int loginTeacherID = homeController.loginTeacherID;

    private int courseID, cclsu_ID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textField_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(x -> x.getFName()
                    .concat(" " + x.getMName())
                    .concat(" " + x.getLName()).toLowerCase()
                    .contains(newValue.toLowerCase()));

            filteredData2.setPredicate(x -> x.getFName()
                    .concat(" " + x.getMName())
                    .concat(" " + x.getLName()).toLowerCase()
                    .contains(newValue.toLowerCase()));
        });

        System.out.println(loginTeacherID);

        comboBox_curriculum.setItems(getCourseList());

        tableColumn_lName.setSortType(TableColumn.SortType.ASCENDING);
        tableView_students.getSortOrder().add(tableColumn_lName);
        tableColumn_lNameML.setSortType(TableColumn.SortType.ASCENDING);
        tableView_studentsML.getSortOrder().add(tableColumn_lNameML);
    }



    // HELPER METHOD

    private void refreshTables() {
        studentList.clear();
        showStudentList();
        studentMasterlist.clear();
        showStudentMasterlist();
    }

    @FXML
    private void updateCCLSU_ID() {
        courseID = comboBox_curriculum.getSelectionModel().getSelectedItem().getCourseID();
        cclsu_ID = comboBox_curriculum.getSelectionModel().getSelectedItem().getCclsu_ID();

        studentList.clear();
        showStudentList();
        studentMasterlist.clear();
        showStudentMasterlist();
    }



    // TABLE

    // combobox_courses items
    private ObservableList<Curriculum> getCourseList() {
        ObservableList<Curriculum> courseList = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT c.courseID, cclsu.cclsu_ID, c.courseName, CONCAT(cl.year, '-', cl.block) " +
                    "AS yearBlock, su.subjectName " +
                    "FROM courses_classes_subjects AS cclsu " +
                    "INNER JOIN courses_classes AS ccl ON ccl.ccl_ID = cclsu.ccl_ID " +
                    "INNER JOIN courses AS c ON c.courseID = ccl.courseID " +
                    "INNER JOIN classes AS cl ON cl.classID = ccl.classID " +
                    "INNER JOIN subjects AS su ON su.subjectID = cclsu.subjectID " +
                    "WHERE cclsu.teacherID = " + loginTeacherID + " " +
                    "ORDER BY c.courseName ASC");

            Curriculum curriculum;
            while(rs.next()) {
                curriculum = new Curriculum(rs.getInt("courseID"), rs.getInt("cclsu_ID"),
                        rs.getString("courseName"), rs.getString("yearBlock"),
                        rs.getString("subjectName"));
                courseList.add(curriculum);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return courseList;
    }

    private SortedList<Student> getStudentList() {
        SortedList<Student> sortableData = new SortedList<>(filteredData);
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT s.studID, s.fName, s.mName, s.lName FROM students_cclsu AS scclsu " +
                    "INNER JOIN students AS s ON s.studID = scclsu.studID " +
                    "WHERE scclsu.cclsu_ID = " + cclsu_ID);

            Student student;
            while(rs.next()) {
                student = new Student(rs.getInt("studID"), rs.getString("fName"),
                        rs.getString("mName"), rs.getString("lName"));
                studentList.add(student);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sortableData;
    }

    private SortedList<Student> getStudentMasterlist(int courseID) {
        SortedList<Student> sortableData = new SortedList<>(filteredData2);
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT * FROM students AS s " +
                    "WHERE courseID = " + courseID);

            Student student;
            while(rs.next()) {
                student = new Student(rs.getInt("studID"), rs.getString("fName"),
                        rs.getString("mName"), rs.getString("lName"));
                studentMasterlist.add(student);
            }
            studentMasterlist.removeIf(c -> studentList.stream().map(Student::getStudID).anyMatch(n -> n.equals(c.getStudID())));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sortableData;
    }

    private void showStudentList() {
        SortedList<Student> list = getStudentList();

        tableColumn_fName.setCellValueFactory(new PropertyValueFactory<>("fName"));
        tableColumn_mName.setCellValueFactory(new PropertyValueFactory<>("mName"));
        tableColumn_lName.setCellValueFactory(new PropertyValueFactory<>("lName"));
        tableColumn_actionStudent.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tableColumn_actionStudent.setCellFactory(p -> new studentsController.ButtonRemoveClass());

        tableView_students.setItems(list);
        list.comparatorProperty().bind(tableView_students.comparatorProperty());
    }

    private void showStudentMasterlist() {
        SortedList<Student> list = getStudentMasterlist(courseID);

        tableColumn_fNameML.setCellValueFactory(new PropertyValueFactory<>("fName"));
        tableColumn_mNameML.setCellValueFactory(new PropertyValueFactory<>("mName"));
        tableColumn_lNameML.setCellValueFactory(new PropertyValueFactory<>("lName"));
        tableColumn_actionStudentML.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tableColumn_actionStudentML.setCellFactory(p -> new studentsController.ButtonAddClass());

        tableView_studentsML.setItems(list);
        list.comparatorProperty().bind(tableView_studentsML.comparatorProperty());
    }




    // TABLE CELL BUTTON

    private class ButtonRemoveClass extends TableCell<Student, Boolean> {
        final Button cellButton = new Button("Remove");

        ButtonRemoveClass() {
            cellButton.getStyleClass().add("button-destructive");
            cellButton.setOnAction(t -> {
                Student student = getTableView().getItems().get(getIndex());
                String studName = student.getFName() + " " + student.getMName() + " " + student.getLName();

                ButtonType buttonType_cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                ButtonType buttonType_remove = new ButtonType("Remove", ButtonBar.ButtonData.OK_DONE);

                Alert alert_remove = new Alert(Alert.AlertType.NONE, "", buttonType_cancel, buttonType_remove);
                alert_remove.setTitle("");
                alert_remove.setHeaderText("Remove student?");
                alert_remove.getDialogPane().setContent(new Label(
                        "You will permanently lose \"" + studName + "'s\" attendance records. \n" +
                                "You cannot undo this action."));
                alert_remove.initOwner(cellButton.getScene().getWindow());
                alert_remove.getDialogPane().getStyleClass().add("dialog");
                alert_remove.getDialogPane().getStylesheets().add("attendanceLotus/style.css");
                alert_remove.getDialogPane().lookupButton(buttonType_cancel).getStyleClass().add("button-cancel");
                alert_remove.getDialogPane().lookupButton(buttonType_remove).getStyleClass().add("button-destructive");

                Optional<ButtonType> result = alert_remove.showAndWait();
                if(result.orElse(buttonType_cancel) == buttonType_remove)
                    removeFromSubjectRecord(student.getStudID());
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

    private class ButtonAddClass extends TableCell<Student, Boolean> {
        final Button cellButton = new Button("Add");

        ButtonAddClass() {
            cellButton.getStyleClass().add("button-suggested");
            cellButton.setOnAction(t -> {
                Student student = getTableView().getItems().get(getIndex());
                addToSubjectRecord(student.getStudID());
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

    private void removeFromSubjectRecord(int studID) {
        CRUDOperation.executeQuery("DELETE FROM students_cclsu " +
                "WHERE studID = " + studID + " AND cclsu_ID = " + cclsu_ID);
        refreshTables();
    }

    private void addToSubjectRecord(int studID) {
        CRUDOperation.executeQuery("INSERT INTO students_cclsu (studID, cclsu_ID)" +
                "VALUES (" + studID + ", " + cclsu_ID + ")");
        refreshTables();
    }

}
