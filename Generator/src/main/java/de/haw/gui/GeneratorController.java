package de.haw.gui;

import de.haw.Generator;
import de.haw.gui.structure.BranchingStructurePane;
import de.haw.gui.structure.Property;
import de.haw.gui.templates.TemplatePane;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Callback;

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
    private BranchingStructurePane paneBranchingStructure;
    private VBox selectedTemplateParent;

    @FXML public TitledPane titledPane_Branching_Structure;
    @FXML public TilePane tilePane_Templates;
    @FXML public ScrollPane scrollPane;

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

        paneBranchingStructure = new BranchingStructurePane(300,300);
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
        paneBranchingStructure.init(state);
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
        titledPane_Branching_Structure.getChildrenUnmodifiable().remove(paneBranchingStructure);
        paneBranchingStructure = new BranchingStructurePane(300,300);
        titledPane_Branching_Structure.setContent(paneBranchingStructure);
    }

    @FXML public void exit() {
        Platform.exit();
    }

    @FXML public void selectTemplate() {
        if (selectedTemplateParent == null) {
            selectedTemplateParent = new VBox();

            ObservableList<Property> data = FXCollections.observableArrayList();
            data.add(new Property("Size", 1.1));
            data.add(new Property("Rotation", 2.2));

            var tableView = new TableView<Property>();
            tableView.setEditable(true);
            tableView.setMinWidth(300);
            tableView.setMaxHeight(260);

            Callback<TableColumn<Property, String>, TableCell<Property, String>> cellFactory =
                    (TableColumn<Property, String> param) -> new PropertyCell();

            var nameColumn = new TableColumn<Property, String>("Property");
            nameColumn.setMinWidth(145);
            nameColumn.setMaxWidth(145);
            nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

            var valueColumn = new TableColumn<Property, String>("Value");
            valueColumn.setMinWidth(145);
            valueColumn.setMaxWidth(145);
            valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
            valueColumn.setCellFactory(cellFactory);
            valueColumn.setOnEditCommit(
                    (TableColumn.CellEditEvent<Property, String> t) -> {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).setValue(t.getNewValue());
                    }
            );

            tableView.getColumns().addAll(nameColumn, valueColumn);
            tableView.setItems(data);

            var titledPaneSelectedTemplate = new TitledPane("Selected Template", tableView);
            titledPaneSelectedTemplate.setCollapsible(false);
            titledPaneSelectedTemplate.setExpanded(true);
            selectedTemplateParent.getChildren().add(titledPaneSelectedTemplate);


            var applyButton = new Button("Apply");
            var cancelButton = new Button("Cancel");
            var bar = new HBox(cancelButton, applyButton);
            bar.setPadding(new Insets(10,10,10,10));
            bar.setSpacing(170);
            selectedTemplateParent.getChildren().add(bar);
        }
        scrollPane.setContent(selectedTemplateParent);
    }

    @FXML public void generate() {

    }

    public TemplatePane getSelectedTemplatePane() {
        return (TemplatePane) tilePane_Templates.getChildren().stream()
                .filter(c -> c instanceof TemplatePane)
                .filter(tg -> ((TemplatePane) tg).isSelected())
                .findFirst()
                .orElse(null);
    }

    public void setState(State state) {
        if (this.state != null) {
            throw new IllegalStateException("State can only be initialized once");
        }
        this.state = state;
    }

    class PropertyCell extends TableCell<Property, String> {
        private TextField textField;

        private PropertyCell() {}

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnAction((e) -> commitEdit(textField.getText()));
            textField.focusedProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean old, Boolean val) -> {
                if (!val) {
                    System.out.println("Commiting " + textField.getText());
                    commitEdit(textField.getText());
                }
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(item);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                        setGraphic(null);
                    }
                    setText(getString());
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(getItem());
            setGraphic(null);
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }
}