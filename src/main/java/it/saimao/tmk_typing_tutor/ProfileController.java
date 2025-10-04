package it.saimao.tmk_typing_tutor;

import it.saimao.tmk_typing_tutor.model.Profile;
import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.utils.ProgressService;
import it.saimao.tmk_typing_tutor.utils.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private TableView<Profile> tvProgress;
    @FXML
    private TableColumn<Profile, String> tcLevel;
    @FXML
    private TableColumn<Profile, String> tcProgress;
    @FXML
    private TableColumn<Profile, Button> tcCertificate;
    @FXML
    private PasswordField pfOldPassword;
    @FXML
    private PasswordField pfNewPassword;
    @FXML
    private PasswordField pfConfirmPassword;
    @FXML
    private Button btnChangePassword;
    @FXML
    private Label lblPasswordStatus;
    @FXML
    private Button btnClose;

    private User user;
    private final int[] lessonsPerLevel = {15, 10, 10, 10}; // Lessons per level

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcLevel.setCellValueFactory(new PropertyValueFactory<>("levelName"));
        tcProgress.setCellValueFactory(new PropertyValueFactory<>("progress"));
        tcCertificate.setCellValueFactory(new PropertyValueFactory<>("certificateButton"));
    }

    public void initData(User user) {
        this.user = user;
        loadProgress();
    }

    private void loadProgress() {
        ObservableList<Profile> profiles = FXCollections.observableArrayList();
        for (int i = 0; i < 4; i++) { // Assuming 4 levels
            int completed = ProgressService.getCompletedLessonCount(user.getId(), i);
            int total = lessonsPerLevel[i];
            Profile profile = new Profile("Level " + (i + 1), completed + " / " + total);
            if (completed >= total) {
                profile.getCertificateButton().setDisable(false);
                final int levelIndex = i;
                profile.getCertificateButton().setOnAction(event -> showCertificate(levelIndex));
            }
            profiles.add(profile);
        }
        tvProgress.setItems(profiles);
    }

    @FXML
    private void changePassword() {
        String oldPassword = pfOldPassword.getText();
        String newPassword = pfNewPassword.getText();
        String confirmPassword = pfConfirmPassword.getText();

        if (!user.getPassword().equals(oldPassword)) {
            lblPasswordStatus.setText("Incorrect old password!");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            lblPasswordStatus.setText("New passwords do not match!");
            return;
        }
        if (newPassword.isEmpty()) {
            lblPasswordStatus.setText("Password cannot be empty!");
            return;
        }

        user.setPassword(newPassword);
        UserService.updateUser(user); // This needs a method to update password
        lblPasswordStatus.setText("Password changed successfully!");
        pfOldPassword.clear();
        pfNewPassword.clear();
        pfConfirmPassword.clear();
    }

    private void showCertificate(int levelIndex) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/certificate.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnClose.getScene().getWindow());
            stage.setScene(new Scene(loader.load()));

            CertificateController controller = loader.getController();
            controller.initData(user.getUsername(), "Level " + (levelIndex + 1));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
