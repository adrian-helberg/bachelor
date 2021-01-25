package de.haw;

import de.haw.gui.GeneratorController;
import de.haw.gui.State;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Generator class containing program entry point as JavaFX Application
 */
public class Generator extends Application {
    private Logger logger = Logger.getLogger(getClass().getName());
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
            logger.info("Loaded main window FXML file");
            mainController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(Objects.requireNonNull(mainWindow));
        primaryStage.setScene(scene);
        // General window properties
        primaryStage.setTitle("Generator");
//        primaryStage.setResizable(false);
        // Application icon
        Image icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.show();

        // Application state
        state = new State(primaryStage);
        logger.info("Created application state " + state);
        mainController.setState(state);
        mainController.loadTemplates();
    }

    /**
     * Program entry points
     * @param args program arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}