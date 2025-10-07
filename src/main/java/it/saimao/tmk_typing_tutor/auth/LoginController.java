package it.saimao.tmk_typing_tutor.auth;

import it.saimao.tmk_typing_tutor.controller.MainController;
import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.services.UserService;
import it.saimao.tmk_typing_tutor.utils.Toast;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

        // Customize the appearance of list cells
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
                            hbox.setSpacing(15);
                            hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                            Label userIcon = new Label("\uD83D\uDC64"); // Unicode user icon

                            Label usernameLabel = new Label(user.getUsername());

                            Region spacer = new Region();
                            HBox.setHgrow(spacer, Priority.ALWAYS);

                            Button deleteButton = new Button("❌");
                            deleteButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                            deleteButton.setOnAction(event -> {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Delete User");
                                alert.setHeaderText("Delete User: " + user.getUsername());
                                alert.setContentText("Are you sure you want to delete this user and all associated data? This action cannot be undone.");
                                
                                alert.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.OK) {
                                        UserService.deleteUser(user.getId());
                                        lvUsers.getItems().remove(user);
                                        if (lvUsers.getItems().isEmpty()) {
                                            pfPassword.setVisible(false);
                                            btnLogin.setVisible(false);
                                            lblSelectedUser.setText("");
                                        }
                                        Toast.showToast(btnLogin.getScene().getWindow(), "User deleted successfully!", 2000);
                                    }
                                });
                            });

                            hbox.getChildren().addAll(userIcon, usernameLabel, spacer, deleteButton);
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
        if (selectedUser.getPassword().equals(password)) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/main.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.hide();
                stage.setScene(scene);

                //Pass the logged-in user and the primary stage to the MainController
                MainController mainController = fxmlLoader.getController();
                mainController.initData(selectedUser, stage);

            } catch (IOException e) {
                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.show(); // Show the login window again
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