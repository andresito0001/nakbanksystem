package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.entities.CardModel;

public class CardController {
    @FXML
    private HBox hBoxMainBack;
    @FXML
    private VBox vBoxMainBack;
    @FXML
    private Label codeLabel;
    @FXML
    private Label bankNameLabel;
    @FXML
    private Label balanceLabelId;

    public void setData(CardModel cardModel) {
        codeLabel.setText(cardModel.getCode());
        bankNameLabel.setText(cardModel.getBankName());
        balanceLabelId.setText(String.valueOf(cardModel.getBalance()));
    }
}
