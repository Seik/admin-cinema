package utils;

import accesoaBD.AccesoaBD;
import com.sun.istack.internal.NotNull;
import modelo.Pelicula;
import modelo.Proyeccion;

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
            hours.add(showing.getHoraInicio());
        }

        return hours;
    }
}
