package it.saimao.tmk_typing_tutor.model;

import javafx.scene.control.Button;

public class Profile {
    private String levelName;
    private String progress;
    private Button certificateButton;
    private Button detailsButton;
    private Button resetButton;
    private int levelIndex;

    public Profile(String levelName, String progress) {
        this.levelName = levelName;
        this.progress = progress;
        this.certificateButton = new Button("Generate");
        this.certificateButton.setDisable(true);
        this.detailsButton = new Button("Details");
        this.detailsButton.setDisable(true);
        this.resetButton = new Button("Reset");
        this.resetButton.setDisable(true);
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public Button getCertificateButton() {
        return certificateButton;
    }

    public void setCertificateButton(Button certificateButton) {
        this.certificateButton = certificateButton;
    }

    public Button getDetailsButton() {
        return detailsButton;
    }

    public void setDetailsButton(Button detailsButton) {
        this.detailsButton = detailsButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public void setResetButton(Button resetButton) {
        this.resetButton = resetButton;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }
}