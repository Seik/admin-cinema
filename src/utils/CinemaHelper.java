package utils;

import accesoaBD.AccesoaBD;
import com.sun.istack.internal.NotNull;
import javafx.scene.control.Alert;
import modelo.Pelicula;
import modelo.Proyeccion;
import modelo.Reserva;
import modelo.Sala;
import models.TicketReservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CinemaHelper {

    private static CinemaHelper instance = null;

    private AccesoaBD db;

    private CinemaHelper() {
        db = new AccesoaBD();
    }

    public static CinemaHelper getInstance() {
        if (instance == null) {
            instance = new CinemaHelper();
        }
        return instance;
    }

    public List<Pelicula> getMovies(@NotNull LocalDate date) {
        return db.getPeliculas(date);
    }

    /**
     * @param name
     * @return movie
     */
    public Pelicula getMovieByName(@NotNull String name) {
        List<Pelicula> movies = db.getTodasPeliculas();

        for (Pelicula movie : movies) {
            if (movie.getTitulo().equals(name)) return movie;
        }

        return null;
    }

    /**
     * @param title
     * @param date
     * @return hours of movie showings
     */
    public List<String> getHoursShowings(String title, LocalDate date) {
        List<String> hours = new ArrayList<>();
        List<Proyeccion> showings = db.getProyeccion(title, date);

        for (Proyeccion showing : showings) {
            if (!isShowingFull(showing)) hours.add(showing.getHoraInicio());
        }

        return hours;
    }

    /**
     * @param showing
     * @return remainig seats for a specific show
     */
    public int getRemainingSeatsForShowing(@NotNull Proyeccion showing) {
        int maxSeats = showing.getSala().getCapacidad();

        List<Reserva> reservations = showing.getReservas();

        int seatsReserved = 0;
        for (Reserva reservation : reservations) {
            seatsReserved += reservation.getNumLocalidades();
        }

        return maxSeats - seatsReserved;
    }

    /**
     * Returns a list with all the TicketReservations for today
     *
     * @param date
     * @return TicketReservation
     */
    public List<TicketReservation> getTicketReservations(@NotNull LocalDate date) {
        List<TicketReservation> ticketReservations = new ArrayList<>();

        List<Proyeccion> showings = db.getProyeccionesDia(date);

        for (Proyeccion showing : showings) {
            List<Reserva> reservations = showing.getReservas();

            for (Reserva reservation : reservations) {
                ticketReservations.add(new TicketReservation(showing, reservation));
            }
        }

        return ticketReservations;
    }

    /**
     * Get a showing based on title, date and hour
     *
     * @param title
     * @param date
     * @param hour
     * @return Showing
     */
    public Proyeccion getShowing(String title, LocalDate date, String hour) {
        return db.getProyeccion(title, date, hour);
    }

    /**
     * Save a showing
     *
     * @param showing
     */
    public void saveShowing(Proyeccion showing) {
        db.salvarProyeccion(showing);
    }

    /**
     * @param showing
     * @return if show is full
     */
    public boolean isShowingFull(@NotNull Proyeccion showing) {
        Sala room = showing.getSala();
        if (room.getCapacidad() <= room.getEntradasVendidas()) {
            return true;
        }

        return false;
    }

    /**
     * Shows info dialog
     *
     * @param message
     */
    public void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Shows error dialog
     *
     * @param message
     */
    public void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
