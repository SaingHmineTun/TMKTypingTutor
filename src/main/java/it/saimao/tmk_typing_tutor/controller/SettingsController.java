package it.saimao.tmk_typing_tutor.controller;

import it.saimao.tmk_typing_tutor.model.Profile;
import it.saimao.tmk_typing_tutor.model.Theme;
import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.services.LessonProgressService;
import it.saimao.tmk_typing_tutor.services.UserService;
import it.saimao.tmk_typing_tutor.utils.Toast;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import static it.saimao.tmk_typing_tutor.model.Data.*;


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
    private Button btnChangePassword;

    // UserProgressTab
    private TableView<Profile> tvProgress;
    private TableColumn<Profile, String> tcLevel;
    private TableColumn<Profile, String> tcProgress;
    private TableColumn<Profile, Button> tcCertificate;
    private TableColumn<Profile, Button> tcDetails;
    private TableColumn<Profile, Button> tcReset;

    // Theme Tab
    private ListView<Theme> lvThemes;

    // Background Music Tab
    private ListView<String> lvMusic;
    private Button btnStopMusic;

    // Key Error Sound Tab
    private ListView<String> lvErrorSounds;

    // Certificate Tab
    private Label lblCertificateStatus;
    private Button btnViewCertificate;

    private User user;
    private MainController mainController;
    private final int[] lessonsPerLevel = {9, 82, 82, 130};
    private static MediaPlayer mediaPlayer;
    private MediaPlayer errorSoundPlayer;
    private String selectedErrorSound = "error1.mp3";
    private String selectedBackgroundMusic = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
// Initially load the first tab (User Profile) after ashortdelay// to ensure apContent is properly injected
        javafx.concurrent.Task<Void> task = new javafx.concurrent.Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Wait forJavaFX to initializeThread.sleep(100);
                return null;
            }

            @Override
            protected void succeeded() {
                try {
                    if (apContent != null) {
                        loadTabContent("/layout/setting_user_profile.fxml");
                        // Highlight thefirst buttonas selected
                        updateButtonStyles(btnUserProfile);
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
            btnChangeDisplayName.setOnAction(event -> onChangeDisplayName());
        }

        node = tabContent.lookup("#btnChangePassword");
        if (node instanceof Button) {
            btnChangePassword = (Button) node;
            btnChangePassword.setOnAction(event -> onChangePassword());
        }

        // Loaduserprofiledata
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

        node = tabContent.lookup("#btnResetAll");
        if (node instanceof Button btn)
            btn.setOnAction(event -> resetAllProgress());

        // Initializetablecolumnsonly ifelementsarefound
        if (tvProgress != null) {
            // Get the columnsfromthetableview
            if (tvProgress.getColumns().size() >= 4) {
                tcLevel = (TableColumn<Profile, String>) tvProgress.getColumns().get(0);
                tcProgress = (TableColumn<Profile, String>) tvProgress.getColumns().get(1);
                tcCertificate = (TableColumn<Profile, Button>) tvProgress.getColumns().get(2);
                tcDetails = (TableColumn<Profile, Button>) tvProgress.getColumns().get(3);
                tcReset = (TableColumn<Profile, Button>) tvProgress.getColumns().get(4);

                tcLevel.setCellValueFactory(new PropertyValueFactory<>("levelName"));
                tcProgress.setCellValueFactory(new PropertyValueFactory<>("progress"));
                tcCertificate.setCellValueFactory(new PropertyValueFactory<>("certificateButton"));
                tcDetails.setCellValueFactory(new PropertyValueFactory<>("detailsButton"));
                tcReset.setCellValueFactory(new PropertyValueFactory<>("resetButton"));

                // Applyspecificstyles to columns
                tcLevel.getStyleClass().add("text-column");
                tcProgress.getStyleClass().add("text-column");
                tcCertificate.getStyleClass().add("button-column");
                tcDetails.getStyleClass().add("button-column");
                tcReset.getStyleClass().add("button-column");
            }

            loadProgress();
        }
    }


    private void resetAllProgress() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("ResetAll Progress");
        confirmAlert.setHeaderText("Reset All Progress");
        confirmAlert.setContentText("Are you sure you want toreset allprogress? This action cannot be undone.");

        // Apply theme to the alert dialog
        Theme theme = Theme.fromIndex(user.getTheme());
        String stylesheet = getClass().getResource("/css/" + theme.id() + ".css").toExternalForm();
        confirmAlert.getDialogPane().getStylesheets().add(stylesheet);

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Delete progress for all levels usingLessonProgressService since weremoved theprogress table
                LessonProgressService.deleteAllProgress(user.getId());

                // Reload progress to reflect changes
                loadProgress();

                // Showsuccess message
                Toast.showToast(mainController.getStage(), "All progress has been reset successfully.", 2000);
            }
        });
    }

    private void initThemeTab(AnchorPane tabContent) {
        Node node = tabContent.lookup("#lvThemes");
        if (node instanceof ListView) {
            lvThemes = (ListView<Theme>) node;
        }

        // Initialize themes list
        if (lvThemes != null) {
            lvThemes.setItems(FXCollections.observableArrayList(Theme.values()));

//Set custom cell factory to show theme name and select button
            lvThemes.setCellFactory(param -> new ListCell<Theme>() {
                @Override
                protected void updateItem(Theme item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(null);
                        Label nameLabel = new Label(item.displayName());
                        nameLabel.setMaxWidth(Double.MAX_VALUE);

                        Button selectButton = new Button("Select");

                        // Check if this is the currently selected theme
                        if (user != null && user.getTheme() == item.ordinal()) {
                            selectButton.setText("\u2713"); // Unicode checkmark
                            selectButton.setDisable(true);
                        }

                        selectButton.setOnAction(e -> {
                            // Apply theme immediately
                            user.setTheme(item.ordinal());
                            lvThemes.getSelectionModel().select(item);
                            UserService.updateUser(user);

                            // Apply theme to main window
                            if (mainController != null) {
                                mainController.getCbTheme().getSelectionModel().select(item.ordinal());
                            }

                            // Also apply themeto current settings window
                            String stylesheet = getClass().getResource("/css/" + item.id() + ".css").toExternalForm();
                            if (getScene() != null) {
                                getScene().getRoot().getStylesheets().clear();
                                getScene().getRoot().getStylesheets().add(stylesheet);
                            }

                            lvThemes.refresh();

                            Toast.showToast(mainController.getStage(), "Theme updated successfully!", 2000);
                        });

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);

                        HBox hbox = new HBox(nameLabel, spacer, selectButton);
                        hbox.setSpacing(10);
                        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                        setGraphic(hbox);
                    }
                }

            });

            //Pre-select the current error sound
            if (user != null && user.getTheme() < Theme.values().length) {
                lvThemes.getSelectionModel().select(user.getTheme());
            }
        }
    }

    private void initBackgroundMusicTab(AnchorPane tabContent) {
        Node node = tabContent.lookup("#lvMusic");
        if (node instanceof ListView) {
            lvMusic = (ListView<String>) node;
        }

        //Initialize music list with user-friendly names
        if (lvMusic != null) {
            lvMusic.setItems(musicList);

            // Set custom cell factory
            lvMusic.setCellFactory(param -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(null);

                        Label nameLabel = new Label(item);
                        nameLabel.setMaxWidth(Double.MAX_VALUE);

                        Button selectButton = new Button("Select");

                        // Check if this is the currently selected music
                        if (user != null && musicNameToFileMap.get(item) != null && musicNameToFileMap.get(item).equals(user.getBackgroundMusic() > 0 ? "bgsound" + user.getBackgroundMusic() + ".mp3" : "")) {
                            selectButton.setText("\u2713"); //Unicode checkmark
                            selectButton.setDisable(true);
                        }

                        selectButton.setOnAction(e -> {
                            String fileName = musicNameToFileMap.get(item);
                            lvMusic.getSelectionModel().select(item);
                            if (fileName != null && !fileName.isEmpty()) {
                                playMusicFile(fileName);
                                selectedBackgroundMusic = fileName;

                                // Save the selection to the database
                                if (user != null) {
                                    int musicIndex = musicList.indexOf(item);
                                    user.setBackgroundMusic(musicIndex);
                                    UserService.updateUser(user);

                                    // Also update in main controller
                                    if (mainController != null) {
                                        mainController.setSelectedBackgroundMusic(fileName);
                                    }
                                }

                                Toast.showToast(btnClose.getScene().getWindow(), "Selected: " + item, 2000);
                            } else {
                                //Stop any currently playing music
                                if (mediaPlayer != null) {
                                    mediaPlayer.stop();
                                }

                                // Also stop in main controller
                                if (mainController != null) {
                                    mainController.stopBackgroundMusic();
                                    mainController.setSelectedBackgroundMusic(null);
                                }

                                selectedBackgroundMusic = null;

                                // Save the selection to the database
                                if (user != null) {
                                    user.setBackgroundMusic(0);
                                    UserService.updateUser(user);
                                }

                                showAlert("No background music selected", Alert.AlertType.INFORMATION);
                            }

                            // Refresh the list to update button text
                            lvMusic.refresh();
                        });

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);

                        HBox hbox = new HBox(nameLabel, spacer, selectButton);
                        hbox.setSpacing(10);
                        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                        setGraphic(hbox);
                    }

                }
            });

            //Pre-select the current error sound
            if (user != null && user.getBackgroundMusic() < musicList.size()) {
                lvMusic.getSelectionModel().select(user.getBackgroundMusic());
            }
        }
    }

    private void initKeyErrorSoundTab(AnchorPane tabContent) {
        Node node = tabContent.lookup("#lvErrorSounds");
        if (node instanceof ListView) {
            lvErrorSounds = (ListView<String>) node;
        }

        // Initialize error sounds list with "NoError Sound" option
        ObservableList<String> errorSoundsList = FXCollections.observableArrayList("No ErrorSound", "Error Sound 1 (error1.mp3)", "Error Sound 2 (error2.mp3)", "Error Sound 3 (error3.mp3)", "Error Sound 4 (error4.mp3)", "Error Sound 5 (error5.mp3)", "Error Sound6(error6.mp3)", "Error Sound 7 (error7.mp3)");
        if (lvErrorSounds != null) {
            lvErrorSounds.setItems(errorSoundsList);

            // Set custom cell factory to show tick/select icons
            lvErrorSounds.setCellFactory(param -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(null);

                        //Create label with tick mark for selected item
                        Label nameLabel = new Label();

                        nameLabel.setText(item);


                        nameLabel.setMaxWidth(Double.MAX_VALUE);

                        Button selectButton = new Button("Select");
                        if (user != null) {
                            int currentIndex = lvErrorSounds.getItems().indexOf(item);
                            if (currentIndex == user.getErrorSound()) {
                                selectButton.setText("\u2713"); // Unicode checkmark
                                selectButton.setDisable(true);
                            }
                        }
                        selectButton.setOnAction(e -> {
                            // Find index of selected item
                            int selectedIndex = lvErrorSounds.getItems().indexOf(item);
                            lvErrorSounds.getSelectionModel().select(selectedIndex);

                            // Update user's error sound setting
                            user.setErrorSound(selectedIndex);
                            UserService.updateUser(user);

                            // Also update in main controller if needed
                            if (mainController != null) {
                                mainController.setSelectedErrorSound(selectedIndex, user);
                            }

                            // Play the sound if it's not "NoErrorSound"
                            if (selectedIndex > 0) {
                                String soundFileName = "error" + selectedIndex + ".mp3";

                                //Stop any currently playing sound
                                if (errorSoundPlayer != null) {
                                    errorSoundPlayer.stop();
                                }

                                // Play the selected error sound
                                String soundPath = "/audio/" + soundFileName;
                                try {
                                    URL soundURL = getClass().getResource(soundPath);
                                    if (soundURL != null) {
                                        errorSoundPlayer = new MediaPlayer(new Media(soundURL.toString()));
                                        errorSoundPlayer.play();
                                    }
                                } catch (Exception ex) {
                                    showAlert("Error playingsound: " + ex.getMessage(), Alert.AlertType.ERROR);
                                }
                            } else {
                                //When "No ErrorSound" is selected, just show a confirmation
                                showAlert("Error sound disabled successfully!", Alert.AlertType.INFORMATION);
                            }

                            // Refresh the list to show the tick mark on the correct item
                            lvErrorSounds.refresh();
                        });

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS);

                        HBox hbox = new HBox(nameLabel, spacer, selectButton);
                        hbox.setSpacing(10);
                        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                        setGraphic(hbox);
                    }
                }
            });

            //Pre-select the current error sound
            if (user != null && user.getErrorSound() < errorSoundsList.size()) {
                lvErrorSounds.getSelectionModel().select(user.getErrorSound());
            }
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
            btnViewCertificate.setOnAction(event -> onViewCertificate());
        }

        //Initiallydisablecertificatebutton
        if (btnViewCertificate != null) {
            btnViewCertificate.setDisable(true);
        }

        // Checkif userhas completedalltests
        checkCertificateStatus();
    }

    private void loadProgress() {
        if (tvProgress == null || user == null) return;

        ObservableList<Profile> profiles = FXCollections.observableArrayList();
        for (int i = 0; i < 4; i++) {// Assuming 4 levels
            int completed = LessonProgressService.getCompletedLessonCount(user.getId(), i);
            int total = lessonsPerLevel[i];
            Profile profile = new Profile("Level " + (i + 1), completed + "/" + total);
            profile.setLevelIndex(i);// Store level indexforlateruse
            final int levelIndex = i;


            if (completed >= total) {
                // Forcompletedlevels, enable both GenerateandDetailsbuttons
                profile.getCertificateButton().setDisable(false);
                profile.getCertificateButton().setText("Generate");
                profile.getCertificateButton().setOnAction(event -> showCertificate(levelIndex));
            }

            if (completed > 0) {
                // For partially completed levels, enable Details button and Reset button as well
                profile.getDetailsButton().setText("Details");
                profile.getDetailsButton().setDisable(false);
                profile.getDetailsButton().setOnAction(event -> showDetailedProgress(levelIndex));

                profile.getResetButton().setDisable(false);
                profile.getResetButton().setOnAction(event -> {
                    resetLevelProgress(levelIndex);
                    loadProgress();
                });
            }
            profiles.add(profile);
        }
        tvProgress.setItems(profiles);
    }

    private void resetLevelProgress(int levelIndex) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Reset Level Progress");
        confirmAlert.setHeaderText("Reset Level Progress");
        confirmAlert.setContentText("Are you sure you want to reset progress for Level " + (levelIndex + 1) + "? This action cannot be undone.");

        // Apply theme to the alert dialog
        Theme theme = Theme.fromIndex(user.getTheme());
        String stylesheet = getClass().getResource("/css/" + theme.id() + ".css").toExternalForm();
        confirmAlert.getDialogPane().getStylesheets().add(stylesheet);

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Delete progress for thislevel using LessonProgressService since we removedthe progresstableLessonProgressService.deleteLessonProgressForLevel(user.getId(), levelIndex);

                // Reload progress to reflect changes
                loadProgress();

                // Show success message
                Toast.showToast(mainController.getStage(), "Progress for Level " + (levelIndex + 1) + " has been reset successfully.", 2000);
            }
        });
    }


    private void checkCertificateStatus() {
        if (user == null) return;

        boolean allLevelsCompleted = true;
        for (int i = 0; i < 3; i++) {
            int completed = LessonProgressService.getCompletedLessonCount(user.getId(), i);
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

    //Navigationmethods
    @FXML
    private void onUserProfileTab() throws IOException {
        if (apContent != null) {
            loadTabContent("/layout/setting_user_profile.fxml");
            // Update button stylesto show selection
            updateButtonStyles(btnUserProfile);
        }
    }

    @FXML
    private void onUserProgressTab() throws IOException {
        if (apContent != null) {
            loadTabContent("/layout/setting_user_progress.fxml");
// Update button stylesto show selection
            updateButtonStyles(btnUserProgress);
        }
    }

    @FXML
    private void onThemeTab() throws IOException {
        if (apContent != null) {
            loadTabContent("/layout/setting_theme.fxml");
            // Update button styles to show selection
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

    //Helper method to update button styles
    private void updateButtonStyles(Button selectedButton) {
        //Reset all buttons to default style
        Button[] allButtons = {btnUserProfile, btnUserProgress, btnTheme, btnBackgroundMusic, btnKeyErrorSound, btnCertificate, btnCredits, btnAbout};

        for (Button button : allButtons) {
            if (button != null) {
                button.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        }

        // Highlight selected button
        if (selectedButton != null) {
            selectedButton.

                    setStyle("-fx-background-color: -fx-to-type-node-color; -fx-background-radius:5; -fx-border-radius:5;");
        }
    }

    @FXML
    private void onChangeDisplayName() {
        if (user == null) return;

        TextInputDialog dialog = new TextInputDialog(user.getDisplayName());
        dialog.setTitle("Change Display Name");
        dialog.setHeaderText("Enter your new display name:");
        dialog.setContentText("Display Name:");

        // Apply themeto dialog
        Theme theme = Theme.fromIndex(user.getTheme());
        String stylesheet = getClass().getResource("/css/" + theme.id() + ".css").toExternalForm();
        dialog.getDialogPane().getStylesheets().add(stylesheet);

        dialog.showAndWait().ifPresent(newDisplayName -> {
            user.setDisplayName(newDisplayName);
            UserService.updateUser(user);
            if (lblDisplayName != null) {
                lblDisplayName.setText("Display Name: " + newDisplayName);
            }

            // Update display name inmain controller ifneeded
            if (mainController != null) {
                //RefreshUIif needed
            }
        });
    }

    @FXML
    private void onPlayMusic() {
        playSelectedMusic();
    }

    private void playSelectedMusic() {
        if (lvMusic == null) return;

        String selectedMusic = lvMusic.getSelectionModel().getSelectedItem();
        if (selectedMusic != null) {
            // Map user-friendly names to actual file names

            String fileName = musicNameToFileMap.get(selectedMusic);
            if (fileName != null && !fileName.isEmpty()) {
                playMusicFile(fileName);
                selectedBackgroundMusic = fileName;
            } else if (fileName != null) {
                // Handle "No Background Music" case
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }

                // Also stop in main controller
                if (mainController != null) {
                    mainController.stopBackgroundMusic();
                }

                selectedBackgroundMusic = null;
                showAlert("No background music selected", Alert.AlertType.INFORMATION);
            }
        }
    }

    private void playMusicFile(String fileName) {
        //Stop any currently playing music
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }

        // Play the selected music
        String musicPath = "/audio/" + fileName;
        try {
            URL musicURL = getClass().getResource(musicPath);
            if (musicURL != null) {
                mediaPlayer = new MediaPlayer(new Media(musicURL.toString()));
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);// Loop indefinitely
                mediaPlayer.play();

                // Also send to main controller to play in background
                if (mainController != null) {
                    mainController.playBackgroundMusic(fileName);
                }

                selectedBackgroundMusic = fileName;
            } else {
                showAlert("Could not findmusicfile: " + fileName, Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            showAlert("Error playing music:" + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
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

        // Check if all level sare completed
        boolean allLevelsCompleted = true;
        for (int i = 0; i < 3; i++) {
            int completed = LessonProgressService.getCompletedLessonCount(user.getId(), i);
            int total = lessonsPerLevel[i];
            if (completed < total) {
                allLevelsCompleted = false;
                break;
            }
        }

        if (allLevelsCompleted) {
            showMainCertificate();
        } else {
            showAlert("You need to complete all tests tov iew the certificate!", Alert.AlertType.WARNING);
        }
    }

    private void showMainCertificate() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/certificate.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnClose.getScene().getWindow());
            Scene scene = new Scene(loader.load());

            //Apply certificate specific styles
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/certificate_style.css").toExternalForm());

            stage.setScene(scene);

            CertificateController controller = loader.getController();

            // Calculate average WPM across all levels
            double totalWpm = 0;
            int levelCount = 0;
            for (int i = 0; i < normalLevelList.size(); i++) {
                double avgWpm = LessonProgressService.getAverageWpmForLevel(user.getId(), i);
                if (avgWpm > 0) {
                    totalWpm += avgWpm;
                    levelCount++;
                }
            }

            double overallAvgWpm = levelCount > 0 ? totalWpm / levelCount : 0;
            DecimalFormat df = new DecimalFormat("#.##");

            controller.initData(user.getDisplayName(), "Shan Typing Tutor", df.format(overallAvgWpm));

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

            // Calculate average WPMforthelevel
            double averageWpm = LessonProgressService.getAverageWpmForLevel(user.getId(), levelIndex);
            DecimalFormat df = new DecimalFormat("#.##");

            controller.initData(user.getDisplayName(), "Level " + (levelIndex + 1), df.format(averageWpm));

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
//Applythecurrentthemetothelessonprogresswindow
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

    @FXML
    private void onChangePassword() {
        if (user == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/change_password_dialog.fxml"));
            DialogPane changePasswordDialogPane = loader.load();

            ChangePasswordController controller = loader.getController();
            controller.initData(user);

            // Apply theme to dialog
            Theme theme = Theme.fromIndex(user.getTheme());
            String stylesheet = getClass().getResource("/css/" + theme.id() + ".css").toExternalForm();
            changePasswordDialogPane.getStylesheets().add(stylesheet);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(changePasswordDialogPane);
            dialog.setTitle("Change Password");

            //Show the dialog and wait for user response
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error opening change password dialog", Alert.AlertType.ERROR);
        }
    }
}