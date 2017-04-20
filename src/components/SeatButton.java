package components;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class SeatButton extends Button {

    private final String RESERVED = "reserved";

    public SeatButton() {
        super();
        setFree();
    }

    public SeatButton(String text) {
        super(text);
        setFree();
    }

    public SeatButton(String text, Node graphic) {
        super(text, graphic);
        setFree();
    }

    public boolean isReserved() {
        String css = getStyle();

        return css.contains(RESERVED);
    }

    public void setFree() {
        setStyle("-fx-background-color: green;");
    }

    public void setReserved() {
        setStyle("-fx-background-color: red;" +
                "class:" + RESERVED);
    }
}
