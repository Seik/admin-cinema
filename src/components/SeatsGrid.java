package components;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import modelo.Sala;
import models.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatsGrid extends GridPane {

    Sala room;

    private int rows;
    private int columns;

    private Sala.localidad[][] seats;

    private int selectedSeats = 0;

    public SeatsGrid(Sala room, int rows, int columns) {
        this.room = room;

        this.rows = rows;
        this.columns = columns;

        seats = room.getLocalidades();

        init();
    }

    private void init() {
        for (int i = 1; i <= columns; i++) {
            Label label = new Label(String.valueOf(i));

            add(label, i, 0);

            SeatsGrid.setHalignment(label, HPos.CENTER);
            SeatsGrid.setValignment(label, VPos.CENTER);

            SeatsGrid.setHgrow(label, Priority.ALWAYS);
            SeatsGrid.setVgrow(label, Priority.ALWAYS);
        }

        for (int i = 1; i <= rows; i++) {
            Label label = new Label(String.valueOf(i));

            add(label, 0, i);

            SeatsGrid.setHalignment(label, HPos.CENTER);
            SeatsGrid.setValignment(label, VPos.CENTER);

            SeatsGrid.setHgrow(label, Priority.ALWAYS);
            SeatsGrid.setVgrow(label, Priority.ALWAYS);

            SeatsGrid.setMargin(label, new Insets(0, 5, 0, 5));
        }

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                SeatButton button = new SeatButton();
                button.setPrefWidth(70);
                button.setPrefHeight(20);

                if (seats[j - 1][i - 1] == Sala.localidad.vendida) {
                    button.setBought();
                }

                final int column = j;
                final int row = i;
                button.setOnAction(event -> {
                    SeatButton button1 = (SeatButton) event.getSource();

                    if (button1.isBought()) return;

                    if (!button1.isReserved()) {
                        button1.setReserved();
                        selectedSeats++;
                        seats[column - 1][row - 1] = Sala.localidad.vendida;
                    } else {
                        button1.setFree();
                        selectedSeats--;
                        seats[column - 1][row - 1] = Sala.localidad.libre;
                    }
                });

                add(button, j, i);

                SeatsGrid.setColumnIndex(button, j);
                SeatsGrid.setRowIndex(button, i);

                SeatsGrid.setHalignment(button, HPos.CENTER);
                SeatsGrid.setValignment(button, VPos.CENTER);

                SeatsGrid.setHgrow(button, Priority.ALWAYS);
                SeatsGrid.setVgrow(button, Priority.ALWAYS);

                SeatsGrid.setMargin(button, new Insets(5));
            }
        }

        setAlignment(Pos.CENTER);
    }

    public int getReservedSeats() {
        return selectedSeats;
    }

    public Sala.localidad[][] getSeats() {
        return seats;
    }

    public List<Seat> getRervedSeats() {
        List<Seat> reservedSeats = new ArrayList<>();

        ObservableList<Node> nodes = getChildren();

        for (Node node : nodes) {
            if ((node instanceof SeatButton)) {
                SeatButton seatButton = (SeatButton) node;

                if (seatButton.isReserved()) {
                    reservedSeats.add(new Seat(getRowIndex(seatButton), getColumnIndex(seatButton)));
                }
            }
        }
        return reservedSeats;
    }
}
