package de.haw.gui;

import de.haw.Generator;
import de.haw.gui.structure.BranchingStructurePane;
import de.haw.gui.structure.Property;
import de.haw.gui.templates.TemplatePane;
import javafx.application.Platform;
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
    @FXML public HBox hBox_Templates;
    @FXML public Button btn_Select_Template;
    @FXML public Button btn_Generate;

    /**
     *
     */
    public GeneratorController() {}

    @FXML private void initialize() {
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
                line = sc.nextLine();
                if (line.startsWith("%") || line.isEmpty()) continue;
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
            e.printStackTrace();
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
        cancel();
        state.reset();
        tilePane_Templates.getChildren().clear();
        paneBranchingStructure = new BranchingStructurePane(300,300);
        paneBranchingStructure.init(state);
        titledPane_Branching_Structure.setContent(paneBranchingStructure);
    }

    @FXML public void exit() {
        Platform.exit();
    }

    @FXML public void selectTemplate() {
        var selectedTemplate = getSelectedTemplatePane();
        if (selectedTemplate == null) return;
        if (selectedTemplateParent == null) {
            selectedTemplateParent = new VBox();

            // TODO: Example data; Remove
            ObservableList<Property> data = FXCollections.observableArrayList();
            data.add(selectedTemplate.getProperty("Scaling"));
            data.add(selectedTemplate.getProperty("Rotation"));
            //data.add(selectedTemplate.getProperty("Branching angle"));

            // Property table
            TableView<Property> tableView = initPropertyTable();
            tableView.setItems(data);

            // Selected template view
            initSelectedTemplateView(tableView);
        }
        // Disable pane click events
        paneBranchingStructure.setClickable(false);
        // Attach template to the branching structure as draft
        paneBranchingStructure.parseWord(getSelectedTemplatePane(), true);

        btn_Select_Template.setDisable(true);
        btn_Generate.setDisable(true);
        scrollPane.setContent(selectedTemplateParent);
    }

    private void initSelectedTemplateView(TableView<Property> tableView) {
        var titledPaneSelectedTemplate = new TitledPane("Selected Template", tableView);
        titledPaneSelectedTemplate.setCollapsible(false);
        titledPaneSelectedTemplate.setExpanded(true);
        selectedTemplateParent.getChildren().add(titledPaneSelectedTemplate);

        var applyButton = new Button("Apply");
        applyButton.setOnAction(e -> apply());
        var cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> cancel());
        var bar = new HBox(cancelButton, applyButton);
        bar.setPadding(new Insets(10,10,10,10));
        bar.setSpacing(170);
        selectedTemplateParent.getChildren().add(bar);
    }

    private TableView<Property> initPropertyTable() {
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
                    t.getTableView().getItems()
                            .get(t.getTablePosition().getRow()).setValue(t.getNewValue());
                    // TODO: Redraw template draft
                    paneBranchingStructure.parseWord(getSelectedTemplatePane(), true);
                }
        );

        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(valueColumn);
        return tableView;
    }

    @FXML public void generate() {

    }

    public void apply() {
        cancel();
        paneBranchingStructure.parseWord(getSelectedTemplatePane(), false);
        state.getSelectedAnchor().use();
        state.selectFirst();
        paneBranchingStructure.updateTurte(state.getSelectedAnchor().getTurtle());
    }

    public void cancel() {
        btn_Select_Template.setDisable(false);
        btn_Generate.setDisable(false);
        scrollPane.setContent(hBox_Templates);
        paneBranchingStructure.setClickable(true);
        paneBranchingStructure.getChildren().removeAll(state.getCurrentDraft());
        state.clearCurrentDraft();
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
        paneBranchingStructure.init(state);
    }

    static class PropertyCell extends TableCell<Property, String> {
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