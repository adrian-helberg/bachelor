package de.haw;

import de.haw.gui.Template;
import de.haw.tree.Anchor;
import de.haw.utils.Templates;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class PrototypeController {
    @FXML private Canvas canvas;
    @FXML private ListView<Template> lstTemplates;
    @FXML private Text txtStatus;

    public PrototypeController() {}

    @FXML private void initialize() {
        // List view cell factory
        lstTemplates.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Template template, boolean empty) {
                super.updateItem(template, empty);
                if (!empty && template != null && !template.getWord().isEmpty()) {
                    setGraphic(template.getCanvas());
                }
            }
        });
        // List view click handler
        lstTemplates.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                select();
            }
        });
    }

    @FXML private void reset() {
        txtStatus.setText("");
        txtStatus.setFill(Color.BLACK);
        Templates.clearEntries();
        lstTemplates.getItems().clear();
        canvas.getGraphicsContext2D().clearRect(0,0,canvas.getHeight(), canvas.getWidth());
    }

    @FXML private void select() {
        var selectedTemplate = lstTemplates.getSelectionModel().getSelectedItem();
        if (selectedTemplate == null) {
            txtStatus.setText("Please select a template first");
            txtStatus.setFill(Color.RED);
        } else {
            txtStatus.setText("You selected: " + selectedTemplate.getID());
            txtStatus.setFill(Color.GREEN);
        }

        Parent root = null;
        ShowController controller;
        try {
            var loader = new FXMLLoader(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("Show.fxml")
            ));
            root = loader.load();
            controller = loader.getController();
            controller.draw(lstTemplates.getSelectionModel().getSelectedItem());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Show Template");
        stage.setScene(new Scene(Objects.requireNonNull(root), 200, 200));
        stage.setResizable(false);
        stage.show();
    }

    @FXML private void exit() {
        System.exit(0);
    }

    @FXML private void nextAnchor() {

    }

    @FXML private void generate() {

    }

    @FXML private void openFileLocation() {
        try {
            var filePath = Prototype.class.getClassLoader().getResource("template_sample");
            var file = new File(Objects.requireNonNull(filePath).getPath());
            Desktop.getDesktop().open(file.getParentFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void loadTemplates() {
        reset();
        Scanner sc = null;
        try {
            var filePath = Prototype.class.getClassLoader().getResource("template_sample");
            if (filePath == null) throw new IOException("Templates file not found");
            sc = new Scanner(new File(filePath.getPath()));

            String line;
            ObservableList<Template> templateList = FXCollections.observableArrayList();
            while (sc.hasNext()) {
                line = sc.next();
                var template = new Template(line);
                templateList.add(template);
            }
            lstTemplates.setItems(templateList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sc != null) sc.close();
        }
    }

    @FXML private void showAbout() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("About");
        dialog.setScene(Prototype.getModalScene(MODALS.ABOUT));
        dialog.showAndWait();
    }

    @FXML private void showHowToUse() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("How to use");
        dialog.setScene(Prototype.getModalScene(MODALS.HOWTOUSE));
        dialog.showAndWait();
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
