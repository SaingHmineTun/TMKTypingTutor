package it.saimao.tmk_typing_tutor;

import it.saimao.tmk_typing_tutor.key_map.NamKhone_KeyMap;
import it.saimao.tmk_typing_tutor.key_map.Panglong_KeyMap;
import it.saimao.tmk_typing_tutor.key_map.SIL_KeyMap;
import it.saimao.tmk_typing_tutor.key_map.Yunghkio_KeyMap;
import it.saimao.tmk_typing_tutor.model.Key;
import it.saimao.tmk_typing_tutor.model.Lesson;
import it.saimao.tmk_typing_tutor.utils.*;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    private BorderPane root;
    private VBox key;
    @FXML
    private VBox vbClose;
    @FXML
    private VBox vbMinimize;
    @FXML
    private VBox vbTheme;
    @FXML
    private ImageView ivTheme;
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

    private Window primaryWindow;

    private Node toTypeNode;
    private Node toTypeSecNode;
    private List<Map<String, String>> allValues;

    // Prevent ‌ေ & ႄ to auto enter in shn_sil keyboard
    private boolean consumeShanCharacter;
    private boolean typingWithEnglish;
    private boolean mustSwap;
    private boolean swap;
    private boolean stop;
    private boolean lightTheme = false;
    private List<Lesson> lessonList;
    private List<String> levelList;
    private Summary summary;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTopBar();
        initComboBoxItems();
        initPracticeListener();
        initSummarDialog();
        relayoutForVariousResolution();
        reqFocusOnPracticeField();
        cbKeyboard.getSelectionModel().selectFirst();

    }


    private void resetLevels(int keyboard) {

        levelList.clear();
        if (keyboard == 3)
            levelList.addAll(Arrays.asList("ၵၢၼ်ၽိုၵ်း 1", "ၵၢၼ်ၽိုၵ်း 2", "ၵၢၼ်ၽိုၵ်း 3", "ၵၢၼ်ၽိုၵ်း 4").stream().toList());
        else {
            levelList.addAll(Arrays.asList("ၵၢၼ်ၽိုၵ်း 1", "ၵၢၼ်ၽိုၵ်း 2", "ၵၢၼ်ၽိုၵ်း 3").stream().toList());
        }
        int seletedIndex = cbLevel.getSelectionModel().getSelectedIndex();
        cbLevel.getItems().setAll(FXCollections.observableArrayList(levelList));
        if (seletedIndex < 0) {
            cbLevel.getSelectionModel().selectFirst();
        } else if (seletedIndex > cbLevel.getItems().size() - 1) {
            cbLevel.getSelectionModel().selectLast();
        } else {
            cbLevel.getSelectionModel().select(seletedIndex);
        }
    }

    private void resetKeyboard() {
        for (Node hBox : vbKeyboardView.getChildren()) {
            ((HBox) hBox).getChildren().clear();
        }

    }

    private void relayoutForVariousResolution() {

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
        cbLessons.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20) + "; -fx-font-family: 'NamKhoneUnicode'");
        cbLevel.setPrefSize(Perc.getDynamicPixel(200), Perc.getDynamicPixel(50));
        cbLevel.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20) + "; -fx-font-family: 'NamKhoneUnicode';");
        cbKeyboard.setPrefSize(Perc.getDynamicPixel(200), Perc.getDynamicPixel(50));
        cbKeyboard.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20) + "; -fx-font-family: 'NamKhoneUnicode'");

        btNext.setPrefSize(Perc.getDynamicPixel(50), Perc.getDynamicPixel(50));
        btPrev.setPrefSize(Perc.getDynamicPixel(50), Perc.getDynamicPixel(50));


        tfView.setPrefHeight(Perc.getDynamicPixel(50));
        tfPractice.setPrefHeight(Perc.getDynamicPixel(50));
        tfView.setStyle("-fx-font-size: " + Perc.getDynamicPixel(22));
        tfPractice.setStyle("-fx-font-size: " + Perc.getDynamicPixel(22));

        vbKeyboardView.setPadding(new Insets(Perc.p1_5h(), Perc.p5w(), Perc.p1_5h(), Perc.p5w()));
        vbKeyboardView.setPrefHeight(Perc.p50h());

        lblAWPM.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20) + "; -fx-font-family: 'VistolSans-Black'");
        lbAWPM.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20) + "; -fx-font-family: 'VistolSans-Black'");
        lblWPM.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20) + "; -fx-font-family: 'VistolSans-Black'");
        lbWPM.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20) + "; -fx-font-family: 'VistolSans-Black'");
        lblMIST.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20) + "; -fx-font-family: 'VistolSans-Black'");
        lbMIST.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20) + "; -fx-font-family: 'VistolSans-Black'");
        lblACCU.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20) + "; -fx-font-family: 'VistolSans-Black'");
        lbACCU.setStyle("-fx-font-size: " + Perc.getDynamicPixel(20) + "; -fx-font-family: 'VistolSans-Black'");

    }

    private void initSummarDialog() {
        summary = new Summary(this);
    }



    private void initTopBar() {
        titleBar.setPrefHeight(Perc.p6h());
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
        vbTheme.setOnMouseClicked(mouseEvent -> {
            if (lightTheme) {
                ivTheme.setImage(new Image(getClass().getResource("/images/day.png").toExternalForm()));
                lightTheme = false;
                // TODO: Change to dark theme
                root.getScene().getRoot().getStylesheets().clear();
                root.getScene().getRoot().getStylesheets().add(getClass().getResource("/css/main_style.css").toExternalForm());
                key.getScene().getStylesheets().clear();
                key.getScene().getStylesheets().add(getClass().getResource("/css/main_style.css").toExternalForm());

                ivNext.setImage(new Image(getClass().getResource("/images/next.png").toExternalForm()));
                ivPrev.setImage(new Image(getClass().getResource("/images/prev.png").toExternalForm()));

            } else {

                ivTheme.setImage(new Image(getClass().getResource("/images/night.png").toExternalForm()));
                lightTheme = true;
                // Change to light theme
                root.getScene().getRoot().getStylesheets().clear();
                root.getScene().getRoot().getStylesheets().add(getClass().getResource("/css/day_style.css").toExternalForm());
                key.getScene().getStylesheets().clear();
                key.getScene().getStylesheets().add(getClass().getResource("/css/day_style.css").toExternalForm());

                ivNext.setImage(new Image(getClass().getResource("/images/next_dark.png").toExternalForm()));
                ivPrev.setImage(new Image(getClass().getResource("/images/prev_dark.png").toExternalForm()));

            }
        });
    }

    private void initComboBoxItems() {

        /************ START KEYBOARD **************/

        levelList = new ArrayList<>();

        cbKeyboard.getItems().setAll("လွၵ်းမိုဝ်း SIL", "လွၵ်းမိုဝ်းယုင်းၶဵဝ်", "လွၵ်းမိုဝ်းပၢင်လူင်", "လွၵ်းမိုဝ်းၼမ်ႉၶူင်း");
        cbKeyboard.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldValue, newValue) -> {
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
//            tutorTyping();
        });

        /************* END KEYBOARD ***********/


        /*********** START LEVEL ************/
        lessonList = new ArrayList<>();
        // Level Items are added according to Keyboard Selection!
        cbLevel.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("MODE SELECTION PROPERTY");
            changeLessons(newValue.intValue());
            tfPractice.clear();
        });

        /************* END LEVEL **************/

        /*********** START LESSONS *************/
        // cbLessons items are added when change in cbLevel occurs!
        cbLessons.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (cbLevel.getSelectionModel().getSelectedIndex() == 0) {
                    List<String> lessons = new ArrayList<>(Arrays.stream(newValue.getLesson().split(" ")).toList());
                    if (cbLessons.getSelectionModel().getSelectedIndex() != 0)
                        lessons = replace_A_WithOtherCharacters(lessons);
                    else {
                        List<String> lessonsCopy = new ArrayList<>(Arrays.stream(newValue.getLesson().split(" ")).toList());
                        lessons.addAll(lessonsCopy);
                    }
                    // Show test text ramdomly
                    Collections.shuffle(lessons);
                    tfView.setText(Arrays.toString(lessons.toArray()).replaceAll("[\\[\\],]", ""));
                } else {
                    tfView.setText(newValue.getLesson());
                }
                resetAndFocusOnPracticeField();
                tutorTyping();
            }
        });

        // NEXT & PREVIOUS
        btNext.setOnAction(event -> {
            nextLesson();
        });

        // NEXT & PREVIOUS
        btPrev.setOnAction(event -> {
            prevLesson();
        });

        /************ END LESSONS *************/

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
                // ZWNBSP ERROR
                line = line.replaceAll("\\uFEFF", "");
                String[] values = line.split(",");
                int no = Integer.parseInt(values[0].trim());
                String title = values[1].trim();
//                System.out.println(title + " : " + values.length);
                String content = values[2].replace("\"", "").trim();
                lessonList.add(new Lesson(no, title, content));
            }
            if (i == 1)
                // using short_lessons, we should reverse it before use it
                Collections.reverse(lessonList);
            int selectedIndex = cbLessons.getSelectionModel().getSelectedIndex();
            cbLessons.setItems(FXCollections.observableArrayList(lessonList));
            if (selectedIndex < 0) {
                cbLessons.getSelectionModel().selectFirst();
            } else if (i == 0 && selectedIndex > cbLessons.getItems().size() - 1) {
                cbLessons.getSelectionModel().selectLast();
            } else {
                cbLessons.getSelectionModel().select(selectedIndex);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> replace_A_WithOtherCharacters(List<String> lessons) {
        List<String> newList = new LinkedList<>();
        String[] characters = cbLessons.getItems().get(0).getLesson().split(" ");
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
        tfPractice.setOnMouseClicked(mouseEvent -> {
            tfPractice.end();
        });
        tfPractice.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.BACK_SPACE) {
                    event.consume();
                }
            }
        });

        tfPractice.addEventHandler(KeyEvent.KEY_TYPED, event -> {
//            System.out.println("KEY TYPE EVENT");
//            System.out.println("Character - " + event.getCharacter());
            if (event.getCharacter().equals("\u200B") || (!typingWithEnglish && consumeShanCharacter)) {
                consumeShanCharacter = false;
                typingWithEnglish = false;
                event.consume();
            }
        });

        tfPractice.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("Text you typed in textProperty() - " + tfPractice.getText());
//            System.out.println("SWAP - " + swap);

            if (newValue == null) return;

            if (newValue.length() == 1) {
                startTime = System.currentTimeMillis();
                misTyped = 0;
                end = false;
            }

            calculateOutcome();

            if (swap) {
                swap = false;
                // For unknown reason, ေ is typing again when all the job is finished!
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
//                System.out.println("200b gone!");
                tfPractice.setText(oldValue);
                return;
            }
            tutorTyping();
        });
    }


    private void tutorTyping() {
        // To be able to type ေ & ႄ first with SIL_Shan Keyman keyboard
        int keyboard = cbKeyboard.getSelectionModel().getSelectedIndex();
        String testText = tfView.getText();
        if (keyboard != 3) {
            testText = testText.replaceAll("([\\u1000-\\u1021\\u1075-\\u1081\\u1022\\u108f\\u1029\\u106e\\u106f\\u1086\\u1090\\u1091\\u1092\\u1097])([\\u1060-\\u1069\\u106c\\u106d\\u1070-\\u107c\\u1085\\u108a])?([\\u103b-\\u103e]*)?\\u1031", "\u1031$1$2$3");
            testText = testText.replaceAll("([\\u1000-\\u1021\\u1075-\\u1081\\u1022\\u108f\\u1029\\u106e\\u106f\\u1086\\u1090\\u1091\\u1092\\u1097])([\\u1060-\\u1069\\u106c\\u106d\\u1070-\\u107c\\u1085\\u108a])?([\\u103b-\\u103e]*)?\\u1084", "\u1084$1$2$3");
        }

        String practiceText = tfPractice.getText();
        int indexOfPractice = 0;
        String typing;
        if (tfPractice.getText() != null && tfPractice.getText().length() > 0) {
            // When typing ​ေ & ​ႄ with sil_shan, this key always comes and we have to omit it first
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
                    // Show  ျွ  key
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

                    // Show ႁႂ် key
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

                    // Show ေႃ key
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
                    if (afterTyping.equals("်"))
                        mustType = "ႂ်";
                }
                typingWithEnglish = true;
                // TODO - Need to decide what keyboard to use
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
                String title = cbLevel.getValue() + " : " + cbLessons.getValue().getTitle();
                summary.showDialog(title, wpm, accuracy, misTyped, awpm);
                return;
            }

        }

        if (indexOfPractice < tfView.getText().length()) {
            // Know which value to type next
            String valueToType = testText.substring(indexOfPractice, indexOfPractice + 1);
            if (keyboard == 3) {
                // Show  ိံ  key
                if (valueToType.equals("ိ")) {
                    String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                    if (afterTyping.equals("ံ")) {
                        valueToType = "ိံ";
                    }
                }
                // Show  ျွ  key
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

                // Show  ၢႆ  key
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
            // Show  ႂ်  key
            if (valueToType.equals("ႂ")) {
                String afterTyping = testText.substring(indexOfPractice + 1, indexOfPractice + 2);
                if (afterTyping.equals("်"))
                    valueToType = "ႂ်";
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

    private void playMistypedSound() {
        var soundURL = getClass().getResource("/audio/error.mp3");
        var errorPlayer = new MediaPlayer(new Media(soundURL.toString()));
        errorPlayer.play();
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
//        toTypeNode.setStyle("-fx-background-color: #000");
//        toTypeSecNode.setStyle("-fx-background-color: #000;");
        toTypeNode.setId("key-node-default");
        toTypeSecNode.setId("key-node-default");
    }

    private void highlightThisValue(String key, int row, int col) {
        if (toTypeSecNode != null) {
//            toTypeSecNode.setStyle("-fx-background-color: #000");
            toTypeSecNode.setId("key-node-default");
        }
        switch (key) {
            case "SHIFT" -> {
                // Calculate which shift key to press!
                int determination = 5;
                //                    case 0,1 -> determination = 5;
                //                    case 2,3 -> determination = 5;
                //                    case 4,5 -> determination = 5;
                //                    case 6,7 -> determination = 5;
                if (col > determination) {
                    // Enable Left Shift
                    toTypeSecNode = row4.getChildren().get(0);
                } else {
                    // Enable Right Shift
                    toTypeSecNode = row4.getChildren().get(row4.getChildren().size() - 1);
                }
            }
            case "SPACE" -> {
                if (toTypeNode != null) {
//                    toTypeNode.setStyle("-fx-background-color: #000;");
                    toTypeNode.setId("key-node-default");
                }
                toTypeSecNode = row5.getChildren().get(3);
            }
        }

        if (toTypeSecNode != null)
            toTypeSecNode.setId("key-node-to-type");
    }

    private void typeThisValue(int x, int y) {
        if (toTypeNode != null) {
//            toTypeNode.setStyle("-fx-background-color: #000;");
            toTypeNode.setId("key-node-default");
        }
        if (toTypeSecNode != null) {
//            toTypeSecNode.setStyle("-fx-background-color: #000");
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
            // u
            Iterator<String> engVal = allValues.get(i).keySet().iterator();
            // ၵ
            Iterator<String> taiVal = allValues.get(i).values().iterator();
            // U
            Iterator<String> engShiftVal = allValues.get(i + 1).keySet().iterator();
            // ၷ
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
                    else
                        row.getChildren().add(createKey(new Key(engShift, eng, taiShift, tai)));
                }
            }

        }
    }


    // Create a keyboard key with 5% width!
    private VBox createKey(Key key) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/key.fxml"));
        try {
            this.key = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setCharacterOnButton(this.key, key, "NamKhoneUnicode", 16);
        return this.key;

    }

    private void setCharacterOnButton(VBox vBox, Key key, String fontFamily, double fontSize) {
        Label charTaiShift = (Label) ((HBox) vBox.getChildren().get(0)).getChildren().get(0);
        charTaiShift.setText(key.getTaiShift());
        charTaiShift.setStyle("-fx-font-size: " + Perc.getDynamicPixel(fontSize) + "; -fx-font-family: '" + fontFamily + "';");

        Label charEngShift = (Label) ((HBox) vBox.getChildren().get(0)).getChildren().get(1);
        charEngShift.setText(key.getEngShift());
        charEngShift.setStyle("-fx-font-size: " + Perc.getDynamicPixel(fontSize) + "; -fx-font-family: '" + fontFamily + "';");

        Label charTai = (Label) ((HBox) vBox.getChildren().get(1)).getChildren().get(0);
        charTai.setText(key.getTai());
        charTai.setStyle("-fx-font-size: " + Perc.getDynamicPixel(fontSize) + "; -fx-font-family: '" + fontFamily + "';");

        Label charEng = (Label) ((HBox) vBox.getChildren().get(1)).getChildren().get(1);
        charEng.setText(key.getEng());
        charEng.setStyle("-fx-font-size: " + Perc.getDynamicPixel(fontSize) + "; -fx-font-family: '" + fontFamily + "';");

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
        // Calculate Interim WPM
        long elapsedTime = System.currentTimeMillis() - startTime;
        int characterCount = tfPractice.getText().length();
//            int wordCount = characterCount / 5; // Assuming an average word length of 5
        double minutes = (double) elapsedTime / 6000;
        int interimWPM = (int) (characterCount / minutes);
        lbWPM.setText(String.valueOf(interimWPM));
        return interimWPM;
    }

    public void clearBlur() {
        btPrev.getScene().getRoot().setEffect(null);
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

    public void blurScreen() {
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(10);
        btPrev.getScene().getRoot().setEffect(blur);

    }

    public void reqFocusOnPracticeField() {
        tfPractice.requestFocus();
        tfPractice.end();

    }

    public void retryLesson() {
//        resetAndFocusOnPracticeField();
        Lesson selectedLesson = cbLessons.getSelectionModel().getSelectedItem();
        cbLessons.getSelectionModel().clearSelection();
        cbLessons.getSelectionModel().select(selectedLesson);
    }
}
