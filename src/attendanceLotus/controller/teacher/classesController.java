package attendanceLotus.controller.teacher;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import attendanceLotus.model.teacher.Class;
import attendanceLotus.util.CRUDOperation;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class classesController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private TableView<Class> tableView_classes;

    @FXML
    private TableColumn<Class, String> tableColumn_course;

    @FXML
    private TableColumn<Class, String> tableColumn_class;

    @FXML
    private TableColumn<Class, String> tableColumn_subject;

    @FXML
    private TableColumn<Class, Boolean> tableColumn_actionClass;

    @FXML
    private TableView<Class> tableView_classesML;

    @FXML
    private TableColumn<Class, String> tableColumn_courseML;

    @FXML
    private TableColumn<Class, String> tableColumn_classML;

    @FXML
    private TableColumn<Class, String> tableColumn_subjectML;

    @FXML
    private TableColumn<Class, Boolean> tableColumn_actionClassML;

    private final int loginTeacherID = homeController.loginTeacherID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(loginTeacherID);

        showClassList();
        showClassMasterlist();

        tableColumn_course.setSortType(TableColumn.SortType.ASCENDING);
        tableView_classes.getSortOrder().add(tableColumn_course);
        tableColumn_courseML.setSortType(TableColumn.SortType.ASCENDING);
        tableView_classesML.getSortOrder().add(tableColumn_courseML);
    }



    // HELPER METHOD

    private void refreshTables() {
        tableView_classes.getItems().clear();
        tableView_classes.getItems().addAll(getClassList());
        tableView_classesML.getItems().clear();
        tableView_classesML.getItems().addAll(getClassMasterlist());

        tableView_classes.sort();
        tableView_classesML.sort();
    }



    // TABLE

    private final String query = "SELECT ccls.cclsu_ID, ccls.teacherID, c.courseName, CONCAT(cl.year, '-', cl.block) " +
            "as yearBlock, s.subjectName " +
            "FROM courses_classes_subjects AS ccls " +
            "INNER JOIN courses_classes AS ccl ON ccl.ccl_ID = ccls.ccl_ID " +
            "INNER JOIN classes AS cl ON cl.classID = ccl.classID " +
            "INNER JOIN courses AS c ON c.courseID = ccl.courseID " +
            "INNER JOIN subjects AS s ON s.subjectID = ccls.subjectID ";

    public ObservableList<Class> getClassList() {
        ObservableList<Class> currClassList = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = CRUDOperation.statement(query + "WHERE teacherID = " + loginTeacherID);

            Class currClass;
            while(rs.next()) {
                currClass = new Class(rs.getInt("cclsu_ID"), rs.getInt("teacherID"), rs.getString("courseName"),
                        rs.getString("yearBlock"), rs.getString("subjectName"));
                currClassList.add(currClass);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currClassList;
    }

    public ObservableList<Class> getClassMasterlist() {
        ObservableList<Class> currClassMasterlist = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = CRUDOperation.statement(query + "WHERE teacherID IS NULL");

            Class currClass;
            while(rs.next()) {
                currClass = new Class(rs.getInt("cclsu_ID"), rs.getInt("teacherID"),
                        rs.getString("courseName"), rs.getString("yearBlock"),
                        rs.getString("subjectName"));
                currClassMasterlist.add(currClass);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currClassMasterlist;
    }

    public void showClassList() {
        ObservableList<Class> list = getClassList();

        tableColumn_course.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tableColumn_class.setCellValueFactory(new PropertyValueFactory<>("yearBlock"));
        tableColumn_subject.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        tableColumn_actionClass.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tableColumn_actionClass.setCellFactory(p -> new ButtonRemoveClass());
        tableView_classes.setItems(list);
    }

    public void showClassMasterlist() {
        ObservableList<Class> list = getClassMasterlist();

        tableColumn_courseML.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tableColumn_classML.setCellValueFactory(new PropertyValueFactory<>("yearBlock"));
        tableColumn_subjectML.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        tableColumn_actionClassML.setCellValueFactory(p -> new SimpleBooleanProperty(p.getValue() != null));
        tableColumn_actionClassML.setCellFactory(p -> new ButtonAddClass());

        tableView_classesML.setItems(list);
    }



    // TABLE CELL BUTTON

    private class ButtonRemoveClass extends TableCell<Class, Boolean> {
        final Button cellButton = new Button("Remove");

        ButtonRemoveClass() {
            cellButton.getStyleClass().add("button-destructive");
            cellButton.setOnAction(t -> {
//                Class currClass = ButtonRemoveClass.this.getTableView().getItems().get(ButtonRemoveClass.this.getIndex());
                Class currClass = getTableView().getItems().get(getIndex());
                removeFromClassRecord(currClass.getCclsu_ID());
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

    private class ButtonAddClass extends TableCell<Class, Boolean> {
        final Button cellButton = new Button("Add");

        ButtonAddClass() {
            cellButton.setOnAction(t -> {
                Class currClass = getTableView().getItems().get(getIndex());
                addToClassRecord(currClass.getCclsu_ID());
            });
            cellButton.getStyleClass().add("button-suggested");
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

    private void removeFromClassRecord(int cclsu_id) {
        CRUDOperation.executeQuery("UPDATE courses_classes_subjects SET teacherID = NULL " +
                "WHERE cclsu_id = " + cclsu_id);
        refreshTables();
    }

    private void addToClassRecord(int cclsu_id) {
        CRUDOperation.executeQuery("UPDATE courses_classes_subjects SET teacherID = " + loginTeacherID + " " +
                "WHERE cclsu_id = " + cclsu_id);
        refreshTables();
    }

}
