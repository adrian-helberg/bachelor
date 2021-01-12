package de.haw;

import de.haw.gui.GeneratorController;
import de.haw.gui.State;
import de.haw.tree.Tree;
import javafx.application.Application;
import javafx.application.Platform;
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

/**
 * Generator class containing program entry point as JavaFX Application
 */
public class Generator extends Application {
    private static final Logger LOGGER = Logger.getLogger(Generator.class.getName());
    public static Properties properties;

    private State state;

    @Override
    public void start(Stage primaryStage) {
        Parent mainWindow = null;
        GeneratorController mainController = null;
        try {
            // Load main window
            var loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("Generator.fxml")
            ));
            mainWindow = loader.load();
            mainController = loader.getController();
        } catch (IOException e) {
            LOGGER.severe("Unable to load generator FXML file");
            e.printStackTrace();
        }
        Scene scene = new Scene(Objects.requireNonNull(mainWindow));
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
        mainController.setState(state);
    }

    @Override
    public void init() {
        properties = new Properties();
        BufferedInputStream stream;
        var filename = "generator.properties";
        try {
            stream = new BufferedInputStream(new FileInputStream(Objects.requireNonNull(
                    getClass().getClassLoader().getResource(filename)).getFile()));
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exit() {
        Platform.exit();
    }

    /**
     * Program entry points
     * @param args program arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}