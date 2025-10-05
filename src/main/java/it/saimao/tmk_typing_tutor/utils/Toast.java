package it.saimao.tmk_typing_tutor.utils;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

public class Toast {
    public static void showToast(Window owner, String message, int durationMillis) {
        Stage ownerStage = (Stage) owner;

        Label label = new Label(message);
        label.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.8);" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10px;" +
                        "-fx-background-radius: 5px;"
        );

        VBox vbox = new VBox(label);
        vbox.setAlignment(Pos.CENTER);

        Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Scene scene = new Scene(vbox);
        scene.setFill(null);
        toastStage.setScene(scene);

        // 定位到窗口中央上方
        toastStage.setX(ownerStage.getX() + ownerStage.getWidth() / 2 - 100);
        toastStage.setY(ownerStage.getY() + ownerStage.getHeight() / 2);

        toastStage.show();

        // 自动关闭
        PauseTransition delay = new PauseTransition(Duration.millis(durationMillis));
        delay.setOnFinished(event -> toastStage.close());
        delay.play();
    }
}
