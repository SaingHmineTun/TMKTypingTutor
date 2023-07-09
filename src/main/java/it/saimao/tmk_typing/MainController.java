package it.saimao.tmk_typing;

import it.saimao.tmk_typing.model.Key;
import it.saimao.tmk_typing.model.Lesson;
import it.saimao.tmk_typing.utils.Perc;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    // TOP BAR
    @FXML
    private ImageView ivClose;
    @FXML
    private Button btNext, btPrev;
    @FXML
    private ComboBox<Lesson> cbLessons;

    @FXML
    private HBox row1, row2, row3, row4, row5;
    @FXML
    private TextField tfView;
    @FXML
    private TextField tfPractice;

    String[] row1Values = {"`", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "Back"};
    String[] row1ShiftValues = {"~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "Back"};
    String[] row2Values = {"Tab", "ၸ", "တ", "ၼ", "မ", "ဢ", "ပ", "ၵ", "င", "ဝ", "ႁ", "[", "]", "\\"};
    String[] row2ShiftValues = {"Tab", "ꩡ", "ၻ", "ꧣ", "႞", "ြ", "ၿ", "ၷ", "ရ", "သ", "ႀ", "{", "}", "|"};
    String[] row3Values = {"Caps", "ေ", "ႄ", "ိ", "်", "ွ", "ႉ", "ႇ", "ု", "ူ", "ႈ", "'", "Enter"};
    String[] row3ShiftValues = {"Caps", "ဵ", "ႅ", "ီ", "ႂ်", "ႂ", "့", "ႆ", "”", "ႊ", "း", "“", "Enter"};
    String[] row4Values = {"Shift", "ၽ", "ထ", "ၶ", "လ", "ယ", "ၺ", "ၢ", ",", ".", "/", "Shift"};
    String[] row4ShiftValues = {"Shift", "ၾ", "ꩪ", "ꧠ", "ꩮ", "ျ", "႟", "ႃ", "၊", "။", "?", "Shift"};

    String[][] allValues = {row1Values, row1ShiftValues, row2Values, row2ShiftValues, row3Values, row3ShiftValues, row4Values, row4ShiftValues, {}, {}};

    List<Lesson> lessonList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initRow1();
        initRow2();
        initRow3();
        initRow4();
        initRow5();
        initTopBar();
        initPracticeLessons();
        initViewValues();
        initPracticeListener();
    }

    private void initTopBar() {
        ivClose.setOnMouseClicked(event -> {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
    }

    private void initViewValues() {
        cbLessons.setItems(FXCollections.observableArrayList(lessonList));
        cbLessons.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            tfView.setText(newValue.getLesson());
            tfPractice.setText("");
            tfPractice.requestFocus();
            haha("", "");
        });
        cbLessons.getSelectionModel().select(0);

        // NEXT & PREVIOUS
        btNext.setOnAction(event -> {
            int currentIndex = cbLessons.getSelectionModel().getSelectedIndex();
            if (currentIndex != lessonList.size() - 1) {
                cbLessons.getSelectionModel().selectNext();
            }
        });

        // NEXT & PREVIOUS
        btPrev.setOnAction(event -> {
            int currentIndex = cbLessons.getSelectionModel().getSelectedIndex();
            if (currentIndex != 0) {
                cbLessons.getSelectionModel().selectPrevious();
            }
        });

    }

    private void initPracticeLessons() {
        lessonList = new ArrayList<>();
        String is = getClass().getResource("/assets/short_lessons.csv").getPath();
        try (BufferedReader br = new BufferedReader(new FileReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int no = Integer.parseInt(values[0].trim());
                String title = values[1].trim();
                System.out.println(no + " : " + values.length);
                String content = values[2].replace("\"", "").trim();
                lessonList.add(new Lesson(no, title, content));
            }
            // using short_lessons, we should reverse it before use it
            Collections.reverse(lessonList);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initPracticeListener() {
        haha("", "");
        tfPractice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tfPractice.getText().length() > tfView.getText().length()) {
                tfPractice.setText(oldValue);
                return;
            }
            haha(oldValue, newValue);
        });
    }

    private void haha(String oldValue, String newValue){
        // To be able to type ေ & ႄ first with SIL_Shan Keyman keyboard
        String testText = tfView.getText().replaceAll("([\\u1000-\\u1021\\u1075-\\u1081\\u1022\\u108f\\u1029\\u106e\\u106f\\u1086\\u1090\\u1091\\u1092\\u1097])([\\u1060-\\u1069\\u106c\\u106d\\u1070-\\u107c\\u1085\\u108a])?([\\u103b-\\u103e]*)?\\u1031", "\u1031$1$2$3");
        testText = testText.replaceAll("([\\u1000-\\u1021\\u1075-\\u1081\\u1022\\u108f\\u1029\\u106e\\u106f\\u1086\\u1090\\u1091\\u1092\\u1097])([\\u1060-\\u1069\\u106c\\u106d\\u1070-\\u107c\\u1085\\u108a])?([\\u103b-\\u103e]*)?\\u1084", "\u1084$1$2$3");
        String practiceText = tfPractice.getText().replaceAll("\u200B", "");
        int indexOfPractice = 0;
        if (tfPractice.getText() != null && tfPractice.getText().length() > 0) {
            // When typing ​ေ & ​ႄ with sil_shan, this key always comes and we have to omit it first
            // For the typing tutor to know exactly what key we need to type
            indexOfPractice = practiceText.length();
            String mustType = testText.substring(indexOfPractice - 1, indexOfPractice);
            String typing = practiceText.substring(indexOfPractice - 1, indexOfPractice);
            System.out.println("You must type - " + mustType);
            System.out.println("You are typing - " + typing);
            if ((!mustType.equals("‌ေ") || !typing.equals("ေ")) && !mustType.equals(typing)) {
                tfPractice.setText(tfPractice.getText().substring(0, indexOfPractice - 1));
                return;
            }

        }


//        System.out.println("TEST: " + tfView.getText().length() + " : " + index);
        if (indexOfPractice < tfView.getText().length()) {
            // Know which value to type next
            String valueToType = testText.substring(indexOfPractice, indexOfPractice + 1);
            if (valueToType.equals(" ")) {
                alsoThisValue("SPACE", 5);
            } else {
                for (int x = 0; x < allValues.length; x++) {
                    String[] row = allValues[x];
                    for (int y = 0; y < row.length; y++) {
                        String val = row[y];
//                        System.out.println(valueToType + " : " + val + (valueToType.equals(val)));
                        if (valueToType.equals(val)) {
                            typeThisValue(x, y);
                            if (x % 2 == 1) {
                                alsoThisValue("SHIFT", y);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private void alsoThisValue(String key, int col) {
        if (toTypeSecNode != null) {
            toTypeSecNode.setStyle("-fx-background-color: #000");
        }
        switch (key) {
            case "SHIFT":
                if (col > 5) {
                    // Enable Left Shift
                    toTypeSecNode = row4.getChildren().get(0);
                } else {
                    // Enable Right Shift
                    toTypeSecNode = row4.getChildren().get(row4.getChildren().size()-1);
                }
                break;
            case "SPACE":
                if (toTypeNode != null) {
                    toTypeNode.setStyle("-fx-background-color: #000;");
                }
                toTypeSecNode = row5.getChildren().get(3);
                break;
        }

        if (toTypeSecNode != null)
            toTypeSecNode.setStyle("-fx-background-color: #013098");
    }

    private Node toTypeNode;
    private Node toTypeSecNode;

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
            toTypeNode.setStyle("-fx-background-color: #013098");

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
        row1.getChildren().add(createKeyWithCustomWidth(new Key("", "Back", "", ""), Perc.p10w()));
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

    // Create a keyboard key with 5% width!
    private VBox createKey(Key key) {

        VBox vBox;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/key.fxml"));
        try {
            vBox = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        vBox.setPrefSize(Perc.p5w(), Perc.p10h());
        Label charTaiShift = (Label) ((HBox) vBox.getChildren().get(0)).getChildren().get(0);
        charTaiShift.setText(key.getTaiShift());
        Label charEngShift = (Label) ((HBox) vBox.getChildren().get(0)).getChildren().get(1);
        charEngShift.setText(key.getEngShift());
        Label charTai = (Label) ((HBox) vBox.getChildren().get(1)).getChildren().get(0);
        charTai.setText(key.getTai());
        Label charEng = (Label) ((HBox) vBox.getChildren().get(1)).getChildren().get(1);
        charEng.setText(key.getEng());
        return vBox;

    }

    private VBox createKeyWithCustomWidth(Key key, double width) {

        VBox vBox;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/key.fxml"));
        try {
            vBox = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        vBox.setPrefSize(width, Perc.p10h());
        Label charTaiShift = (Label) ((HBox) vBox.getChildren().get(0)).getChildren().get(0);
        charTaiShift.setText(key.getTaiShift());
        Label charEngShift = (Label) ((HBox) vBox.getChildren().get(0)).getChildren().get(1);
        charEngShift.setText(key.getEngShift());
        Label charTai = (Label) ((HBox) vBox.getChildren().get(1)).getChildren().get(0);
        charTai.setText(key.getTai());
        Label charEng = (Label) ((HBox) vBox.getChildren().get(1)).getChildren().get(1);
        charEng.setText(key.getEng());
        return vBox;

    }
}
