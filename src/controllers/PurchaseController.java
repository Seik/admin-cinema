package controllers;

import components.SeatsGrid;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modelo.Proyeccion;
import modelo.Reserva;
import modelo.Sala;
import utils.CinemaHelper;
import utils.CompletedTaskEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PurchaseController implements Initializable {

    Logger logger = Logger.getLogger(getClass().getName());

    private Proyeccion showing;
    private CompletedTaskEvent handler;

    private SeatsGrid seatsGrid;

    private Reserva reservation;

    @FXML
    public BorderPane borderPane;
    @FXML
    public Button purchaseButton;
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField phoneTextField;
    @FXML
    public Label notReservedSeatsLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void onPurchase(ActionEvent event) {

        String name = nameTextField.getText().trim();
        String phone = phoneTextField.getText().trim();
        int quantity = seatsGrid.getReservedSeats();

        if (quantity == 0) {
            CinemaHelper.getInstance().showInfoDialog("Por favor, seleccione alguna localidad para continuar.");
            return;
        }

        if (quantity > CinemaHelper.getInstance().getRemainingSeatsForShowing(showing)) {
            CinemaHelper.getInstance().showInfoDialog("El numero de localidades seleccionadas es superior al de " +
                    "asientos disponibles");
            return;
        }

        if (reservation != null && quantity > reservation.getNumLocalidades()) {
            CinemaHelper.getInstance().showInfoDialog("La cantidad de localidades seleccionadas es superior al de " +
                    "la reserva. \n Numero de localidades a seleccionar: " + reservation.getNumLocalidades());
            return;
        }

        if (reservation != null && quantity < reservation.getNumLocalidades()) {
            CinemaHelper.getInstance().showInfoDialog("La cantidad de localidades seleccionadas es inferior al de " +
                    "la reserva. \n Numero de localidades a seleccionar: " + reservation.getNumLocalidades());
            return;
        }

        if (name.trim().isEmpty() || phone.isEmpty()) {
            CinemaHelper.getInstance().showInfoDialog("Rellene todos los campos para continuar.");
            return;
        }

        if (launchPrintProcess()) {
            Sala room = showing.getSala();
            room.setLocalidades(seatsGrid.getSeats());
            showing.setSala(room);

            if (reservation == null) {
                Reserva reservation = new Reserva(name, phone, quantity);

                showing.getReservas().add(reservation);

                int soldTickets = showing.getSala().getEntradasVendidas();
                showing.getSala().setEntradasVendidas(soldTickets + reservation.getNumLocalidades());
            } else {
                showing.getReservas().remove(reservation);
            }

            CinemaHelper.getInstance().saveShowing(showing);


            launchPrinterView();

            // Clean fields at main screen
            if (handler != null) {
                handler.completedTask();
            }

            // Close window
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Sets a showing for the controller and initialize main grid pane
     *
     * @param showing
     * @param reservation
     */
    public void setShowing(Proyeccion showing, int seats, Reserva reservation) {
        this.showing = showing;
        this.reservation = reservation;

        seatsGrid = new SeatsGrid(showing.getSala(), 12, 18);

        String seatsToFill = "";

        if (reservation == null) {
            seatsToFill += "Butacas no reservadas: " + seats;
        } else {
            seatsToFill += "Butacas a seleccionar: " + seats;
        }

        notReservedSeatsLabel.setText(seatsToFill);


        borderPane.centerProperty().setValue(seatsGrid);
    }

    /**
     * Sets a handler for the controller
     *
     * @param handler
     */
    public void setHandler(CompletedTaskEvent handler) {
        this.handler = handler;
    }

    /**
     * Launchs the view for printing the ticket
     */
    private void launchPrinterView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/layouts/ticket_printer_layout.fxml"));
            BorderPane parent = loader.load();

            TicketPrinterController controller = loader.getController();
            controller.setPrintInfo(showing, seatsGrid.getRervedSeats());

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Purchase");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create new Window", e);
            CinemaHelper.getInstance().showErrorDialog("Ocurrió un error al crear la ventana.");
        }
    }

    private boolean launchPrintProcess() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Dialogo de confirmación");
        alert.setHeaderText("Empezar proceso de impresión");

        String alertText = "";

        if (reservation == null) {
            alertText = "Los sitios seleccionados van a ser reservados, ¿Desea proceder a imprimir las entradas?";
        } else {
            alertText = "¿Desea proceder a imprimir las entradas?";
        }

        alert.setContentText(alertText);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
}
