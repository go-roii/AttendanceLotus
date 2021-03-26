package attendanceLotus.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import attendanceLotus.controller.teacher.homeController;
import attendanceLotus.util.ConnectionUtil;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class loginController {

    Connection conn;
    PreparedStatement pst, pst2;
    ResultSet rs, rs2;

    @FXML
    private TextField textField_username;

    @FXML
    private PasswordField textField_password;

    @FXML
    private Button button_login;


    @FXML
    void login() {
        conn = ConnectionUtil.connectdb();

        String user = textField_username.getText();
        String pass = textField_password.getText();

        ButtonType buttonType_ok = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.NONE, "", buttonType_ok);
        alert.setTitle("");
        alert.initOwner(button_login.getScene().getWindow());
        alert.getDialogPane().getStyleClass().add("dialog");
        alert.getDialogPane().getScene().getStylesheets().add("attendanceLotus/style.css");

        // if both text field has no input
        if(user.equals("") && pass.equals("")) {
            alert.setHeaderText("Log in failed");
            alert.getDialogPane().setContent(new Label(
                    "The username or password is blank. Please try again."));
            alert.showAndWait();
        }
        else {
            try {
                // search for matching username and password
                pst = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
                pst.setString(1, user);
                pst.setString(2, pass);

                rs = pst.executeQuery();

                // if has a match
                if(rs.next()) {
                    int userType = rs.getInt("userType");
                    int userId = rs.getInt("userId");
                    Stage stage = new Stage();

                    // if admin
                    if(userType == 1) {
                        pst2 = conn.prepareStatement("SELECT * FROM users");
                        rs2 = pst2.executeQuery();

                        if (rs2.next()) {
                            Parent homeScreen = FXMLLoader.load(getClass().getResource("/attendanceLotus/view/admin/home.fxml"));
                            stage.setTitle("AttendanceLotus - Admin");
                            stage.setScene(new Scene(homeScreen
                                    , 1280, 640
                                    ));
                        }
                    }
                    // if teacher
                    if(userType == 2) {
                        pst2 = conn.prepareStatement("SELECT * FROM teachers WHERE userID = ?");
                        pst2.setInt(1, userId);

                        rs2 = pst2.executeQuery();

                        if (rs2.next()) {
                            String teacherName = rs2.getString("fName") + " " + rs2.getString("mName") + " " +
                                    rs2.getString("lName");

                            homeController.loginTeacherID = rs2.getInt("teacherID");
                            Parent homeScreen = FXMLLoader.load(getClass().getResource("/attendanceLotus/view/teacher/home.fxml"));
                            stage.setTitle("AttendanceLotus - " + teacherName);
                            stage.setScene(new Scene(homeScreen, 1080, 640));
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Teacher data does not exist");
                            return;
                        }
                    }
                    // if student
                    else if (userType == 3) {
                        pst2 = conn.prepareStatement("SELECT * FROM students WHERE userID = ?");
                        pst2.setInt(1, userId);

                        rs2 = pst2.executeQuery();

                        if (rs2.next()) {
                            String name = rs2.getString("fName") + " " +
                                          rs2.getString("mName") + " " +
                                          rs2.getString("lName");
                            alert.setHeaderText("Welcome Student " + name);
                            alert.showAndWait();
                            return;
                        }
                        else {
                            alert.setHeaderText("Log in failed");
                            alert.getDialogPane().setContent(new Label("Student data does not exist."));
                            alert.showAndWait();
                            return;
                        }
                    }

                    stage.show();
                    Stage loginStage = (Stage) button_login.getScene().getWindow();
                    loginStage.close();
                }
                else {
                    alert.setHeaderText("Log in failed");
                    alert.getDialogPane().setContent(new Label(
                            "The username or password is incorrect. Please try again."));
                    alert.showAndWait();
                }
            } catch (Exception e) {
                alert.setHeaderText("Server Error");
                alert.getDialogPane().setContent(new Label(
                        "Failed to connect to server. \n" +
                                "Please start the servers first and try again."));
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

}