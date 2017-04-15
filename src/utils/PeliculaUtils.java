package utils;

import accesoaBD.AccesoaBD;
import com.sun.istack.internal.NotNull;
import modelo.Pelicula;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Ivan on 15/4/17.
 */

public class PeliculaUtils {

    private static PeliculaUtils instance = null;

    private AccesoaBD db;

    private PeliculaUtils() {
        db = new AccesoaBD();
    }

    public static PeliculaUtils getInstance() {
        if (instance == null) {
            instance = new PeliculaUtils();
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
}
