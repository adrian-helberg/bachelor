package de.haw;

import de.haw.gui.Template;
import de.haw.utils.Templates;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class GeneratorController {
    @FXML private ListView<Template> lstTemplates;
    @FXML private Text txtStatus;

    public GeneratorController() {}

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
            var filePath = Generator.class.getClassLoader().getResource("template_sample");
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
            var filePath = Generator.class.getClassLoader().getResource("template_sample");
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

    }

    @FXML private void showHowToUse() {

    }

    private void showTemplate(Template selectedTemplate) {

    }
}
