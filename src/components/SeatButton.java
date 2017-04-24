package components;

import javafx.scene.control.Button;

public class SeatButton extends Button {

    private final String RESERVED = "reserved";

    private final String BOUGHT = "bought";

    public SeatButton() {
        super();
        setFree();
    }

    public boolean isReserved() {
        String css = getStyle();

        return css.contains(RESERVED);
    }

    public boolean isBought() {
        String css = getStyle();

        return css.contains(BOUGHT);
    }

    public void setFree() {
        setStyle("-fx-background-color: green;");
    }

    public void setReserved() {
        setStyle("-fx-background-color: blue;" +
                "class:" + RESERVED);
    }

    public void setBought() {
        setStyle("-fx-background-color: red;" +
                "class:" + BOUGHT);
    }

}
