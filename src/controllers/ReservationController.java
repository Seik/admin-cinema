package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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

        if (quantityTextField.getText().trim().equals("") || quantityTextField.getText().trim().equals("0")) {
            CinemaHelper.getInstance().showInfoDialog("La cantidad introducida tiene que ser > 0");
            return;
        }

        int quantity = Integer.valueOf(quantityTextField.getText().trim());

        if (name.trim().isEmpty() || phone.isEmpty()) {
            CinemaHelper.getInstance().showInfoDialog("Rellene todos los campos para continuar.");
            return;
        }

        if (quantity > CinemaHelper.getInstance().getRemainingSeatsForShowing(showing)) {
            CinemaHelper.getInstance().showErrorDialog("La cantidad introducida es mayor a la capacidad máxima.");
            return;
        }

        Reserva reservation = new Reserva(name, phone, quantity);

        showing.getReservas().add(reservation);

        int soldTickets = showing.getSala().getEntradasVendidas();
        showing.getSala().setEntradasVendidas(soldTickets + reservation.getNumLocalidades());

        CinemaHelper.getInstance().saveShowing(showing);

        CinemaHelper.getInstance().showInfoDialog("Reserva realizada con éxito.");

        // Clean fields at main screen
        handler.completedTask();

        // Close window
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets a showing for the controller
     *
     * @param showing
     */
    public void setShowing(Proyeccion showing) {
        this.showing = showing;
        maxQuantityLabel.setText("/" + CinemaHelper.getInstance().getRemainingSeatsForShowing(showing));
    }

    /**
     * Sets a handler for the controller
     *
     * @param handler
     */
    public void setHandler(CompletedTaskEvent handler) {
        this.handler = handler;
    }
}
