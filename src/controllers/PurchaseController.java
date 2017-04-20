package controllers;

import components.SeatButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import modelo.Proyeccion;
import utils.CompletedTaskEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class PurchaseController implements Initializable {

    private final int columns = 12;
    private final int rows = 18;

    private Proyeccion showing;
    private CompletedTaskEvent handler;

    public GridPane gridPane = new GridPane();

    @FXML
    public BorderPane borderPane;
    @FXML
    public Button purchaseButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void init() {
        for (int i = 1; i <= columns; i++) {
            Label label = new Label(String.valueOf(i));
            gridPane.add(label, i, 0);
        }

        for (int i = 1; i <= rows; i++) {
            Label label = new Label(String.valueOf(i));
            gridPane.add(label, 0, i);
        }

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                SeatButton button = new SeatButton();
                button.setPrefWidth(70);
                button.setPrefHeight(20);

                button.setOnAction(event -> {
                    SeatButton button1 = (SeatButton) event.getSource();

                    button1.toggleSeat();
                });

                gridPane.add(button, j, i);
            }
        }

        for (Node node : gridPane.getChildren()) {
            GridPane.setHalignment(node, HPos.CENTER);
            GridPane.setValignment(node, VPos.CENTER);
            GridPane.setHgrow(node, Priority.ALWAYS);
            GridPane.setVgrow(node, Priority.ALWAYS);
        }

        gridPane.setAlignment(Pos.CENTER);
        borderPane.centerProperty().setValue(gridPane);
    }

    @FXML
    public void onPurchase(ActionEvent event) {
    }

    public void setShowing(Proyeccion showing) {
        this.showing = showing;
    }

    public void setHandler(CompletedTaskEvent handler) {
        this.handler = handler;
    }

}
