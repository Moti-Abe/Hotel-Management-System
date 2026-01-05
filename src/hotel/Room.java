package hotel;

import javafx.beans.property.*;

public class Room {

    private final IntegerProperty roomNumber;
    private final StringProperty type;
    private final BooleanProperty available;

    public Room(int roomNumber, String type, boolean available) {
        this.roomNumber = new SimpleIntegerProperty(roomNumber);
        this.type = new SimpleStringProperty(type);
        this.available = new SimpleBooleanProperty(available);
    }

    // Getters
    public int getRoomNumber() { return roomNumber.get(); }
    public String getType() { return type.get(); }
    public boolean isAvailable() { return available.get(); }

    // Properties
    public IntegerProperty roomNumberProperty() { return roomNumber; }
    public StringProperty typeProperty() { return type; }
    public BooleanProperty availableProperty() { return available; }

    // THIS IS THE KEY FIX
    @Override
    public String toString() {
        return "Room " + roomNumber.get() + " - " + type.get() +
                (available.get() ? " (Available)" : " (Occupied)");
    }
}