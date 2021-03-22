package attendanceLotus.controller.teacher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import attendanceLotus.model.teacher.Curriculum;
import attendanceLotus.model.teacher.Student;
import attendanceLotus.util.CRUDOperation;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class takeAttendanceController implements Initializable {

    @FXML
    private TabPane tabPane_takeAttendance;

    @FXML
    private ComboBox<Curriculum> comboBox_curriculum;

    @FXML
    private Button button_previous;

    @FXML
    private Button button_next;

    @FXML
    private Button button_clear;

    @FXML
    private Button button_save;

    @FXML
    private DatePicker datePicker_attendance;

    @FXML
    private TableView<Student> tableView_students;

    @FXML
    private TableColumn<Student, String> tableColumn_fName;

    @FXML
    private TableColumn<Student, String> tableColumn_mName;

    @FXML
    private TableColumn<Student, String> tableColumn_lName;

    @FXML
    private TableColumn<Student, ToggleButton> tableColumn_present;

    @FXML
    private TableColumn<Student, ToggleButton> tableColumn_absent;

    @FXML
    private TableColumn<Student, ToggleButton> tableColumn_late;

    @FXML
    private TextField textField_search;

    ObservableList<Student> studentList = FXCollections.observableArrayList();
    FilteredList<Student> filteredData = new FilteredList(studentList);

    private final int loginTeacherID = homeController.loginTeacherID;

    private int cclsu_ID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textField_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(x -> x.getFName()
                    .concat(" " + x.getMName())
                    .concat(" " + x.getLName()).toLowerCase()
                    .contains(newValue.toLowerCase()));
        });

        System.out.println(loginTeacherID);

        // prevent tab from detecting arrow keys
        tabPane_takeAttendance.addEventFilter(KeyEvent.ANY, event -> {
                    if(event.getCode().isArrowKey() && event.getTarget() == tabPane_takeAttendance)
                        event.consume();
        });

        comboBox_curriculum.setItems(getCourseList());

        datePicker_attendance.valueProperty().addListener(t ->
                button_next.setDisable(datePicker_attendance.getValue().compareTo(LocalDate.now()) > -1));

        datePicker_attendance.setConverter(new StringConverter<LocalDate>() {
            final String pattern = "MMMM dd, yyyy EEE";
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                datePicker_attendance.setValue(LocalDate.now());
            }

            @Override public String toString(LocalDate date) {
                if (date != null)
                    return dateFormatter.format(date);
                else
                    return "";
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty())
                    return LocalDate.parse(string, dateFormatter);
                else
                    return null;
            }
        });
        // disable future dates
        datePicker_attendance.setDayCellFactory(t -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0);
            }
        });

        // sync attendance when date is changed
        datePicker_attendance.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue != newValue)
                syncAttendance();
        });

        tableColumn_lName.setSortType(TableColumn.SortType.ASCENDING);
        tableView_students.getSortOrder().add(tableColumn_lName);
    }



    // HELPER METHOD

    @FXML
    private void syncAttendance() {
        LocalDate attDate = datePicker_attendance.getValue();
        int present = 0, absent = 0, late = 0;

        button_save.setDisable(true);

        for(Student student : tableView_students.getItems()) {
            ResultSet rs;

            try {
                rs = CRUDOperation.statement("SELECT status FROM attendance " +
                        "WHERE ssu_ID = " + student.getSsu_ID() + " AND attDate = '" + attDate + "'");

                if(rs.next()) {
                    switch (rs.getInt("status")) {
                        case 1:
                            student.getRadioButton_present().setSelected(true);
                            ++present;
                            break;
                        case 2:
                            student.getRadioButton_absent().setSelected(true);
                            ++absent;
                            break;
                        case 3:
                            student.getRadioButton_late().setSelected(true);
                            ++late;
                    }
                }
                else
                    student.getGroup().selectToggle(null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if(present == 0 && absent == 0 && late == 0) {
            button_clear.setDisable(true);
            tableColumn_present.setText("Present");
            tableColumn_absent.setText("Absent");
            tableColumn_late.setText("Late");
        }
        else {
            button_clear.setDisable(false);
            tableColumn_present.setText("Present (" + present + ")");
            tableColumn_absent.setText("Absent (" + absent + ")");
            tableColumn_late.setText("Late (" + late + ")");
        }
    }

    // TODO show alert before replacing previous attendance records
    @FXML
    private void saveAttendance() {
        ButtonType buttonType_ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.NONE, "", buttonType_ok);
        alert.setTitle("");
        alert.setHeaderText("Incomplete attendance");
        alert.getDialogPane().setContent(new Label("Please fill all the students' attendance."));
        alert.initOwner(button_save.getScene().getWindow());
        alert.getDialogPane().getStyleClass().add("dialog");
        alert.getDialogPane().getScene().getStylesheets().add("attendanceLotus/style.css");

        for(Student student : tableView_students.getItems()) {
            if(student.getGroup().getSelectedToggle() == null) {
                alert.showAndWait();
                return;
            }
        }

        LocalDate attDate = datePicker_attendance.getValue();
        boolean ShowDialog = false;

        for(Student student : tableView_students.getItems()) {
//            try {
                int ssu_ID = student.getSsu_ID();
                int status = (int) student.getGroup().getSelectedToggle().getUserData();

                CRUDOperation.executeQuery("DELETE FROM attendance " +
                        "WHERE ssu_ID = " + ssu_ID + " AND attDate = '" + attDate + "'");

                CRUDOperation.executeQuery("INSERT INTO attendance (ssu_ID, attDate, status)" +
                        "VALUES (" + ssu_ID + ", '" + attDate + "', " + status + ")");
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//                if(!ShowDialog)
//                    ShowDialog = true;
//            }
        }

//        if(ShowDialog) {
//            System.out.println("tae");
//        }

        syncAttendance();
    }

    @FXML
    private void clearAttendance() {
        LocalDate attDate = datePicker_attendance.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String formattedString = attDate.format(formatter);

        ButtonType buttonType_cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonType_clear = new ButtonType("Clear", ButtonBar.ButtonData.OK_DONE);

        Alert alert_clear = new Alert(Alert.AlertType.NONE, "", buttonType_cancel, buttonType_clear);
        alert_clear.setTitle("");
        alert_clear.setHeaderText("Clear attendance records?");
        alert_clear.getDialogPane().setContent(new Label(
                "Are you sure you want to clear attendance records on " + formattedString + "? \n" +
                        "You cannot undo this action."));
        alert_clear.initOwner(button_save.getScene().getWindow());
        alert_clear.getDialogPane().getStyleClass().add("dialog");
        alert_clear.getDialogPane().getScene().getStylesheets().add("attendanceLotus/style.css");
        alert_clear.getDialogPane().lookupButton(buttonType_cancel).getStyleClass().add("button-cancel");
        alert_clear.getDialogPane().lookupButton(buttonType_clear).getStyleClass().add("button-destructive");

        Optional<ButtonType> result = alert_clear.showAndWait();
        if(result.orElse(buttonType_cancel) == buttonType_clear) {
            for (Student student : tableView_students.getItems()) {
                int ssu_ID = student.getSsu_ID();

                CRUDOperation.executeQuery("DELETE FROM attendance " +
                        "WHERE ssu_ID = " + ssu_ID + " AND attDate = '" + attDate + "'");
            }

            syncAttendance();
        }
    }

    @FXML
    private void updateCCLSU_ID() {
        cclsu_ID = comboBox_curriculum.getSelectionModel().getSelectedItem().getCclsu_ID();

        studentList.clear();
        showStudentList();
        syncAttendance();
    }

    private Alert dialog(String header, String content) {
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



    // HANDLE EVENT

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == button_previous)
            datePicker_attendance.setValue(datePicker_attendance.getValue().minusDays(1));
        else if(event.getSource() == button_next)
            datePicker_attendance.setValue(datePicker_attendance.getValue().plusDays(1));
    }



    // TABLE

    // combobox_courses items
    private ObservableList<Curriculum> getCourseList() {
        ObservableList<Curriculum> courseList = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT cclsu.cclsu_ID, c.courseName, CONCAT(cl.year, '-', cl.block) " +
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
                curriculum = new Curriculum(rs.getInt("cclsu_ID"), rs.getString("courseName"),
                        rs.getString("yearBlock"), rs.getString("subjectName"));
                courseList.add(curriculum);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return courseList;
    }

    private SortedList<Student> getStudentList() {
        SortedList<Student> sortedData = new SortedList<>(filteredData);
        ResultSet rs;

        try {
            rs = CRUDOperation.statement("SELECT scclsu.ssu_ID, s.studID, s.fName, s.mName, s.lName " +
                    "FROM students_cclsu AS scclsu " +
                    "INNER JOIN students AS s ON s.studID = scclsu.studID " +
                    "WHERE scclsu.cclsu_ID = " + cclsu_ID);

            Student student;
            while(rs.next()) {
                student = new Student(rs.getInt("ssu_ID"), rs.getInt("studID"), rs.getString("fName"),
                        rs.getString("mName"), rs.getString("lName"), true);

                // iterate all radio buttons inside toggle group
                student.getGroup().getToggles().forEach(radioButtons -> {
                    RadioButton radioButton = (RadioButton) radioButtons;

                    // enable save button when radio button is clicked
                    radioButton.setOnAction(t -> {
                        if(button_save.isDisabled())
                            button_save.setDisable(false);
                    });
                });

                studentList.add(student);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return sortedData;
    }

    private void showStudentList() {
        SortedList<Student> list = getStudentList();

        tableColumn_fName.setCellValueFactory(new PropertyValueFactory<>("fName"));
        tableColumn_mName.setCellValueFactory(new PropertyValueFactory<>("mName"));
        tableColumn_lName.setCellValueFactory(new PropertyValueFactory<>("lName"));
        tableColumn_present.setCellValueFactory(new PropertyValueFactory<>("radioButton_present"));
        tableColumn_absent.setCellValueFactory(new PropertyValueFactory<>("radioButton_absent"));
        tableColumn_late.setCellValueFactory(new PropertyValueFactory<>("radioButton_late"));

        tableView_students.setItems(list);
        list.comparatorProperty().bind(tableView_students.comparatorProperty());
    }

}
