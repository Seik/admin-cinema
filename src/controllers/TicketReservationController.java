package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.TicketReservation;
import utils.CompletedTaskEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketReservationController implements Initializable {

    Logger logger = Logger.getLogger(getClass().getName());

    private TicketReservation ticketReservation;

    private CompletedTaskEvent handler;

    @FXML
    private Label movieTitleLabel;
    @FXML
    private Label clientNameLabel;
    @FXML
    private Label clientPhoneLabel;
    @FXML
    public Label hourLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void onPurchaseReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/layouts/purchase_layout.fxml"));
            BorderPane parent = loader.load();


            PurchaseController controller = loader.getController();

            int seats = ticketReservation.getReservation().getNumLocalidades();
            controller.setShowing(ticketReservation.getShowing(), seats, ticketReservation.getReservation());
            controller.setHandler(() -> handler.completedTask());

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Compra de tickets");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create new Window", e);
        }
    }

    /**
     * Sets ticket reservation for the controller and fills the views with data
     *
     * @param ticketReservation
     */
    public void setTicketReservation(TicketReservation ticketReservation) {
        this.ticketReservation = ticketReservation;

        String hour = ticketReservation.getShowing().getHoraInicio();

        movieTitleLabel.setText(ticketReservation.getShowing().getPelicula().getTitulo());
        clientNameLabel.setText(ticketReservation.getReservation().getNombre());
        clientPhoneLabel.setText(ticketReservation.getReservation().getTelefono());
        hourLabel.setText("Hora: " + hour);
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
