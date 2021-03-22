package attendanceLotus.controller.teacher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import attendanceLotus.util.FxmlLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class homeController implements Initializable {

    @FXML
    private HBox titleBar;

    @FXML
    private Tooltip tooltip_sidebar;

    @FXML
    private BorderPane mainPane;

    @FXML
    private VBox sidebar;

    @FXML
    private Button button_expand;

    @FXML
    private Button button_logout;

    @FXML
    private ToggleButton toggleButton_attendance;

    @FXML
    private ToggleButton toggleButton_students;

    @FXML
    private ToggleButton toggleButton_classes;

    @FXML
    private ImageView icon_attendance;

    @FXML
    private ImageView icon_students;

    @FXML
    private ImageView icon_classes;

    @FXML
    private Label label_attendance;

    @FXML
    private Label label_students;

    @FXML
    private Label label_classes;

    @FXML
    private Label label_logout;

    private boolean isExpanded = false;

    public static int loginTeacherID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(loginTeacherID);

        sidebarStatus();
        attendanceToggleAction();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        if(event.getSource() == button_expand)
            sidebarStatus();
        else if(event.getSource() == button_logout) {
            ButtonType buttonType_logout = new ButtonType("Log out", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonType_cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            Alert alert_logout = new Alert(Alert.AlertType.NONE,
                    "You will be returned to the log in screen.", buttonType_cancel, buttonType_logout);
            alert_logout.setTitle("");
            alert_logout.setHeaderText("Log out");
            alert_logout.initOwner(button_logout.getScene().getWindow());
            alert_logout.getDialogPane().getStyleClass().add("dialog");
            alert_logout.getDialogPane().getScene().getStylesheets().add("attendanceLotus/style.css");
            // button_cancel style
            alert_logout.getDialogPane().lookupButton(alert_logout.getButtonTypes().get(0)).getStyleClass().add("button-cancel");

            Optional<ButtonType> result = alert_logout.showAndWait();
            if(result.orElse(buttonType_cancel) == buttonType_logout) {
                Parent login = FXMLLoader.load(getClass().getResource("/attendanceLotus/view/login.fxml"));
                Stage stage = new Stage();
                stage.setTitle("School Attendance");
                stage.setScene(new Scene(login));
                stage.show();

                Stage loginStage = (Stage) button_logout.getScene().getWindow();
                loginStage.close();
            }
        }
    }

    // TOGGLE BUTTON FUNCTION
    @FXML
    private void attendanceToggleAction() {
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("teacher", "takeAttendance");
        mainPane.setCenter(view);

        selectToggleButton(toggleButton_attendance, toggleButton_students, toggleButton_classes);
        selectIcon(icon_attendance, icon_students, icon_classes);
    }

    @FXML
    private void studentsToggleAction() {
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("teacher", "students");
        mainPane.setCenter(view);

        selectToggleButton(toggleButton_students, toggleButton_attendance, toggleButton_classes);
        selectIcon(icon_students, icon_attendance, icon_classes);
    }

    @FXML
    private void classesToggleAction() {
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("teacher", "classes");
        mainPane.setCenter(view);

        selectToggleButton(toggleButton_classes, toggleButton_attendance, toggleButton_students);
        selectIcon(icon_classes, icon_attendance, icon_students);
    }
//
//
//    @FXML
//    private void classesToggleAction() {
//        FxmlLoader object = new FxmlLoader();
//        Pane view = object.getPage("admin", "classes");
//        mainPane.setCenter(view);
//
//        selectToggleButton(toggleButton_classes, toggleButton_students,
//                toggleButton_teachers, toggleButton_curriculums);
//    }

    // HELPER METHODS

    private void sidebarStatus() {
        if(isExpanded) {
            isExpanded = false;
            tooltip_sidebar.setText("Collapse sidebar");

            showLabel(label_attendance, label_students, label_classes, label_logout);
            sidebar.setPrefWidth(250);
        }
        else {
            isExpanded = true;
            tooltip_sidebar.setText("Expand sidebar");

            hideLabel(label_attendance, label_students, label_classes, label_logout);
            sidebar.setPrefWidth(0);
        }
    }

    private void selectToggleButton(ToggleButton selected, ToggleButton... unselected) {
        selected.setSelected(true);

        for(ToggleButton toggleButton: unselected)
            toggleButton.setSelected(false);
    }

    // TODO filled icons when selected
    private void selectIcon(ImageView selected, ImageView... unselected) {
        String iconID, iconName;

        iconID = selected.getId().replace("icon_","");
        iconName = iconID.charAt(0) + iconID.substring(1).toLowerCase();
        Image image = new Image(getClass().getResourceAsStream(
                "/attendanceLotus/resource/image/20dp/" + iconName + "_selected_20dp.png"));
        selected.setImage(image);

        for(ImageView icon : unselected) {
            String un_iconID, un_iconName;

            un_iconID = icon.getId().replace("icon_","");
            un_iconName = un_iconID.charAt(0) + un_iconID.substring(1).toLowerCase();
            Image un_image = new Image(getClass().getResourceAsStream(
                    "/attendanceLotus/resource/image/20dp/" + un_iconName + "_20dp.png"));
            icon.setImage(un_image);
        }
    }

    private void showLabel(Label... labels) {
        for(Label label : labels) {
            label.setManaged(true);
            label.setDisable(false);
        }
    }

    private void hideLabel(Label... labels) {
        for(Label label : labels) {
            label.setManaged(false);
            label.setDisable(true);
        }
    }

    @FXML
    private void handleMousePressed(MouseEvent pressEvent) {
        Stage stage = (Stage) mainPane.getScene().getWindow();

        titleBar.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        });
    }

}
