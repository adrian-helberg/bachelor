package de.haw;

import de.haw.gui.Template;
import de.haw.test.Node_;
import de.haw.tree.Anchor;
import de.haw.tree.Node;
import de.haw.tree.Tree;
import de.haw.turtle.TurtleGraphic;
import de.haw.utils.Anchors;
import de.haw.utils.Drawings;
import de.haw.utils.Templates;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mikera.vectorz.Vector;

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
        PrototypeController controller = null;
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

        var turtleGraphic = new TurtleGraphic(controller.getCanvas());
        var initialAnchor = new Anchor(turtleGraphic.getTurtle().getPosition());
        controller.loadTemplates();
        var n1 = new Node(initialAnchor, Templates.getTemplateFromID(1).instantiate());
        var n2 = new Node(Templates.getTemplateFromID(0).instantiate());

        // Build tree
        n1.attachNode(n2, n1.getHooks_().keySet().iterator().next());

        var n1_ = new Node_<>(Templates.getTemplateFromID(0), initialAnchor);

        var tree = new Tree(n1);
        turtleGraphic.drawTree(tree);
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
