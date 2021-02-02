package de.haw;

import de.haw.gui.GeneratorController;
import de.haw.gui.State;
import de.haw.utils.Logging;
import javafx.application.Application;
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
public class Generator extends Application implements Logging {
    private final Logger LOGGER = getLogger();

    @Override
    public void start(Stage primaryStage) {
        Parent mainWindow = null;
        GeneratorController mainController = null;
        try {
            // Load main window
            var loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("Generator.fxml")
            ));
            LOGGER.info("Loaded main window FXML file");
            mainWindow = loader.load();
            mainController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(Objects.requireNonNull(mainWindow));
        primaryStage.setScene(scene);
        // General window properties
        primaryStage.setTitle("Generator");
        // Application icon
        Image icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("icon.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setMinWidth(620);
        primaryStage.setMinHeight(440);
        primaryStage.show();
        // Application state
        LOGGER.info("Create application state");
        State state = new State(primaryStage);
        mainController.init(state);
        // TODO: Remove
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