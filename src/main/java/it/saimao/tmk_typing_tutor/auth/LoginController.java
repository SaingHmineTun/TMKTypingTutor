package it.saimao.tmk_typing_tutor.auth;

import it.saimao.tmk_typing_tutor.MainController;
import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.utils.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private ListView<User> lvUsers;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Label lbError, lblSelectedUser;
    @FXML
    private Button btnLogin;

    private User selectedUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load all users into the list view
        lvUsers.getItems().setAll(UserService.getAllUsers());

        // Add a listener to handle user selection
        lvUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedUser = newValue;
                lblSelectedUser.setText("Logging in as: " + selectedUser.getUsername());
                pfPassword.setVisible(true);
                btnLogin.setVisible(true);
                pfPassword.requestFocus();
            }
        });
    }

    @FXML
    private void login() {
        if (selectedUser == null) {
            lbError.setText("Please select a user from the list.");
            lbError.setVisible(true);
            return;
        }

        String password = pfPassword.getText();
        if (selectedUser.getPassword().equals(password)) {
            // Successful login, open the main window
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/main.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.setScene(scene);
                stage.setMaximized(true);

                // Pass the logged-in user and the primary stage to the MainController
                MainController mainController = fxmlLoader.getController();
                mainController.initData(selectedUser, stage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            lbError.setText("Invalid password!");
            lbError.setVisible(true);
        }
    }

    @FXML
    private void showRegister() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/register.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
