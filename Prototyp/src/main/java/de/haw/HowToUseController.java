package de.haw;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HowToUseController {
    @FXML Button BtnOk;

    public HowToUseController() {}

    @FXML public void initialize() {
        BtnOk.setOnAction(e -> {
            Node source = (Node) e.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });
    }
}
