package it.saimao.tmk_typing_tutor.controller;

import it.saimao.tmk_typing_tutor.key_map.NamKhone_KeyMap;
import it.saimao.tmk_typing_tutor.key_map.Panglong_KeyMap;
import it.saimao.tmk_typing_tutor.key_map.SIL_KeyMap;
import it.saimao.tmk_typing_tutor.key_map.Yunghkio_KeyMap;
import it.saimao.tmk_typing_tutor.model.*;
import it.saimao.tmk_typing_tutor.services.LessonProgressService;
import it.saimao.tmk_typing_tutor.services.UserService;
import it.saimao.tmk_typing_tutor.utils.Perc;
import it.saimao.tmk_typing_tutor.utils.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

import static it.saimao.tmk_typing_tutor.model.Data.*;

public class MainController implements Initializable {
    @FXML
    private BorderPane root;
    @FXML
    private VBox vbClose;
    @FXML
    private VBox vbMinimize;
    @FXML
    private VBox vbProfile;
    @FXML
    private ImageView imgClose;
    @FXML
    private ImageView imgMinimize;
    @FXML
    private VBox tLogo;
    @FXML
    private ImageView imgLogo;
    @FXML
    private Label tLabel;
    @FXML
    private Button btNext, btPrev;
    @FXML
    private ComboBox<Lesson> cbLessons;
    @FXML
    private ComboBox<String> cbLevel;
    @FXML
    private ComboBox<Theme> cbTheme;
    @FXML
    private ComboBox<String> cbKeyboard;
    @FXML
    private HBox row1, row2, row3, row4, row5;
    @FXML
    private TextField tfView;
    @FXML
    private TextField tfPractice;
    @FXML
    private Label lbWPM;
    @FXML
    private Label lblWPM;
    @FXML
    private Label lbACCU;
    @FXML
    private Label lblACCU;
    @FXML
    private Label lbAWPM;
    @FXML
    private Label lblAWPM;
    @FXML
    private Label lbMIST;
    @FXML
    private Label lblMIST;
    @FXML
    private VBox vbKeyboardView;
    @FXML
    private HBox hbSelection;
    @FXML
    private HBox titleBar;
    @FXML
    private ImageView ivNext;
    @FXML
    private ImageView ivPrev;

    private Stage primaryStage;
    private User loggedInUser;
    private boolean isInitializing = false;

    private Node toTypeNode;
    private Node toTypeSecNode;
    private List<Map<String, String>> allValues;

    private boolean consumeShanCharacter;
    private boolean typingWithEnglish;
    private boolean mustSwap;
    private boolean swap;
    private boolean stop;
    private List<Lesson> lessonList;
    private List<String> levelList;

    //Adda static reference to the main controller for communication
    private static MainController instance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set the static instance
        instance = this;
        initTopBar();
        initComboBoxItems();
        initPracticeListener();
        adjustForVariousResolution();
        reqFocusOnPracticeField();
    }

    private SettingsController settingsController;

    // Static method to get the main controllerinstance
    public static MainController getInstance() {
        return instance;
    }

    // Method to setsettings controller instance
    public void setSettingsController(SettingsController settingsController) {
        this.settingsController = settingsController;
    }

    //Method to get settings controller instance
    public SettingsController getSettingsController() {
        return settingsController;
    }

    // Method to navigate to a specific lesson
    public void navigateToLesson(int levelIndex, int lessonIndex) {
        Platform.runLater(() -> {
            try {
// Select the level
                cbLevel.getSelectionModel().select(levelIndex);

                // Wait a bit for the level change to propagate,then select the lesson
                Platform.runLater(() -> {
                    // Ensurethe lessons areloaded
                    if (cbLessons.getItems().size() > lessonIndex) {
                        cbLessons.getSelectionModel().select(lessonIndex);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void initData(User user, Stage stage) {
        this.loggedInUser = user;
        this.primaryStage = stage;
        isInitializing = true;

        var dialog = showProgressLoadingDialog();

        Platform.runLater(() -> {
            try {

                cbTheme.getSelectionModel().select(loggedInUser.getTheme());
                cbKeyboard.getSelectionModel().select(loggedInUser.getKeyboard());

                //Play background music if user has selected one (only when not already playing)
                if (loggedInUser.getBackgroundMusic() > 0) {
                    String backgroundMusicFile = "bgsound" + loggedInUser.getBackgroundMusic() + ".mp3";
                    // Checkif mp3 exists, otherwise trym4a
                    var musicURL = getClass().getResource("/audio/" + backgroundMusicFile);
                    if (musicURL == null && backgroundMusicFile.endsWith(".mp3")) {
                        backgroundMusicFile = "bgsound" + loggedInUser.getBackgroundMusic() + ".m4a";
                    }
                    playBackgroundMusic(backgroundMusicFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (dialog != null) {
                    dialog.close();
                }
                if (stage != null) {
                    stage.show();
                    stage.setMaximized(true);
                }
                isInitializing = false;
            }
        });
    }


    private Stage showProgressLoadingDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/dialog_loading.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Loading");

            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);
            dialogStage.show();

            return dialogStage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void resetLevels(int keyboard) {
        levelList.clear();
        if (keyboard == 3) levelList.addAll(normalLevelList);
        else {
            levelList.addAll(namkhoneLevelList);
        }
        cbLevel.getItems().setAll(FXCollections.observableArrayList(levelList));
        if (!isInitializing) {
            cbLevel.getSelectionModel().selectFirst();
        }
    }

    private void resetKeyboard() {
        row1.getChildren().clear();
        row2.getChildren().clear();
        row3.getChildren().clear();
        row4.getChildren().clear();
        row5.getChildren().clear();
    }

    private void adjustForVariousResolution() {
        tLogo.setPrefSize(Perc.getDynamicPixel(50), Perc.getDynamicPixel(35));
        imgLogo.setFitHeight(Perc.getDynamicPixel(25));
        imgLogo.setFitWidth(Perc.getDynamicPixel(25));
        tLabel.setPrefHeight(Perc.getDynamicPixel(35));
        tLabel.setStyle("-fx-font-size: " + Perc.getDynamicPixel(22));
        vbClose.setPrefSize(Perc.getDynamicPixel(50), Perc.getDynamicPixel(35));
        imgClose.setFitHeight(Perc.getDynamicPixel(15));
        imgClose.setFitWidth(Perc.getDynamicPixel(15));
        vbMinimize.setPrefSize(Perc.getDynamicPixel(50), Perc.getDynamicPixel(35));
        imgMinimize.setFitHeight(Perc.getDynamicPixel(15));
        imgMinimize.setFitWidth(Perc.getDynamicPixel(15));
        cbLessons.setPrefSize(Perc.getDynamicPixel(200), Perc.getDynamicPixel(50));
        cbLessons.setStyle("-fx-font-size: " + Perc.getDynamicPixel(18) + ";-fx-font-family: 'AJ 00'");
        cbLevel.setPrefSize(Perc.getDynamicPixel(150), Perc.getDynamicPixel(50));
        cbLevel.setStyle("-fx-font-size: " + Perc.getDynamicPixel(18) + ";-fx-font-family: 'AJ 00';");
        cbTheme.setPrefSize(Perc.getDynamicPixel(150), Perc.getDynamicPixel(50));
        cbTheme.setStyle("-fx-font-size: " + Perc.getDynamicPixel(18) + "; -fx-font-family: 'AJ 00';");
        cbKeyboard.setPrefSize(Perc.getDynamicPixel(200), Perc.getDynamicPixel(50));
        cbKeyboard.setStyle("-fx-font-size: " + Perc.getDynamicPixel(18) + "; -fx-font-family: 'AJ 00'");
        btNext.setPrefSize(Perc.getDynamicPixel(50), Perc.getDynamicPixel(50));
        btPrev.setPrefSize(Perc.getDynamicPixel(50), Perc.getDynamicPixel(50));
        tfView.setPrefHeight(Perc.getDynamicPixel(50));
        tfPractice.setPrefHeight(Perc.getDynamicPixel(50));
        tfView.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20));
        tfPractice.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20));
        vbKeyboardView.setPadding(new Insets(Perc.p1_5h(), Perc.p5w(), Perc.p1_5h(), Perc.p5w()));
        vbKeyboardView.setPrefHeight(Perc.p50h());
        lblAWPM.setStyle("-fx-font-size: " + Perc.getDynamicPixel(18) + "; -fx-font-family: 'VistolSans-Black'");
        lbAWPM.setStyle("-fx-font-size: " + Perc.getDynamicPixel(18) + "; -fx-font-family: 'VistolSans-Black'");
        lblWPM.setStyle("-fx-font-size: " + Perc.getDynamicPixel(18) + "; -fx-font-family: 'VistolSans-Black'");
        lbWPM.setStyle("-fx-font-size:" + Perc.getDynamicPixel(18) + "; -fx-font-family: 'VistolSans-Black'");
        lblMIST.setStyle("-fx-font-size: " + Perc.getDynamicPixel(18) + "; -fx-font-family: 'VistolSans-Black'");
        lbMIST.setStyle("-fx-font-size: " + Perc.getDynamicPixel(18) + "; -fx-font-family: 'VistolSans-Black'");
        lblACCU.setStyle("-fx-font-size: " + Perc.getDynamicPixel(18) + "; -fx-font-family: 'VistolSans-Black'");
        lbACCU.setStyle("-fx-font-size: " + Perc.getDynamicPixel(18) + "; -fx-font-family: 'VistolSans-Black'");
    }

    private void initTopBar() {
        titleBar.setPrefHeight(Perc.getDynamicPixel(40));
        vbClose.setOnMouseClicked(event -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
        vbMinimize.setOnMouseClicked(mouseEvent -> {
            Node source = (Node) mouseEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setIconified(true);
        });
        vbProfile.setOnMouseClicked(event -> showSettings());
    }

    private void initComboBoxItems() {
        levelList = new ArrayList<>();
        cbKeyboard.getItems().setAll(keyboards);
        cbKeyboard.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                if (loggedInUser != null && !isInitializing) {
                    loggedInUser.setKeyboard(newValue.intValue());
                    UserService.updateUser(loggedInUser);
                }
                if (newValue.intValue() == 0) {
                    allValues = SIL_KeyMap.getAllValuesList();
                } else if (newValue.intValue() == 1) {
                    allValues = Yunghkio_KeyMap.getAllValuesList();
                } else if (newValue.intValue() == 2) {
                    allValues = Panglong_KeyMap.getAllValuesList();
                } else {
                    allValues = NamKhone_KeyMap.getAllValuesList();
                }
                resetKeyboard();
                createKeyBoard();
                reqFocusOnPracticeField();
                resetLevels(newValue.intValue());
                if (isInitializing) {
                    cbLevel.getSelectionModel().select(loggedInUser.getLevel());
                }
            }
        });

        cbTheme.getItems().addAll(List.of(Theme.values()));
        cbTheme.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Theme item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.displayName().toUpperCase());
                }
            }
        });
        cbTheme.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Theme item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.displayName().toUpperCase());
                }
            }
        });
        cbTheme.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && root.getScene() != null) {
                Theme theme = cbTheme.getItems().get(newValue.intValue());
                if (loggedInUser != null && !isInitializing) {
                    loggedInUser.setTheme(newValue.intValue());
                    UserService.updateUser(loggedInUser);
                }
                String stylesheet = getClass().getResource("/css/" + theme.id() + ".css").toExternalForm();
                root.getStylesheets().setAll(stylesheet);
                ivNext.setImage(new Image(getClass().getResource("/images/next_" + theme.iconColor() + ".png").toExternalForm()));
                ivPrev.setImage(new Image(getClass().getResource("/images/prev_" + theme.iconColor() + ".png").toExternalForm()));
                reqFocusOnPracticeField();
            }
        });

        lessonList = new ArrayList<>();
        cbLevel.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (loggedInUser != null && !isInitializing) {
                    loggedInUser.setLevel(newValue.intValue());
                    UserService.updateUser(loggedInUser);
                }
                changeLessons(newValue.intValue());
                tfPractice.clear();
                if (isInitializing) {
                    cbLessons.getSelectionModel().select(loggedInUser.getLesson());
                }
            }
        });

        cbLessons.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (loggedInUser != null && !isInitializing) {
                    int lessonIndex = cbLessons.getSelectionModel().getSelectedIndex();
                    if (lessonIndex != -1) {
                        loggedInUser.setLesson(lessonIndex);
                        UserService.updateUser(loggedInUser);
                    }
                }
                if (cbLevel.getSelectionModel().getSelectedIndex() == 0) {
                    List<String> lessons = new ArrayList<>(Arrays.asList(newValue.getLesson().split(" ")));
                    if (cbLessons.getSelectionModel().getSelectedIndex() != 0)
                        lessons = replace_A_WithOtherCharacters(lessons);
                    else {
                        lessons.addAll(new ArrayList<>(Arrays.asList(newValue.getLesson().split(" "))));
                    }
                    Collections.shuffle(lessons);
                    tfView.setText(String.join(" ", lessons));
                } else {
                    tfView.setText(newValue.getLesson());
                }
                resetAndFocusOnPracticeField();
                tutorTyping();
                if (isInitializing) {
                    Platform.runLater(() -> isInitializing = false);
                }
            }
        });

        btNext.setOnAction(event -> nextLesson());
        btPrev.setOnAction(event -> prevLesson());
    }

    private void changeLessons(int i) {
        lessonList.clear();
        InputStream is;
        if (i == 0) {
            is = getClass().getResourceAsStream("/assets/lesson_1.csv");
        } else if (i == 1) {
            is = getClass().getResourceAsStream("/assets/lesson_2.csv");
        } else if (i == 2) {
            is = getClass().getResourceAsStream("/assets/lesson_3.csv");
        } else {
            is = getClass().getResourceAsStream("/assets/lesson_4.csv");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("\\uFEFF", "");
                String[] values = line.split(",");
                int no = Integer.parseInt(values[0].trim());
                String title = values[1].trim();
                String content = values[2].replace("\"", "").trim();
                lessonList.add(new Lesson(no, title, content));
            }
            if (i == 1) Collections.reverse(lessonList);
            cbLessons.setItems(FXCollections.observableArrayList(lessonList));
            if (!isInitializing) {
                cbLessons.getSelectionModel().selectFirst();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> replace_A_WithOtherCharacters(List<String> lessons) {
        List<String> newList = new LinkedList<>();
        String[] characters = cbLessons.getItems().getFirst().getLesson().split(" ");
        for (String lesson : lessons) {
            String newValue = lesson.replaceAll("ဢ", characters[new Random().nextInt(characters.length)]);
            newList.add(newValue);
        }
        return newList;
    }

    private void resetAndFocusOnPracticeField() {
        tfPractice.clear();
        reqFocusOnPracticeField();
        lbWPM.setText("0");
        lbAWPM.setText("0");
        lbACCU.setText("0%");
        lbMIST.setText("0");
    }

    private long startTime;
    private int misTyped;
    private boolean end;

    private void initPracticeListener() {
        var soundURL = getClass().getResource("/audio/error1.mp3");
        errorPlayer = new MediaPlayer(new Media(soundURL.toString()));
        tfPractice.setOnMouseClicked(mouseEvent -> tfPractice.end());
        tfPractice.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                event.consume();
            }
        });
        tfPractice.addEventHandler(KeyEvent.KEY_TYPED, event -> {
            if (event.getCharacter().equals("\u200B") || (!typingWithEnglish && consumeShanCharacter)) {
                consumeShanCharacter = false;
                typingWithEnglish = false;
                event.consume();
            }
        });
        tfPractice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) return;
            if (newValue.length() == 1) {
                startTime = System.currentTimeMillis();
                misTyped = 0;
                end = false;
            }
            calculateOutcome();
            if (swap) {
                swap = false;
                consumeShanCharacter = true;
                return;
            }
            if (stop) {
                stop = false;
                return;
            }
            if (tfPractice.getText().length() > tfView.getText().length()) {
                tfPractice.setText(oldValue);
                return;
            }
            if (tfPractice.getText().endsWith("\u200b")) {
                tfPractice.setText(oldValue);
                return;
            }
            tutorTyping();
        });
    }

    private void tutorTyping() {
        int keyboard = cbKeyboard.getSelectionModel().getSelectedIndex();
        String testText = tfView.getText();
        if (keyboard != 3) {
            testText = testText.replaceAll("([\\u1000-\\u1021\\u1075-\\u1081\\u1022\\u108f\\u1029\\u106e\\u106f\\u1086\\u1090\\u1091\\u1092\\u1097])([\\u1060-\\u1069\\u106c\\u106d\\u1070-\\u107c\\u1085\\u108a])?([\\u103b-\\u103e]*)?\\u1031", "\u1031$1$2$3");
            testText = testText.replaceAll("([\\u1000-\\u1021\\u1075-\\u1081\\u1022\\u108f\\u1029\\u106e\\u106f\\u1086\\u1090\\u1091\\u1092\\u1097])([\\u1060-\\u1069\\u106c\\u106d\\u1070-\\u107c\\u1085\\u108a])?([\\u103b-\\u103e]*)?\\u1084", "\u1084$1$2$3");
        }

        String practiceText = tfPractice.getText();
        int indexOfPractice = 0;
        String typing;
        if (tfPractice.getText() != null && !tfPractice.getText().isEmpty()) {
            // When typing ​ে& ​ໄwith sil_shan, this key always comes and we have to omit it first
            // For the typing tutor to know exactly what key we need to type
            indexOfPractice = practiceText.length();
            String mustType = testText.substring(indexOfPractice - 1, indexOfPractice);
            typing = practiceText.substring(indexOfPractice - 1, indexOfPractice);
            if (Utils.isEnglishCharacter(typing) && !isConverted) {
                // Config for Namkhone Keyboard
                if (keyboard == 3) {
                    // Show  ိံ  key
                    if (mustType.equals("ိ")) {
                        String afterTyping = testText.substring(indexOfPractice, indexOfPractice + 1);
                        if (afterTyping.equals("ံ")) {
                            mustType = "ိံ";
                        }
                    }
                    // Show ျွ  key
                    if (mustType.equals("ျ")) {
                        String afterTyping = testText.substring(indexOfPractice, indexOfPractice + 1);
                        if (afterTyping.equals("ွ")) {
                            mustType = "ျွ";
                        }
                    }


                    // Show ို key
                    if (mustType.equals("ြ")) {
                        try {
                            String afterTyping = testText.substring(indexOfPractice, indexOfPractice + 1);
                            if (afterTyping.equals("ႃ")) {
                                mustType = "ြႃ";
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    //Showႁႂ် key
                    if (mustType.equals("ႁ")) {
                        try {
                            String afterTyping2 = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                            String afterTyping = testText.substring(indexOfPractice, indexOfPractice + 1);
                            if (afterTyping.equals("ႂ") && afterTyping2.equals("်")) {
                                mustType = "ႁႂ်";
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    // Show  ၢႆ  key
                    if (mustType.equals("ၢ")) {
                        try {
                            String afterTyping = testText.substring(indexOfPractice, indexOfPractice + 1);
                            if (afterTyping.equals("ႆ")) {
                                mustType = "ၢႆ";
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    // Show ေႃkey
                    if (mustType.equals("ေ")) {
                        try {
                            String afterTyping = testText.substring(indexOfPractice, indexOfPractice + 1);
                            if (afterTyping.equals("ႃ")) {
                                mustType = "ေႃ";
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    // Show ို key
                    if (mustType.equals("ိ")) {
                        try {
                            String afterTyping = testText.substring(indexOfPractice, indexOfPractice + 1);
                            if (afterTyping.equals("ု")) {
                                mustType = "ို";
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    // Show ိူ key
                    if (mustType.equals("ိ")) {
                        try {
                            String afterTyping = testText.substring(indexOfPractice, indexOfPractice + 1);
                            if (afterTyping.equals("ူ")) {
                                mustType = "ိူ";
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    // Show ိူ key
                    if (mustType.equals("ိ")) {
                        try {
                            String afterTyping = testText.substring(indexOfPractice, indexOfPractice + 1);
                            if (afterTyping.equals("ူ")) {
                                mustType = "ိူ";
                            }
                        } catch (Exception ignored) {
                        }
                    }
// Show ႁူ  ႁွ key
                    if (mustType.equals("ႁ")) {
                        try {
                            String afterTyping = testText.substring(indexOfPractice, indexOfPractice + 1);
                            if (afterTyping.equals("ူ")) {
                                mustType = "ႁူ";
                            } else if (afterTyping.equals("ွ")) {
                                mustType = "ႁွ";
                            }
                        } catch (Exception ignored) {
                        }
                    }

                }

                if (mustType.equals("ႂ")) {
                    String afterTyping = testText.substring(indexOfPractice, indexOfPractice + 1);
                    if (afterTyping.equals("်")) mustType = "ႂ်";
                }
                typingWithEnglish = true;
                String shanChar = convertToShanChar(typing);
                tfPractice.setText(null);
                if (mustType.equals(shanChar)) {
                    isConverted = true;
                    tfPractice.setText(practiceText.substring(0, indexOfPractice - 1) + shanChar);
                } else {
                    misTyped++;
                    stop = true;
                    tfPractice.setText(practiceText.substring(0, indexOfPractice - 1));
                    playMistypedSound();
                }
                return;

            } else {
                isConverted = false;
                if (!mustType.equals(typing)) {
                    misTyped++;
                    stop = true;
                    tfPractice.setText(tfPractice.getText().substring(0, indexOfPractice - 1));
                    playMistypedSound();
                    return;
                }
            }
            if (practiceText.length() >= 2 && keyboard != 3) {
                String beforeTyping = practiceText.substring(indexOfPractice - 2, indexOfPractice - 1);
                if (beforeTyping.equals("ေ") || beforeTyping.equals("ႄ")) {
                    if (mustSwap) {
                        swap = true;
                        mustSwap = false;
                        String newText = tfPractice.getText(0, indexOfPractice - 2) + typing + beforeTyping;
                        tfPractice.setText(newText);
                    }
                }
            }
            if ((typing.equals("ေ") || typing.equals("ႄ")) && keyboard != 3) {
                mustSwap = true;
            }

            if (practiceText.length() == tfView.getText().length()) {
                end = true;
                clearToTypeValues();
                //Save progress using lesson_progress table instead of the redundant progress table
                LessonProgressService.saveProgress(loggedInUser.getId(), cbLevel.getSelectionModel().getSelectedIndex(), cbLessons.getSelectionModel().getSelectedIndex());

                // Save detailed lesson progress
                saveLessonProgress();

                String title = cbLevel.getValue() + " :" + cbLessons.getValue().getTitle();
                SummaryController summaryControllerDialog = new SummaryController(this, primaryStage);
                summaryControllerDialog.showDialog(title, wpm, accuracy, misTyped, awpm);
                return;
            }

        }

        if (indexOfPractice < tfView.getText().length()) {
            // Knowwhich value to type next
            String valueToType = testText.substring(indexOfPractice, indexOfPractice + 1);
            if (keyboard == 3) {
                // Show  ိံ key
                if (valueToType.equals("ိ")) {
                    String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                    if (afterTyping.equals("ံ")) {
                        valueToType = "ိံ";
                    }
                }
                //Showျွ key
                if (valueToType.equals("ျ")) {
                    String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                    if (afterTyping.equals("ွ")) {
                        valueToType = "ျွ";
                    }
                }

                // Show ို key
                if (valueToType.equals("ြ")) {
                    try {
                        String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                        if (afterTyping.equals("ႃ")) {
                            valueToType = "ြႃ";
                        }
                    } catch (Exception ignored) {
                    }
                }

// Show ႁႂ် key
                if (valueToType.equals("ႁ")) {
                    try {
                        String afterTyping2 = testText.substring(indexOfPractice + 2, indexOfPractice + 3);
                        String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                        if (afterTyping.equals("ႂ") && afterTyping2.equals("်")) {
                            valueToType = "ႁႂ်";
                        }
                    } catch (Exception ignored) {
                    }
                }

                //Show ၢႆ  key
                if (valueToType.equals("ၢ")) {
                    try {
                        String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                        if (afterTyping.equals("ႆ")) {
                            valueToType = "ၢႆ";
                        }
                    } catch (Exception ignored) {
                    }
                }

                // Show ေႃ key
                if (valueToType.equals("ေ")) {
                    try {
                        String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                        if (afterTyping.equals("ႃ")) {
                            valueToType = "ေႃ";
                        }
                    } catch (Exception ignored) {
                    }
                }

                // Show ို key
                if (valueToType.equals("ိ")) {
                    try {
                        String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                        if (afterTyping.equals("ု")) {
                            valueToType = "ို";
                        }
                    } catch (Exception ignored) {
                    }
                }


                // Show ိူ key
                if (valueToType.equals("ိ")) {
                    try {
                        String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                        if (afterTyping.equals("ူ")) {
                            valueToType = "ိူ";
                        }
                    } catch (Exception ignored) {
                    }
                }

                // Show ိူ key
                if (valueToType.equals("ိ")) {
                    try {
                        String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                        if (afterTyping.equals("ူ")) {
                            valueToType = "ိူ";
                        }
                    } catch (Exception ignored) {
                    }
                }
// Show ႁူ  ႁွ key
                if (valueToType.equals("ႁ")) {
                    try {
                        String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                        if (afterTyping.equals("ူ")) {
                            valueToType = "ႁူ";
                        } else if (afterTyping.equals("ွ")) {
                            valueToType = "ႁွ";
                        }
                    } catch (Exception ignored) {
                    }
                }

            }
            // Show ႂ်  key
            if (valueToType.equals("ႂ")) {
                String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                if (afterTyping.equals("်")) valueToType = "ႂ်";
            }


            if (valueToType.equals(" ")) {
                highlightThisValue("SPACE", 0, 5);
            } else {
                for (int x = 0; x < allValues.size(); x++) {
                    Map<String, String> row = allValues.get(x);
                    if (row.containsValue(valueToType)) {
                        List<String> values = row.values().stream().toList();
                        for (int y = 0; y < values.size(); y++) {
                            String val = values.get(y);
                            if (valueToType.equals(val)) {
                                typeThisValue(x, y);
                                if (x % 2 == 1) {
                                    highlightThisValue("SHIFT", x, y);
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private void saveLessonProgress() {
        // Create a lesson progress object with the current stats
        LessonProgress lessonProgress = new LessonProgress(loggedInUser.getId(), cbLevel.getSelectionModel().getSelectedIndex(), cbLessons.getSelectionModel().getSelectedIndex(), wpm, accuracy, misTyped);
        // Save the lesson progress
        LessonProgressService.saveLessonProgress(lessonProgress);
    }

    private void showSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/settings.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(primaryStage);
            stage.setResizable(false);
            stage.setTitle("Settings");
            Scene scene = new Scene(loader.load());

            // Apply the current theme to the settings window
            Theme theme = Theme.fromIndex(loggedInUser.getTheme());
            String stylesheet = getClass().getResource("/css/" + theme.id() + ".css").toExternalForm();
            scene.getStylesheets().add(stylesheet);

            stage.setScene(scene);
            stage.setOnCloseRequest(event -> {
// When settings window is closed, ensure any background music continues playing
                if (settingsController != null) {
                    String selectedMusic = settingsController.getSelectedBackgroundMusic();
                    if (selectedMusic != null) {
                        playBackgroundMusic(selectedMusic);
                    }
                }
                stage.close();
            });

            SettingsController controller = loader.getController();
            controller.initData(loggedInUser, this);

            // Set the settingscontroller instance
            setSettingsController(controller);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private MediaPlayer errorPlayer;
    private MediaPlayer backgroundMusicPlayer;

    private void playMistypedSound() {
        // Check if user has enabled error sounds
        if (loggedInUser.getErrorSound() <= 0) {
            return; // User has disabled error sounds
        }

        // Get the selected error sound from user settings
        String errorSound = "error" + loggedInUser.getErrorSound() + ".mp3";

        // Stop any currently playing error sound
        if (errorPlayer != null) {
            errorPlayer.stop();
        }

        // Load and play the selected error sound
        var soundURL = getClass().getResource("/audio/" + errorSound);
        if (soundURL != null) {
            errorPlayer = new MediaPlayer(new Media(soundURL.toString()));
            errorPlayer.play();
        } else {
            // Try other extensions
            errorSound = "error" + loggedInUser.getErrorSound() + ".m4a";
            soundURL = getClass().getResource("/audio/" + errorSound);
            if (soundURL != null) {
                errorPlayer = new MediaPlayer(new Media(soundURL.toString()));
                errorPlayer.play();
            } else {
                // Fallback todefault error sound
                soundURL = getClass().getResource("/audio/error1.mp3");
                if (errorPlayer == null && soundURL != null) {
                    errorPlayer = new MediaPlayer(new Media(soundURL.toString()));
                }
                if (errorPlayer != null) {
                    errorPlayer.play();
                }
            }
        }
    }

    //Background music methods
    public void playBackgroundMusic(String musicFileName) {
        // Stop any currently playing background music
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }

        // Load and play the selectedbackground music
        var musicURL = getClass().getResource("/audio/" + musicFileName);
        if (musicURL != null) {
            backgroundMusicPlayer = new MediaPlayer(new Media(musicURL.toString()));
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop indefinitely
            backgroundMusicPlayer.play();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }

    public void setSelectedBackgroundMusic(String musicFileName) {
        if (musicFileName != null && !musicFileName.isEmpty()) {
            playBackgroundMusic(musicFileName);
        } else {
            stopBackgroundMusic();
        }
    }

    public void setSelectedErrorSound(int errorSoundIndex, User user) {
        // This method will be called when error sound is updated in settings
        user.setErrorSound(errorSoundIndex);
        UserService.updateUser(user);

    }

    private boolean isConverted;

    private String convertToShanChar(String character) {
        String shanChar;
        int keyboard = cbKeyboard.getSelectionModel().getSelectedIndex();
        if (keyboard == 0) {
            shanChar = SIL_KeyMap.getAllValuesMap().getOrDefault(character, "");
        } else if (keyboard == 1) {
            shanChar = Yunghkio_KeyMap.getAllValuesMap().getOrDefault(character, "");
        } else if (keyboard == 2) {
            shanChar = Panglong_KeyMap.getAllValuesMap().getOrDefault(character, "");
        } else {
            shanChar = NamKhone_KeyMap.getAllValuesMap().getOrDefault(character, "");
        }
        return shanChar;
    }

    private void clearToTypeValues() {
        if (toTypeNode != null) toTypeNode.setId("key-node-default");
        if (toTypeSecNode != null) toTypeSecNode.setId("key-node-default");
    }

    private void highlightThisValue(String key, int row, int col) {
        if (toTypeSecNode != null) {
            toTypeSecNode.setId("key-node-default");
        }
        switch (key) {
            case "SHIFT" -> {
                int determination = 5;
                if (col > determination) {
                    toTypeSecNode = row4.getChildren().get(0);
                } else {
                    toTypeSecNode = row4.getChildren().get(row4.getChildren().size() - 1);
                }
            }
            case "SPACE" -> {
                if (toTypeNode != null) {
                    toTypeNode.setId("key-node-default");
                }
                toTypeSecNode = row5.getChildren().get(3);
            }
        }
        if (toTypeSecNode != null) toTypeSecNode.setId("key-node-to-type");
    }

    private void typeThisValue(int x, int y) {
        if (toTypeNode != null) {
            toTypeNode.setId("key-node-default");
        }
        if (toTypeSecNode != null) {
            toTypeSecNode.setId("key-node-default");
        }
        switch (x) {
            case 0, 1 -> toTypeNode = row1.getChildren().get(y);
            case 2, 3 -> toTypeNode = row2.getChildren().get(y);
            case 4, 5 -> toTypeNode = row3.getChildren().get(y);
            case 6, 7 -> toTypeNode = row4.getChildren().get(y);
        }
        if (toTypeNode != null) {
            toTypeNode.setId("key-node-to-type");
        }
    }

    protected void createKeyBoard() {
        for (int i = 0; i < allValues.size(); i += 2) {
            Iterator<String> engVal = allValues.get(i).keySet().iterator();
            Iterator<String> taiVal = allValues.get(i).values().iterator();
            Iterator<String> engShiftVal = allValues.get(i + 1).keySet().iterator();
            Iterator<String> taiShiftVal = allValues.get(i + 1).values().iterator();
            while (engVal.hasNext() && taiVal.hasNext() && engShiftVal.hasNext() && taiShiftVal.hasNext()) {
                HBox row = null;
                switch (i) {
                    case 0 -> row = row1;
                    case 2 -> row = row2;
                    case 4 -> row = row3;
                    case 6 -> row = row4;
                    case 8 -> row = row5;
                }
                if (row != null) {
                    String engShift = engShiftVal.next();
                    String eng = engVal.next();
                    String taiShift = taiShiftVal.next();
                    String tai = taiVal.next();
                    if (eng.equalsIgnoreCase("Back"))
                        row.getChildren().add(createKeyWithCustomWidth(new Key("", eng, "", ""), 8 * 0.02));
                    else if (eng.equalsIgnoreCase("Tab"))
                        row.getChildren().add(createKeyWithCustomWidth(new Key("", eng, "", ""), 7 * 0.02));
                    else if (eng.equalsIgnoreCase("Caps"))
                        row.getChildren().add(createKeyWithCustomWidth(new Key("", eng, "", ""), 8 * 0.02));
                    else if (eng.equalsIgnoreCase("Enter"))
                        row.getChildren().add(createKeyWithCustomWidth(new Key("", eng, "", ""), 8 * 0.02));
                    else if (eng.equalsIgnoreCase("Shift1") || eng.equalsIgnoreCase("Shift2"))
                        row.getChildren().add(createKeyWithCustomWidth(new Key("", tai, "", ""), 10.5 * 0.02));
                    else if (eng.equalsIgnoreCase("Ctrl1") || eng.equalsIgnoreCase("Ctrl2"))
                        row.getChildren().add(createKeyWithCustomWidth(new Key("", tai, "", ""), 6 * 0.02));
                    else if (eng.equalsIgnoreCase("Alt1") || eng.equalsIgnoreCase("Alt2"))
                        row.getChildren().add(createKeyWithCustomWidth(new Key("", tai, "", ""), 2 * 0.02));
                    else if (eng.equalsIgnoreCase("Space"))
                        row.getChildren().add(createKeyWithCustomWidth(new Key("", eng, "", ""), 70 * 0.02));
                    else if (eng.equalsIgnoreCase("Win1") || eng.equalsIgnoreCase("Win2") || eng.equalsIgnoreCase("Menu"))
                        row.getChildren().add(createKeyWithCustomWidth(new Key("", tai, "", ""), 2 * 0.02));
                    else if (eng.equals(tai) && engShift.equals(taiShift))
                        row.getChildren().add(createKey(new Key(engShift, eng, "", "")));
                    else row.getChildren().add(createKey(new Key(engShift, eng, taiShift, tai)));
                }
            }
        }
    }

    private VBox createKey(Key key) {
        VBox keyNode;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/key.fxml"));
        try {
            keyNode = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setCharacterOnButton(keyNode, key, "AJ 00", 16);
        return keyNode;
    }

    private void setCharacterOnButton(VBox vBox, Key key, String fontFamily, double fontSize) {
        Label charTaiShift = (Label) ((HBox) vBox.getChildren().get(0)).getChildren().get(0);
        charTaiShift.setText(key.getTaiShift());
        charTaiShift.setStyle("-fx-font-size:" + Perc.getDynamicPixel(fontSize) + "; -fx-font-family: '" + fontFamily + "';");
        Label charEngShift = (Label) ((HBox) vBox.getChildren().get(0)).getChildren().get(1);
        charEngShift.setText(key.getEngShift());
        charEngShift.setStyle("-fx-font-size: " + Perc.getDynamicPixel(fontSize) + "; -fx-font-family:'" + fontFamily + "';");
        Label charTai = (Label) ((HBox) vBox.getChildren().get(1)).getChildren().get(0);
        charTai.setText(key.getTai());
        charTai.setStyle("-fx-font-size: " + Perc.getDynamicPixel(fontSize) + "; -fx-font-family: '" + fontFamily + "';");
        Label charEng = (Label) ((HBox) vBox.getChildren().get(1)).getChildren().get(1);
        charEng.setText(key.getEng());
        charEng.setStyle("-fx-font-size:" + Perc.getDynamicPixel(fontSize) + "; -fx-font-family: '" + fontFamily + "';");
    }

    private VBox createKeyWithCustomWidth(Key key, double width) {
        VBox vBox;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/key.fxml"));
        try {
            vBox = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        vBox.setPrefWidth(vBox.getPrefWidth() + (vBox.getPrefWidth() * width));
        setCharacterOnButton(vBox, key, "VistolSans-Black", 18);
        return vBox;
    }

    private int wpm;
    private double accuracy;
    private int awpm;

    private void calculateOutcome() {
        if (tfPractice.getText().length() > 1 && !end) {
            wpm = calculateWPM();
            accuracy = calculateACCU();
            awpm = calculateAWPM(wpm, accuracy);
        }
    }

    private int calculateAWPM(int wpm, double accuracy) {
        int avgWPM = (int) Math.round(wpm * accuracy);
        lbAWPM.setText(String.valueOf(avgWPM));
        return avgWPM;
    }

    private double calculateACCU() {
        double accuracy = (double) (tfPractice.getText().length() - misTyped) / tfPractice.getText().length();
        DecimalFormat format = new DecimalFormat("#0.00");
        lbMIST.setText(String.valueOf(misTyped));
        lbACCU.setText(format.format(accuracy * 100) + "%");
        return accuracy;
    }

    private int calculateWPM() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        int characterCount = tfPractice.getText().length();
        double minutes = (double) elapsedTime / 60000;
        var interimWPM = Math.round((characterCount / 5.0) / minutes);
        lbWPM.setText(String.valueOf(interimWPM));
        return Math.toIntExact(interimWPM);
    }

    public boolean nextLesson() {
        int currentIndex = cbLessons.getSelectionModel().getSelectedIndex();
        if (currentIndex != lessonList.size() - 1) {
            cbLessons.getSelectionModel().selectNext();
            return true;
        } else if (cbLevel.getSelectionModel().getSelectedIndex() < cbLevel.getItems().size() - 1) {
            cbLevel.getSelectionModel().selectNext();
            cbLessons.getSelectionModel().selectFirst();
            return true;
        }
        return false;
    }

    public boolean prevLesson() {
        int currentIndex = cbLessons.getSelectionModel().getSelectedIndex();
        if (currentIndex != 0) {
            cbLessons.getSelectionModel().selectPrevious();
            return true;
        } else if (cbLevel.getSelectionModel().getSelectedIndex() != 0) {
            cbLevel.getSelectionModel().selectPrevious();
            cbLessons.getSelectionModel().selectLast();
            return true;
        }
        return false;
    }

    public void reqFocusOnPracticeField() {
        tfPractice.requestFocus();
        tfPractice.end();
    }

    public void retryLesson() {
        Lesson selectedLesson = cbLessons.getSelectionModel().getSelectedItem();
        cbLessons.getSelectionModel().clearSelection();
        cbLessons.getSelectionModel().select(selectedLesson);
    }

    public ComboBox<Theme> getCbTheme() {

        return cbTheme;
    }

    public Window getStage() {
        return root.getScene().getWindow();
    }
}