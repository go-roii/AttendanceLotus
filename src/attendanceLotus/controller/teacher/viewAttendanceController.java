package attendanceLotus.controller.teacher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import attendanceLotus.model.teacher.Curriculum;
import attendanceLotus.model.teacher.Student;
import attendanceLotus.util.CRUDOperation;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class viewAttendanceController implements Initializable {

    @FXML
    private ComboBox<Curriculum> comboBox_curriculum;

    @FXML
    private Button button_refresh;

    @FXML
    private DatePicker datePicker_attendanceFrom;

    @FXML
    private DatePicker datePicker_attendanceTo;

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

        comboBox_curriculum.setItems(getCourseList());

        datePicker_attendanceFrom.setConverter(new StringConverter<LocalDate>() {
            final String pattern = "MMMM dd, yyyy";
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                try {
                    ResultSet rs;

                    rs = CRUDOperation.statement("SELECT MIN(attDate) AS attDate FROM attendance");
                    if(rs.first())
                        if(rs.getDate("attDate") != null)
                            datePicker_attendanceFrom.setValue(rs.getDate("attDate").toLocalDate());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
        datePicker_attendanceTo.setConverter(new StringConverter<LocalDate>() {
            final String pattern = "MMMM dd, yyyy";
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                datePicker_attendanceTo.setValue(LocalDate.now());
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

        // sync attendance when date is changed
        datePicker_attendanceFrom.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue != newValue)
                syncAttendance();
        });
        datePicker_attendanceTo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue != newValue)
                syncAttendance();
        });
        // disable overlapping from to dates
        datePicker_attendanceFrom.setDayCellFactory(t -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(datePicker_attendanceTo.getValue()) > -1);
            }
        });
        datePicker_attendanceTo.setDayCellFactory(t -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0 ||
                        date.compareTo(datePicker_attendanceFrom.getValue()) < 1);
            }
        });

        tableView_students.setRowFactory(tv -> new TableRow<Student>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);
                if (student == null || student.getTotal() == 0) {
                    getStyleClass().remove("FDA");
                    getStyleClass().remove("nearFDA");
                }
                else if (student.getTotal() >= 6)
                    getStyleClass().add("FDA");
                else if (student.getTotal() >= 4)
                    getStyleClass().add("nearFDA");
                else {
                    getStyleClass().remove("FDA");
                    getStyleClass().remove("nearFDA");
                }
            }
        });

        tableColumn_lName.setSortType(TableColumn.SortType.ASCENDING);
        tableView_students.getSortOrder().add(tableColumn_lName);
    }



    // HELPER METHOD

    @FXML
    private void syncAttendance() {
        LocalDate fromDate = datePicker_attendanceFrom.getValue();
        LocalDate toDate = datePicker_attendanceTo.getValue();

        studentList.clear();
        showStudentList();

        for(Student student : tableView_students.getItems()) {
            ResultSet rs;

            try {
                int present = 0, absent = 0, late = 0;

                rs = CRUDOperation.statement("SELECT status FROM attendance " +
                        "WHERE ssu_ID = " + student.getSsu_ID() + " AND attDate BETWEEN '" + fromDate + "' AND '" + toDate + "'");
                while(rs.next()) {
                    switch (rs.getInt("status")) {
                        case 1:
                            ++present;
                            student.setPresent(present);
                            break;
                        case 2:
                            ++absent;
                            student.setAbsent(absent);
                            break;
                        case 3:
                            ++late;
                            student.setLate(late);
                    }
                }
                student.setTotal((absent*2) + late);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void updateCCLSU_ID() {
        cclsu_ID = comboBox_curriculum.getSelectionModel().getSelectedItem().getCclsu_ID();

        datePicker_attendanceFrom.setConverter(new StringConverter<LocalDate>() {
            final String pattern = "MMMM dd, yyyy";
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                try {
                    ResultSet rs;

                    rs = CRUDOperation.statement("SELECT MIN(attDate) AS attDate FROM attendance AS att " +
                            "INNER JOIN students_cclsu AS scclsu ON scclsu.ssu_ID = att.ssu_ID " +
                            "WHERE cclsu_ID = " + cclsu_ID);

                    if(rs.first())
                        if(rs.getDate("attDate") != null)
                            datePicker_attendanceFrom.setValue(rs.getDate("attDate").toLocalDate());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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

        syncAttendance();
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
                        rs.getString("mName"), rs.getString("lName"), false);
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
        tableColumn_present.setCellValueFactory(new PropertyValueFactory<>("present"));
        tableColumn_absent.setCellValueFactory(new PropertyValueFactory<>("absent"));
        tableColumn_late.setCellValueFactory(new PropertyValueFactory<>("late"));

        tableView_students.setItems(list);
        list.comparatorProperty().bind(tableView_students.comparatorProperty());
    }

}
