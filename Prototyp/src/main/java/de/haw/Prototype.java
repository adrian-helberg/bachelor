package de.haw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

enum MODALS {
    ABOUT, HOWTOUSE
}

public class Prototype extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up FXML from resources
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("Prototype.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(Objects.requireNonNull(root));
        primaryStage.setScene(scene);
        // General window properties
        primaryStage.setTitle("Prototyp");
        primaryStage.setResizable(false);
        // TODO: Application icon
        Image icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.show();
    }

    static Scene getModalScene(MODALS key) {
        URL resource = null;
        switch (key) {
            case ABOUT:
                resource = Prototype.class.getClassLoader().getResource("About.fxml");
                break;
            case HOWTOUSE:
                resource = Prototype.class.getClassLoader().getResource("HowToUse.fxml");
        }

        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(resource));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Scene(Objects.requireNonNull(parent));
    }
}
