package it.saimao.tmk_typing_tutor.controller;

import it.saimao.tmk_typing_tutor.model.Profile;
import it.saimao.tmk_typing_tutor.model.Theme;
import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.services.LessonProgressService;
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
import java.text.DecimalFormat;
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
    private TableColumn<Profile, Button> tcDetails; // New column for details button
    @FXML
    private TableColumn<Profile, Button> tcReset; // New column for reset button
    @FXML
    private Button btnResetAll; // New button to reset all levels

    // Password change fields are now in dialog, not directly in profile view
    private PasswordField pfOldPassword;
    private PasswordField pfNewPassword;
    private PasswordField pfConfirmPassword;
    private Label lblPasswordStatus;

    @FXML
    private Button btnChangePasswordTop;
    @FXML
    private Button btnClose;

    private User user;
    private final int[] lessonsPerLevel = {9, 82, 82, 130}; // Lessons per level (Level 1: 9, Level 2: 82, Level 3: 82, Level 4: 130)
    private MainController mainController; // Reference to main controller

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set styling for the TableView
        tvProgress.getStyleClass().add("progress-table");

        tcLevel.setCellValueFactory(new PropertyValueFactory<>("levelName"));
        tcProgress.setCellValueFactory(new PropertyValueFactory<>("progress"));
        tcCertificate.setCellValueFactory(new PropertyValueFactory<>("certificateButton"));
        tcDetails.setCellValueFactory(new PropertyValueFactory<>("detailsButton")); // Set up details column
        tcReset.setCellValueFactory(new PropertyValueFactory<>("resetButton")); // Set up reset column

        // Apply specific styles to columns
        tcLevel.getStyleClass().add("text-column");
        tcProgress.getStyleClass().add("text-column");
        tcCertificate.getStyleClass().add("button-column");
        tcDetails.getStyleClass().add("button-column");
        tcReset.getStyleClass().add("button-column");
    }

    public void initData(User user) {
        this.user = user;
        loadProgress();
        // Apply user's selected theme
        applyTheme();
    }

    // Methodtoset the main controller reference
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void applyTheme() {
        Theme theme = Theme.fromIndex(user.getTheme());
        String stylesheet = getClass().getResource("/css/" + theme.id() + ".css").toExternalForm();

        // Get the scene and apply theme
        if (btnClose.getScene() != null) {
            Scene scene = btnClose.getScene();
            // Remove the default light theme
            scene.getStylesheets().clear();
            // Apply the user's selected theme
            scene.getStylesheets().add(stylesheet);
            // Add list styles
            scene.getStylesheets().add(getClass().getResource("/css/list_styles.css").toExternalForm());
        }
    }

    private void loadProgress() {
        ObservableList<Profile> profiles = FXCollections.observableArrayList();
        for (int i = 0; i < 4; i++) { // Assuming 4 levels
            // Use LessonProgressService instead of ProgressService since we removedthe progress table
            int completed = LessonProgressService.getCompletedLessonCount(user.getId(), i);
            int total = lessonsPerLevel[i];
            Profile profile = new Profile("Level " + (i + 1), completed + " / " + total);
            profile.setLevelIndex(i); // Store levelindex for later use// Set up reset button for each level
            final int levelIndex = i;
            profile.getResetButton().setOnAction(event -> resetLevelProgress(levelIndex));

            if (completed >= total) {
                // For completed levels, enable both Generate and Details buttons
                profile.getCertificateButton().setDisable(false);
                profile.getCertificateButton().setText("Generate");
                profile.getCertificateButton().setOnAction(event -> showCertificate(levelIndex));
                profile.getDetailsButton().setDisable(false);
                profile.getDetailsButton().setOnAction(event -> showDetailedProgress(levelIndex));
            } else if (completed > 0) {
                // For partially completed levels, enable only Details button
                profile.getDetailsButton().setText("Details");
                profile.getDetailsButton().setDisable(false);
                profile.getDetailsButton().setOnAction(event -> showDetailedProgress(levelIndex));
            }
            profiles.add(profile);
        }
        tvProgress.setItems(profiles);
    }

    private void showDetailedProgress(int levelIndex) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/lesson_progress.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnClose.getScene().getWindow());
            Scene scene = new Scene(loader.load());
// Apply the current theme to the lesson progress window
            Theme theme = Theme.fromIndex(user.getTheme());
            String stylesheet = getClass().getResource("/css/" + theme.id() + ".css").toExternalForm();
            scene.getStylesheets().add(stylesheet);

            stage.setScene(scene);

            LessonProgressController controller = loader.getController();
            controller.initData(user, levelIndex);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showChangePasswordDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/change_password_dialog.fxml"));
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(loader.load());
            dialog.setTitle("Change Password");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(btnClose.getScene().getWindow());

            //Apply the current theme to the dialog
            Theme theme = Theme.fromIndex(user.getTheme());
            String stylesheet = getClass().getResource("/css/" + theme.id() + ".css").toExternalForm();
            dialog.getDialogPane().getStylesheets().add(stylesheet);

            // Get controller and pass user reference
            ChangePasswordController controller = loader.getController();
            controller.initData(user);

            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCertificate(int levelIndex) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/certificate.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnClose.getScene().getWindow());
            Scene scene = new Scene(loader.load());

// Onlyapply certificate specific styles,not the user theme
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/certificate_style.css").toExternalForm());

            stage.setScene(scene);

            CertificateController controller = loader.getController();

            // Calculate average WPM for the level
            double averageWpm = LessonProgressService.getAverageWpmForLevel(user.getId(), levelIndex);
            DecimalFormat df = new DecimalFormat("#.##");

            controller.initData(user.getDisplayName(), "Level" + (levelIndex + 1), df.format(averageWpm));

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

    @FXML
    private void resetAllProgress() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Reset All Progress");
        alert.setHeaderText("Reset All Progress");
        alert.setContentText("Are you sureyou want to reset all progress for all levels? Thisactioncannot be undone.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Reset progress for all levels (0-3) using LessonProgressService since we removed the progress table
                for (int i = 0; i < 4; i++) {
                    resetLevelProgress(i, false); //False means don't show individual alerts
                }
                // Reload the progress display
                loadProgress();
                // Show success message
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("All Progress Reset");
                successAlert.setContentText("All progress for all levels has been reset successfully.");
                successAlert.showAndWait();
            }
        });
    }

    private void resetLevelProgress(int levelIndex) {
        resetLevelProgress(levelIndex, true);
    }

    private void resetLevelProgress(int levelIndex, boolean showAlert) {
//Delete progress for this level using LessonProgressService since we removed the progress table
        LessonProgressService.deleteLessonProgressForLevel(user.getId(), levelIndex);

        if (showAlert) {
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Progress Reset");
            alert.setContentText("Progress for " + "Level " + (levelIndex + 1) + " has been reset successfully.");
            alert.showAndWait();
// Reload the progress display
            loadProgress();
        }
    }
}