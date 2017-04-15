package controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import modelo.Pelicula;
import utils.CinemaHelper;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private List<String> filmNames = new ArrayList<>();
    private List<String> hours = new ArrayList<>();

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
        LocalDate date = dateSelector.getValue();
        List<Pelicula> movies = CinemaHelper.getInstance().getMovies(date);

        filmNames.clear();
        for (Pelicula movie : movies) {
            filmNames.add(movie.getTitulo());
        }

        movieSelector.setItems(new ObservableListWrapper<>(filmNames));
    }

    @FXML
    public void onMovieSelected(ActionEvent actionEvent) {
        String movieTitle = movieSelector.getValue();
        LocalDate date = dateSelector.getValue();

        List<String> hours = CinemaHelper.getInstance().getHoursShowings(movieTitle, date);

        hourSelector.setItems(new ObservableListWrapper<>(hours));
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
