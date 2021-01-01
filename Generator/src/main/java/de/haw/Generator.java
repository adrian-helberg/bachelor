package de.haw;

import de.haw.gui.GeneratorController;
import de.haw.gui.State;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Generator class containing program entry point as JavaFX Application
 */
public class Generator extends Application {
    private static final Logger LOGGER = Logger.getLogger(Generator.class.getName());
    public static Properties properties;
    private GeneratorController controller;
    private State state;

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            var loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("Generator.fxml")
            ));
            root = loader.load();
            controller = loader.getController();;
        } catch (IOException e) {
            LOGGER.severe("Unable to load generator FXML file");
            e.printStackTrace();
        }
        Scene scene = new Scene(Objects.requireNonNull(root));
        primaryStage.setScene(scene);
        // General window properties
        primaryStage.setTitle("Generator");
        primaryStage.setResizable(false);
        // Application icon
        Image icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.show();

        // Application state
        state = new State(scene);

        // TODO: Remove
        controller.loadTemplates();
        controller.setState(state);

    }

    @Override
    public void init() {
        properties = new Properties();
        BufferedInputStream stream = null;
        var filename = "generator.properties";
        try {
            stream = new BufferedInputStream(new FileInputStream(Objects.requireNonNull(
                    getClass().getClassLoader().getResource(filename)).getFile()));
            properties.load(stream);
        } catch (FileNotFoundException e) {
            LOGGER.severe("Properties file ["+ filename + "] not found");
            // TODO: Handle exception
        } catch (IOException e) {
            LOGGER.severe("Unable to load properties from file");
            // TODO: Handle exception
        }
    }

    /**
     * Program entry points
     * @param args program arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}