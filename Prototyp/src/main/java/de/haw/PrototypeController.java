package de.haw;

import de.haw.gui.Template;
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

        // Canvas mouse events
//        canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
//            System.out.println("canvas clicked: (" + event.getX() + "|" + event.getY() + ")");
//        });
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

        showTemplate(selectedTemplate);
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
        Parent parent = null;
        try {
            var loader = new FXMLLoader(Objects.requireNonNull(
                    Prototype.class.getClassLoader().getResource("About.fxml")));
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Stage dialog = new Stage();
        var scene = new Scene(Objects.requireNonNull(parent));
        dialog.setHeight(400);
        dialog.setWidth(400);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Show Template");
        dialog.setResizable(false);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    @FXML private void showHowToUse() {
        Parent parent = null;
        try {
            var loader = new FXMLLoader(Objects.requireNonNull(
                    Prototype.class.getClassLoader().getResource("HowToUse.fxml")));
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Stage dialog = new Stage();
        var scene = new Scene(Objects.requireNonNull(parent));
        dialog.setHeight(400);
        dialog.setWidth(400);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Show Template");
        dialog.setResizable(false);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void showTemplate(Template selectedTemplate) {
        Parent parent = null;
        ShowController controller = null;
        try {
            var loader = new FXMLLoader(Objects.requireNonNull(
                    Prototype.class.getClassLoader().getResource("Show.fxml")));
            parent = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Stage dialog = new Stage();
        var scene = new Scene(Objects.requireNonNull(parent));
        dialog.setHeight(400);
        dialog.setWidth(400);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Show Template");
        dialog.setResizable(false);
        dialog.setScene(scene);

        controller.draw(selectedTemplate);
        dialog.showAndWait();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Text getText() {
        return txtStatus;
    }
}
