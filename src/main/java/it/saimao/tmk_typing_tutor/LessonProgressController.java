package it.saimao.tmk_typing_tutor;

import it.saimao.tmk_typing_tutor.model.LessonProgress;
import it.saimao.tmk_typing_tutor.utils.LessonProgressService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class LessonProgressController implements Initializable {

    @FXML
    private TableView<LessonProgress>tvLessonProgress;
    @FXML
    private TableColumn<LessonProgress, Integer> tcLesson;
    @FXML
    private TableColumn<LessonProgress, Integer> tcWPM;
    @FXML
    private TableColumn<LessonProgress, Double> tcAccuracy;
    @FXML
    private TableColumn<LessonProgress, Integer> tcMistakes;
    @FXML
    private TableColumn<LessonProgress, Button> tcAction;
    @FXML
    private Label lblLevel;
    @FXML
    private Button btnClose;

    private int userId;
    private int levelIndex;
    private String[] levelNames = {"Level 1:Basic Shan Typing", "Level 2: Numbers and Punctuation", "Level 3: Advanced Shan Typing", "Level 4: Complex Texts"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcLesson.setCellValueFactory(new PropertyValueFactory<>("lessonIndex"));
        tcWPM.setCellValueFactory(new PropertyValueFactory<>("wpm"));
        tcAccuracy.setCellValueFactory(new PropertyValueFactory<>("accuracy"));
        tcMistakes.setCellValueFactory(new PropertyValueFactory<>("mistakes"));
        tcAction.setCellValueFactory(new PropertyValueFactory<>("retryButton"));
    }

    public void initData(int userId, int levelIndex) {
        this.userId =userId;
        this.levelIndex = levelIndex;
        lblLevel.setText(levelNames[levelIndex]);
        loadLessonProgress();
    }

    private void loadLessonProgress() {
        System.out.println("Loading lesson progress for User=" + userId + ", Level=" + levelIndex);
        List<LessonProgress> lessonProgressList = LessonProgressService.getAllLessonProgressForLevel(userId, levelIndex);
        System.out.println("Retrieved " + lessonProgressList.size() + " records");
        ObservableList<LessonProgress> observableList = FXCollections.observableArrayList(lessonProgressList);
        
        // Add retry buttons to each lesson progress
        for (LessonProgress progress : observableList) {
            Button retryButton = new Button("Retry");
            retryButton.setOnAction(event -> retryLesson(progress));
            progress.setRetryButton(retryButton);
}
        
        tvLessonProgress.setItems(observableList);
    }

    private void retryLesson(LessonProgress progress) {
        // This would be implemented to allow the user to retry the lesson
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Retry Lesson");
        alert.setHeaderText(null);
        DecimalFormat df = new DecimalFormat("#.##");
        alert.setContentText("To retry lesson " + progress.getLessonIndex() + 
                           "\nWPM: " + progress.getWpm() + 
                           "\nAccuracy: " + df.format(progress.getAccuracy() * 100) + "%" +
"\nMistakes: " + progress.getMistakes());
        alert.showAndWait();
    }

    @FXML
    private void close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}