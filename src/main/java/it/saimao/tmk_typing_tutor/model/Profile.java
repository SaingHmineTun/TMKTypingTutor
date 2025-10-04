package it.saimao.tmk_typing_tutor.model;

import javafx.scene.control.Button;

public class Profile {
    private String levelName;
    private String progress;
    private Button certificateButton;

    public Profile(String levelName, String progress) {
        this.levelName = levelName;
        this.progress = progress;
        this.certificateButton = new Button("Generate");
        this.certificateButton.setDisable(true); // Disabled by default
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
}
