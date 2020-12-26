package de.haw;

import de.haw.tree.Node;
import de.haw.tree.Tree;
import de.haw.turtle.TurtleGraphic;
import de.haw.utils.Templates;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Prototype extends Application implements EventHandler {
    public static Properties properties;
    private static final Logger LOGGER = Logger.getLogger(Prototype.class.getName());

    private Tree tree;
    private TurtleGraphic turtleGraphic;
    private PrototypeController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        properties = new Properties();
        try {
            BufferedInputStream stream = new BufferedInputStream(
                    new FileInputStream(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("app.properties")
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
                    getClass().getClassLoader().getResource("Prototype.fxml")
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

        turtleGraphic = new TurtleGraphic(controller.getCanvas());
        turtleGraphic.setScene(scene);
    }

    @Override
    public void handle(Event event) {
        LOGGER.info("Handle: " + event);
    }
}
