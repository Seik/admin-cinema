package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import modelo.Proyeccion;
import modelo.Reserva;
import utils.CinemaHelper;
import utils.CompletedTaskEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {

    private Proyeccion showing;
    private CompletedTaskEvent handler;

    @FXML
    public TextField nameTextField;
    @FXML
    public TextField phoneTextField;
    @FXML
    public TextField quantityTextField;
    @FXML
    public Label maxQuantityLabel;
    @FXML
    public Button reservationButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Make TextField numeric only
        quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantityTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    public void onReservation(ActionEvent event) {

        String name = nameTextField.getText().trim();
        String phone = phoneTextField.getText().trim();
        int quantity = Integer.valueOf(quantityTextField.getText().trim());

        if (name.trim().isEmpty() || phone.isEmpty() || quantity == 0) {
            CinemaHelper.getInstance().showInfoDialog("Rellene todos los campos para continuar.");
            return;
        }

        if (quantity > CinemaHelper.getInstance().getRemainingSeatsForShowing(showing)) {
            CinemaHelper.getInstance().showErrorDialog("La cantidad introducida es mayor a la capacidad máxima.");
        }

        Reserva reservation = new Reserva(name, phone, quantity);

        showing.getReservas().add(reservation);

        CinemaHelper.getInstance().showInfoDialog("Reserva realizada con éxito.");

        // Clean fields at main screen
        handler.cleanFields();

        // Close window
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets a showing
     *
     * @param showing
     */
    public void setShowing(Proyeccion showing) {
        this.showing = showing;
        maxQuantityLabel.setText("/" + CinemaHelper.getInstance().getRemainingSeatsForShowing(showing));
    }

    /**
     *  Sets a handler
     * @param handler
     */
    public void setHandler(CompletedTaskEvent handler) {
        this.handler = handler;
    }
}
