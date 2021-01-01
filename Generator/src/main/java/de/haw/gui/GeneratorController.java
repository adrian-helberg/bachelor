package de.haw.gui;

import de.haw.Generator;
import de.haw.gui.structure.Anchor;
import de.haw.gui.structure.BranchingStructurePane;
import de.haw.gui.templates.TemplatePane;
import de.haw.gui.templates.TurtleGraphic;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 *
 */
public class GeneratorController {
    private static final Logger LOGGER = Logger.getLogger(GeneratorController.class.getName());
    private State state;
    private TurtleGraphic paneBranchingStructure;

    @FXML public TitledPane titledPane_Branching_Structure;
    @FXML public TilePane tilePane_Templates;
    @FXML public TitledPane titledPane_templates;

    private VBox templateSelectionContainer;

    /**
     *
     */
    public GeneratorController() {}

    @FXML private void initialize() {
        tilePane_Templates.setVgap(4);
        tilePane_Templates.setHgap(4);
        tilePane_Templates.setPrefColumns(4);
        tilePane_Templates.setMaxWidth(Region.USE_PREF_SIZE);
        tilePane_Templates.setAlignment(Pos.TOP_CENTER);

        paneBranchingStructure = new BranchingStructurePane(
                300,
                300
        );
        var rootAnchor = new Anchor(
                (int) paneBranchingStructure.getTurtle().getPosition().get(0),
                (int) paneBranchingStructure.getTurtle().getPosition().get(1)
        );
        paneBranchingStructure.getChildren().add(rootAnchor);
        titledPane_Branching_Structure.setContent(paneBranchingStructure);
    }

    @FXML public void loadTemplates() {
        tilePane_Templates.getChildren().clear();
        try {
            var filePath = Generator.class.getClassLoader().getResource("templates");
            var sc = new Scanner(new File(Objects.requireNonNull(filePath).getPath()));

            String line;
            while (sc.hasNext()) {
                line = sc.next();
                var pane = new TemplatePane(
                        Integer.parseInt(Generator.properties.getProperty("template_width")),
                        Integer.parseInt(Generator.properties.getProperty("template_height")),
                        line
                );
                // Clicking
                pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    tilePane_Templates.getChildren().stream().filter(
                            c -> c instanceof TemplatePane).forEach(tg -> ((TemplatePane) tg).unselect()
                    );
                    ((TemplatePane)event.getSource()).select();
                });
                // Double clicking
                pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        selectTemplate();
                    }
                });
                // Hover
                pane.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
                    state.getScene().setCursor(Cursor.HAND);
                });
                pane.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
                    state.getScene().setCursor(Cursor.DEFAULT);
                });
                tilePane_Templates.getChildren().add(pane);
            }
        } catch (NullPointerException | FileNotFoundException e) {
            LOGGER.severe("Unable to find templates file ");
            // TODO: Handle exception
        }
    }

    @FXML public void openFileLocation() {
        try {
            var filePath = Generator.class.getClassLoader().getResource("templates");
            var file = new File(Objects.requireNonNull(filePath).getPath());
            Desktop.getDesktop().open(file.getParentFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void reset() {
        tilePane_Templates.getChildren().clear();
        paneBranchingStructure.getChildren().clear();
    }

    @FXML public void exit() {
        Platform.exit();
    }

    @FXML public void selectNextAnchor() {

    }

    @FXML public void selectTemplate() {
        var selected = getSelectedTemplate();
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Selected Template");
        alert.setContentText(selected.getWordNormalized());
        alert.showAndWait().ifPresent(x -> {
            if (x == ButtonType.OK) {
                alert.close();
            }
        });

        if (templateSelectionContainer != null)
    }

    @FXML public void generate() {

    }

    public TemplatePane getSelectedTemplate() {
        return (TemplatePane) tilePane_Templates.getChildren().stream()
                .filter(c -> c instanceof TemplatePane)
                .filter(tg -> ((TemplatePane) tg)
                .isSelected())
                .findFirst()
                .orElse(null);
    }

    public void setState(State state) {
        if (this.state != null) {
            throw new IllegalStateException("State can only be initialized once");
        }
        this.state = state ;
    }
}