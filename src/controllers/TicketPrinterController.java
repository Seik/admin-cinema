package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Proyeccion;
import models.Seat;
import utils.CinemaHelper;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketPrinterController implements Initializable {

    Logger logger = Logger.getLogger(getClass().getName());

    Proyeccion showing;
    List<Seat> seats;

    private Printer printer = Printer.getDefaultPrinter();

    @FXML
    public Label dayLabel;
    @FXML
    public Label roomLabel;
    @FXML
    public Label hourLabel;
    @FXML
    public Label seatLabel;
    @FXML
    public Label titleLabel;
    @FXML
    public Label selectedPrinterLabel;
    @FXML
    public VBox printingNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onSelectPrinter(ActionEvent event) {
        ChoiceDialog dialog = new ChoiceDialog(Printer.getDefaultPrinter(),
                Printer.getAllPrinters());
        dialog.setContentText("Seleccionar una impresora de las disponibles");
        dialog.setTitle("Selección Impresora");
        Optional<Printer> opt = dialog.showAndWait();
        opt.ifPresent(printer -> printer = printer);
    }

    @FXML
    public void onPrint(ActionEvent event) {
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null) {
            boolean printed = job.printPage(printingNode);
            if (printed) {
                job.endJob();
                printNextTicket(event);
            } else {
                logger.log(Level.SEVERE, "Printing failed");

                CinemaHelper.getInstance().showErrorDialog("Fallo al imprimir");

            }
        } else {
            logger.log(Level.WARNING, "Can't create printing task.");

            CinemaHelper.getInstance().showErrorDialog("No puede crearse el job de impresión.");
        }
    }

    private void printNextTicket(ActionEvent event) {
        if (seats.isEmpty()) {
            // Close window
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } else {
            CinemaHelper.getInstance().showInfoDialog("Se va a mostrar el proxinmo ticket para la impresión" +
                    ", cola restante: " + seats.size());

            setPrintInfo(showing, seats);
        }
    }

    /**
     * Sets all the needed info for the printing process
     *
     * @param showing
     * @param seats
     */
    public void setPrintInfo(Proyeccion showing, List<Seat> seats) {
        this.showing = showing;
        this.seats = seats;

        titleLabel.setText(showing.getPelicula().getTitulo());
        dayLabel.setText("Día: " + showing.getDia());
        roomLabel.setText("Sala: " + showing.getSala().getNombresala());
        hourLabel.setText("Hora: " + showing.getHoraInicio());
        seatLabel.setText("Localidad: Fila " + seats.get(0).getRow() + ", Asiento " + seats.get(0).getColumn());

        String defaultPrinterName = (printer == null) ? "" : printer.getName();
        selectedPrinterLabel.setText(defaultPrinterName);

        // Remove first item
        seats.remove(0);
    }
}
