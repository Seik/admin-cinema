package controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Pelicula;
import modelo.Proyeccion;
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

    private CompletedTaskEvent handler = () -> {
        dateSelector.getEditor().clear();
        movieSelector.getItems().clear();
        hourSelector.getItems().clear();
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.log(Level.FINE, "Controller initialized");

    }

    @FXML
    public void onDateSelected(ActionEvent event) {
        LocalDate date = dateSelector.getValue();

        logger.log(Level.FINE, "Date selected" + date);

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

        logger.log(Level.FINE, "Movie selected" + movieTitle);

        LocalDate date = dateSelector.getValue();

        List<String> hours = CinemaHelper.getInstance().getHoursShowings(movieTitle, date);

        hourSelector.setItems(new ObservableListWrapper<>(hours));
    }

    @FXML
    public void onReservation(ActionEvent actionEvent) {

        if (dateSelector.getValue() == null || movieSelector.getValue() == null || hourSelector.getValue() == null) {
            CinemaHelper.getInstance().showInfoDialog("Rellene todos los campos para continuar.");
            return;
        }

        logger.log(Level.FINE, "Start reservation flow");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../layouts/reservation_layout.fxml"));
            VBox parent = loader.load();

            Proyeccion showing = CinemaHelper.getInstance().getShowing(movieSelector.getValue(),
                    dateSelector.getValue(), hourSelector.getValue());

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
        if (dateSelector.getValue() == null || movieSelector.getValue() == null || hourSelector.getValue() == null) {
            CinemaHelper.getInstance().showInfoDialog("Rellene todos los campos para continuar.");
            return;
        }

        logger.log(Level.FINE, "Start purchase flow");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../layouts/purchase_layout.fxml"));
            BorderPane parent = loader.load();

            Proyeccion showing = CinemaHelper.getInstance().getShowing(movieSelector.getValue(),
                    dateSelector.getValue(), hourSelector.getValue());

            PurchaseController controller = loader.getController();
            controller.setShowing(showing);
            controller.setHandler(handler);
            controller.init();

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
