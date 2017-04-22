package models;

public class Ticket {

    private String movietitle;
    private String date;
    private String room;
    private String hour;
    private Seat seat;

    public Ticket(String movietitle, String date, String room, String hour, Seat seat) {
        this.movietitle = movietitle;
        this.date = date;
        this.room = room;
        this.hour = hour;
        this.seat = seat;
    }

    public String getMovietitle() {
        return movietitle;
    }

    public void setMovietitle(String movietitle) {
        this.movietitle = movietitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}
