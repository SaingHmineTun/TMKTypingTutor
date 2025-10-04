package it.saimao.tmk_typing_tutor.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CertificateController {

    @FXML
    private Label lblUsername;
    @FXML
    private Label lblLevel;
    @FXML
    private Label lblAwpm;
    @FXML
    private Label lblDate;
    @FXML
    private Button btnClose;

    public void initData(String username, String levelName) {
        lblUsername.setText(username);
        lblLevel.setText(levelName);
        // Set current date
        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
    }
    
    public void initData(String username, String levelName, String averageWpm) {
        lblUsername.setText(username);
        lblLevel.setText(levelName);
        if (lblAwpm != null) {
            lblAwpm.setText(averageWpm + " WPM");
        }
        // Set current date
        if (lblDate != null) {
            lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        }
    }

    @FXML
    private void close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}