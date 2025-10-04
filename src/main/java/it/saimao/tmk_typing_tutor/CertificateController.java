package it.saimao.tmk_typing_tutor;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CertificateController {

    @FXML
    private Label lblUsername;
    @FXML
    private Label lblLevel;
    @FXML
    private Button btnClose;

    public void initData(String username, String levelName) {
        lblUsername.setText(username);
        lblLevel.setText(levelName);
    }

    @FXML
    private void close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
