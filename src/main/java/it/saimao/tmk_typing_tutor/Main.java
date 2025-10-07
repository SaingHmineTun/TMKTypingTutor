package it.saimao.tmk_typing_tutor;

import it.saimao.tmk_typing_tutor.services.DatabaseService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

// UUID : e4b0c9d6-7f3a-4b8a-9c3f-2d6f8e1a5b7c

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        DatabaseService.initializeDatabase();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout/login.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/app_icon.png").toExternalForm()));
        primaryStage.setTitle("TMK Typing Tutor");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}