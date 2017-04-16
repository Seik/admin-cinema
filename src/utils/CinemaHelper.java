package utils;

import accesoaBD.AccesoaBD;
import com.sun.istack.internal.NotNull;
import javafx.scene.control.Alert;
import modelo.Pelicula;
import modelo.Proyeccion;
import modelo.Sala;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 15/4/17.
 */

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

    public Pelicula getMovieByName(@NotNull String name) {
        List<Pelicula> movies = db.getTodasPeliculas();

        for (Pelicula movie : movies) {
            if (movie.getTitulo().equals(name)) return movie;
        }

        return null;
    }

    public List<String> getHoursShowings(String title, LocalDate date) {
        List<String> hours = new ArrayList<>();
        List<Proyeccion> showings = db.getProyeccion(title, date);

        for (Proyeccion showing : showings) {
            if (!isShowingFull(showing)) hours.add(showing.getHoraInicio());
        }

        return hours;
    }

    public boolean isShowingFull(Proyeccion showing) {
        Sala room = showing.getSala();
        if (room.getCapacidad() <= room.getEntradasVendidas()) {
            return true;
        }

        return false;
    }

    public void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
