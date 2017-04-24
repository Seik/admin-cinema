package models;

import modelo.Proyeccion;
import modelo.Reserva;

public class TicketReservation {
    private Proyeccion showing;
    private Reserva reservation;

    public TicketReservation(Proyeccion showing, Reserva reservation) {
        this.reservation = reservation;
        this.showing = showing;
    }

    public Reserva getReservation() {
        return reservation;
    }

    public void setReservation(Reserva reservation) {
        this.reservation = reservation;
    }

    public Proyeccion getShowing() {
        return showing;
    }

    public void setShowing(Proyeccion showing) {
        this.showing = showing;
    }
}
