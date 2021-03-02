package de.haw.gui;

import de.haw.Generator;
import de.haw.State;
import de.haw.gui.shape.Anchor;
import de.haw.gui.structure.BranchingStructurePane;
import de.haw.gui.structure.Draft;
import de.haw.gui.structure.Property;
import de.haw.gui.template.TemplatePane;
import de.haw.gui.turtle.TurtleGraphic;
import de.haw.pipeline.pipe.*;
import de.haw.pipeline.Pipeline;
import de.haw.gui.template.Template;
import de.haw.gui.template.TemplateInstance;
import de.haw.tree.TreeNode;
import de.haw.utils.Dots;
import de.haw.utils.Logging;
import de.haw.utils.Templates;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.awt.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Random;
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
    // Randomize instance
    Random randomizer;
    // FXML elements bound to this controller
    @FXML public SplitPane splitPane;
    @FXML public TitledPane titledPane_Branching_Structure;
    @FXML public TitledPane titledPane_Templates;
    @FXML public TilePane tilePane_Templates;
    @FXML public ScrollPane scrollPane;
    @FXML public HBox hBox_Templates;
    @FXML public Button btn_Select_Template;
    @FXML public Button btn_Generate;
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
        // Initialize random instance
        randomizer = new Random();
    }

    /**
     * Extend a text field to "accept" only numerous input
     * @param textField Text field to be extended
     * @param floating True if floating point numbers shall be accepted, false for non-floating
     */
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

        var is = GeneratorController.class.getClassLoader().getResourceAsStream("templates.txt");

        if (is == null) {
            is = getClass().getResourceAsStream("/templates");
        }

        try {
            var reader = new BufferedReader(new InputStreamReader(is));
            // Current file line
            String line = reader.readLine();
            while (line != null) {
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

                line = reader.readLine();
            }
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens an explorer window where the templates file is located at
     */
    @FXML public void openFileLocation() {
        var filePath = GeneratorController.class.getClassLoader().getResource("templates.txt");

        if (filePath == null) {
            filePath = getClass().getResource("/templates");
        }

        try {
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

    /**
     * Set up view that is called after selecting a template
     * @param tableView View to be set up
     */
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
     * Generates the tree structure
     */
    @FXML public void generate() {
        reset();
        var tree = state.getTree();

        if (tree.isEmpty()) {
            loadTemplates();
            // 0
            var t1 = new TemplateInstance(Templates.getTemplateByID(7));
            var n1 = new TreeNode<>(t1);
            // 1
            var t2 = new TemplateInstance(Templates.getTemplateByID(3));
            t2.setParameter("Rotation", 20f);
            var n2 = new TreeNode<>(t2);
            n1.addChild(n2);
            var t3 = new TemplateInstance(Templates.getTemplateByID(6));
            t3.setParameter("Rotation", 20f);
            t3.setParameter("Branching angle", 20f);
            var n3 = new TreeNode<>(t3);
            n1.addChild(n3);
            var t4 = new TemplateInstance(Templates.getTemplateByID(6));
            t4.setParameter("Rotation", -20f);
            var n4 = new TreeNode<>(t4);
            n1.addChild(n4);
            // 2
            var t5 = new TemplateInstance(Templates.getTemplateByID(4));
            t5.setParameter("Rotation", -20f);
            t5.setParameter("Branching angle", 30f);
            var n5 = new TreeNode<>(t5);
            n2.addChild(n5);
            var n6 = new TreeNode<TemplateInstance>();
            n3.addChild(n6);
            var t7 = new TemplateInstance(Templates.getTemplateByID(2));
            var n7 = new TreeNode<>(t7);
            n3.addChild(n7);
            var t8 = new TemplateInstance(Templates.getTemplateByID(8));
            t8.setParameter("Rotation", -10f);
            t8.setParameter("Branching angle", 30f);
            var n8 = new TreeNode<>(t8);
            n4.addChild(n8);
            var t9 = new TemplateInstance(Templates.getTemplateByID(6));
            t9.setParameter("Rotation", -30f);
            var n9 = new TreeNode<>(t9);
            n4.addChild(n9);
            // 3
            var n10 = new TreeNode<TemplateInstance>();
            n5.addChild(n10);
            var t11 = new TemplateInstance(Templates.getTemplateByID(4));
            var n11 = new TreeNode<>(t11);
            n5.addChild(n11);
            var n12 = new TreeNode<TemplateInstance>();
            n7.addChild(n12);
            var n13 = new TreeNode<TemplateInstance>();
            n8.addChild(n13);
            var n14 = new TreeNode<TemplateInstance>();
            n8.addChild(n14);
            var t15 = new TemplateInstance(Templates.getTemplateByID(4));
            t15.setParameter("Branching angle", 30f);
            var n15 = new TreeNode<>(t15);
            n8.addChild(n15);
            var t16 = new TemplateInstance(Templates.getTemplateByID(4));
            t16.setParameter("Rotation", -20f);
            t16.setParameter("Branching angle", 30f);
            var n16 = new TreeNode<>(t16);
            n9.addChild(n16);
            var n17 = new TreeNode<TemplateInstance>();
            n9.addChild(n17);
            // 4
            var t18 = new TemplateInstance(Templates.getTemplateByID(3));
            t18.setParameter("Rotation", -20f);
            t18.setParameter("Branching angle", 60f);
            var n18 = new TreeNode<>(t18);
            n11.addChild(n18);
            var n19 = new TreeNode<TemplateInstance>();
            n11.addChild(n19);
            var t20 = new TemplateInstance(Templates.getTemplateByID(3));
            t20.setParameter("Scaling", 2f);
            var n20 = new TreeNode<>(t20);
            n15.addChild(n20);
            var n21 = new TreeNode<TemplateInstance>();
            n15.addChild(n21);
            var t22 = new TemplateInstance(Templates.getTemplateByID(3));
            var n22 = new TreeNode<>(t22);
            n16.addChild(n22);
            var n23 = new TreeNode<TemplateInstance>();
            n16.addChild(n23);
            // 5
            var t24 = new TemplateInstance(Templates.getTemplateByID(8));
            t24.setParameter("Scaling", .4f);
            var n24 = new TreeNode<>(t24);
            n18.addChild(n24);
            var t25 = new TemplateInstance(Templates.getTemplateByID(8));
            var n25 = new TreeNode<>(t25);
            n20.addChild(n25);
            var t26 = new TemplateInstance(Templates.getTemplateByID(8));
            t26.setParameter("Scaling", .6f);
            var n26 = new TreeNode<>(t26);
            n22.addChild(n26);
            // 6
            var n27 = new TreeNode<TemplateInstance>();
            n24.addChild(n27);
            var n28 = new TreeNode<TemplateInstance>();
            n24.addChild(n28);
            var n29 = new TreeNode<TemplateInstance>();
            n24.addChild(n29);
            var n30 = new TreeNode<TemplateInstance>();
            n25.addChild(n30);
            var n31 = new TreeNode<TemplateInstance>();
            n25.addChild(n31);
            var n32 = new TreeNode<TemplateInstance>();
            n25.addChild(n32);
            var n33 = new TreeNode<TemplateInstance>();
            n26.addChild(n33);
            var n34 = new TreeNode<TemplateInstance>();
            n26.addChild(n34);
            var n35 = new TreeNode<TemplateInstance>();
            n26.addChild(n35);

            tree = n1;
        }
        Dots.treeToDot("user_structure", tree);

        var numberOfIterations = Float.parseFloat(iterations.getText());
        var numberOfGenerations = Float.parseFloat(generations.getText());
        // Pipeline context
        var ctx = new PipelineContext();
        ctx.tree = tree;
        ctx.randomizer = state.getRandomizer();

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
                var derivation = result.lSystem.deriveAndPopulate((int) numberOfIterations, result.estimator);

                var turtleGraphic = new TurtleGraphic(300,300);
                TemplateInstance templateInstance = new TemplateInstance(derivation);
                turtleGraphic.parseWord(templateInstance, false);

                showPopup("Generierte Verzweigungsstruktur", turtleGraphic, templateInstance.getWord(), 600, 600);
            });
        }
    }

    public static void showPopup(String title, TurtleGraphic turtleGraphic, String description, int posX, int posY) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        var pane = new BorderPane();
        pane.setCenter(turtleGraphic);
        var word = new Label(description);
        pane.setBottom(word);
        var dialogScene = new Scene(pane, 600, 600);
        dialog.setScene(dialogScene);
        dialog.setTitle(title);
        dialog.setX(posX);
        dialog.setY(posY);
        dialog.show();
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