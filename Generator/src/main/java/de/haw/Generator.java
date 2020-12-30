package de.haw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class Generator extends Application {
    public static Properties properties;
    private static final Logger LOGGER = Logger.getLogger(Generator.class.getName());
    private GeneratorController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        properties = new Properties();
        try {
            BufferedInputStream stream = new BufferedInputStream(
                    new FileInputStream(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("generator.properties")
                    ).getFile())
            );
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Set up FXML from resources
        Parent root = null;
        try {
            var loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("Generator.fxml")
            ));
            root = loader.load();
            controller = loader.getController();;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(Objects.requireNonNull(root));
        primaryStage.setScene(scene);
        // General window properties
        primaryStage.setTitle("Prototype");
        primaryStage.setResizable(false);
        // Application icon
        Image icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.show();

        // TEST TODO: Remove
        controller.loadTemplates();
    }
}
