package controllers;

import components.SeatsGrid;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modelo.Proyeccion;
import modelo.Reserva;
import utils.CinemaHelper;
import utils.CompletedTaskEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PurchaseController implements Initializable {

    Logger logger = Logger.getLogger(getClass().getName());

    private Proyeccion showing;
    private CompletedTaskEvent handler;

    private SeatsGrid seatsGrid = new SeatsGrid(12, 18);

    @FXML
    public BorderPane borderPane;
    @FXML
    public Button purchaseButton;
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField phoneTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        init();
    }

    private void init() {
        borderPane.centerProperty().setValue(seatsGrid);
    }

    @FXML
    public void onPurchase(ActionEvent event) {
        String name = nameTextField.getText().trim();
        String phone = phoneTextField.getText().trim();
        int quantity = seatsGrid.getReservedSeats();

        if (quantity == 0) {
            CinemaHelper.getInstance().showInfoDialog("Por favor, seleccione algún asiento para continuar.");
            return;
        }

        if (name.trim().isEmpty() || phone.isEmpty()) {
            CinemaHelper.getInstance().showInfoDialog("Rellene todos los campos para continuar.");
            return;
        }

        Reserva reservation = new Reserva(name, phone, quantity);

        showing.getReservas().add(reservation);

        CinemaHelper.getInstance().showInfoDialog("Compra realizada con éxito");

        // Clean fields at main screen
        handler.cleanFields();

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
        //TODO pass data to controller
        logger.log(Level.FINE, "Start purchase flow");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../layouts/ticket_printer_layout.fxml"));
            BorderPane parent = loader.load();

            TicketPrinterController controller = loader.getController();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Purchase");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create new Window", e);
        }
    }
}
