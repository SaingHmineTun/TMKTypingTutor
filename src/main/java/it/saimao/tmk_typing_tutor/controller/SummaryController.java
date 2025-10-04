package it.saimao.tmk_typing_tutor.controller;

import it.saimao.tmk_typing_tutor.model.Theme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.DecimalFormat;

public class SummaryController {
    @FXML
    private Label sTitle;
    @FXML
    private Label sACCU;

    @FXML
private Label sAWPM;

    @FXML
    private Button sClose;

    @FXML
    private Label sMIST;

    @FXML
    private Button sNext;

    @FXML
    private Button sPrev;
    @FXML
    private Button sRetry;
    @FXML
    private Label sWPM;

    private final Stage stage;
    private final MainController mainController;
    private final Stage owner;
    private int wpm, mistype, awpm;
    private double accuracy;
    private String title;

    public SummaryController(MainController mainController, Stage owner) {
        this.mainController = mainController;
this.owner = owner; // Store the owner stage

        stage = new Stage();
        stage.initOwner(owner);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/summary.fxml"));
        loader.setController(this);
        Parent parent = null;
        try {
            parent = loader.load();
        } catch(IOException e){
e.printStackTrace();
        }
        initAction();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);

    }

    public void showDialog(String title, int wpm, double accuracy, int mistype,int awpm){
        this.title = title;
        this.wpm = wpm;
        this.awpm = awpm;
        this.accuracy = accuracy;
        this.mistype = mistype;

        stage.getScene().getStylesheets().addAll(owner.getScene().getStylesheets());
        stage.getScene().getStylesheets().add(getClass().getResource("/css/summary_style.css").toExternalForm());

        showSummary();
        stage.show();
    }

    private void initAction(){
        sClose.setOnAction(event->stage.close());
        sNext.setOnAction(event -> {
            if (mainController.nextLesson()) {
                stage.close();
            }
        });
        sRetry.setOnAction(actionEvent -> {
            mainController.retryLesson();
            stage.close();
        });
        sPrev.setOnAction(event -> {
            if (mainController.prevLesson()){
                stage.close();
}
        });
    }

    private void showSummary() {
        sTitle.setText(title);
        sWPM.setText(String.valueOf(wpm));
        sACCU.setText(new DecimalFormat("#0.00").format(accuracy * 100) + "%");
        sMIST.setText(String.valueOf(mistype));
sAWPM.setText(String.valueOf(awpm));
        sNext.requestFocus();

        // Get the selected theme from the main controller
        Theme selectedTheme = mainController.getCbTheme().getSelectionModel().getSelectedItem();
        String iconColor = selectedTheme != null ? selectedTheme.iconColor(): "white";
//Changeicon color for CLOSE, PREV, RETRY, NEXT according to the theme
        sClose.setGraphic(createIcon("close", iconColor));
        sPrev.setGraphic(createIcon("prev", iconColor));
        sRetry.setGraphic(createIcon("retry",iconColor));
       sNext.setGraphic(createIcon("next", iconColor));
        
        // Apply the theme's stylesheet to the stage
        String themeId = selectedTheme != null ? selectedTheme.id() : "light_theme";
        String stylesheet = getClass().getResource("/css/" + themeId + ".css").toExternalForm();
       stage.getScene().getStylesheets().add(stylesheet);
    }

    private ImageView createIcon(String iconName, String iconColor) {
       String imagePath = String.format("/images/%s_%s.png", iconName, iconColor);
        
        // Check if the specific icon exists, if not use appropriate fallback
        if (getClass().getResourceAsStream(imagePath) == null) {
            // For close, prev, next and retry icons, fallback to either dark orwhite depending on what's available
            if ("close".equals(iconName) || "prev".equals(iconName) || "next".equals(iconName) || "retry".equals(iconName)) {
                String fallbackColor = "white".equals(iconColor) ? "dark" : "white";
                String fallbackPath= String.format("/images/%s_%s.png", iconName, fallbackColor);
                if (getClass().getResourceAsStream(fallbackPath) != null) {
                    imagePath = fallbackPath;
                } else {
                    // If neither color variant exists, use the default one without color suffix
                    imagePath = String.format("/images/%s.png", iconName);
                }
            }
        }
        
        // Final check to ensure we have a valid image path
        if (getClass().getResourceAsStream(imagePath) == null) {
            imagePath = "/images/" + iconName + ".png";
        }
        
        Image image = new Image(getClass().getResourceAsStream(imagePath), 20, 20, true, true);
        return new ImageView(image);
    }
}