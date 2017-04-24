package controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import components.ReservationListViewCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Pelicula;
import modelo.Proyeccion;
import models.TicketReservation;
import utils.CinemaHelper;
import utils.CompletedTaskEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Initializable {
    Logger logger = Logger.getLogger(getClass().getName());

    ObservableList<TicketReservation> ticketList = FXCollections.observableArrayList();
    FilteredList<TicketReservation> filteredList = new FilteredList<>(ticketList);

    @FXML
    public DatePicker dateSelectorPurchase;
    @FXML
    public ComboBox<String> movieSelector;
    @FXML
    public ComboBox<String> hourSelector;
    @FXML
    public Button reservationButton;
    @FXML
    public Button purchaseButton;
    @FXML
    public ListView<TicketReservation> listView;
    @FXML
    public DatePicker reservationsDatePicker;
    @FXML
    public TextField nameOrPhoneTextField;

    private CompletedTaskEvent handler = () -> {
        dateSelectorPurchase.getEditor().clear();
        movieSelector.getItems().clear();
        hourSelector.getItems().clear();

        updateListViewWithSelectedDate();
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        listView.setItems(filteredList);
        listView.setCellFactory(ticketReservationListView -> new ReservationListViewCell(this::updateListViewWithSelectedDate));

        nameOrPhoneTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String filter = nameOrPhoneTextField.getText();
            if (filter == null || filter.trim().isEmpty()) {
                filteredList.setPredicate(s -> true);
            } else {
                filteredList.setPredicate(ticketReservation -> (ticketReservation.getReservation().getNombre().contains(filter) ||
                        ticketReservation.getReservation().getTelefono().contains(filter)));
            }
        });
    }

    @FXML
    public void onDateSelectedPurchase(ActionEvent event) {
        LocalDate date = dateSelectorPurchase.getValue();

        List<Pelicula> movies = CinemaHelper.getInstance().getMovies(date);

        List<String> moviesNames = new ArrayList<>();
        for (Pelicula movie : movies) {
            moviesNames.add(movie.getTitulo());
        }

        movieSelector.setItems(new ObservableListWrapper<>(moviesNames));
    }

    @FXML
    public void onMovieSelected(ActionEvent actionEvent) {
        String movieTitle = movieSelector.getValue();

        LocalDate date = dateSelectorPurchase.getValue();

        List<String> hours = CinemaHelper.getInstance().getHoursShowings(movieTitle, date);

        hourSelector.setItems(new ObservableListWrapper<>(hours));
    }

    @FXML
    public void onReservation(ActionEvent actionEvent) {

        if (dateSelectorPurchase.getValue() == null || movieSelector.getValue() == null || hourSelector.getValue() == null) {
            CinemaHelper.getInstance().showInfoDialog("Rellene todos los campos para continuar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../layouts/reservation_layout.fxml"));
            VBox parent = loader.load();

            Proyeccion showing = CinemaHelper.getInstance().getShowing(movieSelector.getValue(),
                    dateSelectorPurchase.getValue(), hourSelector.getValue());

            ReservationController controller = loader.getController();
            controller.setShowing(showing);
            controller.setHandler(handler);

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Reservation");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create new Window", e);
        }
    }

    @FXML
    public void onPurchase(ActionEvent actionEvent) {
        if (dateSelectorPurchase.getValue() == null || movieSelector.getValue() == null || hourSelector.getValue() == null) {
            CinemaHelper.getInstance().showInfoDialog("Rellene todos los campos para continuar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../layouts/purchase_layout.fxml"));
            BorderPane parent = loader.load();

            Proyeccion showing = CinemaHelper.getInstance().getShowing(movieSelector.getValue(),
                    dateSelectorPurchase.getValue(), hourSelector.getValue());

            PurchaseController controller = loader.getController();

            int seats = CinemaHelper.getInstance().getRemainingSeatsForShowing(showing);

            controller.setShowing(showing, seats, null);
            controller.setHandler(handler);

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Compra de tickets");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create new Window", e);

            CinemaHelper.getInstance().showErrorDialog("Ocurrió un error al crear la ventana.");
        }
    }

    @FXML
    public void onDateSelectedReservation(ActionEvent event) {
        updateListViewWithSelectedDate();
    }

    private void updateListViewWithSelectedDate() {
        LocalDate date = reservationsDatePicker.getValue();

        ticketList.clear();
        ticketList.addAll(CinemaHelper.getInstance().getTicketReservations(date));
    }
}
