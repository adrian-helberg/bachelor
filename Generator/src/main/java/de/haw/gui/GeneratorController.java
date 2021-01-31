package de.haw.gui;

import de.haw.Generator;
import de.haw.gui.shape.Anchor;
import de.haw.gui.structure.BranchingStructurePane;
import de.haw.gui.structure.Draft;
import de.haw.gui.structure.Property;
import de.haw.gui.template.TemplatePane;
import de.haw.gui.turtle.TurtleGraphic;
import de.haw.pipeline.pipe.*;
import de.haw.pipeline.Pipeline;
import de.haw.tree.Template;
import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;
import de.haw.utils.Logging;
import de.haw.utils.Templates;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Generator controller for corresponding FXML file to be called on application's FXMLLoader.load()
 */
public class GeneratorController implements Logging {
    private final Logger logger = getLogger();
    // Application state
    private State state;
    // Branching structure view pane
    private BranchingStructurePane paneBranchingStructure;
    // Stored template selection view parent
    private VBox selectedTemplateParent;
    // FXML elements bound to this controller
    @FXML public SplitPane splitPane;
    @FXML public TitledPane titledPane_Branching_Structure;
    @FXML public TitledPane titledPane_Templates;
    @FXML public TilePane tilePane_Templates;
    @FXML public ScrollPane scrollPane;
    @FXML public HBox hBox_Templates;
    @FXML public Button btn_Select_Template;
    @FXML public Button btn_Generate;
    // Options
    @FXML public TextField iterations;
    @FXML public TextField generations;
    @FXML public TextField rules;
    @FXML public TextField merges;

    /**
     * public constructor without any parameters required by JavaFX framework
     */
    public GeneratorController() {}

    // METHODS
    /**
     * Initializes a new branching structure pane and sets it to the view parent
     */
    @FXML private void initialize() {
        logger.info("Initialize Generator Controller");
        paneBranchingStructure = new BranchingStructurePane(300,300);
        // Bind branching structure pane size to its parent size
        titledPane_Branching_Structure.prefWidthProperty().bind(splitPane.widthProperty());
        titledPane_Branching_Structure.prefHeightProperty().bind(splitPane.heightProperty());
        titledPane_Branching_Structure.setContent(paneBranchingStructure);
        // Bind templates view size to its parents sizes
        hBox_Templates.prefWidthProperty().bind(scrollPane.widthProperty());
        hBox_Templates.prefHeightProperty().bind(scrollPane.heightProperty());
        titledPane_Templates.prefWidthProperty().bind(hBox_Templates.widthProperty());
        titledPane_Templates.prefHeightProperty().bind(hBox_Templates.heightProperty());
        // Prevent right side to resize; Let only left side resize
        SplitPane.setResizableWithParent(scrollPane, false);
        makeTextFieldNumerous(iterations, false);
        makeTextFieldNumerous(generations, false);
        makeTextFieldNumerous(rules, true);
        makeTextFieldNumerous(merges, true);
    }

    private void makeTextFieldNumerous(TextField textField, boolean floating) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (floating) {
                if (!newValue.matches("[+-]?((\\d+\\.?\\d*)|(\\.\\d+))")) Platform.runLater(() -> textField.setText("0"));
            } else {
                if (!newValue.matches("\\d*")) Platform.runLater(() -> textField.setText("0"));
            }
        });
    }

    /**
     * Loads templates from templates file (resource folder)
     */
    @FXML public void loadTemplates() {
        logger.info("Load templates");
        tilePane_Templates.getChildren().clear();

        try {
            // Resource folder templates file path
            var filePath = Generator.class.getClassLoader().getResource("templates");
            var sc = new Scanner(new File(Objects.requireNonNull(filePath).getPath()));
            // Current file line
            String line;
            while (sc.hasNext()) {
                line = sc.nextLine();
                // File comments start with a %-sign
                if (line.startsWith("%") || line.isEmpty()) continue;
                // Create a template out of the read line
                var pane = new TemplatePane( 100, 100, new Template(line));
                logger.info(pane.getTemplate().toString());
                // Register mouse clicking event
                pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    tilePane_Templates.getChildren().stream().filter(
                            c -> c instanceof TemplatePane).forEach(tg -> ((TemplatePane) tg).unselect()
                    );
                    ((TemplatePane)event.getSource()).select();
                });
                // Register mouse double clicking event
                pane.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        selectTemplate();
                    }
                });
                // Register mouse hover events
                pane.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
                    state.getScene().setCursor(Cursor.HAND);
                });
                pane.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
                    state.getScene().setCursor(Cursor.DEFAULT);
                });
                // Add template to the corresponding view tile pane
                tilePane_Templates.getChildren().add(pane);
            }
        } catch (NullPointerException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens an explorer window where the templates file is located at
     */
    @FXML public void openFileLocation() {
        try {
            var filePath = Generator.class.getClassLoader().getResource("templates");
            var file = new File(Objects.requireNonNull(filePath).getPath());
            Desktop.getDesktop().open(file.getParentFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the application to a state like the program has started yet
     */
    @FXML public void reset() {
        cancel();
        state.reset();
        tilePane_Templates.getChildren().clear();
        paneBranchingStructure = new BranchingStructurePane(300,300);
        paneBranchingStructure.init(state);
        titledPane_Branching_Structure.setContent(paneBranchingStructure);
    }

    /**
     * System exit
     */
    @FXML public void exit() {
        Platform.exit();
    }

    /**
     * Selects a template by switching the templates-view with a selected-template-view
     */
    @FXML public void selectTemplate() {
        var selectedTemplatePane = getSelectedTemplatePane();
        if (selectedTemplatePane == null) return;
        // Reset the selected template view parent
        selectedTemplateParent = new VBox();
        // Bind selected template view size to its parent size
        selectedTemplateParent.prefHeightProperty().bind(scrollPane.heightProperty());
        // Create template instance and attach it to the branching structure as draft
        var templateInstance = new TemplateInstance(selectedTemplatePane.getTemplate());
        // Create default data
        ObservableList<Property> data = FXCollections.observableArrayList();
        templateInstance.getParameters().forEach((name, value) -> data.add(new Property(name, (Float) value)));
        // Property table
        TableView<Property> tableView = initPropertyTable();
        tableView.setItems(data);
        // Create selected template view
        initSelectedTemplateView(tableView);
        // Disable pane click events
        paneBranchingStructure.setClickable(false);
        // Update application state accordingly
        state.setCurrentDraft(new Draft(templateInstance.getTemplate()));
        // Draw draft
        paneBranchingStructure.parseWord(templateInstance, true);
        // Disable controls that should not be used in this state
        btn_Select_Template.setDisable(true);
        btn_Generate.setDisable(true);
        // Attach selected view parent to the scroll pane (right side of the application)
        scrollPane.setContent(selectedTemplateParent);
    }

    private void initSelectedTemplateView(TableView<Property> tableView) {
        VBox box = new VBox(tableView);
        tableView.prefHeightProperty().bind(box.heightProperty());
        var titledPaneSelectedTemplate = new TitledPane("Selected Template", box);
        titledPaneSelectedTemplate.setCollapsible(false);
        titledPaneSelectedTemplate.setExpanded(true);
        titledPaneSelectedTemplate.prefHeightProperty().bind(selectedTemplateParent.heightProperty());
        titledPaneSelectedTemplate.setAlignment(Pos.TOP_CENTER);

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

    /**
     * Creates a template view of properties with a name and a value
     * @return New JavaFX TableView
     */
    private TableView<Property> initPropertyTable() {
        var tableView = new TableView<Property>();
        tableView.setEditable(true);
        var tableWidth = 270;
        tableView.setMaxWidth(tableWidth);
        // Cell factory
        Callback<TableColumn<Property, String>, TableCell<Property, String>> cellFactory =
                (TableColumn<Property, String> param) -> new PropertyCell();

        var columnSize = tableWidth / 2 - 1;
        var nameColumn = new TableColumn<Property, String>("Property");
        nameColumn.setMinWidth(columnSize);
        nameColumn.setMaxWidth(columnSize);
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        // Column value
        var valueColumn = new TableColumn<Property, String>("Value");
        valueColumn.setMinWidth(columnSize);
        valueColumn.setMaxWidth(columnSize);
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        valueColumn.setCellFactory(cellFactory);
        // Register a function to be called on cell change
        valueColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Property, String> t) -> {
                    // Set new value to table data
                    t.getTableView().getItems().get(t.getTablePosition().getRow()).setValue(t.getNewValue());
                    // get current draft from application state
                    var templateInstance = state.getCurrentDraft();
                    // Set parameter that corresponding property has changed
                    templateInstance.setParameter(t.getRowValue().getName(), Float.parseFloat(t.getRowValue().getValue()));
                    // Redraw template instance as draft
                    paneBranchingStructure.parseWord(templateInstance, true);
                }
        );
        // Add columns to table
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(valueColumn);

        return tableView;
    }

    /**
     * Generates the tree structure. TODO
     */
    @FXML public void generate() {
        var numberOfIterations = Float.parseFloat(iterations.getText());
        var numberOfGenerations = Float.parseFloat(generations.getText());
        // Pipeline context
        var ctx = new PipelineContext();
        ctx.tree = state.getTree();
        ctx.randomizer = state.getRandomizer();

        if (ctx.tree.isEmpty()) return;

        ctx.wL = Float.parseFloat(rules.getText());
        ctx.w0 = Float.parseFloat(merges.getText());
        // Execute pipeline
        var result = new Pipeline<>(new InfererPipe())
                .pipe(new CompressorPipe())
                .pipe(new GeneralizerPipe())
                .pipe(new EstimatorPipe())
                .execute(ctx);

        for (int i = 0; i < numberOfGenerations; i++) {
            Platform.runLater(() -> {
                var derivation = result.lSystem.deriveAndPopulate((int) numberOfIterations, ctx.estimator);

                var turtleGraphic = new TurtleGraphic(300,300);
                TemplateInstance templateInstance = new TemplateInstance(derivation);
                turtleGraphic.parseWord(templateInstance, false);

                final Stage dialog = new Stage();
                dialog.initModality(Modality.NONE);
                dialog.initOwner(state.getStage());
                var pane = new BorderPane();
                pane.setCenter(turtleGraphic);
                var word = new Label(templateInstance.getWord());
                pane.setBottom(word);
                var dialogScene = new Scene(pane, 600, 600);
                dialog.setScene(dialogScene);
                dialog.show();
            });
        }
    }

    /**
     * Applies the current selected template instance with parameters and draws it on the branching structure pane
     */
    public void apply() {
        // Leave selected template view
        cancel();
        // Draft to be applied
        Draft currentDraft = state.getCurrentDraft();
        // Draw template instance
        paneBranchingStructure.parseWord(currentDraft, false);
        // Get selected anchor from application state
        Anchor selectedAnchor = state.getSelectedAnchor();
        // Use selected anchor
        state.setAnchorToTreeNode(selectedAnchor, new TreeNode<>(currentDraft));
        selectedAnchor.use();
        // Select the next available anchor on the branching structure pane
        state.selectFirst();
        // Resets the branching structure turtle to the new selected anchor
        paneBranchingStructure.updateTurtle(state.getSelectedAnchor().getTurtle());
    }

    /**
     * Leaves the selected-template-view and resets controls accordingly
     */
    public void cancel() {
        btn_Select_Template.setDisable(false);
        btn_Generate.setDisable(false);
        // Change content in the scene graph
        scrollPane.setContent(hBox_Templates);
        // Make the branching structure pane clickable again
        paneBranchingStructure.setClickable(true);
        // Removes draft from branching structure pane
        var draft = state.getCurrentDraft();
        if (draft != null) paneBranchingStructure.getChildren().removeAll(state.getCurrentDraft().getShapes());
    }

    /**
     * Returns the template pane selected (clicked) by the user
     * @return Selected template pane
     */
    public TemplatePane getSelectedTemplatePane() {
        return (TemplatePane) tilePane_Templates.getChildren().stream()
                .filter(c -> c instanceof TemplatePane)
                .filter(tg -> ((TemplatePane) tg).isSelected())
                .findFirst()
                .orElse(null);
    }

    /**
     * Attaches application state to the generator controller and initializes the branching structure pane
     * @param state Application state
     */
    public void init(State state) {
        if (this.state != null) {
            throw new IllegalStateException("State can only be initialized once");
        }
        this.state = state;
        paneBranchingStructure.init(state);
        // Disable split pane divider repositioning
        splitPane.lookupAll(".split-pane-divider").forEach(div ->  div.setMouseTransparent(true) );
    }

    /**
     * Table view cell class
     */
    static class PropertyCell extends TableCell<Property, String> {
        private TextField textField;

        private PropertyCell() {}

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.setOnAction((e) -> commitEdit(textField.getText()));
            textField.focusedProperty().addListener((ObservableValue<? extends Boolean> obs, Boolean old, Boolean val) -> {
                if (!val) commitEdit(textField.getText());
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