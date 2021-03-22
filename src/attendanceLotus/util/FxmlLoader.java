package attendanceLotus.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import attendanceLotus.controller.admin.homeController;

import java.net.URL;

public class FxmlLoader {

    private Pane view;

    public Pane getPage(String userType, String fileName) {

        try {
            URL fileUrl = homeController.class.getResource("/attendanceLotus/view/" + userType + "/" + fileName + ".fxml");
            if(fileUrl == null)
                throw new java.io.FileNotFoundException("FXML file can't be found");

            view = new FXMLLoader().load(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
