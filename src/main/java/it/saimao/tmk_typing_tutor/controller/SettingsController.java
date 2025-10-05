package it.saimao.tmk_typing_tutor.controller;

import it.saimao.tmk_typing_tutor.model.Profile;
import it.saimao.tmk_typing_tutor.model.Theme;
import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.services.LessonProgressService;
import it.saimao.tmk_typing_tutor.services.ProgressService;
import it.saimao.tmk_typing_tutor.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    // Navigation Buttons
    @FXML
    private Button btnUserProfile;
    @FXML
    private Button btnUserProgress;
    @FXML
    private Button btnTheme;
    @FXML
    private Button btnBackgroundMusic;
    @FXML
    private Button btnKeyErrorSound;
    @FXML
    private Button btnCertificate;
    @FXML
    private Button btnCredits;
    @FXML
    private Button btnAbout;
    @FXML
    private Button btnClose;
    // Content Area
    @FXML
    private AnchorPane apContent;

    // UserProfile Tab
    private Label lblUsername;
    private Label lblDisplayName;
    private Button btnChangeDisplayName;

    // User Progress Tab
    private TableView<Profile> tvProgress;
    private TableColumn<Profile, String> tcLevel;
    private TableColumn<Profile, String> tcProgress;
    private TableColumn<Profile, Button> tcCertificate;
    private TableColumn<Profile, Button> tcDetails;

    // Theme Tab
    private ListView<Theme> lvThemes;
    private Button btnApplyTheme;

    // Background Music Tab
    private ListView<String> lvMusic;
    private Button btnPlayMusic;
    private Button btnStopMusic;

    // Key Error Sound Tab
    private ListView<String> lvErrorSounds;
    private Button btnPlayErrorSound;

    // Certificate Tab
    private Label lblCertificateStatus;
    private Button btnViewCertificate;

    private User user;
    private MainController mainController;
    private final int[] lessonsPerLevel = {9, 82, 82, 130};
    private MediaPlayer mediaPlayer;
    private MediaPlayer errorSoundPlayer;
    private String selectedErrorSound = "error1.mp3";
    private String selectedBackgroundMusic = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
// Initially load the first tab (User Profile) after a short delay
        // to ensure apContent is properly injected
        javafx.concurrent.Task<Void> task = new javafx.concurrent.Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Wait for JavaFX to initialize
                Thread.sleep(100);
                return null;
            }

            @Override
            protected void succeeded() {
                try {
                    if (apContent != null) {
                        loadTabContent("/layout/setting_user_profile.fxml");
                        // Highlightthe first button as selected
                        if (btnUserProfile != null) {
                            btnUserProfile.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-border-color: #007acc; -fx-border-radius: 5;");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(task).start();
    }

    public void initData(User user, MainController mainController) {
        this.user = user;
        this.mainController = mainController;
    }

    private void loadTabContent(String fxmlPath) throws IOException {
        if (apContent == null) return;

        apContent.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        AnchorPane tabContent = loader.load();
        apContent.getChildren().add(tabContent);

        // Initialize the content based on the tab
        switch (fxmlPath) {
            case "/layout/setting_user_profile.fxml":
                initUserProfileTab(tabContent);
                break;
            case "/layout/setting_user_progress.fxml":
                initUserProgressTab(tabContent);
                break;
            case "/layout/setting_theme.fxml":
                initThemeTab(tabContent);
                break;
            case "/layout/setting_background_music.fxml":
                initBackgroundMusicTab(tabContent);
                break;
            case "/layout/setting_key_error_sound.fxml":
                initKeyErrorSoundTab(tabContent);
                break;
            case "/layout/setting_certificate.fxml":
                initCertificateTab(tabContent);
                break;
        }
    }

    private void initUserProfileTab(AnchorPane tabContent) {
        Node node = tabContent.lookup("#lblUsername");
        if (node instanceof Label) {
            lblUsername = (Label) node;
        }

        node = tabContent.lookup("#lblDisplayName");
        if (node instanceof Label) {
            lblDisplayName = (Label) node;
        }

        node = tabContent.lookup("#btnChangeDisplayName");
        if (node instanceof Button) {
            btnChangeDisplayName = (Button) node;
        }

        // Load user profile data
        if (lblUsername != null) {
            lblUsername.setText("Username: " + user.getUsername());
        }
        if (lblDisplayName != null) {
            lblDisplayName.setText("Display Name: " + user.getDisplayName());
        }
    }

    private void initUserProgressTab(AnchorPane tabContent) {
        Node node = tabContent.lookup("#tvProgress");
        if (node instanceof TableView) {
            tvProgress = (TableView<Profile>) node;
        }

// Initializetable columns only if elements are found
        if (tvProgress != null) {
            // Get the columns from the table view
            if (tvProgress.getColumns().size() >= 4) {
                tcLevel = (TableColumn<Profile, String>) tvProgress.getColumns().get(0);
                tcProgress = (TableColumn<Profile, String>) tvProgress.getColumns().get(1);
                tcCertificate = (TableColumn<Profile, Button>) tvProgress.getColumns().get(2);
                tcDetails = (TableColumn<Profile, Button>) tvProgress.getColumns().get(3);

                tcLevel.setCellValueFactory(new PropertyValueFactory<>("levelName"));
                tcProgress.setCellValueFactory(new PropertyValueFactory<>("progress"));
                tcCertificate.setCellValueFactory(new PropertyValueFactory<>("certificateButton"));
                tcDetails.setCellValueFactory(new PropertyValueFactory<>("detailsButton"));

                //Apply specific styles to columns
                tcLevel.getStyleClass().add("text-column");
                tcProgress.getStyleClass().add("text-column");
                tcCertificate.getStyleClass().add("button-column");
                tcDetails.getStyleClass().add("button-column");
            }

            loadProgress();
        }
    }

    private void initThemeTab(AnchorPane tabContent) {
        Node node = tabContent.lookup("#lvThemes");
        if (node instanceof ListView) {
            lvThemes = (ListView<Theme>) node;
        }

        node = tabContent.lookup("#btnApplyTheme");
        if (node instanceof Button) {
            btnApplyTheme = (Button) node;
        }

        // Initialize themes list
        if (lvThemes != null) {
            lvThemes.setItems(FXCollections.observableArrayList(Theme.values()));
        }
    }

    private void initBackgroundMusicTab(AnchorPane tabContent) {
        Node node = tabContent.lookup("#lvMusic");
        if (node instanceof ListView) {
            lvMusic = (ListView<String>) node;
        }

        node = tabContent.lookup("#btnPlayMusic");
        if (node instanceof Button) {
            btnPlayMusic = (Button) node;
        }

        node = tabContent.lookup("#btnStopMusic");
        if (node instanceof Button) {
            btnStopMusic = (Button) node;
        }

        // Initialize music list withuser-friendly names
        ObservableList<String> musicList = FXCollections.observableArrayList(
                "Classroom Timer with Relaxing WAVES Music",
                "Soft Peaceful Music Timer",
                "Timer with Relaxing Music and Alarm",
                "Nature Background Sound",
                "Peaceful Ambient Stress Relief",
                "CalmNature Sounds",
                "Rain and Thunderstorm Sounds",
                "Waterfall Relaxing Nature Sounds",
                "Relaxing Music for Stress Relief",
                "Nature Sounds for Focus"
        );

        // Map user-friendly names to actual file names
        Map<String, String> musicNameToFileMap = new HashMap<>();
        musicNameToFileMap.put("Classroom Timer with Relaxing WAVES Music", "bgsound1.mp3");
        musicNameToFileMap.put("Soft Peaceful Music Timer", "bgsound2.m4a");
        musicNameToFileMap.put("Timer with Relaxing Music and Alarm", "bgsound3.m4a");
        musicNameToFileMap.put("Nature Background Sound", "1-MinuteNature Background Sound(M4A_128K).m4a");
        musicNameToFileMap.put("Peaceful Ambient Stress Relief", "bgsound8.m4a");
        musicNameToFileMap.put("Calm Nature Sounds", "bgsound6.m4a");
        musicNameToFileMap.put("Rainand Thunderstorm Sounds", "bgsound5.m4a");
        musicNameToFileMap.put("Waterfall Relaxing Nature Sounds", "bgsound10.m4a");
        musicNameToFileMap.put("Relaxing Music for Stress Relief", "bgsound4.mp3");
        musicNameToFileMap.put("Nature Sounds for Focus", "bgsound9.mp3");

        if (lvMusic != null) {
            lvMusic.setItems(musicList);
            // Add listener to automatically play when selection changes
            lvMusic.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    String fileName = musicNameToFileMap.get(newValue);
                    if (fileName != null) {
                        playMusicFile(fileName);
                    }
                }
            });
        }
    }

    private void initKeyErrorSoundTab(AnchorPane tabContent) {
        Node node = tabContent.lookup("#lvErrorSounds");
        if (node instanceof ListView) {
            lvErrorSounds = (ListView<String>) node;
        }

        node = tabContent.lookup("#btnPlayErrorSound");
        if (node instanceof Button) {
            btnPlayErrorSound = (Button) node;
        }

        // Initialize errorsounds list
        ObservableList<String> errorSoundsList = FXCollections.observableArrayList(
                "Error Sound 1 (error1.mp3)",
                "Error Sound 2 (error2.mp3)",
                "Error Sound 3 (error3.mp3)",
                "Error Sound 4 (error4.mp3)",
                "Error Sound 5 (error5.mp3)",
                "Error Sound 6 (error6.mp3)",
                "Error Sound 7 (error7.mp3)"
        );
        if (lvErrorSounds != null) {
            lvErrorSounds.setItems(errorSoundsList);
        }
    }

    private void initCertificateTab(AnchorPane tabContent) {
        Node node = tabContent.lookup("#lblCertificateStatus");
        if (node instanceof Label) {
            lblCertificateStatus = (Label) node;
        }

        node = tabContent.lookup("#btnViewCertificate");
        if (node instanceof Button) {
            btnViewCertificate = (Button) node;
        }

        // Initially disable certificate button
        if (btnViewCertificate != null) {
            btnViewCertificate.setDisable(true);
        }

        // Check if user has completed all tests
        checkCertificateStatus();
    }

    private void loadProgress() {
        if (tvProgress == null || user == null) return;

        ObservableList<Profile> profiles = FXCollections.observableArrayList();
        for (int i = 0; i < 4; i++) { // Assuming 4 levels
            int completed = ProgressService.getCompletedLessonCount(user.getId(), i);
            int total = lessonsPerLevel[i];
            Profile profile = new Profile("Level " + (i + 1), completed + " / " + total);
            profile.setLevelIndex(i); // Store level index for later use

            if (completed >= total) {
                // For completed levels, enable both Generate and Details buttons
                profile.getCertificateButton().setDisable(false);
                profile.getCertificateButton().setText("Generate");
                final int levelIndex = i;
                profile.getCertificateButton().setOnAction(event -> showCertificate(levelIndex));

                profile.getDetailsButton().setDisable(false);
                profile.getDetailsButton().setOnAction(event -> showDetailedProgress(levelIndex));
            } else if (completed > 0) {
                // For partially completed levels, enable only Details button
                profile.getDetailsButton().setText("Details");
                profile.getDetailsButton().setDisable(false);
                final int levelIndex = i;
                profile.getDetailsButton().setOnAction(event -> showDetailedProgress(levelIndex));
            }
            profiles.add(profile);
        }
        tvProgress.setItems(profiles);
    }

    private void checkCertificateStatus() {
        if (user == null) return;

        boolean allLevelsCompleted = true;
        for (int i = 0; i < 4; i++) {
            int completed = ProgressService.getCompletedLessonCount(user.getId(), i);
            int total = lessonsPerLevel[i];
            if (completed < total) {
                allLevelsCompleted = false;
                break;
            }
        }

        if (allLevelsCompleted && lblCertificateStatus != null && btnViewCertificate != null) {
            lblCertificateStatus.setText("Congratulations! You have completed all tests.");
            btnViewCertificate.setDisable(false);
        }
    }

    // Navigation methods
    @FXML
    private void onUserProfileTab() throws IOException {
        if (apContent != null) {
            loadTabContent("/layout/setting_user_profile.fxml");
            // Update button styles to show selection
            updateButtonStyles(btnUserProfile);
        }
    }

    @FXML
    private void onUserProgressTab() throws IOException {
        if (apContent != null) {
            loadTabContent("/layout/setting_user_progress.fxml");
            // Update button styles to show selection
            updateButtonStyles(btnUserProgress);
        }
    }

    @FXML
    private void onThemeTab() throws IOException {
        if (apContent != null) {
            loadTabContent("/layout/setting_theme.fxml");
            // Update button stylesto show selection
            updateButtonStyles(btnTheme);
        }
    }

    @FXML
    private void onBackgroundMusicTab() throws IOException {
        if (apContent != null) {
            loadTabContent("/layout/setting_background_music.fxml");
            // Update button styles to show selection
            updateButtonStyles(btnBackgroundMusic);
        }
    }

    @FXML
    private void onKeyErrorSoundTab() throws IOException {
        if (apContent != null) {
            loadTabContent("/layout/setting_key_error_sound.fxml");
            // Update button styles to show selection
            updateButtonStyles(btnKeyErrorSound);
        }
    }

    @FXML
    private void onCertificateTab() throws IOException {
        if (apContent != null) {
            loadTabContent("/layout/setting_certificate.fxml");
            // Update button styles to show selection
            updateButtonStyles(btnCertificate);
        }
    }

    @FXML
    private void onCreditsTab() throws IOException {
        if (apContent != null) {
            loadTabContent("/layout/setting_credits.fxml");
            // Update button styles to show selection
            updateButtonStyles(btnCredits);
        }
    }

    @FXML
    private void onAboutTab() throws IOException {
        if (apContent != null) {
            loadTabContent("/layout/setting_about.fxml");
            // Update button styles to show selection
            updateButtonStyles(btnAbout);
        }
    }

    // Helper method to update button styles
    private void updateButtonStyles(Button selectedButton) {
        // Reset allbuttons to default style
        Button[] allButtons = {btnUserProfile, btnUserProgress, btnTheme, btnBackgroundMusic,
                btnKeyErrorSound, btnCertificate, btnCredits, btnAbout};

        for (Button button : allButtons) {
            if (button != null) {
                button.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5;");
            }
        }

        // Highlight selected button
        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-border-color: #007acc; -fx-border-radius: 5;");
        }
    }

    @FXML
    private void onChangeDisplayName() {
        if (user == null) return;

        TextInputDialog dialog = new TextInputDialog(user.getDisplayName());
        dialog.setTitle("Change Display Name");
        dialog.setHeaderText("Enter your new display name:");
        dialog.setContentText("Display Name:");

        // Apply theme to dialog
        Theme theme = Theme.fromIndex(user.getTheme());
        String stylesheet = getClass().getResource("/css/" + theme.id() + ".css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(stylesheet);

        dialog.showAndWait().ifPresent(newDisplayName -> {
            user.setDisplayName(newDisplayName);
            UserService.updateUser(user);
            if (lblDisplayName != null) {
                lblDisplayName.setText("Display Name: " + newDisplayName);
            }

            // Update display name in main controller if needed
            if (mainController != null) {
                // Refresh UI if needed
            }
        });
    }

    @FXML
    private void onApplyTheme() {
        if (lvThemes == null) return;

        Theme selectedTheme = lvThemes.getSelectionModel().getSelectedItem();
        if (selectedTheme != null) {
            user.setTheme(selectedTheme.ordinal());
            UserService.updateUser(user);

            // Apply theme to main window
            if (mainController != null) {
                // Select the theme in main controller which will automatically trigger the listener
                mainController.getCbTheme().getSelectionModel().select(selectedTheme.ordinal());
            }

            // Also apply theme to current settings window
            String stylesheet = getClass().getResource("/css/" + selectedTheme.id() + ".css").toExternalForm();
            btnApplyTheme.getScene().getRoot().getStylesheets().clear();
            btnApplyTheme.getScene().getRoot().getStylesheets().add(stylesheet);

            // Show confirmation
            showAlert("Theme updated successfully!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Please select a theme first!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void onPlayMusic() {
        playSelectedMusic();
    }

    private void playSelectedMusic() {
        if (lvMusic == null) return;

        String selectedMusic = lvMusic.getSelectionModel().getSelectedItem();
        if (selectedMusic != null) {
            // Mapuser-friendly names to actual file names
            Map<String, String> musicNameToFileMap = new HashMap<>();
            musicNameToFileMap.put("Classroom Timer with Relaxing WAVES Music", "bgsound1.mp3");
            musicNameToFileMap.put("Soft Peaceful Music Timer", "bgsound2.m4a");
            musicNameToFileMap.put("Timer with Relaxing Music and Alarm", "bgsound3.m4a");
            musicNameToFileMap.put("Nature Background Sound", "bgsound7.m4a");
            musicNameToFileMap.put("Peaceful Ambient Stress Relief", "bgsound8.m4a");
            musicNameToFileMap.put("Calm Nature Sounds", "bgsound6.m4a");
            musicNameToFileMap.put("Rain and Thunderstorm Sounds", "bgsound5.m4a");
            musicNameToFileMap.put("Waterfall Relaxing Nature Sounds", "bgsound10.m4a");
            musicNameToFileMap.put("Relaxing Music for Stress Relief", "bgsound4.mp3");
            musicNameToFileMap.put("Nature Sounds for Focus", "bgsound9.mp3");

            String fileName = musicNameToFileMap.get(selectedMusic);
            if (fileName != null) {
                playMusicFile(fileName);
            }
        }
    }

    private void playMusicFile(String fileName) {
        // Stop any currently playing music
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        // Play the selected music
        String musicPath = "/audio/" + fileName;
        try {
            URL musicURL = getClass().getResource(musicPath);
            if (musicURL != null) {
                mediaPlayer = new MediaPlayer(new Media(musicURL.toString()));
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
                mediaPlayer.play();

                // Also send to main controller to play in background
                if (mainController != null) {
                    mainController.playBackgroundMusic(fileName);
                }

                selectedBackgroundMusic = fileName;
            } else {
                showAlert("Could not find music file: " + fileName, Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error playing music: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void onStopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            showAlert("Music stopped", Alert.AlertType.INFORMATION);
        }

        // Also stop in main controller
        if (mainController != null) {
            mainController.stopBackgroundMusic();
        }

        selectedBackgroundMusic = null;
    }

    @FXML
    private void onPlayErrorSound() {
        if (lvErrorSounds == null) return;

        String selectedSound = lvErrorSounds.getSelectionModel().getSelectedItem();
        if (selectedSound != null) {
            // Extract the actual filename from the display text
            String[] parts = selectedSound.split("\\(");
            if (parts.length > 1) {
                selectedErrorSound = parts[1].replace(")", "");

                // Stop any currently playing sound
                if (errorSoundPlayer != null) {
                    errorSoundPlayer.stop();
                }

                // Play the selected error sound
                String soundPath = "/audio/" + selectedErrorSound;
                try {
                    URL soundURL = getClass().getResource(soundPath);
                    if (soundURL != null) {
                        errorSoundPlayer = new MediaPlayer(new Media(soundURL.toString()));
                        errorSoundPlayer.play();
                        showAlert("Playing error sound: " + selectedSound, Alert.AlertType.INFORMATION);
                    }
                } catch (Exception e) {
                    showAlert("Error playing sound: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Please select an error sound first!", Alert.AlertType.WARNING);
        }
    }

    public String getSelectedErrorSound() {
        return selectedErrorSound;
    }

    public String getSelectedBackgroundMusic() {
        return selectedBackgroundMusic;
    }

    @FXML
    private void onViewCertificate() {
        if (user == null) return;

        // Check if all levels are completed
        boolean allLevelsCompleted = true;
        for (int i = 0; i < 4; i++) {
            int completed = ProgressService.getCompletedLessonCount(user.getId(), i);
            int total = lessonsPerLevel[i];
            if (completed < total) {
                allLevelsCompleted = false;
                break;
            }
        }

        if (allLevelsCompleted) {
            showMainCertificate();
        } else {
            showAlert("You need to complete all tests to view the certificate!", Alert.AlertType.WARNING);
        }
    }

    private void showMainCertificate() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/certificate.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnClose.getScene().getWindow());
            Scene scene = new Scene(loader.load());

            // Apply certificate specific styles
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/certificate_style.css").toExternalForm());

            stage.setScene(scene);

            CertificateController controller = loader.getController();

            // Calculate average WPM across all levels
            double totalWpm = 0;
            int levelCount = 0;
            for (int i = 0; i < 4; i++) {
                double avgWpm = LessonProgressService.getAverageWpmForLevel(user.getId(), i);
                if (avgWpm > 0) {
                    totalWpm += avgWpm;
                    levelCount++;
                }
            }

            double overallAvgWpm = levelCount > 0 ? totalWpm / levelCount : 0;
            DecimalFormat df = new DecimalFormat("#.##");

            controller.initData(user.getUsername(), "Master Typist", df.format(overallAvgWpm));

            stage.show();
        } catch (IOException e) {
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

            // Apply certificate specific styles
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/certificate_style.css").toExternalForm());

            stage.setScene(scene);

            CertificateController controller = loader.getController();

            // Calculate average WPM forthelevel
            double averageWpm = LessonProgressService.getAverageWpmForLevel(user.getId(), levelIndex);
            DecimalFormat df = new DecimalFormat("#.##");

            controller.initData(user.getUsername(), "Level " + (levelIndex + 1), df.format(averageWpm));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            controller.initData(user.getId(), levelIndex);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onClose() {
        if (btnClose != null) {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Settings");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Apply theme to alert
        Theme theme = Theme.fromIndex(user.getTheme());
        String stylesheet = getClass().getResource("/css/" + theme.id() + ".css").toExternalForm();
        alert.getDialogPane().getStylesheets().add(stylesheet);

        alert.showAndWait();
    }
}