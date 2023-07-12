package it.saimao.tmk_typing;

import it.saimao.tmk_typing.model.Key;
import it.saimao.tmk_typing.model.Lesson;
import it.saimao.tmk_typing.utils.KeyValue;
import it.saimao.tmk_typing.utils.Perc;
import it.saimao.tmk_typing.utils.Utils;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    private VBox vbClose;
    @FXML
    private ImageView imgClose;
    @FXML
    private VBox tLogo;
    @FXML
    private ImageView imgLogo;
    @FXML
    private Label tLabel;
    @FXML
    Button btNext, btPrev;
    @FXML
    private ComboBox<Lesson> cbLessons;
    @FXML
    private ComboBox<String> cbMode;
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
    private Window primaryWindow;
    private final String[] modes = {"ၵၢၼ်ၽိုၵ်းဢွၼ်ႇ", "ၵၢၼ်ၽိုၵ်းလူင်"};

    private Node toTypeNode;
    private Node toTypeSecNode;
    private final List<Map<String, String>> allValues = KeyValue.getAllValuesList();

    // Prevent ‌ေ & ႄ to auto enter in shn_sil keyboard
    private boolean consumeShanCharacter;
    private boolean typingWithEnglish;
    private boolean mustSwap;
    private boolean swap;
    private boolean stop;
    private List<Lesson> lessonList;
    private Summary summary;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTopBar();
        createKeyBoard();
        initPracticeLessons();
        initPracticeListener();
        initViewValues();
        initSummarDialog();
        relayoutForVariousResolution();
        tfPractice.requestFocus();
//        primaryWindow = btNext.getScene().getWindow();
//        btPrev.getScene().getRoot().setEffect(new BoxBlur(10, 10, 3));

    }

    private void relayoutForVariousResolution() {

        tLogo.setPrefSize(Perc.getDynamicPixel(50), Perc.getDynamicPixel(50));
        imgLogo.setFitHeight(Perc.getDynamicPixel(40));
        imgLogo.setFitWidth(Perc.getDynamicPixel(40));
        tLabel.setPrefHeight(Perc.getDynamicPixel(50));
        tLabel.setStyle("-fx-font-size: " + Perc.getDynamicPixel(30));
        vbClose.setPrefSize(Perc.getDynamicPixel(50), Perc.getDynamicPixel(50));
        imgClose.setFitHeight(Perc.getDynamicPixel(20));
        imgClose.setFitWidth(Perc.getDynamicPixel(20));


        cbLessons.setPrefSize(Perc.getDynamicPixel(200), Perc.getDynamicPixel(50));
        cbLessons.setStyle("-fx-font-size: " + Perc.getDynamicPixel(17) + "; -fx-font-family: 'Myanmar Taungthu'");
        cbMode.setPrefSize(Perc.getDynamicPixel(200), Perc.getDynamicPixel(50));
        cbMode.setStyle("-fx-font-size: " + Perc.getDynamicPixel(17) + "; -fx-font-family: 'Myanmar Taungthu'");
        btNext.setPrefSize(Perc.getDynamicPixel(50), Perc.getDynamicPixel(50));
        btPrev.setPrefSize(Perc.getDynamicPixel(50), Perc.getDynamicPixel(50));


        tfView.setPrefHeight(Perc.getDynamicPixel(55));
        tfPractice.setPrefHeight(Perc.getDynamicPixel(55));
        tfView.setStyle("-fx-font-size: " + Perc.getDynamicPixel(23));
        tfPractice.setStyle("-fx-font-size: " + Perc.getDynamicPixel(23));

        vbKeyboardView.setPadding(new Insets(Perc.p5h(), Perc.p12_5w(), Perc.p5h(), Perc.p12_5w()));
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
        vbClose.setOnMouseClicked(event -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
    }

    private void initPracticeLessons() {
        lessonList = new ArrayList<>();

        cbMode.setItems(FXCollections.observableArrayList(modes));
        cbMode.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("MODE SELECTION PROPERTY");
            changeLessons(newValue.intValue());
            tfPractice.clear();
        });
        cbMode.getSelectionModel().select(0);
    }

    private void changeLessons(int i) {
        lessonList.clear();
        InputStream is;
        if (i == 0) {
            is = getClass().getResourceAsStream("/assets/short_lessons.csv");
        } else {
            is = getClass().getResourceAsStream("/assets/tai_lessons.csv");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int no = Integer.parseInt(values[0].trim());
                String title = values[1].trim();
//                System.out.println(title + " : " + values.length);
                String content = values[2].replace("\"", "").trim();
                lessonList.add(new Lesson(no, title, content));
            }
            if (i == 0)
                Collections.reverse(lessonList);
            int selectedIndex = cbLessons.getSelectionModel().getSelectedIndex();
            cbLessons.setItems(FXCollections.observableArrayList(lessonList));
            cbLessons.getSelectionModel().select(selectedIndex);
            // using short_lessons, we should reverse it before use it
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initViewValues() {
        cbLessons.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tfView.setText(newValue.getLesson());
                resetAndFocusOnPracticeField();
                doHeavyJob();
            }
        });
        cbLessons.getSelectionModel().select(0);

        // NEXT & PREVIOUS
        btNext.setOnAction(event -> {
            nextLesson();
        });

        // NEXT & PREVIOUS
        btPrev.setOnAction(event -> {
            prevLesson();
        });

    }

    private void resetAndFocusOnPracticeField() {

        tfPractice.setText("");
        tfPractice.requestFocus();
        lbWPM.setText("0");
        lbAWPM.setText("0");
        lbACCU.setText("0%");
        lbMIST.setText("0");
    }

    private long startTime;
    private int misTyped;
    private boolean end;

    private void initPracticeListener() {
        doHeavyJob();
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
                // TODO - Because in Tai Typing, ေ is sometimes auto-typing!
                typingWithEnglish = false;
                event.consume();
            }
        });

        tfPractice.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("Text you typed in textProperty() - " + tfPractice.getText());
//            System.out.println("SWAP - " + swap);

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
            doHeavyJob();
        });
    }

    private void doHeavyJob() {
        // To be able to type ေ & ႄ first with SIL_Shan Keyman keyboard
        String testText = tfView.getText().replaceAll("([\\u1000-\\u1021\\u1075-\\u1081\\u1022\\u108f\\u1029\\u106e\\u106f\\u1086\\u1090\\u1091\\u1092\\u1097])([\\u1060-\\u1069\\u106c\\u106d\\u1070-\\u107c\\u1085\\u108a])?([\\u103b-\\u103e]*)?\\u1031", "\u1031$1$2$3");
        testText = testText.replaceAll("([\\u1000-\\u1021\\u1075-\\u1081\\u1022\\u108f\\u1029\\u106e\\u106f\\u1086\\u1090\\u1091\\u1092\\u1097])([\\u1060-\\u1069\\u106c\\u106d\\u1070-\\u107c\\u1085\\u108a])?([\\u103b-\\u103e]*)?\\u1084", "\u1084$1$2$3");

        String practiceText = tfPractice.getText();
        int indexOfPractice = 0;
        String typing;
        if (tfPractice.getText() != null && tfPractice.getText().length() > 0) {
            // When typing ​ေ & ​ႄ with sil_shan, this key always comes and we have to omit it first
            // For the typing tutor to know exactly what key we need to type
            indexOfPractice = practiceText.length();
            String mustType = testText.substring(indexOfPractice - 1, indexOfPractice);
            typing = practiceText.substring(indexOfPractice - 1, indexOfPractice);
            if (Utils.isEnglishConsonant(typing)) {
                if (mustType.equals("ႂ")) {
                    String afterTyping = testText.substring(indexOfPractice, indexOfPractice + 1);
                    if (afterTyping.equals("်"))
                        mustType = "ႂ်";
                }
                typingWithEnglish = true;
                String shanChar = Utils.convertToShanChar(typing);
                if (mustType.equals(shanChar)) {
                    tfPractice.setText(practiceText.substring(0, indexOfPractice - 1) + shanChar);
                } else {
                    misTyped++;
                    stop = true;
                    tfPractice.setText(practiceText.substring(0, indexOfPractice - 1));
                }
                return;
            } else {
                if (!mustType.equals(typing)) {
                    misTyped++;
                    stop = true;
                    tfPractice.setText(tfPractice.getText().substring(0, indexOfPractice - 1));
                    return;
                }
            }
            if (practiceText.length() >= 2) {
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
            if (typing.equals("ေ") || typing.equals("ႄ")) {
                mustSwap = true;
            }

            if (practiceText.length() == tfView.getText().length()) {
                end = true;
                clearToTypeValues();
                summary.showDialog(wpm, accuracy, misTyped, awpm);
                return;
            }

        }

        if (indexOfPractice < tfView.getText().length()) {
            // Know which value to type next
            String valueToType = testText.substring(indexOfPractice, indexOfPractice + 1);
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

    private void clearToTypeValues() {
        toTypeNode.setStyle("-fx-background-color: #000");
        toTypeSecNode.setStyle("-fx-background-color: #000;");
    }

    private void highlightThisValue(String key, int row, int col) {
        if (toTypeSecNode != null) {
            toTypeSecNode.setStyle("-fx-background-color: #000");
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
                    toTypeNode.setStyle("-fx-background-color: #000;");
                }
                toTypeSecNode = row5.getChildren().get(3);
            }
        }

        if (toTypeSecNode != null)
            toTypeSecNode.setStyle("-fx-background-color: #006dff");
    }

    private void typeThisValue(int x, int y) {
        if (toTypeNode != null) {
            toTypeNode.setStyle("-fx-background-color: #000;");
        }
        if (toTypeSecNode != null) {
            toTypeSecNode.setStyle("-fx-background-color: #000");
        }
        switch (x) {
            case 0, 1 -> toTypeNode = row1.getChildren().get(y);
            case 2, 3 -> toTypeNode = row2.getChildren().get(y);
            case 4, 5 -> toTypeNode = row3.getChildren().get(y);
            case 6, 7 -> toTypeNode = row4.getChildren().get(y);
        }
        if (toTypeNode != null)
            toTypeNode.setStyle("-fx-background-color: #006dff");

    }

    // Total keyboard width - 75%
    private void initRow1() {
        row1.getChildren().add(createKey(new Key("~", "`", "", "")));
        row1.getChildren().add(createKey(new Key("!", "1", "", "")));
        row1.getChildren().add(createKey(new Key("@", "2", "", "")));
        row1.getChildren().add(createKey(new Key("#", "3", "", "")));
        row1.getChildren().add(createKey(new Key("$", "4", "", "")));
        row1.getChildren().add(createKey(new Key("%", "5", "", "")));
        row1.getChildren().add(createKey(new Key("^", "6", "", "")));
        row1.getChildren().add(createKey(new Key("&", "7", "", "")));
        row1.getChildren().add(createKey(new Key("*", "8", "", "")));
        row1.getChildren().add(createKey(new Key("(", "9", "", "")));
        row1.getChildren().add(createKey(new Key(")", "0", "", "")));
        row1.getChildren().add(createKey(new Key("_", "-", "", "")));
        row1.getChildren().add(createKey(new Key("+", "=", "", "")));
        row1.getChildren().add(createKeyWithCustomWidth(new Key("", "Back", "", ""), 2));
    }


    private void initRow2() {
        row2.getChildren().add(createKeyWithCustomWidth(new Key("", "Tab", "", ""), Perc.p8w()));
        row2.getChildren().add(createKey(new Key("", "Q", "ꩡ", "ၸ")));
        row2.getChildren().add(createKey(new Key("", "W", "ၻ", "တ")));
        row2.getChildren().add(createKey(new Key("", "E", "ꧣ", "ၼ")));
        row2.getChildren().add(createKey(new Key("", "R", "႞", "မ")));
        row2.getChildren().add(createKey(new Key("", "T", "ြ", "ဢ")));
        row2.getChildren().add(createKey(new Key("", "Y", "ၿ", "ပ")));
        row2.getChildren().add(createKey(new Key("", "U", "ၷ", "ၵ")));
        row2.getChildren().add(createKey(new Key("", "I", "ရ", "င")));
        row2.getChildren().add(createKey(new Key("", "O", "သ", "ဝ")));
        row2.getChildren().add(createKey(new Key("", "P", "ႀ", "ႁ")));
        row2.getChildren().add(createKey(new Key("{", "{", "[", "[")));
        row2.getChildren().add(createKey(new Key("}", "}", "]", "]")));
        row2.getChildren().add(createKeyWithCustomWidth(new Key("|", "|", "\\", "\\"), Perc.p7w()));
    }

    private void initRow3() {
        row3.getChildren().add(createKeyWithCustomWidth(new Key("", "Caps", "", ""), Perc.p10w()));
        row3.getChildren().add(createKey(new Key("", "A", "ဵ", "​ေ")));
        row3.getChildren().add(createKey(new Key("", "S", "ႅ", "​ႄ")));
        row3.getChildren().add(createKey(new Key("", "D", "ီ", "ိ")));
        row3.getChildren().add(createKey(new Key("", "F", "ႂ်", "်")));
        row3.getChildren().add(createKey(new Key("", "G", "ႂ", "ွ")));
        row3.getChildren().add(createKey(new Key("", "H", "့", "ႉ")));
        row3.getChildren().add(createKey(new Key("", "J", "ႆ", "ႇ")));
        row3.getChildren().add(createKey(new Key("", "K", "”", "ု")));
        row3.getChildren().add(createKey(new Key("", "L", "ႊ", "ူ")));
        row3.getChildren().add(createKey(new Key(";", ":", "း", "ႈ")));
        row3.getChildren().add(createKey(new Key("\"", "'", "“", "'")));
        row3.getChildren().add(createKeyWithCustomWidth(new Key("", "Enter", "", ""), Perc.p10w()));
    }

    private void initRow4() {
        row4.getChildren().add(createKeyWithCustomWidth(new Key("", "Shift", "", ""), Perc.p13w()));
        row4.getChildren().add(createKey(new Key("", "Z", "ၾ", "ၽ")));
        row4.getChildren().add(createKey(new Key("", "X", "ꩪ", "ထ")));
        row4.getChildren().add(createKey(new Key("", "C", "ꧠ", "ၶ")));
        row4.getChildren().add(createKey(new Key("", "V", "ꩮ", "လ")));
        row4.getChildren().add(createKey(new Key("", "B", "ျ", "ယ")));
        row4.getChildren().add(createKey(new Key("", "N", "႟", "ၺ")));
        row4.getChildren().add(createKey(new Key("", "M", "ႃ", "ၢ")));
        row4.getChildren().add(createKey(new Key(",", "<", "၊", ",")));
        row4.getChildren().add(createKey(new Key(">", ".", "။", ".")));
        row4.getChildren().add(createKey(new Key("?", "/", "?", "/")));
        row4.getChildren().add(createKeyWithCustomWidth(new Key("", "Shift", "", ""), Perc.p12w()));
    }

    private void initRow5() {
        row5.getChildren().add(createKeyWithCustomWidth(new Key("", "Ctrl", "", ""), Perc.p8w()));
        row5.getChildren().add(createKey(new Key("", "Win", "", "")));
        row5.getChildren().add(createKeyWithCustomWidth(new Key("", "Alt", "", ""), Perc.p7w()));
        row5.getChildren().add(createKeyWithCustomWidth(new Key("", "Space", "", ""), Perc.p30w()));
        row5.getChildren().add(createKeyWithCustomWidth(new Key("", "Alt", "", ""), Perc.p7w()));
        row5.getChildren().add(createKey(new Key("", "Win", "", "")));
        row5.getChildren().add(createKey(new Key("", "Menu", "", "")));
        row5.getChildren().add(createKeyWithCustomWidth(new Key("", "Ctrl", "", ""), Perc.p8w()));
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
//                    else if (eng.equalsIgnoreCase("\\"))
//                        row.getChildren().add(createKeyWithCustomWidth(new Key(engShift, eng, taiShift, tai), 2 * 0.02));
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
                        row.getChildren().add(createKeyWithCustomWidth(new Key("", eng, "", ""), 35 * 0.02));
                    else if (eng.equalsIgnoreCase("Win1") || eng.equalsIgnoreCase("Win2") || eng.equalsIgnoreCase("Menu"))
                        row.getChildren().add(createKeyWithCustomWidth(new Key("", tai, "", ""), 1 * 0.02));
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

        VBox vBox;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/key.fxml"));
        try {
            vBox = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setCharacterOnButton(vBox, key, "Myanmar Taungthu", 16);
//        vBox.setPrefSize(Perc.p5w(), Perc.p10h());
//        VBox.setVgrow(vBox, Priority.ALWAYS);
//        HBox.setHgrow(vBox, Priority.ALWAYS);

        return vBox;

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
//        VBox.setVgrow(vBox, Priority.ALWAYS);
//        HBox.setHgrow(vBox, Priority.ALWAYS);
//        System.out.println("Pref Width : " + vBox.getPrefWidth());
//        System.out.println("To add width : " + (vBox.getPrefWidth() * width));
        vBox.setPrefWidth(vBox.getPrefWidth() + (vBox.getPrefWidth() * width));
//        Label charTaiShift = (Label) ((HBox) vBox.getChildren().get(0)).getChildren().get(0);
//        charTaiShift.setText(key.getTaiShift());
//        Label charEngShift = (Label) ((HBox) vBox.getChildren().get(0)).getChildren().get(1);
//        charEngShift.setText(key.getEngShift());
//        Label charTai = (Label) ((HBox) vBox.getChildren().get(1)).getChildren().get(0);
//        charTai.setText(key.getTai());
//        Label charEng = (Label) ((HBox) vBox.getChildren().get(1)).getChildren().get(1);
//        charEng.setText(key.getEng());
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
        }
        return false;
    }

    public boolean prevLesson() {
        int currentIndex = cbLessons.getSelectionModel().getSelectedIndex();
        if (currentIndex != 0) {
            cbLessons.getSelectionModel().selectPrevious();
            return true;
        }
        return false;
    }

    public void blurScreen() {
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(20);
        btPrev.getScene().getRoot().setEffect(blur);

    }

    protected void whatIsKeyWidth() {
        VBox key1 = (VBox) row1.getChildren().get(0);
        System.out.println("Key1 width is - " + key1.getWidth());

        VBox key2 = (VBox) row1.getChildren().get(1);
        System.out.println("Key2 width is - " + key2.getBoundsInParent().getWidth());

    }

}
