package de.haw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Prototyp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("Prototyp.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(Objects.requireNonNull(root));
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
