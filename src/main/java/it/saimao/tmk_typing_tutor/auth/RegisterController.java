package it.saimao.tmk_typing_tutor.auth;

import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.utils.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private PasswordField pfConfirmPassword;
    @FXML
    private Label lbError;

    @FXML
    private void register() {
        String username = tfUsername.getText();
        String password = pfPassword.getText();
        String confirmPassword = pfConfirmPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            lbError.setText("Username and password cannot be empty.");
            lbError.setVisible(true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            lbError.setText("Passwords do not match!");
            lbError.setVisible(true);
            return;
        }

        if (UserService.findByUsername(username) != null) {
            lbError.setText("Username already exists!");
            lbError.setVisible(true);
            return;
        }

        UserService.saveUser(new User(username, password));
        showLogin();
    }

    @FXML
    private void showLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) tfUsername.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
