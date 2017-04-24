package components;

import controllers.TicketReservationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import models.TicketReservation;

import java.io.IOException;

public class ReservationListViewCell extends ListCell<TicketReservation> {

    @Override
    protected void updateItem(TicketReservation reservation, boolean empty) {
        super.updateItem(reservation, empty);

        if (empty || reservation == null) {

            setText(null);
            setGraphic(null);

        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../layouts/reservation_item_layout.fxml"));

            try {
                setText(null);
                setGraphic(loader.load());

                TicketReservationController controller = loader.getController();
                controller.setTicketReservation(reservation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
