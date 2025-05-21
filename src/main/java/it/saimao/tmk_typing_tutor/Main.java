package it.saimao.tmk_typing_tutor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        System.setProperty("prism.allowhidpi", "true");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/main.fxml"));
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainController mainController = loader.getController();
        primaryStage.setOnShown(windowEvent -> mainController.reqFocusOnPracticeField());
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(parent, screen.getWidth(), screen.getHeight());
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/app_icon.png").toExternalForm()));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
}
