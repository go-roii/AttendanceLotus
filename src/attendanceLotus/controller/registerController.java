package attendanceLotus.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class registerController {

    @FXML
    private Button button_logout;

    @FXML
    void logout(ActionEvent event) throws IOException {
        Parent homeScreen = FXMLLoader.load(getClass().getResource("/attendanceLotus/view/login.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(homeScreen));

        Stage loginStage = (Stage) button_logout.getScene().getWindow();
        loginStage.close();
        stage.show();

    }

}
