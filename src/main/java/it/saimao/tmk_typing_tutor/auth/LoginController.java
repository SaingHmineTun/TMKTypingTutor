package it.saimao.tmk_typing_tutor.auth;

import it.saimao.tmk_typing_tutor.controller.MainController;
import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;

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
        
        // Customize the appearanceof list cells
        lvUsers.setCellFactory(new Callback<>() {
            @Override
            public ListCell<User> call(ListView<User> listView) {
                return new ListCell<User>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
if (empty || user == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox();
                            hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                            hbox.getStyleClass().add("user-list-cell");

                            Label userIcon = new Label("\uD83D\uDC64"); // Unicode user icon
                            userIcon.getStyleClass().add("user-icon");

                            Label usernameLabel = new Label(user.getUsername());
                            usernameLabel.getStyleClass().add("username-label");

                            Region spacer = new Region();
                            HBox.setHgrow(spacer, Priority.ALWAYS);

                            hbox.getChildren().addAll(userIcon, usernameLabel, spacer);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });

        pfPassword.setOnAction(event -> login());

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

        if (!lvUsers.getItems().isEmpty())
            lvUsers.getSelectionModel().selectFirst();
    }

    @FXML
    private void login() {
        if (selectedUser == null) {
            lbError.setText("Please select a user from the list.");
            lbError.setVisible(true);
            return;
        }

        String password = pfPassword.getText();
        if (selectedUser.getPassword().equals(password)){
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
                
                // Play background music if user has selected one
                if (selectedUser.getBackgroundMusic() > 0) {
// Assuming there are multiplebackground music options
                    String backgroundMusicFile = "bgsound" + selectedUser.getBackgroundMusic() + ".mp3";
                    // Check if mp3 exists, otherwise try m4a
                    var musicURL = mainController.getClass().getResource("/audio/" + backgroundMusicFile);
                    if (musicURL ==null && backgroundMusicFile.endsWith(".mp3")) {
                        backgroundMusicFile = "bgsound" + selectedUser.getBackgroundMusic() + ".m4a";
                    }
                    mainController.playBackgroundMusic(backgroundMusicFile);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
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