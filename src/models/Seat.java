package models;

/**
 * Created by Ivan on 22/4/17.
 */
public class Seat {

    private final Integer row;
    private final Integer column;

    public Seat(Integer row, Integer column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
