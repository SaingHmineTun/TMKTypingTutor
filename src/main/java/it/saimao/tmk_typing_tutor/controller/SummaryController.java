package it.saimao.tmk_typing_tutor.controller;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        initAction();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);

    }

    public void showDialog(String title, int wpm, double accuracy, int mistype, int awpm) {
        this.title = title;
        this.wpm = wpm;
        this.awpm = awpm;
        this.accuracy = accuracy;
        this.mistype = mistype;

        // Apply summary-specific styles first
        stage.getScene().getStylesheets().add(getClass().getResource("/css/summary_style.css").toExternalForm());
        // Apply the main theme from the owner window (this will override summary styles where needed)
        stage.getScene().getStylesheets().addAll(owner.getScene().getStylesheets());

        showSummary();
        stage.show();
    }

    private void initAction() {
        sClose.setOnAction(event -> stage.close());
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
            if (mainController.prevLesson()) {
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

        String iconColor = mainController.getCbTheme().getSelectionModel().getSelectedItem().iconColor();
        // Change icon color for CLOSE, PREV, RETRY, NEXT according to the theme
        sClose.setGraphic(createIcon("close", iconColor));
        sPrev.setGraphic(createIcon("prev", iconColor));
        sRetry.setGraphic(createIcon("retry", iconColor));
        sNext.setGraphic(createIcon("next", iconColor));
    }

    private ImageView createIcon(String iconName, String iconColor) {
        String imagePath = String.format("/images/%s_%s.png", iconName, iconColor);
        Image image = new Image(getClass().getResourceAsStream(imagePath), 20, 20, true, true);
        return new ImageView(image);
    }
}