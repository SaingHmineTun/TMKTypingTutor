package it.saimao.tmk_typing_tutor.auth;

import it.saimao.tmk_typing_tutor.MainController;
import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.utils.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label lbError;

    @FXML
    private void login() {
        String username = tfUsername.getText();
        String password = pfPassword.getText();

        User user = UserService.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            // Successful login, open the main window
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/main.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) tfUsername.getScene().getWindow();
                stage.setScene(scene);
                stage.setMaximized(true);

                // Pass the primary stage to the MainController
                MainController mainController = fxmlLoader.getController();
                mainController.setPrimaryStage(stage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            lbError.setText("Invalid username or password!");
            lbError.setVisible(true);
        }
    }

    @FXML
    private void showRegister() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/register.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) tfUsername.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
