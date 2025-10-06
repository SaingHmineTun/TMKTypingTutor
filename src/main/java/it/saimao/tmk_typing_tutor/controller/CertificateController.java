package it.saimao.tmk_typing_tutor.controller;

import it.saimao.tmk_typing_tutor.model.User;
import it.saimao.tmk_typing_tutor.services.LessonProgressService;
import it.saimao.tmk_typing_tutor.utils.Toast;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
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
    private VBox vbCertificate;// 添加新的字段引用

    @FXML
    public void save() {
        // 创建文件选择器
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Certificate as PNG");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png")
        );
        fileChooser.setInitialFileName(lblUsername.getText() + "'s Certificate.png");

        // 显示文件选择对话框
        Stage stage = (Stage) btnClose.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                // 创建快照参数并设置3倍缩放
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                snapshotParameters.setTransform(javafx.scene.transform.Scale.scale(3.0, 3.0));

                // 获取 root 节点的快照（3倍尺寸）
                WritableImage writableImage = vbCertificate.snapshot(snapshotParameters, null);

                // 保存为 PNG 文件
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
                Toast.showToast(btnClose.getScene().getWindow(), "Saved certificate success", 2000);
            } catch (IOException e) {
                e.printStackTrace();
                // 可以添加错误提示对话框
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


    @FXML
    public void print() {
        // 创建打印任务
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job != null) {
            // 设置打印内容为整个证书面板
            boolean success = job.showPrintDialog(btnClose.getScene().getWindow());

            if (success) {
                // 执行打印
                boolean printed = job.printPage(vbCertificate);
                if (printed) {
                    job.endJob();
                }
            }
        }
    }

}