package components;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class SeatsGrid extends GridPane {

    private int rows;
    private int columns;

    private int selectedSeats = 0;

    public SeatsGrid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        init();
    }

    private void init() {
        for (int i = 1; i <= columns; i++) {
            Label label = new Label(String.valueOf(i));

            add(label, i, 0);

            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
            GridPane.setHgrow(label, Priority.ALWAYS);
            GridPane.setVgrow(label, Priority.ALWAYS);
        }

        for (int i = 1; i <= rows; i++) {
            Label label = new Label(String.valueOf(i));

            add(label, 0, i);

            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
            GridPane.setHgrow(label, Priority.ALWAYS);
            GridPane.setVgrow(label, Priority.ALWAYS);

            GridPane.setMargin(label, new Insets(0, 5, 0 , 5));
        }

        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= columns; j++) {
                SeatButton button = new SeatButton();
                button.setPrefWidth(70);
                button.setPrefHeight(20);

                button.setOnAction(event -> {
                    SeatButton button1 = (SeatButton) event.getSource();

                    if (!button.isReserved()) {
                        button.setReserved();
                        selectedSeats++;
                    } else {
                        button.setFree();
                        selectedSeats--;
                    }
                });

                add(button, j, i);

                GridPane.setHalignment(button, HPos.CENTER);
                GridPane.setValignment(button, VPos.CENTER);
                GridPane.setHgrow(button, Priority.ALWAYS);
                GridPane.setVgrow(button, Priority.ALWAYS);
                GridPane.setMargin(button, new Insets(5));
            }
        }

        setAlignment(Pos.CENTER);
    }

    public int getReservedSeats() {
        return selectedSeats;
    }
}
