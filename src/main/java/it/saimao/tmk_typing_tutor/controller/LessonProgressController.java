package it.saimao.tmk_typing_tutor.controller;

import it.saimao.tmk_typing_tutor.model.Lesson;
import it.saimao.tmk_typing_tutor.model.LessonProgress;
import it.saimao.tmk_typing_tutor.model.Theme;
import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.services.LessonProgressService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LessonProgressController implements Initializable {

    @FXML
    private TableView<LessonProgress> tvLessonProgress;
    @FXML
    private TableColumn<LessonProgress, String> tcLesson; // Changed from Integer to String
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

    private int levelIndex;
    private String[] levelNames = {"Level 1: Basic Shan Typing", "Level 2: Numbers and Punctuation", "Level 3: Advanced Shan Typing", "Level 4: Complex Texts"};
    private User user;
    private List<Lesson> lessonList; // Added to store lessons

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tcLesson.setCellValueFactory(new PropertyValueFactory<>("lessonTitle")); // Changed to lessonTitle
        tcWPM.setCellValueFactory(new PropertyValueFactory<>("wpm"));
        tcAccuracy.setCellValueFactory(new PropertyValueFactory<>("accuracy"));
        tcAccuracy.setCellFactory(new Callback<>() {
            @Override
            public TableCell<LessonProgress, Double> call(TableColumn<LessonProgress, Double> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setText(Math.round(item * 100) + "%");
                        }
                    }
                };
            }
        });
        tcMistakes.setCellValueFactory(new PropertyValueFactory<>("mistakes"));
        tcAction.setCellValueFactory(new PropertyValueFactory<>("retryButton"));

        // Set styling for the TableView
        tvLessonProgress.getStyleClass().add("progress-table");

        // Apply specific styles to columns
        tcLesson.getStyleClass().add("text-column");
        tcWPM.getStyleClass().add("text-column");
        tcAccuracy.getStyleClass().add("text-column");
        tcMistakes.getStyleClass().add("text-column");
        tcAction.getStyleClass().add("button-column");
    }

    public void initData(User user, int levelIndex) {
        this.levelIndex = levelIndex;
        this.user = user;
        lblLevel.setText(levelNames[levelIndex]);
        loadLessons(); // Load lessons first
        loadLessonProgress();
        applyTheme();
    }

    private void loadLessons() {
        lessonList = LessonProgressService.getLessonsForLevel(levelIndex);
    }

    private void applyTheme() {
        if (user != null && btnClose.getScene() != null) {
            Theme theme = Theme.fromIndex(user.getTheme());
            String stylesheet = getClass().getResource("/css/" + theme.id() + ".css").toExternalForm();

            Scene scene = btnClose.getScene();
            // Apply the user's selected theme
            scene.getStylesheets().clear();
            scene.getStylesheets().add(stylesheet);
        }
    }

    private void loadLessonProgress() {
        System.out.println("Loading lesson progress for User=" + user.getId() + ", Level=" + levelIndex);
        List<LessonProgress> lessonProgressList = LessonProgressService.getAllLessonProgressForLevel(user.getId(), levelIndex);
        System.out.println("Retrieved " + lessonProgressList.size() + " records");
        ObservableList<LessonProgress> observableList = FXCollections.observableArrayList(lessonProgressList);

        // Add retry buttons to each lesson progress and set lesson titles
        for (LessonProgress progress : observableList) {
            // Set the lesson title based on the lesson index
            if (lessonList != null && progress.getLessonIndex() < lessonList.size()) {
                Lesson lesson = lessonList.get(progress.getLessonIndex());
                progress.setLessonTitle(lesson.toString()); // Use the lesson's toString method
            } else {
                progress.setLessonTitle("Lesson " + (progress.getLessonIndex() + 1));
            }

            Button retryButton = new Button("Retry");
            retryButton.setOnAction(event -> retryLesson(progress));
            progress.setRetryButton(retryButton);
        }

        tvLessonProgress.setItems(observableList);
    }

    private void retryLesson(LessonProgress progress) {
        // Close the current dialog
        Stage currentStage = (Stage) btnClose.getScene().getWindow();
        currentStage.close();

        // Navigate to the lesson in the main application
        navigateToLessonInMainApplication(progress.getLevelIndex(), progress.getLessonIndex());
    }

    private void navigateToLessonInMainApplication(int levelIndex, int lessonIndex) {
        try {
            // Get the main controller instance
            MainController mainController = MainController.getInstance();
            if (mainController != null) {
                // Navigate to the specific lesson
                mainController.navigateToLesson(levelIndex, lessonIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}