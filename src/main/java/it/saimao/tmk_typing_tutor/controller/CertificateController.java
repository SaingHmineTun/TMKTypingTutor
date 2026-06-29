package it.saimao.tmk_typing_tutor.controller;

import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.services.LessonProgressService;
import it.saimao.tmk_typing_tutor.utils.Toast;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CertificateController {

    @FXML
    private Label lblUsername;
    @FXML
    private Label lblLevel;
    @FXML
    private Label lblAwpm;
    @FXML
    private Label lblDate;
    @FXML
    private Button btnClose;

    @FXML
    private Pane vbCertificate;

    @FXML
    public void save() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Certificate as PNG");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png")
        );
        fileChooser.setInitialFileName(lblUsername.getText() + "'s Certificate.png");

        Stage stage = (Stage) btnClose.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                snapshotParameters.setTransform(javafx.scene.transform.Scale.scale(3.0, 3.0));

                WritableImage writableImage = vbCertificate.snapshot(snapshotParameters, null);

                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                Toast.showToast(btnClose.getScene().getWindow(), "Saved certificate success", 2000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void initData(User user, String levelName, int levelIndex) {
        double averageWpm = LessonProgressService.getAverageWpmForLevel(user.getId(), levelIndex);
        DecimalFormat df = new DecimalFormat("#.##");
        initData(user.getDisplayName(), levelName, df.format(averageWpm));
    }

    public void initData(String displayName, String levelName, String averageWpm) {
        lblUsername.setText(displayName);
        lblLevel.setText(levelName);
        if (lblAwpm != null) {
            lblAwpm.setText(averageWpm + " WPM");
        }
        // Set current date
        if (lblDate != null) {
            lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        }
    }

    @FXML
    private void close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

}
