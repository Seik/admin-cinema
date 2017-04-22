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

    public void init() {
        logger.log(Level.FINE, "Setting up controller ");

        seatsGrid = new SeatsGrid(showing.getSala(), 12, 18);

        notReservedSeatsLabel.setText("Butacas no reservadas: " +
                CinemaHelper.getInstance().getRemainingSeatsForShowing(showing));

        borderPane.centerProperty().setValue(seatsGrid);
    }

    @FXML
    public void onPurchase(ActionEvent event) {
        logger.log(Level.FINE, "On purchase triggered.");

        String name = nameTextField.getText().trim();
        String phone = phoneTextField.getText().trim();
        int quantity = seatsGrid.getReservedSeats();

        if (quantity == 0) {
            CinemaHelper.getInstance().showInfoDialog("Por favor, seleccione algún asiento para continuar.");
            return;
        }

        if (quantity > CinemaHelper.getInstance().getRemainingSeatsForShowing(showing)) {
            CinemaHelper.getInstance().showInfoDialog("El numero de asientos seleccionado es superior al de asientos " +
                    "no reservados");
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

            Reserva reservation = new Reserva(name, phone, quantity);

            showing.getReservas().add(reservation);


            launchPrinterView();

            // Clean fields at main screen
            handler.cleanFields();

            // Close window
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Sets a showing for the controller
     *
     * @param showing
     */
    public void setShowing(Proyeccion showing) {
        this.showing = showing;
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
        logger.log(Level.FINE, "Start purchase flow");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../layouts/ticket_printer_layout.fxml"));
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
        }
    }

    private boolean launchPrintProcess() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Dialogo de confirmación");
        alert.setHeaderText("Empezar proceso de impresión");
        alert.setContentText("Los sitios seleccionados van a ser reservados, ¿Desea proceder a imprimir las entradas?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
}
