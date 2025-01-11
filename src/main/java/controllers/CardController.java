package main.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardController {
    @FXML
    private Label codigoLabel;

    @FXML
    private Label saldoLabel;

    @FXML
    private ImageView iconImage;

    public void setCodigo(String codigo) {
        codigoLabel.setText(codigo);
    }

    public void setSaldo(String saldo) {
        saldoLabel.setText(saldo);
    }

    public void setIconImage(String imagePath) {
        iconImage.setImage(new Image(imagePath));
    }
}
