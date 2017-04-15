package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public DatePicker dateSelector;
    @FXML
    public ComboBox<String> movieSelector;
    @FXML
    public ComboBox<String> hourSelector;
    @FXML
    public Button reservationButton;
    @FXML
    public Button purchaseButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onDateSelected(ActionEvent actionEvent) {

    }

    @FXML
    public void onMovieSelected(ActionEvent actionEvent) {

    }

    @FXML
    public void onHourSelected(ActionEvent actionEvent) {

    }

    @FXML
    public void onReservation(ActionEvent actionEvent) {

    }

    @FXML
    public void onPurchase(ActionEvent actionEvent) {

    }


}
