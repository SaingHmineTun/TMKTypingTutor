package it.saimao.tmk_typing_tutor.model;

import javafx.scene.control.Button;

public class LessonProgress {
    private int id;
    private int userId;
    private int levelIndex;
    private int lessonIndex;
    private int wpm;
    private double accuracy;
    private int mistakes;
    private Button retryButton;

    public LessonProgress() {
        this.retryButton = new Button("Retry");
    }

    public LessonProgress(int userId, int levelIndex, int lessonIndex, int wpm, double accuracy, int mistakes) {
        this.userId = userId;
        this.levelIndex = levelIndex;
        this.lessonIndex = lessonIndex;
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.mistakes = mistakes;
        this.retryButton = new Button("Retry");
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }

    public int getLessonIndex() {
        return lessonIndex;
    }

    public void setLessonIndex(int lessonIndex) {
        this.lessonIndex = lessonIndex;
    }

    public int getWpm() {
        return wpm;
    }

    public void setWpm(int wpm) {
        this.wpm = wpm;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }

    public Button getRetryButton() {
        return retryButton;
    }

    public void setRetryButton(Button retryButton) {
        this.retryButton = retryButton;
    }

    @Override
    public String toString() {
        return "LessonProgress{" +
                "id=" + id +
                ", userId=" + userId +
                ", levelIndex=" + levelIndex +
                ", lessonIndex=" + lessonIndex +
                ", wpm=" + wpm +
                ", accuracy=" + accuracy +
                ", mistakes=" + mistakes +
                '}';
    }
}